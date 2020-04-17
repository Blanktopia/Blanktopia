package me.weiwen.blanktopia.enchants

import me.weiwen.blanktopia.Blanktopia
import me.weiwen.blanktopia.level
import org.bukkit.Bukkit
import org.bukkit.entity.EntityType
import org.bukkit.entity.ExperienceOrb
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.GrindstoneInventory
import org.bukkit.inventory.meta.Damageable
import org.bukkit.inventory.meta.Repairable

class GrindStoneWatcher(val plugin: Blanktopia) : Listener {
    @EventHandler
    private fun onInventoryClick(event: InventoryClickEvent) {
        val inventory = event.inventory as? GrindstoneInventory ?: return
        if (event.whoClicked !is Player) return

        if (event.slotType == InventoryType.SlotType.RESULT) {
            val target = inventory.getItem(0)
            val sacrifice = inventory.getItem(1)
            val result = inventory.getItem(2)
            var exp = 0.0
            if (target != null) {
                var targetExp = 0
                for ((enchant, index) in target.enchantments.entries) {
                    if (enchant is CustomEnchantment) {
                        targetExp += enchant.getAnvilCostPerLevel() * index / 2
                    }
                }
                exp += targetExp * target.amount
            }
            if (sacrifice != null) {
                var sacrificeExp = 0
                for ((enchant, index) in sacrifice.enchantments.entries) {
                    if (enchant is CustomEnchantment) {
                        sacrificeExp += enchant.getAnvilCostPerLevel() * index / 2
                    }
                }
                exp += sacrificeExp * sacrifice.amount
            }
            if (result != null) {
                val meta = result.itemMeta
                if (meta is Repairable) {
                    meta.repairCost = 0
                }
                result.itemMeta = meta
            }
            if (exp > 0.0) {
                event.inventory.location?.run {
                    val orb = world?.spawnEntity(this, EntityType.EXPERIENCE_ORB) as ExperienceOrb
                    orb.level = exp
                }
            }
        } else {
            Bukkit.getScheduler().runTask(plugin) { _ ->
                val target = inventory.getItem(0)
                val sacrifice = inventory.getItem(1)

                val result = if (target != null && sacrifice != null) {
                    if (target.type != sacrifice.type) return@runTask
                    val result = target.clone()
                    val resultMeta = result.itemMeta
                    val targetMeta = target.itemMeta
                    val sacrificeMeta = sacrifice.itemMeta
                    if (resultMeta is Damageable && targetMeta is Damageable && sacrificeMeta is Damageable) {
                        val max = result.type.maxDurability.toInt()
                        resultMeta.damage = max - minOf(
                            (max - targetMeta.damage + max - sacrificeMeta.damage + max * 1.05).toInt(),
                            max
                        )
                    }
                    result
                } else {
                    (target ?: sacrifice ?: return@runTask).clone()
                }

                val meta = result.itemMeta
                if (meta?.hasEnchants() == true) {
                    for (enchant in ENCHANTMENTS) {
                        if (result.containsEnchantment(enchant)) {
                            enchant.disenchantItem(result)
                        }
                    }
                    for (enchant in result.enchantments.keys) {
                        result.removeEnchantment(enchant)
                    }
                    inventory.setItem(2, result)
                }
                result.itemMeta = meta
            }
        }
    }
}


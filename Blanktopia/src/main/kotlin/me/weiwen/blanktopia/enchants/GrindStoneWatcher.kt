package me.weiwen.blanktopia.enchants

import me.weiwen.blanktopia.Blanktopia
import me.weiwen.blanktopia.enchants.enchantments.FINAL
import me.weiwen.blanktopia.level
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.ExperienceOrb
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.GrindstoneInventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.inventory.meta.EnchantmentStorageMeta

class GrindStoneWatcher(val plugin: Blanktopia) : Listener {

    @EventHandler
    private fun onInventoryDrag(event: InventoryDragEvent) {
        val inventory = event.inventory as? GrindstoneInventory ?: return
        val player = event.whoClicked
        if (player !is Player) return

        val target = inventory.getItem(0)
        val sacrifice = inventory.getItem(1)
        val result = getResult(target, sacrifice)
        inventory.setItem(2, result)
        Bukkit.getScheduler().runTaskLater(plugin, { _ ->
            val target = inventory.getItem(0)
            val sacrifice = inventory.getItem(1)
            inventory.setItem(2, getResult(target, sacrifice))
        }, 2)
    }

    @EventHandler
    private fun onInventoryClick(event: InventoryClickEvent) {
        val inventory = event.inventory as? GrindstoneInventory ?: return
        val player = event.whoClicked
        if (player !is Player) return

        if (event.slotType == InventoryType.SlotType.RESULT) {
            val target = inventory.getItem(0)
            val sacrifice = inventory.getItem(1)
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
            inventory.setItem(2, getResult(target, sacrifice))
            if (exp > 0.0) {
                event.inventory.location?.run {
                    val orb = world?.spawnEntity(this, EntityType.EXPERIENCE_ORB) as ExperienceOrb
                    orb.level = exp
                }
            }
        } else {
            val target = inventory.getItem(0)
            val sacrifice = inventory.getItem(1)
            val result = getResult(target, sacrifice)
            inventory.setItem(2, result)
            Bukkit.getScheduler().runTaskLater(plugin, { _ ->
                val target = inventory.getItem(0)
                val sacrifice = inventory.getItem(1)
                inventory.setItem(2, getResult(target, sacrifice))
            }, 2)
        }
    }

    fun getResult(target: ItemStack?, sacrifice: ItemStack?): ItemStack? {
        if (target != null && sacrifice != null && target.type != sacrifice.type) return null
        val result = (target ?: sacrifice ?: return null).clone()
        if (result.enchantments.containsKey(FINAL)) return null
        for (enchant in result.enchantments.keys) {
            result.disenchant(enchant)
        }
        val meta = result.itemMeta
        if (meta is EnchantmentStorageMeta) {
            return ItemStack(Material.BOOK)
        }
        val resultMeta = result.itemMeta
        if (target != null && sacrifice != null) {
            val targetMeta = target.itemMeta
            val sacrificeMeta = sacrifice.itemMeta
            if (resultMeta is Damageable && targetMeta is Damageable && sacrificeMeta is Damageable) {
                val max = result.type.maxDurability.toInt()
                resultMeta.damage = max - minOf(
                    (max - targetMeta.damage + max - sacrificeMeta.damage + max * 1.05).toInt(),
                    max
                )
            }
        }
        result.itemMeta = resultMeta
        return result
    }
}


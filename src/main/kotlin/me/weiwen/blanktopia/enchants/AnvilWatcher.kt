package me.weiwen.blanktopia.enchants

import me.weiwen.blanktopia.Blanktopia
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.PrepareAnvilEvent
import org.bukkit.inventory.AnvilInventory
import org.bukkit.inventory.meta.Damageable
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import org.bukkit.inventory.meta.Repairable
import kotlin.math.ceil

class AnvilWatcher(val plugin: Blanktopia) : Listener {
    @EventHandler
    private fun onPrepareAnvil(event: PrepareAnvilEvent) {
        val target = event.inventory.contents[0] ?: return
        val sacrifice = event.inventory.contents[1] ?: return

        // Unit repair should be handled normally
        if (target.type != sacrifice.type && sacrifice.type != Material.ENCHANTED_BOOK) return

        val result = target.clone()
        val resultMeta = result.itemMeta ?: Bukkit.getItemFactory().getItemMeta(result.type)!!
        val targetMeta = target.itemMeta ?: Bukkit.getItemFactory().getItemMeta(target.type)!!
        val sacrificeMeta = sacrifice.itemMeta ?: Bukkit.getItemFactory().getItemMeta(sacrifice.type)!!

        var repairCost = 0
        var canAnvil = false

        // Rename
        if (!event.inventory.renameText.isNullOrBlank()) {
            if (targetMeta is Repairable && target.amount != 1) {
                targetMeta.repairCost += 1
            }
            resultMeta.setDisplayName(event.inventory.renameText)
            repairCost += 1
            canAnvil = true
        }

        // Repair
        if (target.type == sacrifice.type && resultMeta is Damageable && targetMeta is Damageable && sacrificeMeta is Damageable && targetMeta.hasDamage()) {
            repairCost += 2
            val max = result.type.maxDurability.toInt()
            resultMeta.damage = max - minOf((max - targetMeta.damage + max - sacrificeMeta.damage + max * 1.12).toInt(), max)
            canAnvil = true
        }

        // Prior work
        if (resultMeta is Repairable && targetMeta is Repairable && sacrificeMeta is Repairable) {
            resultMeta.repairCost = maxOf(targetMeta.repairCost, sacrificeMeta.repairCost) + 1
            val targetRepairCost = maxOf(0, 1 shl (targetMeta.repairCost - 1))
            val sacrificeRepairCost = maxOf(0, 1 shl (sacrificeMeta.repairCost - 1))
            repairCost += targetRepairCost + sacrificeRepairCost
        }

        result.itemMeta = resultMeta

        val targetBookMeta = targetMeta as? EnchantmentStorageMeta
        val targetEnchantments = targetBookMeta?.storedEnchants ?: target.enchantments
        val sacrificeBookMeta = sacrificeMeta as? EnchantmentStorageMeta
        val sacrificeEnchantments = sacrificeBookMeta?.storedEnchants ?: sacrifice.enchantments

        // Combining
        for ((enchant, sacrificeLevel) in sacrificeEnchantments.entries) {
            val targetLevel = targetBookMeta?.getStoredEnchantLevel(enchant) ?: target.getEnchantmentLevel(enchant)
            if (sacrificeLevel < targetLevel) continue
            if (targetBookMeta == null) {
                if (!enchant.canEnchantItem(result)) continue
                if (enchant is CustomEnchantment && !enchant.canAnvilItem(result)) continue
            }
            if (targetLevel == 0 && targetEnchantments.keys.any { enchant.conflictsWith(it) }) {
                repairCost += 1
                continue
            }
            canAnvil = true
            val level = minOf(enchant.maxLevel, if (targetLevel == sacrificeLevel) targetLevel + 1 else sacrificeLevel)
            if (enchant is CustomEnchantment) {
                enchant.enchantItem(result, level)
            } else if (targetBookMeta != null) {
                val meta = result.itemMeta as? EnchantmentStorageMeta
                meta?.addStoredEnchant(enchant, level, true)
                result.itemMeta = meta
            } else {
                result.addEnchantment(enchant, level)
            }
            repairCost += if (sacrificeBookMeta != null) {
                enchant.getAnvilCostPerLevel() * level
            } else {
                ceil(enchant.getAnvilCostPerLevel().toDouble() * level / 2).toInt()
            }
        }

        // No change, abort
        if (!canAnvil) {
            event.result = null
            (event.viewers.get(0) as? Player)?.updateInventory()
            return
        }

        repairCost = minOf(repairCost, 39)
        event.result = result
        val inventory = event.inventory
        (event.viewers.get(0) as? Player)?.updateInventory()
        plugin.server.scheduler.runTask(plugin, {
            inventory.repairCost = repairCost
        } as () -> Unit)
    }
}


package me.weiwen.blanktopia.enchants.enchantments

import me.weiwen.blanktopia.enchants.ALL
import me.weiwen.blanktopia.enchants.BOOKS
import me.weiwen.blanktopia.enchants.CustomEnchantment
import me.weiwen.blanktopia.enchants.NONE
import org.bukkit.ChatColor
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent

val SOULBOUND = CustomEnchantment(
    "soulbound",
    "Soulbound",
    1,
    ALL + BOOKS,
    NONE,
    0.05,
    30,
    30,
    15,
    { setOf(Enchantment.VANISHING_CURSE, Enchantment.BINDING_CURSE) },
    Soulbound
)

object Soulbound : Listener {
    init {}

    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent): Boolean {
        if (event.keepInventory) {
            return false
        }
        val player = event.entity
        val items = player.inventory.contents.clone()
        for ((i, item) in items.withIndex()) {
            item ?: continue
            if (item.containsEnchantment(SOULBOUND) ||
                item.itemMeta?.lore?.contains(ChatColor.GRAY.toString() + "Soulbound") == true // compat
               )
            {
                event.drops.remove(item)
            } else {
                items[i] = null;
            }
        }
        event.keepInventory = true
        player.inventory.contents = items
        return true
    }
}

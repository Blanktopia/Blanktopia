package me.weiwen.blanktopia.enchants.enchantments

import me.weiwen.blanktopia.enchants.ALL
import me.weiwen.blanktopia.enchants.BOOKS
import me.weiwen.blanktopia.enchants.CustomEnchantment
import me.weiwen.blanktopia.enchants.NONE
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.PrepareAnvilEvent

val FINAL = CustomEnchantment(
    "final",
    "Final",
    1,
    NONE,
    ALL + BOOKS,
    0.0,
    30,
    30,
    10,
    { setOf() },
    Final
)

object Final : Listener {
    init {}

    @EventHandler
    fun onPrepareAnvilEvent(event: PrepareAnvilEvent) {
        val items = event.inventory.contents
        for (item in items) {
            if (item != null) {
                print(item.containsEnchantment(FINAL))
            }
        }
        if (items.any { it != null && it.containsEnchantment(FINAL) }) {
            event.result = null
            for (viewer in event.viewers) {
                (viewer as? Player)?.updateInventory()
            }
        }
    }
}

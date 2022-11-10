package me.weiwen.blanktopia.enchants.enchantments

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent
import me.weiwen.blanktopia.enchants.BOOKS
import me.weiwen.blanktopia.enchants.BOOTS
import me.weiwen.blanktopia.enchants.CustomEnchantment
import me.weiwen.blanktopia.enchants.NONE
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

val STRIDE = CustomEnchantment(
    "Stride",
    "Stride",
    5,
    BOOTS + BOOKS,
    NONE,
    0.2,
    10,
    10,
    2,
    { setOf() },
    Stride
)

object Stride : Listener {
    init {}

    @EventHandler
    fun onPlayerArmorChange(event: PlayerArmorChangeEvent) {
        val newItem = event.newItem
        val oldItem = event.oldItem
        if (newItem != null && newItem.enchantments.containsKey(STRIDE)) {
            event.player.walkSpeed = when (newItem.enchantments[STRIDE]) {
                0 -> 0.2f
                1 -> 0.225f
                2 -> 0.240f
                3 -> 0.260f
                4 -> 0.270f
                5 -> 0.275f
                else -> 0.2f
            }
        } else if (oldItem != null && oldItem.enchantments.containsKey(STRIDE)) {
            event.player.walkSpeed = 0.2f
        }
    }
}

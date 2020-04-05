package me.weiwen.blanktopia.enchants.enchantments

import me.weiwen.blanktopia.enchants.BOWS
import me.weiwen.blanktopia.enchants.CustomEnchantment
import me.weiwen.blanktopia.enchants.NONE
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityShootBowEvent

val SNIPER = CustomEnchantment(
    "sniper",
    "Sniper",
    3,
    BOWS,
    NONE,
    0.2,
    10,
    15,
    4,
    { setOf<Enchantment>() },
    Sniper
)

object Sniper : Listener {
    init {}

    @EventHandler
    fun onEntityShootBow(event: EntityShootBowEvent) {
        val item = event.entity.equipment?.itemInMainHand ?: return

        if (!item.containsEnchantment(SNIPER)) return
        val level = item.getEnchantmentLevel(SNIPER)

        val projectile = event.projectile
        projectile.velocity = projectile.velocity.multiply(when (level) {
            1 -> 1.25
            2 -> 1.4
            3 -> 1.5
            else -> 1.5
        })
    }
}

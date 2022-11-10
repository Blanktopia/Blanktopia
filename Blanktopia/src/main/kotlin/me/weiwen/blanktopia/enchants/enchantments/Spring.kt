package me.weiwen.blanktopia.enchants.enchantments

import com.destroystokyo.paper.event.player.PlayerJumpEvent
import me.weiwen.blanktopia.enchants.BOOKS
import me.weiwen.blanktopia.enchants.CustomEnchantment
import me.weiwen.blanktopia.enchants.LEGGINGS
import me.weiwen.blanktopia.enchants.NONE
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

val SPRING = CustomEnchantment(
    "Spring",
    "Spring",
    3,
    LEGGINGS + BOOKS,
    NONE,
    0.2,
    20,
    10,
    4,
    { setOf() },
    Spring
)

object Spring : Listener {
    init {}

    @EventHandler
    fun onPlayerJump(event: PlayerJumpEvent) {
        for (item in event.player.inventory.armorContents!!) {
            item ?: continue
            if (item.enchantments.containsKey(SPRING)) {
                val level = item.enchantments[SPRING] ?: 1
                val prevLevel = event.player.getPotionEffect(PotionEffectType.JUMP)?.amplifier ?: -1
                val amplifier = minOf(level - 1, prevLevel + 1)
                event.player.addPotionEffect(
                    PotionEffect(
                        PotionEffectType.JUMP,
                        20 + 10 * amplifier,
                        amplifier,
                        true
                    )
                )
            }
        }
    }
}

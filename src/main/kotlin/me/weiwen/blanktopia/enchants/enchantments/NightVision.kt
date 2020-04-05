package me.weiwen.blanktopia.enchants.enchantments

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent
import me.weiwen.blanktopia.enchants.*
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

val NIGHT_VISION = CustomEnchantment(
    "nightvision",
    "Night Vision",
    1,
    HELMET + BOOKS,
    NONE,
    0.1,
    20,
    20,
    12,
    { setOf() },
    NightVision
)

object NightVision : Listener, EveryTenTicks {
    init {}

    override fun everyTenTicks(player: Player, level: Int) {
        player.addPotionEffect(PotionEffect(PotionEffectType.NIGHT_VISION, 319, 0, true))
    }

    @EventHandler
    fun onPlayerArmorChange(event: PlayerArmorChangeEvent) {
        val newItem = event.newItem
        if (newItem != null && newItem.containsEnchantment(NIGHT_VISION)) {
            event.player.addPotionEffect(PotionEffect(PotionEffectType.NIGHT_VISION, 319, 0, true))
        }
    }
}

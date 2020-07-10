package me.weiwen.blanktopia.enchants.enchantments

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent
import me.weiwen.blanktopia.Blanktopia
import me.weiwen.blanktopia.addPermanentPotionEffects
import me.weiwen.blanktopia.enchants.BOOKS
import me.weiwen.blanktopia.enchants.CustomEnchantment
import me.weiwen.blanktopia.enchants.HELMET
import me.weiwen.blanktopia.enchants.NONE
import me.weiwen.blanktopia.removePermanentPotionEffects
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
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

object NightVision : Listener {
    lateinit var plugin: Blanktopia

    fun enable(plugin: Blanktopia) {
        this.plugin = plugin

        for (player in plugin.server.onlinePlayers) {
            for (item in player.inventory.armorContents) {
                if (item == null) continue
                if (item.enchantments.containsKey(NIGHT_VISION)) {
                    player.addPermanentPotionEffects("night_vision", mapOf(
                        Pair(PotionEffectType.NIGHT_VISION, 0)
                    ))
                }
            }
        }
    }

    @EventHandler
    fun onPlayerArmorChange(event: PlayerArmorChangeEvent) {
        val newItem = event.newItem
        val oldItem = event.oldItem
        val player = event.player
        if (newItem != null && newItem.containsEnchantment(NIGHT_VISION)) {
            if (player.world.name.startsWith("DXL_Game_")) {
                return
            }
            player.addPermanentPotionEffects("night_vision", mapOf(
                Pair(PotionEffectType.NIGHT_VISION, 0)
            ))
        } else if (oldItem != null && oldItem.containsEnchantment(NIGHT_VISION)) {
            player.removePermanentPotionEffects("night_vision")
        }
    }
}

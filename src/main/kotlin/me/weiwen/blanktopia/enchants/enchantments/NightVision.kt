package me.weiwen.blanktopia.enchants.enchantments

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent
import me.weiwen.blanktopia.Blanktopia
import me.weiwen.blanktopia.enchants.*
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.persistence.PersistentDataType
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

object NightVision : Listener {
    init {}

    @EventHandler
    fun onPlayerArmorChange(event: PlayerArmorChangeEvent) {
        val newItem = event.newItem
        val oldItem = event.oldItem
        val player = event.player
        if (newItem != null && newItem.containsEnchantment(NIGHT_VISION)) {
            Blanktopia.INSTANCE.customItems.potionEffect.addPotionEffects(player, "night_vision", mapOf(
                Pair(PotionEffectType.NIGHT_VISION, 0)
            ))
        } else if (oldItem != null && oldItem.containsEnchantment(NIGHT_VISION)) {
            Blanktopia.INSTANCE.customItems.potionEffect.removePotionEffects(player, "night_vision")
        }
    }
}

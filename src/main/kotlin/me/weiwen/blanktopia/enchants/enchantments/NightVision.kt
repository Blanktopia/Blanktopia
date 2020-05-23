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

object NightVision : Listener, EveryTenTicks {
    init {}

    override fun everyTenTicks(player: Player, level: Int) {
        player.addPotionEffect(PotionEffect(PotionEffectType.NIGHT_VISION, 319, 0, true))
    }

    @EventHandler
    fun onPlayerArmorChange(event: PlayerArmorChangeEvent) {
        val newItem = event.newItem
        val player = event.player
        if (newItem != null && newItem.containsEnchantment(NIGHT_VISION)) {
            player.persistentDataContainer.set(
                NamespacedKey(Blanktopia.INSTANCE, "potion-effect-type"),
                PersistentDataType.STRING,
                "NIGHT_VISION"
            )
            player.addPotionEffect(PotionEffect(PotionEffectType.NIGHT_VISION, 619, 0, true))
        } else {
            player.persistentDataContainer.remove(
                NamespacedKey(Blanktopia.INSTANCE, "potion-effect-type")
            )
            player.removePotionEffect(PotionEffectType.NIGHT_VISION)
        }
    }
}

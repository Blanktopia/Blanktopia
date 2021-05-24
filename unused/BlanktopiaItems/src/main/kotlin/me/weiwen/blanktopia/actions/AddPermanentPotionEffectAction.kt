package me.weiwen.blanktopia.actions

import me.weiwen.blanktopia.addPermanentPotionEffects
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffectType

class AddPermanentPotionEffectAction(private val key: String, private val effects: Map<PotionEffectType, Int>) : Action {
    override fun run(player: Player) {
        player.addPermanentPotionEffects(key, effects)
    }
}
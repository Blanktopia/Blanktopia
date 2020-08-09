package me.weiwen.blanktopia.actions

import me.weiwen.blanktopia.addPermanentPotionEffects
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class AddPotionEffectAction(private val effect: PotionEffectType, private val duration: Int, private val level: Int) : Action {
    override fun run(player: Player) {
        player.addPotionEffect(PotionEffect(effect, duration, level))
    }
}
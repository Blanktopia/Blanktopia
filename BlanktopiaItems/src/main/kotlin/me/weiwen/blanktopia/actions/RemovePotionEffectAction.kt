package me.weiwen.blanktopia.actions

import me.weiwen.blanktopia.removePermanentPotionEffects
import org.bukkit.entity.Player

class RemovePotionEffectAction(private val key: String) : Action {
    override fun run(player: Player) {
        player.removePermanentPotionEffects(key)
    }
}
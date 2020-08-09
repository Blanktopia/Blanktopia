package me.weiwen.blanktopia.actions

import me.weiwen.blanktopia.removePermanentPotionEffects
import org.bukkit.entity.Player

class RemovePermanentPotionEffectAction(private val key: String) : Action {
    override fun run(player: Player) {
        player.removePermanentPotionEffects(key)
    }
}
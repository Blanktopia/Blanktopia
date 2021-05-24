package me.weiwen.blanktopia.actions

import me.weiwen.blanktopia.canFlyInClaims
import org.bukkit.GameMode
import org.bukkit.entity.Player

class FlyInClaimsAction(private val canFly: Boolean) : Action {
    override fun run(player: Player) {
        player.canFlyInClaims = canFly
        if (!canFly) {
            if (player.gameMode != GameMode.CREATIVE && player.gameMode != GameMode.SPECTATOR) {
                player.allowFlight = false
                player.isFlying = false
            }
        }
    }
}
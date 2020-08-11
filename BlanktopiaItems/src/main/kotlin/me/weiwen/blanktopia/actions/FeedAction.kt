package me.weiwen.blanktopia.actions

import org.bukkit.entity.Player

class FeedAction(val amount: Int, val saturation: Float) : Action {
    override fun run(player: Player) {
        player.foodLevel += amount
        player.saturation += saturation
    }
}
package me.weiwen.blanktopia.actions

import org.bukkit.entity.Player

class ActionBarAction(private val message: String) : Action {
    override fun run(player: Player) {
        player.sendActionBar(message)
    }
}
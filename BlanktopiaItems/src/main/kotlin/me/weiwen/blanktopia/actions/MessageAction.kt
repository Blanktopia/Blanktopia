package me.weiwen.blanktopia.actions

import org.bukkit.entity.Player

class MessageAction(private val message: String) : Action {
    override fun run(player: Player) {
        player.sendMessage(message)
    }
}
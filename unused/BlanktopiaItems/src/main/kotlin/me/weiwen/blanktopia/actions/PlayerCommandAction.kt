package me.weiwen.blanktopia.actions

import org.bukkit.entity.Player

class PlayerCommandAction(private val command: String) : Action {
    override fun run(player: Player) {
        player.performCommand(command)
    }
}
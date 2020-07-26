package me.weiwen.blanktopia.actions

import org.bukkit.entity.Player

class ConsoleCommandAction(private val command: String) : Action {
    override fun run(player: Player) {
        val formatted = command.replace("%p", player.name)
        player.server.dispatchCommand(player.server.consoleSender, formatted)
    }
}
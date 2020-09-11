package me.weiwen.blanktopia.actions

import me.weiwen.blanktopia.BlanktopiaItems
import org.bukkit.entity.Player

class SudoCommandAction(private val command: String, private val permissions: List<String>) : Action {
    override fun run(player: Player) {
        val formatted = command.replace("%p", player.name)
        val permissions = permissions.map { player.addAttachment(BlanktopiaItems.INSTANCE, it, true) }
        try {
            player.performCommand(formatted)
        } finally {
            permissions.forEach { it.remove() }
        }
    }
}
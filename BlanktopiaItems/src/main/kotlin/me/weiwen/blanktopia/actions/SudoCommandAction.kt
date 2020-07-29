package me.weiwen.blanktopia.actions

import me.weiwen.blanktopia.BlanktopiaItems
import org.bukkit.entity.Player

class SudoCommandAction(private val command: String) : Action {
    override fun run(player: Player) {
        val formatted = command.replace("%p", player.name)
        val permission = player.addAttachment(BlanktopiaItems.INSTANCE, "*", true)
        val isOp = player.isOp
        if (!isOp) {
            player.isOp = true
        }
        try {
            player.performCommand(formatted)
        } finally {
            if (!isOp) {
                player.isOp = false
            }
            permission.remove()
        }
    }
}
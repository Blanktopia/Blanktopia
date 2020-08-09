package me.weiwen.blanktopia.actions

import me.weiwen.blanktopia.BlanktopiaItems
import org.bukkit.entity.Player
import org.bukkit.permissions.Permission

class SudoCommandAction(private val command: String) : Action {
    override fun run(player: Player) {
        val formatted = command.replace("%p", player.name)
        player.asOp(true).performCommand(formatted)
    }
}

private fun Player.asOp(allPerms: Boolean = false) = OppedPlayer(this, allPerms)
private class OppedPlayer(val sub: Player, val allPerms: Boolean = true) : Player by sub {
    override fun isOp() = true
    override fun hasPermission(name: String): Boolean = allPerms || sub.hasPermission(name)
    override fun hasPermission(perm: Permission): Boolean = allPerms || sub.hasPermission(perm)
}
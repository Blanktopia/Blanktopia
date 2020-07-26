package me.weiwen.blanktopia.actions

import me.libraryaddict.disguise.DisguiseAPI
import me.libraryaddict.disguise.disguisetypes.Disguise
import org.bukkit.entity.Player

class UndisguiseAction() : Action {
    override fun run(player: Player) {
        DisguiseAPI.undisguiseToAll(player)
    }
}
package me.weiwen.blanktopia.actions

import me.libraryaddict.disguise.DisguiseAPI
import me.libraryaddict.disguise.disguisetypes.Disguise
import org.bukkit.entity.Player

class DisguiseAction(private val disguise: Disguise) : Action {
    override fun run(player: Player) {
        val disguise = this.disguise.clone()
        disguise.entity = player
        disguise.startDisguise()
    }
}
package me.weiwen.blanktopia.actions

import de.themoep.minedown.MineDown
import org.bukkit.entity.Player

class ActionBarAction(message: String) : Action {
    private val message = MineDown.parse(message)

    override fun run(player: Player) {
        player.sendActionBar(*message)
    }
}
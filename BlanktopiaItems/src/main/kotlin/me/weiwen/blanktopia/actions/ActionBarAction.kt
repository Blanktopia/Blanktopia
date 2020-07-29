package me.weiwen.blanktopia.actions

import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.entity.Player

class ActionBarAction(private val message: String) : Action {
    override fun run(player: Player) {
        val formatted = TextComponent(*TextComponent.fromLegacyText(message))
        player.sendActionBar(formatted)
    }
}
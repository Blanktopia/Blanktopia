package me.weiwen.blanktopia.actions

import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.entity.Player

class MessageAction(private val message: String) : Action {
    override fun run(player: Player) {
        val formatted = TextComponent(*TextComponent.fromLegacyText(message))
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, formatted)
    }
}
package me.weiwen.blanktopia.items

import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player

class CustomItemAction(config: ConfigurationSection) {
    private var playerCommand: String? = config.getString("player-command")

    fun run(player: Player) {
        playerCommand?.let { player.performCommand(it) }
    }
}
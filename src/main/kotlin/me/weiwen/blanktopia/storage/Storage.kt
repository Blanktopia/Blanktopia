package me.weiwen.blanktopia.storage

import me.weiwen.blanktopia.Blanktopia
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import java.io.File

private const val PATH = "data.yml"

class Storage(private val plugin: Blanktopia) {
    private val data: FileConfiguration =
            YamlConfiguration.loadConfiguration(File(plugin.dataFolder, PATH))

    init {
        plugin.server.scheduler.runTaskTimer(plugin, ::save, 6000, 6000)
    }

    fun player(player: Player): ConfigurationSection {
        val uuid = player.uniqueId.toString()
        return data.getConfigurationSection(uuid) ?: data.createSection(uuid)
    }

    fun save() {
        plugin.logger.info("Saving player data.")
        data.save(File(plugin.dataFolder, PATH))
    }
}
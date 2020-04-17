package me.weiwen.blanktopia.storage

import me.weiwen.blanktopia.Blanktopia
import me.weiwen.blanktopia.Module
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import java.io.File
import java.util.*

private const val PATH = "playerData"

class Storage(private val plugin: Blanktopia) : Module {
    private var playerConfigs = mutableMapOf<UUID, FileConfiguration>()
    private val playerDataFolder = File(plugin.dataFolder, PATH)

    override fun enable() {
        plugin.server.scheduler.runTaskTimerAsynchronously(plugin, ::save, 6000, 6000)
        try {
            if (!playerDataFolder.exists()) {
                playerDataFolder.mkdirs()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun disable() {
        save()
    }

    override fun reload() {
        save()
        playerConfigs.clear()
    }

    fun player(player: Player): FileConfiguration {
        val uuid = player.uniqueId
        if (playerConfigs[uuid] != null) return playerConfigs[uuid]!!

        val config = YamlConfiguration.loadConfiguration(File(playerDataFolder, "$uuid.json"))
        playerConfigs[uuid] = config
        return config
    }

    fun save() {
        plugin.logger.info("Saving player data.")
        for ((uuid, config) in playerConfigs.entries) {
            try {
                val file = File(playerDataFolder, "$uuid.yml")
                config.save(file)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        playerConfigs.clear()
    }
}
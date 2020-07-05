package me.weiwen.blanktopia.storage

import me.weiwen.blanktopia.Blanktopia
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.util.*

private const val PATH = "playerData"

class FlatFileStorage(dataFolder: File): IStorage {
    private var playerConfigs = mutableMapOf<UUID, FileConfiguration>()
    private val playerDataFolder = File(dataFolder, PATH)

    override fun enable() {
        try {
            if (!playerDataFolder.exists()) {
                playerDataFolder.mkdirs()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun disable() {}

    override fun save() {
        Blanktopia.INSTANCE.logger.info("Saving ${playerConfigs.size} players' data.")
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

    override fun savePlayer(uuid: UUID, data: PlayerData) {
        val config = playerConfigs[uuid] ?: YamlConfiguration.loadConfiguration(File(playerDataFolder, "$uuid.yml"))
        config.set("data", data)
    }

    override fun loadPlayer(uuid: UUID, callback: (PlayerData) -> Unit) {
        if (playerConfigs[uuid] != null) return
        val config = YamlConfiguration.loadConfiguration(File(playerDataFolder, "$uuid.yml"))
        playerConfigs[uuid] = config
        callback(config.getObject("data", PlayerData::class.java) ?: PlayerData(uuid))
    }

    override fun getPlayerData(uuid: UUID): PlayerData? {
        return playerConfigs[uuid]?.getObject("data", PlayerData::class.java)
    }
}
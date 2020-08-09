package me.weiwen.blanktopia.storage

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.util.*
import java.util.logging.Logger

class FlatFileStorage(private val logger: Logger, dataFolder: File): IStorage {
    private val playerDataFolder = File(dataFolder, "playerData")

    private var playerConfigs = mutableMapOf<UUID, FileConfiguration>()
    private var likes = YamlConfiguration.loadConfiguration(File(dataFolder, "likes.yml"))

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
        logger.info("Saving ${playerConfigs.size} players' data.")
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
        val file = File(playerDataFolder, "$uuid.yml")
        val config = playerConfigs[uuid] ?: YamlConfiguration.loadConfiguration(file)
        config.set("data", data)
        config.save(file)
    }

    override fun loadPlayer(uuid: UUID, callback: (PlayerData) -> Unit) {
        if (playerConfigs[uuid] != null) return
        val file = File(playerDataFolder, "$uuid.yml")
        val config = YamlConfiguration.loadConfiguration(file)
        playerConfigs[uuid] = config
        callback(config.getObject("data", PlayerData::class.java) ?: PlayerData(uuid))
    }

    override fun getPlayerData(uuid: UUID): PlayerData? {
        return playerConfigs[uuid]?.getObject("data", PlayerData::class.java)
    }

    override fun createLikes(uuid: UUID, world: String, x: Int, y: Int, z: Int, callback: (String) -> Unit) {
        val id = likes.getKeys(false).size.toString()
        val config = likes.createSection(id)
        config.set("world", world)
        config.set("x", x)
        config.set("y", y)
        config.set("z", z)
        config.set("votes", listOf<String>())
        callback(id)
    }

    override fun like(uuid: UUID, id: String, callback: (Int) -> Unit) {
        val config = likes.getConfigurationSection(id) ?: return
        val votes = config.getStringList("votes")
        if (!votes.contains(uuid.toString())) {
            votes.add(uuid.toString())
        }
        callback(votes.size)
    }

    override fun unlike(uuid: UUID, id: String, callback: (Int) -> Unit) {
        val config = likes.getConfigurationSection(id) ?: return
        val votes = config.getStringList("votes")
        if (votes.contains(uuid.toString())) {
            votes.remove(uuid.toString())
        }
        callback(votes.size)
    }
}
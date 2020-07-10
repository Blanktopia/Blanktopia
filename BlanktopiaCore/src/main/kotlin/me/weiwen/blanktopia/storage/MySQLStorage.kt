package me.weiwen.blanktopia.storage

import me.weiwen.blanktopia.sql.MySQL
import java.sql.SQLException
import java.util.*
import java.util.logging.Logger

class MySQLStorage(private val logger: Logger, private val hostname: String, private val port: Int, private val username: String, private val password: String, private val database: String, private val useSSL: Boolean): IStorage {
    private lateinit var conn: MySQL
    private var playerDatas = mutableMapOf<UUID, PlayerData>()

    override fun enable() {
        conn = MySQL(logger, hostname, port, username, password, database, useSSL)
        setup()
    }

    override fun disable() {
        save()
        conn.close()
    }

    override fun save() {
        logger.info("Saving ${playerDatas.size} players' data.")
        for ((uuid, data) in playerDatas.entries) {
            savePlayer(uuid, data)
        }
        playerDatas.clear()
    }

    fun setup() {
        conn.executeUpdate("CREATE TABLE IF NOT EXISTS players (" +
                "uuid VARCHAR(36) PRIMARY KEY, " +
                "has_spawned_dragon BOOLEAN DEFAULT false)");
        conn.executeUpdate("CREATE TABLE IF NOT EXISTS shop_purchases (" +
                "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                "buyer VARCHAR(36), " +
                "seller VARCHAR(36), " +
                "item_name VARCHAR(255), " +
                "item_amount VARCHAR(255), " +
                "price_name VARCHAR(255), " +
                "price_amount VARCHAR(255), " +
                "x INT, " +
                "y INT, " +
                "z INT)")
    }

    override fun savePlayer(uuid: UUID, data: PlayerData) {
        conn.executeUpdate("INSERT INTO players (uuid, has_spawned_dragon) " +
                "VALUES (?, ?) ON DUPLICATE KEY UPDATE " +
                "has_spawned_dragon = ?",
            uuid.toString(),
            data.hasSpawnedDragon,
            data.hasSpawnedDragon
        )
    }

    override fun loadPlayer(uuid: UUID, callback: (PlayerData) -> Unit) {
        if (playerDatas[uuid] != null) {
            callback(playerDatas[uuid]!!)
            return
        }

        conn.executeQuery("SELECT has_spawned_dragon FROM players WHERE uuid = ?", uuid.toString()) {
            try {
                val data = if (it.next()) {
                    PlayerData(
                        uuid,
                        it.getBoolean("has_spawned_dragon")
                    )
                } else {
                    PlayerData(uuid)
                }
                playerDatas[uuid] = data
                callback(data)
            } catch (e: SQLException) {
                logger.warning("An error occured while trying to load a player from the database!")
                e.printStackTrace()
            }
        }
    }

    override fun getPlayerData(uuid: UUID): PlayerData? {
        return playerDatas[uuid]
    }

}
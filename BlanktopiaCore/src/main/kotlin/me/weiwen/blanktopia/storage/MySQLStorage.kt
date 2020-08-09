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
        conn.executeUpdate("CREATE TABLE IF NOT EXISTS likes (" +
                "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                "uuid VARCHAR(36) NOT NULL, " +
                "world VARCHAR(36) NOT NULL, " +
                "x INT NOT NULL, " +
                "y INT NOT NULL, " +
                "z INT NOT NULL)")
        conn.executeUpdate("CREATE TABLE IF NOT EXISTS likes_players (" +
                "id INT NOT NULL, " +
                "uuid VARCHAR(36) NOT NULL, " +
                "PRIMARY KEY (id, uuid))")
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

    override fun createLikes(uuid: UUID, world: String, x: Int, y: Int, z: Int, callback: (String) -> Unit) {
        conn.executeUpdate("INSERT INTO likes (uuid, world, x, y, z) " +
                "VALUES (?, ?, ?, ?, ?)",
            uuid.toString(),
            world,
            x,
            y,
            z
        )
        conn.executeQuery("SELECT LAST_INSERT_ID()") {
            try {
                if (it.next()) callback(it.getInt(1).toString())
            } catch (e: SQLException) {
                logger.warning("An error occured while trying to create a likes sign!")
                e.printStackTrace()
            }
        }
    }

    override fun like(uuid: UUID, id: String, callback: (Int) -> Unit) {
        conn.executeUpdate("INSERT IGNORE INTO likes_players (id, uuid) " +
                "VALUES (?, ?)",
            id,
            uuid.toString()
        )
        conn.executeQuery("SELECT COUNT(*) FROM likes_players WHERE id = ?", id) {
            try {
                if (it.next()) callback(it.getInt(1))
            } catch (e: SQLException) {
                logger.warning("An error occured while trying to fetch like counts!")
                e.printStackTrace()
            }
        }
    }

    override fun unlike(uuid: UUID, id: String, callback: (Int) -> Unit) {
        conn.executeUpdate("DELETE FROM likes_players WHERE id = ? AND uuid = ?",
            id,
            uuid.toString()
        )
        conn.executeQuery("SELECT COUNT(*) FROM likes_players WHERE id = ?", id) {
            try {
                if (it.next()) callback(it.getInt(1))
            } catch (e: SQLException) {
                logger.warning("An error occured while trying to fetch like counts!")
                e.printStackTrace()
            }
        }
    }
}
package me.weiwen.blanktopia.storage

import me.weiwen.blanktopia.sql.MySQL
import java.sql.SQLException
import java.util.*
import java.util.logging.Logger
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MySQLStorage(private val logger: Logger, private val hostname: String, private val port: Int, private val username: String, private val password: String, private val database: String, private val useSSL: Boolean): IStorage {
    private lateinit var conn: MySQL
    private var playerDatas = mutableMapOf<UUID, PlayerData>()

    override suspend fun enable() {
        conn = MySQL(logger, hostname, port, username, password, database, useSSL)
        setup()
    }

    override suspend fun disable() {
        save()
        conn.close()
    }

    override suspend fun save() {
        logger.info("Saving ${playerDatas.size} players' data.")
        for ((uuid, data) in playerDatas.entries) {
            savePlayer(uuid, data)
        }
        playerDatas.clear()
    }

    suspend fun setup() {
        conn.executeUpdate("CREATE TABLE IF NOT EXISTS migrations (" +
                                   "version INT PRIMARY KEY)")
        val rs = conn.executeQuery("SELECT version FROM migrations")
        if (rs.isAfterLast) {
            conn.executeUpdate("INSERT INTO migrations VALUES (0)")
        } else {
            rs.next()
        }
        val version = rs.getInt("version")
        rs.close()

        if (version < 1) {
            conn.executeUpdate("CREATE TABLE IF NOT EXISTS players (" +
                                       "uuid VARCHAR(36) PRIMARY KEY, " +
                                       "has_spawned_dragon BOOLEAN DEFAULT false)")
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
            conn.executeUpdate("UPDATE migrations SET version = 0")
        }

        if (version < 2) {
            conn.executeUpdate("ALTER TABLE players " +
                                       "ADD seen_tutorials SET('break', 'place', 'crafting', 'enderchest', 'tnt')")
            conn.executeUpdate("UPDATE migrations SET version = 1")
        }
    }

    override suspend fun savePlayer(uuid: UUID, data: PlayerData) {
        conn.executeUpdate("INSERT INTO players (uuid, has_spawned_dragon, seen_tutorials) " +
                "VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE " +
                "has_spawned_dragon = ? " +
                "seen_tutorials = ?",
            uuid.toString(),
            data.hasSpawnedDragon,
            data.seenTutorials.joinToString(","),
            data.hasSpawnedDragon,
            data.seenTutorials.joinToString(",")
        )
    }

    override suspend fun loadPlayer(uuid: UUID): PlayerData {
        if (playerDatas[uuid] != null) {
            return playerDatas[uuid]!!
        }

        val rs = conn.executeQuery("SELECT has_spawned_dragon, seen_tutorials FROM players WHERE uuid = ?", uuid.toString())
        try {
            val data = if (rs.next()) {
                val data = PlayerData(
                        uuid,
                        rs.getBoolean("has_spawned_dragon"),
                        rs.getString("seen_tutorials").split(',').toMutableSet()
                )
                data
            } else {
                PlayerData(uuid)
            }
            rs.close()
            playerDatas[uuid] = data
            return data
        } catch (e: SQLException) {
            logger.warning("An error occured while trying to load a player from the database!")
            e.printStackTrace()
            throw e
        }
    }

    override suspend fun createLikes(uuid: UUID, world: String, x: Int, y: Int, z: Int, callback: (String) -> Unit) {
        conn.executeUpdate("INSERT INTO likes (uuid, world, x, y, z) " +
                "VALUES (?, ?, ?, ?, ?)",
            uuid.toString(),
            world,
            x,
            y,
            z
        )
        val rs = conn.executeQuery("SELECT LAST_INSERT_ID()")
        try {
            if (rs.next()) callback(rs.getInt(1).toString())
            rs.close()
        } catch (e: SQLException) {
            logger.warning("An error occured while trying to create a likes sign!")
            e.printStackTrace()
        }
    }

    override suspend fun like(uuid: UUID, id: String, callback: (Int) -> Unit) {
        conn.executeUpdate("INSERT IGNORE INTO likes_players (id, uuid) " +
                "VALUES (?, ?)",
            id,
            uuid.toString()
        )
        val rs = conn.executeQuery("SELECT COUNT(*) FROM likes_players WHERE id = ?", id)
        try {
            if (rs.next()) callback(rs.getInt(1))
            rs.close()
        } catch (e: SQLException) {
            logger.warning("An error occured while trying to fetch like counts!")
            e.printStackTrace()
        }
    }

    override suspend fun unlike(uuid: UUID, id: String, callback: (Int) -> Unit) {
        conn.executeUpdate("DELETE FROM likes_players WHERE id = ? AND uuid = ?",
            id,
            uuid.toString()
        )
        val rs = conn.executeQuery("SELECT COUNT(*) FROM likes_players WHERE id = ?", id)
        try {
            if (rs.next()) callback(rs.getInt(1))
            rs.close()
        } catch (e: SQLException) {
            logger.warning("An error occured while trying to fetch like counts!")
            e.printStackTrace()
        }
    }
}
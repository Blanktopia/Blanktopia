package me.weiwen.blanktopia.storage

import me.weiwen.blanktopia.Blanktopia
import me.weiwen.blanktopia.Module
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import java.io.File
import java.util.*

class Storage(private val plugin: Blanktopia) : Module {
    var storage: IStorage? = null

    override fun enable() {
        plugin.server.scheduler.runTaskTimerAsynchronously(plugin, ::save, 9577, 18077)
        reload()
    }

    override fun disable() {
        storage?.disable()
    }

    override fun reload() {
        storage?.save()
        val database = plugin.config.getString("database", "flatfile")
        storage = if (database == "mysql") {
            val hostname = plugin.config.getString("mysql.hostname", "localhost")!!
            val port = plugin.config.getInt("mysql.port", 3306)
            val username = plugin.config.getString("mysql.username", "root")!!
            val password = plugin.config.getString("mysql.password", "")!!
            val database = plugin.config.getString("mysql.database", "blanktopia")!!
            val useSSL = plugin.config.getBoolean("mysql.use-ssl")
            MySQLStorage(hostname, port, username, password, database, useSSL)
        } else {
            FlatFileStorage(plugin.dataFolder)
        }
        storage?.enable()
    }

    fun save() {
        storage?.save()
    }
}
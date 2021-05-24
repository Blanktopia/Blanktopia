package me.weiwen.blanktopia.storage

import com.github.shynixn.mccoroutine.launch
import com.github.shynixn.mccoroutine.launchAsync
import me.weiwen.blanktopia.Module
import org.bukkit.plugin.java.JavaPlugin

class Storage(private val plugin: JavaPlugin) : Module {
    var storage: IStorage? = null

    override fun enable() {
        plugin.server.scheduler.scheduleSyncRepeatingTask(plugin, {
            plugin.launchAsync {
                save()
            }
        }, 18077, 18077)
        reload()
    }

    override fun disable() {
        plugin.launch { storage?.disable() }
    }

    override fun reload() {
        plugin.launchAsync {
            storage?.save()
            val database = plugin.config.getString("database", "flatfile")
            storage = if (database == "mysql") {
                val hostname = plugin.config.getString("mysql.hostname", "localhost")!!
                val port = plugin.config.getInt("mysql.port", 3306)
                val username = plugin.config.getString("mysql.username", "root")!!
                val password = plugin.config.getString("mysql.password", "")!!
                val database = plugin.config.getString("mysql.database", "blanktopia")!!
                val useSSL = plugin.config.getBoolean("mysql.use-ssl")
                MySQLStorage(plugin.logger, hostname, port, username, password, database, useSSL)
            } else {
                FlatFileStorage(plugin.logger, plugin.dataFolder)
            }
            storage?.enable()
        }
    }

    suspend fun save() {
        storage?.save()
    }
}
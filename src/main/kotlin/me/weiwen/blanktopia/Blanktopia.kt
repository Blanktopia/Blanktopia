package me.weiwen.blanktopia

import me.weiwen.blanktopia.books.Books
import me.weiwen.blanktopia.enchants.CustomEnchants
import me.weiwen.blanktopia.items.CustomItems
import me.weiwen.blanktopia.storage.Storage
import org.bukkit.ChatColor
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File


class Blanktopia : JavaPlugin() {
    lateinit var storage: Storage

    companion object {
        lateinit var INSTANCE: Blanktopia
            private set
    }

    override fun onLoad() {
        INSTANCE = this
        createConfig()
    }

    override fun onEnable() {
        reloadConfig()
        storage = Storage(this)
        getCommand("blanktopia")?.setExecutor { sender, _, _, args ->
            when (args[0]) {
                "reload" -> {
                    this.onDisable()
                    this.onEnable()
                    sender.sendMessage(ChatColor.GOLD.toString() + "Reloaded!")
                    true
                }
                else -> false
            }
        }
        PortableTools(this)
        CustomEnchants(this)
        Books(this)
        CustomItems(this)
        logger.info("Blanktopia is enabled")
    }

    override fun onDisable() {
        storage.save()
        logger.info("Blanktopia is disabled")
    }

    private fun createConfig() {
        try {
            if (!dataFolder.exists()) {
                dataFolder.mkdirs()
            }
            val file = File(dataFolder, "config.yml")
            if (!file.exists()) {
                saveDefaultConfig()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}


package me.weiwen.blanktopia

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.weiwen.blanktopia.books.Books
import me.weiwen.blanktopia.enchants.CustomEnchants
import org.bukkit.ChatColor
import org.bukkit.plugin.java.JavaPlugin
import java.io.File


class Blanktopia : JavaPlugin() {
    var modules = mutableListOf<Module>()

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
        getCommand("blanktopia")?.setExecutor { sender, _, _, args ->
            when (args[0]) {
                "reload" -> {
                    reloadConfig()
                    for (module in modules) {
                        module.reload()
                    }
                    sender.sendMessage(ChatColor.GOLD.toString() + "Reloaded configuration!")
                    true
                }
                "save" -> {
                    GlobalScope.launch {
                        BlanktopiaCore.INSTANCE.storage.save()
                    }
                    sender.sendMessage(ChatColor.GOLD.toString() + "Saved player data!")
                    true
                }
                else -> false
            }
        }
        modules.add(CustomEnchants(this))
        modules.add(Books(this))
        for (module in modules) {
            module.enable()
        }
        logger.info("Blanktopia is enabled")
    }

    override fun onDisable() {
        for (module in modules) {
            module.disable()
        }
        modules = mutableListOf()
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

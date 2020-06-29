package me.weiwen.blanktopia

import me.weiwen.blanktopia.books.Books
import me.weiwen.blanktopia.enchants.CustomEnchants
import me.weiwen.blanktopia.items.CustomItems
import me.weiwen.blanktopia.kits.Kits
import me.weiwen.blanktopia.storage.Storage
import org.bukkit.ChatColor
import org.bukkit.plugin.java.JavaPlugin
import java.io.File


class Blanktopia : JavaPlugin() {
    lateinit var storage: Storage

    var modules = mutableListOf<Module>()
    lateinit var customItems: CustomItems

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
                    reloadConfig()
                    for (module in modules) {
                        module.reload()
                    }
                    sender.sendMessage(ChatColor.GOLD.toString() + "Reloaded configuration!")
                    true
                }
                "save" -> {
                    storage.save()
                    sender.sendMessage(ChatColor.GOLD.toString() + "Saved player data!")
                    true
                }
                else -> false
            }
        }
        modules.add(storage)
        modules.add(PortableTools(this))
//        modules.add(PetTeleport(this))
        modules.add(SinglePlayerSleep(this))
        modules.add(EnderDragon(this, storage))
        modules.add(ShulkerRespawn(this))
        customItems = CustomItems(this)
        modules.add(customItems)
        modules.add(CustomEnchants(this))
        modules.add(Books(this, customItems))
        modules.add(Kits(this, customItems))
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

interface Module {
    fun enable()
    fun disable()
    fun reload()
}

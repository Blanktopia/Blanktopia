package me.weiwen.blanktopia

import org.bukkit.ChatColor
import org.bukkit.plugin.java.JavaPlugin
import java.io.File


class BlanktopiaTweaks : JavaPlugin() {
    var modules = mutableListOf<Module>()

    companion object {
        lateinit var INSTANCE: BlanktopiaTweaks
            private set
    }

    override fun onLoad() {
        INSTANCE = this
        createConfig()
    }

    override fun onEnable() {
        reloadConfig()
        getCommand("blanktopiatweaks")?.setExecutor { sender, _, _, args ->
            when (args[0]) {
                "reload" -> {
                    reloadConfig()
                    for (module in modules) {
                        module.reload()
                    }
                    sender.sendMessage(ChatColor.GOLD.toString() + "Reloaded configuration!")
                    true
                }
                else -> false
            }
        }
        if (config.getBoolean("use-crafting-table-and-ender-chest-from-inventory")) {
            modules.add(PortableTools(this))
        }
        if (config.getBoolean("disable-crop-trampling")) {
            modules.add(CropTrampling(this))
        }
        if (config.getBoolean("pet-teleport-together")) {
            modules.add(PetTeleport(this))
        }
        if (config.getBoolean("single-player-sleep")) {
            modules.add(SinglePlayerSleep(this))
        }
        if (config.getBoolean("ender-dragon-drops-elytra")) {
            modules.add(EnderDragonElytra(this))
        }
        if (config.getBoolean("shulkers-respawn")) {
            modules.add(ShulkerRespawn(this))
        }
        for (module in modules) {
            module.enable()
        }
        logger.info("BlanktopiaTweaks is enabled")
    }

    override fun onDisable() {
        for (module in modules) {
            module.disable()
        }
        modules = mutableListOf()
        logger.info("BlanktopiaTweaks is disabled")
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
package me.weiwen.blanktopia

import me.weiwen.blanktopia.enchants.enchantments.NightVision.plugin
import me.weiwen.blanktopia.items.CustomItems
import me.weiwen.blanktopia.kits.Kits
import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.io.File


class BlanktopiaItems : JavaPlugin() {
    private lateinit var customItems: CustomItems
    private lateinit var kits: Kits

    companion object {
        lateinit var INSTANCE: BlanktopiaItems
            private set
    }

    override fun onLoad() {
        INSTANCE = this
        createConfig()
    }

    override fun onEnable() {
        reloadConfig()
        getCommand("blanktopiaitems")?.setExecutor { sender, _, _, args ->
            when (args[0]) {
                "reload" -> {
                    reloadConfig()
                    customItems.reload()
                    kits.reload()
                    sender.sendMessage(ChatColor.GOLD.toString() + "Reloaded configuration!")
                    true
                }
                else -> false
            }
        }

        val wheadCommand = getCommand("whead")
        wheadCommand?.setExecutor { sender, _, _, args ->
            if (sender !is Player) return@setExecutor false
            if (args.size != 2) return@setExecutor false
            sender.inventory.addItem(playerHeadFromUrl(args[0].replace("_", " "), args[1]))
            sender.playSound(sender.location, Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 1.0f)
            true
        }

        val wserializeCommand = getCommand("wserialize")
        wserializeCommand?.setExecutor { sender, _, _, _ ->
            if (sender !is Player) return@setExecutor false
            val item = sender.inventory.itemInMainHand

            val file = File(plugin.dataFolder, "serialized.yml")
            if (!file.exists()) {
                file.createNewFile()
            }

            val config = YamlConfiguration()
            if (!config.isList("serialized")) {
                config.set("serialized", listOf<Any>());
            }
            (config.getList("serialized") as? MutableList<Any>)?.add(item)

            config.save(file)
            sender.playSound(sender.location, Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 1.0f)
            true
        }
        customItems = CustomItems(this)
        kits = Kits(this, customItems)
        customItems.enable()
        kits.enable()
        logger.info("BlanktopiaItems is enabled")
    }

    override fun onDisable() {
        kits.disable()
        customItems.disable()
        logger.info("BlanktopiaItems is disabled")
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

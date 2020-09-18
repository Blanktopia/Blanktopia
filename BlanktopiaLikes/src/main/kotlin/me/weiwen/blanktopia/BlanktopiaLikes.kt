package me.weiwen.blanktopia

import org.bukkit.*
import org.bukkit.block.Sign
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.SignChangeEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.*
import java.util.logging.Level

class BlanktopiaLikes : JavaPlugin(), Listener {
    companion object {
        lateinit var INSTANCE: BlanktopiaLikes
            private set
    }

    override fun onLoad() {
        INSTANCE = this
        createConfig()
    }

    override fun onEnable() {
        reloadConfig()
        server.pluginManager.registerEvents(this, this)
        logger.info("BlanktopiaLikes is enabled")
    }

    override fun onDisable() {
        logger.info("BlanktopiaLikes is disabled")
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

    @EventHandler(ignoreCancelled = true)
    fun onSignChange(event: SignChangeEvent) {
        if (event.getLine(0)?.toLowerCase() != "[likes]") return
        event.isCancelled = true
        val player = event.player
        if (!player.hasPermission("blanktopia.likes.create")) {
            player.sendMessage("${ChatColor.RED}You don't have permission to create a Likes sign!")
            event.block.breakNaturally()
            return
        }

        var owner = event.getLine(3)
        if (!owner.isNullOrEmpty()) {
            player.sendMessage("${ChatColor.RED}Leave the last line blank.")
            event.isCancelled = true
            event.block.breakNaturally()
            return
        }

        suspend {
            BlanktopiaCore.INSTANCE.storage.storage?.createLikes(player.uniqueId, event.block.world.name, event.block.x, event.block.y, event.block.z) { id ->
                val sign = event.block.state as? Sign ?: return@createLikes
                sign.setLine(0, "${ChatColor.GOLD}[Likes]")
                sign.setLine(2, "${ChatColor.GOLD}${ChatColor.BOLD}0 ⭐")
                sign.setLine(3, "${ChatColor.GOLD}${player.displayName}${ChatColor.GOLD} : ${id}")
                sign.isEditable = false
                val container = sign.persistentDataContainer
                container.set(NamespacedKey(this, "owner"), PersistentDataType.STRING, player.uniqueId.toString())
                container.set(NamespacedKey(this, "id"), PersistentDataType.STRING, id)
                sign.update()
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onInteractSign(event: PlayerInteractEvent) {
        val player = event.player
        val block = event.clickedBlock ?: return
        val sign = block.state as? Sign ?: return
        val container = sign.persistentDataContainer
        val id = container.get(NamespacedKey(this, "id"), PersistentDataType.STRING) ?: return
        val uuid = container.get(NamespacedKey(this, "uuid"), PersistentDataType.STRING) ?: return
        if (!player.hasPermission("blanktopia.likes.like")) {
            player.sendMessage("${ChatColor.RED}You don't have permission to like a sign!")
            return
        }

        if (event.action == Action.RIGHT_CLICK_BLOCK) {
            suspend {
                BlanktopiaCore.INSTANCE.storage.storage?.like(player.uniqueId, id) { likes ->
                    val owner = server.getOfflinePlayer(UUID.fromString(id)).name
                    logger.log(Level.INFO, "${player.name} has liked $owner : $id ($likes)")
                    sign.setLine(2, "${ChatColor.GOLD}${ChatColor.BOLD}$likes ⭐")
                    sign.update()
                    player.sendActionBar("${ChatColor.GOLD}You liked ${owner}'s build.")
                }
            }
        } else if (event.action == Action.LEFT_CLICK_BLOCK){
            suspend {
                BlanktopiaCore.INSTANCE.storage.storage?.unlike(player.uniqueId, id) { likes ->
                    val owner = server.getOfflinePlayer(UUID.fromString(id)).name
                    logger.log(Level.INFO, "${player.name} has unliked $owner : $id ($likes)")
                    sign.setLine(2, "${ChatColor.GOLD}${ChatColor.BOLD}$likes ⭐")
                    sign.update()
                    player.sendActionBar("${ChatColor.GOLD}You unliked ${owner}'s build.")
                }
            }
        }
    }
}

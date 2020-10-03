package me.weiwen.blanktopia

import com.okkero.skedule.schedule
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.plugin.java.JavaPlugin
import java.util.logging.Level

suspend fun showTutorial(player: Player, key: String): Boolean {
    val data = BlanktopiaCore.INSTANCE.storage.storage?.loadPlayer(player.uniqueId) ?: return false
    if (data.seenTutorials.contains(key)) return false
    BlanktopiaTutorial.INSTANCE.server.scheduler.schedule(BlanktopiaTutorial.INSTANCE) {
        if (player.performCommand("help tutorial $key")) {
            data.seenTutorials.add(key)
            GlobalScope.launch {
                BlanktopiaCore.INSTANCE.storage.storage?.savePlayer(player.uniqueId, data)
            }
        }
    }
    return true
}

class BlanktopiaTutorial : JavaPlugin(), Listener {
    companion object {
        lateinit var INSTANCE: BlanktopiaTutorial
            private set
    }

    override fun onLoad() {
        INSTANCE = this
    }

    override fun onEnable() {
        server.pluginManager.registerEvents(this, this)
        logger.info("BlanktopiaTutorial is enabled")
    }

    override fun onDisable() {
        logger.info("BlanktopiaTutorial is disabled")
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onBlockBreak(event: BlockBreakEvent) {
        GlobalScope.launch {
            showTutorial(event.player, "grief")
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onBlockPlace(event: BlockPlaceEvent) {
        GlobalScope.launch {
            showTutorial(event.player, "build")
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onCraftItem(event: CraftItemEvent) {
        val player = event.whoClicked as? Player ?: return
        GlobalScope.launch {
            when (event.recipe.result.type) {
                Material.CRAFTING_TABLE -> showTutorial(player, "crafting")
                Material.ENDER_CHEST -> showTutorial(player, "enderchest")
                else -> null
            }
        }
    }
}
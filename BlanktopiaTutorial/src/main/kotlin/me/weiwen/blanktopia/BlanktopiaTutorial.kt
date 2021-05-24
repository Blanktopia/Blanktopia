package me.weiwen.blanktopia

import com.github.shynixn.mccoroutine.registerSuspendingEvents
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.plugin.java.JavaPlugin

suspend fun showTutorial(player: Player, key: String): Boolean {
    val data = BlanktopiaCore.INSTANCE.storage.storage?.loadPlayer(player.uniqueId) ?: return false

    BlanktopiaTutorial.INSTANCE.logger.info(key)

    if (!data.seenTutorials.add(key)) {
        return false
    }

    BlanktopiaCore.INSTANCE.storage.storage?.savePlayer(player.uniqueId, data)

    BlanktopiaTutorial.INSTANCE.server.scheduler.scheduleSyncDelayedTask(BlanktopiaTutorial.INSTANCE) {
        player.performCommand("help tutorial $key")
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
        server.pluginManager.registerSuspendingEvents(this, this)
        logger.info("BlanktopiaTutorial is enabled")
    }

    override fun onDisable() {
        logger.info("BlanktopiaTutorial is disabled")
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    suspend fun onBlockBreak(event: BlockBreakEvent) {
        showTutorial(event.player, "grief")
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    suspend fun onBlockPlace(event: BlockPlaceEvent) {
        showTutorial(event.player, "build")
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    suspend fun onCraftItem(event: CraftItemEvent) {
        val player = event.whoClicked as? Player ?: return
        when (event.recipe.result.type) {
            Material.CRAFTING_TABLE -> showTutorial(player, "crafting")
            Material.ENDER_CHEST -> showTutorial(player, "enderchest")
            else -> null
        }
    }
}
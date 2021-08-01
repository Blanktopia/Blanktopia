package me.weiwen.blanktopia.tweaks.modules

import io.papermc.paper.event.player.PlayerDeepSleepEvent
import me.weiwen.blanktopia.tweaks.Module
import org.bukkit.ChatColor
import org.bukkit.World
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerBedEnterEvent
import org.bukkit.event.player.PlayerBedLeaveEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask
import java.util.*

class SinglePlayerSleep(val plugin: JavaPlugin) : Module, Listener {
    val sleeping: MutableSet<UUID> = mutableSetOf()

    override fun enable() {
        plugin.server.pluginManager.registerEvents(this, plugin)
    }

    override fun disable() {}

    override fun reload() {}

    @EventHandler
    fun onPlayerBedEnter(event: PlayerBedEnterEvent) {
        if (event.player.world.environment == World.Environment.NETHER || event.player.world.environment == World.Environment.THE_END) {
            event.isCancelled = true
            return
        }
        if (event.bedEnterResult != PlayerBedEnterEvent.BedEnterResult.OK) {
            return
        }
        if (sleeping.add(event.player.uniqueId)) {
            plugin.server.broadcastMessage(event.player.displayName + ChatColor.GRAY + " is going to bed. Sweet dreams!")
        }
    }

    @EventHandler
    fun onPlayerDeepSleep(event: PlayerDeepSleepEvent) {
        skipNight(event.player.world)
        sleeping.clear()
    }

    private fun skipNight(world: World) {
        world.time = 1000
        if (world.hasStorm()) {
            world.setStorm(false)
        }
    }
}
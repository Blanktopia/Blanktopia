package me.weiwen.blanktopia

import org.bukkit.ChatColor
import org.bukkit.World
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerBedEnterEvent

class SinglePlayerSleep(val plugin: Blanktopia) : Module, Listener {
    override fun enable() {
        plugin.server.pluginManager.registerEvents(this, plugin)
    }

    override fun disable() {}

    override fun reload() {}

    @EventHandler
    fun onPlayerBedEnter(event: PlayerBedEnterEvent) {
        if (event.getPlayer().getWorld().getEnvironment() == World.Environment.NETHER || event.getPlayer().getWorld().getEnvironment() == World.Environment.THE_END) {
            event.isCancelled = true
            return
        }
        if (event.bedEnterResult != PlayerBedEnterEvent.BedEnterResult.OK) {
            return
        }
        val world = event.player.world
        world.time = 1000
        world.setStorm(false)
        plugin.server.broadcastMessage(event.player.displayName + ChatColor.GRAY + " went to bed. Sweet dreams!")
    }
}
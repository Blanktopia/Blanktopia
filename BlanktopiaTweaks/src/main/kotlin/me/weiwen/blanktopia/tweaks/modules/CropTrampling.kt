package me.weiwen.blanktopia

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.plugin.java.JavaPlugin

class CropTrampling(private val plugin: JavaPlugin) :
    Listener, Module {
    override fun enable() {
        plugin.server.pluginManager.registerEvents(this, plugin)
    }

    override fun disable() {}

    override fun reload() {}

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (event.action != Action.PHYSICAL) return
        if (event.clickedBlock?.type != Material.FARMLAND) return
        event.isCancelled = true
    }
}

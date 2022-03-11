package me.weiwen.blanktopia.tweaks.modules

import me.weiwen.blanktopia.tweaks.Module
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.plugin.java.JavaPlugin

class SuffocateKeepInventory(val plugin: JavaPlugin) : Module, Listener {
    override fun enable() {
        plugin.server.pluginManager.registerEvents(this, plugin)
    }

    override fun disable() {}

    override fun reload() {}

    @EventHandler(priority = EventPriority.LOWEST)
    fun onPlayerDeath(event: PlayerDeathEvent) {
        val player = event.entity
        if (player.lastDamageCause?.cause == EntityDamageEvent.DamageCause.SUFFOCATION) {
            event.keepInventory = true
            event.drops.clear()
        }
    }
}
package me.weiwen.blanktopia.tweaks.modules

import me.weiwen.blanktopia.tweaks.Module
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerTeleportEvent
import org.bukkit.plugin.java.JavaPlugin

class PetTeleport(val plugin: JavaPlugin) : Module, Listener {
    override fun enable() {
        plugin.server.pluginManager.registerEvents(this, plugin)
    }

    override fun disable() {}

    override fun reload() {}

    @EventHandler
    fun onPlayerTeleport(event: PlayerTeleportEvent) {
        if (event.cause != PlayerTeleportEvent.TeleportCause.PLUGIN &&
            event.cause != PlayerTeleportEvent.TeleportCause.COMMAND) return

        val player = event.player
        val leashedEntities = player.world.getNearbyLivingEntities(player.location, 5.0).filter {
            it.isLeashed && it.leashHolder == player
        }
        val vehicle = player.vehicle as? LivingEntity

        plugin.server.scheduler.runTaskLater(plugin, { ->
            leashedEntities.forEach {
                it.setLeashHolder(null)
                it.teleport(event.to)
                it.setLeashHolder(player)
            }
            if (vehicle != null) {
                vehicle.removePassenger(player)
                vehicle.teleport(event.to)
                vehicle.addPassenger(player)
            }
        }, 1)
    }
}
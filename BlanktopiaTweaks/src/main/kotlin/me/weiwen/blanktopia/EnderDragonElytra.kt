package me.weiwen.blanktopia

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

class EnderDragonElytra(val plugin: JavaPlugin) : Module, Listener {
    override fun enable() {
        plugin.server.pluginManager.registerEvents(this, plugin)
    }

    override fun disable() {}

    override fun reload() {}

    @EventHandler
    fun onEntityDeath(event: EntityDeathEvent) {
        if (event.entityType != EntityType.ENDER_DRAGON) return
        val world = event.entity.world
        if (world.enderDragonBattle == null) return
        val player = event.entity.killer
        Bukkit.getScheduler().runTaskLater(plugin, runTaskLater@{
            player?.inventory?.addItem(ItemStack(Material.ELYTRA))
        } as () -> Unit, 200)
    }
}


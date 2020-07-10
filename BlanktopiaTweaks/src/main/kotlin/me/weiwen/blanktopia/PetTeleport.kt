package me.weiwen.blanktopia

import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.world.ChunkUnloadEvent
import org.bukkit.plugin.java.JavaPlugin

class PetTeleport(val plugin: JavaPlugin) : Module, Listener {
    override fun enable() {
        plugin.server.pluginManager.registerEvents(this, plugin)
    }

    override fun disable() {}

    override fun reload() {}

    @EventHandler
    fun onChunkUnload(event: ChunkUnloadEvent) {
        for (entity in event.chunk.entities) {
            if (entity !is Wolf && entity !is Cat && entity !is Parrot) return
            if (entity !is Sittable || entity.isSitting) return
            if (entity !is Tameable) return
            val owner = entity.owner as? Player ?: return
            entity.teleport(owner)
        }
    }
}
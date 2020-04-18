package me.weiwen.blanktopia

import org.bukkit.Material
import org.bukkit.block.Biome
import org.bukkit.entity.Enderman
import org.bukkit.entity.Shulker
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent

val END_BIOMES = setOf(Biome.END_HIGHLANDS, Biome.END_MIDLANDS)
val END_CITY_BLOCKS = setOf(Material.PURPUR_BLOCK, Material.PURPUR_PILLAR, Material.PURPUR_SLAB, Material.PURPUR_SLAB)

class ShulkerRespawn(val plugin: Blanktopia) : Module, Listener {
    private var maxShulkersPerChunk = plugin.config.getInt("max-shulkers-per-chunk")

    override fun enable() {
        plugin.server.pluginManager.registerEvents(this, plugin)
    }

    override fun disable() {}

    override fun reload() {
        maxShulkersPerChunk = plugin.config.getInt("max-shulkers-per-chunk")
    }

    @EventHandler
    fun onCreatureSpawn(event: CreatureSpawnEvent) {
        if (event.entity !is Enderman) return
        if (!END_BIOMES.contains(event.location.world.getBiome(event.location.blockX, event.location.blockZ))) return
        if (!END_CITY_BLOCKS.contains(event.location.subtract(0.0, 1.0, 0.0).block.type)) return
        var shulkerCount = 0
        for (entity in event.location.chunk.entities) {
            if (entity is Shulker) shulkerCount += 1
            if (shulkerCount > maxShulkersPerChunk) return
        }
        event.isCancelled = true
        event.location.world.spawn(event.location, Shulker::class.java)
    }
}


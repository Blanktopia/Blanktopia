package me.weiwen.blanktopia.tweaks.modules

import me.weiwen.blanktopia.tweaks.Module
import org.bukkit.Material
import org.bukkit.block.Biome
import org.bukkit.entity.Enderman
import org.bukkit.entity.Shulker
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.plugin.java.JavaPlugin
import kotlin.random.Random

val END_BIOMES = setOf(Biome.END_HIGHLANDS, Biome.END_MIDLANDS)
val END_CITY_BLOCKS = setOf(Material.PURPUR_BLOCK, Material.PURPUR_PILLAR, Material.PURPUR_SLAB, Material.PURPUR_SLAB)

class ShulkerRespawn(val plugin: JavaPlugin) : Module, Listener {
    private var maxShulkersPerChunk: Int = 2
    private var shulkerSpawnChance: Double = 0.1

    override fun enable() {
        reload()
        plugin.server.pluginManager.registerEvents(this, plugin)
    }

    override fun disable() {}

    override fun reload() {
        maxShulkersPerChunk = plugin.config.getInt("max-shulkers-per-chunk", 2)
        shulkerSpawnChance = plugin.config.getDouble("shulker-spawn-chance", 0.1)
    }

    @EventHandler
    fun onCreatureSpawn(event: CreatureSpawnEvent) {
        if (event.entity !is Enderman) return
        if (Random.nextDouble() > shulkerSpawnChance) return
        val world = event.location.world ?: return
        if (!END_BIOMES.contains(world.getBiome(event.location.blockX, event.location.blockZ))) return
        if (!END_CITY_BLOCKS.contains(event.location.subtract(0.0, 1.0, 0.0).block.type)) return
        var shulkerCount = 0
        for (entity in event.location.chunk.entities) {
            if (entity is Shulker) shulkerCount += 1
            if (shulkerCount > maxShulkersPerChunk) return
        }
        event.isCancelled = true
        world.spawn(event.location, Shulker::class.java)
    }
}


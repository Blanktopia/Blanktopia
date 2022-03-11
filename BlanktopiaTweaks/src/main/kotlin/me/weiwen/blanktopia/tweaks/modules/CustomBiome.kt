package me.weiwen.blanktopia.tweaks.modules

import de.freesoccerhdx.advancedworldcreatorapi.AdvancedWorldCreatorAPI
import de.freesoccerhdx.advancedworldcreatorapi.biome.BiomeCreator
import de.freesoccerhdx.advancedworldcreatorapi.biome.BiomeGrassColorModifier
import de.freesoccerhdx.advancedworldcreatorapi.biome.BiomeParticle
import de.freesoccerhdx.advancedworldcreatorapi.biome.BiomePrecipitation
import me.weiwen.blanktopia.tweaks.Module
import org.bukkit.Location
import org.bukkit.NamespacedKey
import org.bukkit.Particle
import org.bukkit.craftbukkit.v1_18_R1.CraftWorld
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import java.awt.Color
import kotlin.math.max
import kotlin.math.min


class CustomBiome(private val plugin: JavaPlugin) :
    Listener, Module {

    private val biomes: MutableMap<NamespacedKey, BiomeCreator.CustomBiome> = mutableMapOf()

    override fun enable() {
        try {
            biomes[NamespacedKey("blanktopia", "lobby")] = BiomeCreator("blanktopia", "lobby").apply {
                foliageColor = Color(0xFFBCD2)
                grassColor = Color(0xC4FCCD)
                fogColor = Color(0xFFA4A4)
                skyColor = Color(0x9FBAFF)
                waterColor = Color(4962255)
                waterFogColor = Color(329011)

                // Christmas
                temperature = 0.1f
                downfall = 0.9f
                precipitation = BiomePrecipitation.SNOW
                setAmbientParticle(BiomeParticle(Particle.SNOWFLAKE, 0.0005f))

                mobSpawnProbability = 0f

            }.createBiome(true)
        } catch (e: NoClassDefFoundError) {
            plugin.logger.severe("No class def found!")
        }
    }

    override fun disable() {}

    override fun reload() {}

    fun setBiome(from: Location, to: Location, key: NamespacedKey) {
        if (from.world != to.world) {
            return
        }

        val craftWorld = from.world as CraftWorld

        val x0 = min(from.blockX, to.blockX)
        val x1 = max(from.blockX, to.blockX)
        val y0 = min(from.blockY, to.blockY)
        val y1 = max(from.blockY, to.blockY)
        val z0 = min(from.blockZ, to.blockZ)
        val z1 = max(from.blockZ, to.blockZ)

        biomes[key]?.let { biome ->
            (x0..x1).forEach { x ->
                (y0..y1).forEach { y ->
                    (z0..z1).forEach { z ->
                        craftWorld.setBiome(x, y, z, biome.nmsBiome)
                    }
                }
            }
        }
    }
}

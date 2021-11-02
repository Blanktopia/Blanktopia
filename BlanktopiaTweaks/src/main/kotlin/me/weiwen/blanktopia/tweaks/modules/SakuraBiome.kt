package me.weiwen.blanktopia.tweaks.modules

import de.freesoccerhdx.advancedworldcreator.main.AdvancedWorldCreatorAPI
import de.freesoccerhdx.advancedworldcreator.main.CustomBiome
import de.freesoccerhdx.advancedworldcreator.main.RegisteredCustomBiome
import me.weiwen.blanktopia.tweaks.Module
import org.bukkit.Location
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import java.awt.Color
import java.lang.Integer.max
import java.lang.Integer.min


class SakuraBiome(private val plugin: JavaPlugin) :
    Listener, Module {

    private lateinit var registeredBiome: RegisteredCustomBiome

    override fun enable() {
        val biome = CustomBiome("sakura").apply {
            setFoliageColor(Color(16747695))
//            setGrassColor(Color(7982981))
            setFogColor(Color(0xFFAFAF))
            setSkyColor(Color(0xD3EDFF))
            setWaterColor(Color(4962255))
            setWaterFogColor(Color(329011))
        }

        registeredBiome = AdvancedWorldCreatorAPI.registerCustomBiome(biome)
    }

    override fun disable() {}

    override fun reload() {}

    fun setBiome(from: Location, to: Location) {
        if (from.world != to.world) {
            return
        }

        val world = from.world

        val x0 = min(from.blockX, to.blockX)
        val x1 = max(from.blockX, to.blockX)
        val z0 = min(from.blockZ, to.blockZ)
        val z1 = max(from.blockZ, to.blockZ)

        (x0..x1).forEach { x ->
            (z0..z1).forEach { z ->
                registeredBiome.insertBiome(world, x, z)
            }
        }
    }
}

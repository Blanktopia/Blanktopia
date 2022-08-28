package me.weiwen.blanktopia.tweaks.modules

import io.papermc.paper.event.player.PlayerDeepSleepEvent
import me.weiwen.blanktopia.tweaks.Module
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.World
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerBedEnterEvent
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class SinglePlayerSleep(val plugin: JavaPlugin) : Module, Listener {
    val sleeping: MutableSet<UUID> = mutableSetOf()

    override fun enable() {
        plugin.server.pluginManager.registerEvents(this, plugin)
    }

    override fun disable() {}

    override fun reload() {}

    @EventHandler
    fun onPlayerBedEnter(event: PlayerBedEnterEvent) {
        if (event.player.world.environment == World.Environment.NETHER || event.player.world.environment == World.Environment.THE_END) {
            event.isCancelled = true
            return
        }
        if (event.bedEnterResult != PlayerBedEnterEvent.BedEnterResult.OK) {
            return
        }
        if (sleeping.add(event.player.uniqueId)) {
            event.player.world.sendMessage(
                event.player.displayName().append(
                    Component.text(" is going to bed. Sweet dreams!").color(
                        TextColor.color(0xbf9999)
                    )
                )
            )
        }
    }

    @EventHandler
    fun onPlayerDeepSleep(event: PlayerDeepSleepEvent) {
        skipNight(event.player.world)
        sleeping.clear()
    }

    private fun skipNight(world: World) {
        world.time = 1000
        world.setStorm(false)
    }
}
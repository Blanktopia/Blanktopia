package me.weiwen.blanktopia.tweaks.modules

import de.jeff_media.chestsort.api.ChestSortEvent
import me.weiwen.blanktopia.tweaks.Module
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.Material
import org.bukkit.plugin.java.JavaPlugin

val SHULKER_BOXES = listOf(
    Material.SHULKER_BOX,
    Material.WHITE_SHULKER_BOX,
    Material.RED_SHULKER_BOX,
    Material.ORANGE_SHULKER_BOX,
    Material.PINK_SHULKER_BOX,
    Material.YELLOW_SHULKER_BOX,
    Material.LIME_SHULKER_BOX,
    Material.GREEN_SHULKER_BOX,
    Material.LIGHT_BLUE_SHULKER_BOX,
    Material.CYAN_SHULKER_BOX,
    Material.BLUE_SHULKER_BOX,
    Material.MAGENTA_SHULKER_BOX,
    Material.PURPLE_SHULKER_BOX,
    Material.BROWN_SHULKER_BOX,
    Material.GRAY_SHULKER_BOX,
    Material.LIGHT_GRAY_SHULKER_BOX,
    Material.BLACK_SHULKER_BOX,
)

class ChestSortHook(private val plugin: JavaPlugin) : Module, Listener {
    override fun enable() {
        if (plugin.server.pluginManager.isPluginEnabled("ChestSort")) {
            Bukkit.getServer().pluginManager.registerEvents(this, plugin)
        }
    }

    override fun disable() {
        HandlerList.unregisterAll(this)
    }

    override fun reload() {}

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    fun onChestSortEvent(event: ChestSortEvent) {
        event.inventory.contents.forEachIndexed { i, item ->
            if (item?.type in SHULKER_BOXES) event.setUnmovable(i)
        }
    }
}


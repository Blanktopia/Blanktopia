package me.weiwen.blanktopia.tweaks.modules

import me.weiwen.blanktopia.tweaks.Module
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

class PortableTools(private val plugin: JavaPlugin) :
    Listener, Module {
    override fun enable() {
        plugin.server.pluginManager.registerEvents(this, plugin)
    }

    override fun disable() {}

    override fun reload() {}

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (event.isCancelled) return
        if (event.action != Action.RIGHT_CLICK_AIR) return
        val player = event.player
        val item = player.inventory.itemInMainHand
        event.isCancelled = usePortableTool(player, item)
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onInventoryClick(event: InventoryClickEvent) {
        if (event.isCancelled) return
        if (!event.isRightClick) return
        val item = event.currentItem ?: return
        val player = event.whoClicked as? Player ?: return
        event.isCancelled = usePortableTool(player, item)
    }

    private fun usePortableTool(player: Player, item: ItemStack): Boolean {
        val scheduler = plugin.server.scheduler
        when (item.type) {
            Material.CRAFTING_TABLE -> scheduler.scheduleSyncDelayedTask(plugin, {
                player.openWorkbench(null, true)
            }, 1)
            Material.ENDER_CHEST -> {
                player.world.playSound(player.location, Sound.BLOCK_ENDER_CHEST_OPEN, SoundCategory.BLOCKS, 1.0f, 1.0f)
                scheduler.scheduleSyncDelayedTask(plugin, {
                    player.openInventory(player.enderChest)
                }, 1)
            }
            Material.LOOM -> scheduler.scheduleSyncDelayedTask(plugin, {
                player.openLoom(null, true)
            }, 1)
            Material.STONECUTTER -> scheduler.scheduleSyncDelayedTask(plugin, {
                player.openStonecutter(null, true)
            }, 1)
            Material.GRINDSTONE -> scheduler.scheduleSyncDelayedTask(plugin, {
                player.openGrindstone(null, true)
            }, 1)
            Material.CARTOGRAPHY_TABLE -> scheduler.scheduleSyncDelayedTask(plugin, {
                player.openCartographyTable(null, true)
            }, 1)
            else -> return false
        }
        return true
    }
}
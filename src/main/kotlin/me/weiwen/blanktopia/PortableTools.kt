package me.weiwen.blanktopia

import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.InventoryView
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BlockStateMeta
import java.util.*

class PortableTools(private val plugin: Blanktopia) :
    Listener {
    init {
        plugin.server.pluginManager.registerEvents(this, plugin)
    }

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (event.action != Action.RIGHT_CLICK_AIR) return
        val player = event.player
        val item = player.inventory.itemInMainHand
        event.isCancelled = usePortableTool(player, item)
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        if (!event.isRightClick) return
        val item = event.currentItem ?: return
        val player = event.whoClicked as? Player ?: return
        event.isCancelled = usePortableTool(player, item)
    }

    fun usePortableTool(player: Player, item: ItemStack): Boolean {
        if (item.type == Material.CRAFTING_TABLE) {
            plugin.server.scheduler.runTask(plugin) { ->
                player.openWorkbench(null, true)
            }
            return true
        } else if (item.type == Material.ENDER_CHEST) {
            plugin.server.scheduler.runTask(plugin) { ->
                player.openInventory(player.enderChest)
            }
            return true
        } else {
            return false
        }
    }
}
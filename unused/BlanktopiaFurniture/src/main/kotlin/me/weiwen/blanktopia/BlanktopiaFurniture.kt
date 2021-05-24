package me.weiwen.blanktopia

import org.bukkit.*
import org.bukkit.entity.EntityType
import org.bukkit.entity.ItemFrame
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin
import java.util.logging.Level
import kotlin.math.roundToInt

class BlanktopiaFurniture : JavaPlugin(), Listener {
    companion object {
        lateinit var INSTANCE: BlanktopiaFurniture
            private set
    }

    override fun onLoad() {
        INSTANCE = this
    }

    override fun onEnable() {
        server.pluginManager.registerEvents(this, this)
        logger.info("BlanktopiaFurniture is enabled")
    }

    override fun onDisable() {
        logger.info("BlanktopiaFurniture is disabled")
    }

    @EventHandler(priority = EventPriority.NORMAL)
    fun onPlayerInteract(event: PlayerInteractEvent) {
        val block = event.clickedBlock ?: return

        if (event.action == Action.LEFT_CLICK_BLOCK) {
            if (block.type != Material.BARRIER) return
            val location = block.location.add(0.5, 0.5, 0.5)
            val itemFrames = block.world.getNearbyEntities(location, 0.5, 0.5, 0.5) { it.type == EntityType.ITEM_FRAME }
            if (itemFrames.isEmpty()) return
            val itemFrame = itemFrames.first() as? ItemFrame ?: return
            val item = itemFrame.item

            itemFrame.remove()
            block.type = Material.AIR
            block.world.dropItemNaturally(block.location, item)
            location.world.playSound(location, Sound.BLOCK_WOOD_BREAK, 1.0f, 1.0f)
        }

        val item = when (event.hand) {
            EquipmentSlot.HAND -> event.player.inventory.itemInMainHand
            EquipmentSlot.OFF_HAND -> event.player.inventory.itemInOffHand
            else -> return
        }
        if (event.action == Action.RIGHT_CLICK_BLOCK) {
            val blockFace = event.blockFace ?: return
            if (item.type != Material.LEATHER_HORSE_ARMOR) return
            if (item.itemMeta?.hasCustomModelData() != true) return

            val location = block.location.clone().add(blockFace.direction)

            val itemFrame = try {
                location.world.spawnEntity(location, EntityType.ITEM_FRAME) as ItemFrame
            } catch (e: java.lang.IllegalArgumentException) {
                return
            }
            itemFrame.setFacingDirection(blockFace, true)
            itemFrame.isFixed = true
            itemFrame.isVisible = false
            itemFrame.rotation = when (Math.floorMod((event.player.location.yaw / 45).roundToInt(), 8)) {
                0 -> Rotation.FLIPPED
                1 -> Rotation.FLIPPED_45
                2 -> Rotation.COUNTER_CLOCKWISE
                3 -> Rotation.COUNTER_CLOCKWISE_45
                4 -> Rotation.NONE
                5 -> Rotation.CLOCKWISE_45
                6 -> Rotation.CLOCKWISE
                7 -> Rotation.CLOCKWISE_135
                else -> Rotation.NONE
            }

            val furnitureItem = item.clone()
            furnitureItem.amount = 1
            item.amount -= 1
            itemFrame.setItem(furnitureItem, false)

            location.block.type = Material.BARRIER
            location.world.playSound(location, Sound.BLOCK_WOOD_PLACE, 1.0f, 1.0f)
        }
    }
}
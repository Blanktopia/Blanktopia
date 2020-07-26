package me.weiwen.blanktopia.actions

import me.weiwen.blanktopia.EMPTY_BLOCKS
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.block.data.Directional
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

class PlaceBlockAction(material: Material) : Action {
    override fun run(player: Player, item: ItemStack, block: Block, face: BlockFace) {
        when (item.type) {
            Material.TORCH -> torch(player, item, block, face)
            else -> return
        }
    }

    private fun torch(player: Player, item: ItemStack, block: Block, face: BlockFace) {
        val (block, target, face) = when (block.type) {
            Material.GRASS, Material.TALL_GRASS, Material.FERN, Material.LARGE_FERN -> Triple(
                block.getRelative(
                    BlockFace.DOWN
                ), block, BlockFace.DOWN
            )
            else -> Triple(block, block.getRelative(face), face)
        }
        if (!EMPTY_BLOCKS.contains(target.type)) return
        val state = target.state
        if (block.type.isSolid && face != BlockFace.DOWN) {
            when (face) {
                BlockFace.EAST, BlockFace.WEST, BlockFace.SOUTH, BlockFace.NORTH -> {
                    state.type = Material.WALL_TORCH
                    val data = Bukkit.getServer().createBlockData(Material.WALL_TORCH)
                    (data as? Directional)?.facing = face
                    state.blockData = data
                }
                else -> {
                    state.type = Material.TORCH
                    val data = Bukkit.getServer().createBlockData(Material.TORCH)
                    state.blockData = data
                }
            }
        } else {
            var canPlace = false
            for (tryFace in listOf(BlockFace.EAST, BlockFace.WEST, BlockFace.SOUTH, BlockFace.NORTH)) {
                if (block.getRelative(tryFace).type.isSolid) {
                    state.type = Material.WALL_TORCH
                    val data = Bukkit.getServer().createBlockData(Material.WALL_TORCH)
                    (data as? Directional)?.facing = tryFace
                    state.blockData = data
                    canPlace = true
                    break
                }
            }
            if (block.getRelative(BlockFace.DOWN).type.isSolid) {
                state.type = Material.TORCH
                val data = Bukkit.getServer().createBlockData(Material.TORCH)
                state.blockData = data
                canPlace = true
            }
            if (!canPlace) return
        }
        val buildEvent = BlockPlaceEvent(
            target,
            state,
            block,
            item,
            player,
            true,
            EquipmentSlot.HAND
        )
        Bukkit.getPluginManager().callEvent(buildEvent)
        if (buildEvent.isCancelled) return
        state.update(true)
    }
}
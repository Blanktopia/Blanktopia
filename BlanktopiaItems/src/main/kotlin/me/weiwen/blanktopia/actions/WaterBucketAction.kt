package me.weiwen.blanktopia.actions

import me.weiwen.blanktopia.EMPTY_BLOCKS
import me.weiwen.blanktopia.canBuildAt
import org.bukkit.Bukkit
import org.bukkit.FluidCollisionMode
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.block.data.Levelled
import org.bukkit.block.data.Waterlogged
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class WaterBucketAction() : Action {
    override fun run(player: Player, item: ItemStack) {
        if (player.world.environment == World.Environment.NETHER) return
        val result = player.rayTraceBlocks(5.0, FluidCollisionMode.NEVER) ?: return
        val block = result.hitBlock ?: return
        val face = result.hitBlockFace ?: return
        val data = block.blockData
        if (block.type == Material.WATER || block.type == Material.LAVA) {
            block.type = Material.AIR
        } else if (block.type == Material.CAULDRON) {
            val data = block.blockData
            (data as Levelled).level = 0
            block.blockData = data
        } else if (data is Waterlogged) {
            data.isWaterlogged = !data.isWaterlogged
            block.blockData = data
        } else {
            val target = block.getRelative(face)
            if (target.type == null || !EMPTY_BLOCKS.contains(target.type)) return
            val state = target.state
            state.type = Material.WATER
            val data = Bukkit.getServer().createBlockData(Material.WATER)
            (data as? Levelled)?.level = 0
            state.blockData = data
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
}
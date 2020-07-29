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

        val result = player.rayTraceBlocks(5.0, FluidCollisionMode.SOURCE_ONLY) ?: return
        val block = result.hitBlock ?: return
        if (!player.canBuildAt(block.location)) return
        val state = block.state
        val data = state.blockData

        if (block.type == Material.WATER || block.type == Material.LAVA) {
            state.type = Material.AIR
            state.update(true)
            return
        } else if (block.type == Material.CAULDRON) {
            if (data is Levelled) {
                data.level = if (data.level == 0) { 3 } else { 0 }
            }
            state.blockData = data
            state.update(true)
            return
        } else if (data is Waterlogged) {
            data.isWaterlogged = !data.isWaterlogged
            state.blockData = data
            state.update()
            return
        } else {
            val face = result.hitBlockFace ?: return
            val target = block.getRelative(face)
            if (!player.canBuildAt(target.location)) return
            if (!EMPTY_BLOCKS.contains(target.type)) return
            val state = target.state
            state.type = Material.WATER
            val data = Bukkit.getServer().createBlockData(Material.WATER)
            (data as? Levelled)?.level = 0
            state.blockData = data
            state.update(true)
        }
    }
}
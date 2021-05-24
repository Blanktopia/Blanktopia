package me.weiwen.blanktopia.actions

import me.weiwen.blanktopia.EMPTY_BLOCKS
import me.weiwen.blanktopia.canBuildAt
import org.bukkit.Bukkit
import org.bukkit.FluidCollisionMode
import org.bukkit.Material
import org.bukkit.block.data.Levelled
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class LavaBucketAction() : Action {
    override fun run(player: Player, item: ItemStack) {
        val result = player.rayTraceBlocks(5.0, FluidCollisionMode.SOURCE_ONLY) ?: return
        val block = result.hitBlock ?: return
        if (!player.canBuildAt(block.location)) return
        val state = block.state
        val data = state.blockData

        if (block.type == Material.WATER || block.type == Material.LAVA) {
            state.type = Material.AIR
            state.update(true)
            return
        } else {
            val face = result.hitBlockFace ?: return
            val target = block.getRelative(face)
            if (!player.canBuildAt(target.location)) return
            if (!EMPTY_BLOCKS.contains(target.type)) return
            val state = target.state
            state.type = Material.LAVA
            val data = Bukkit.getServer().createBlockData(Material.LAVA)
            (data as? Levelled)?.level = 0
            state.blockData = data
            state.update(true)
        }
    }
}
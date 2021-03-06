package me.weiwen.blanktopia.actions

import me.weiwen.blanktopia.canBuildAt
import me.weiwen.blanktopia.canMineBlock
import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector

class HammerAction(val range: Int, val depth: Int) : Action {
    override fun run(player: Player, item: ItemStack, block: Block) {
        val face = player.rayTraceBlocks(6.0)?.hitBlockFace ?: return
        if (!item.type.canMineBlock(block)) return
        val hardness = block.type.hardness
        for (location in locationsInRange(block.location, face, range)) {
            if (!player.canBuildAt(location)) continue
            val other = location.block
            if (other.type.hardness > hardness) continue
            if (!item.type.canMineBlock(other)) continue
            other.breakNaturally(item)
        }
    }

    private fun locationsInRange(origin: Location, face: BlockFace, range: Int): MutableList<Location> {
        val (xOffset, yOffset, zOffset) = if (face.modX != 0) {
            Triple(Vector(0, 1, 0), Vector(0, 0, 1), Vector(1, 0, 0))
        } else if (face.modY != 0) {
            Triple(Vector(1, 0, 0), Vector(0, 0, 1), Vector(0, 1, 0))
        } else {
            Triple(Vector(1, 0, 0), Vector(0, 1, 0), Vector(0, 0, 1))
        }
        val locations: MutableList<Location> = mutableListOf()
        for (x in -range .. range) {
            for (y in -range .. range) {
                for (z in -depth .. depth) {
                    locations.add(
                            origin.clone()
                                    .add(xOffset.clone().multiply(x))
                                    .add(yOffset.clone().multiply(y))
                                    .add(zOffset.clone().multiply(z)))
                }
            }
        }
        return locations
    }

}
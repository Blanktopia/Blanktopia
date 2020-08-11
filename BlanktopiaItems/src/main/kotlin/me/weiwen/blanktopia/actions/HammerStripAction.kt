package me.weiwen.blanktopia.actions

import me.weiwen.blanktopia.canBuildAt
import me.weiwen.blanktopia.playSoundAt
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.block.data.BlockData
import org.bukkit.block.data.Orientable
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector

class HammerStripAction(val range: Int) : Action {
    override fun run(player: Player, item: ItemStack, block: Block) {
        val face = player.rayTraceBlocks(6.0)?.hitBlockFace ?: return
        var didStrip = false
        loop@ for (location in locationsInRange(block.location, face, range)) {
            if (!player.canBuildAt(location)) continue
            val other = location.block
            val stripped = when (other.type) {
                Material.OAK_LOG -> Material.STRIPPED_OAK_LOG
                Material.OAK_WOOD -> Material.STRIPPED_OAK_WOOD
                Material.SPRUCE_LOG -> Material.STRIPPED_SPRUCE_LOG
                Material.SPRUCE_WOOD -> Material.STRIPPED_SPRUCE_WOOD
                Material.BIRCH_LOG -> Material.STRIPPED_BIRCH_LOG
                Material.BIRCH_WOOD -> Material.STRIPPED_BIRCH_WOOD
                Material.JUNGLE_LOG -> Material.STRIPPED_JUNGLE_LOG
                Material.JUNGLE_WOOD -> Material.STRIPPED_JUNGLE_WOOD
                Material.ACACIA_LOG -> Material.STRIPPED_ACACIA_LOG
                Material.ACACIA_WOOD -> Material.STRIPPED_ACACIA_WOOD
                Material.DARK_OAK_LOG -> Material.STRIPPED_DARK_OAK_LOG
                Material.DARK_OAK_WOOD -> Material.STRIPPED_DARK_OAK_WOOD
                Material.CRIMSON_STEM -> Material.STRIPPED_CRIMSON_STEM
                Material.CRIMSON_HYPHAE -> Material.STRIPPED_CRIMSON_HYPHAE
                Material.WARPED_STEM -> Material.STRIPPED_WARPED_STEM
                Material.WARPED_HYPHAE -> Material.STRIPPED_WARPED_HYPHAE
                else -> continue@loop
            }
            didStrip = true
            val data = other.blockData
            other.type = stripped
            val newData = other.blockData
            if (data is Orientable && newData is Orientable) {
                newData.axis = data.axis
                other.blockData = newData
            }
        }
        if (didStrip) {
            player.playSoundAt(Sound.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1.0f, 1.0f)
        }
    }

    private fun locationsInRange(origin: Location, face: BlockFace, range: Int): MutableList<Location> {
        val (xOffset, yOffset) = if (face.modX != 0) {
            Pair(Vector(0, 1, 0), Vector(0, 0, 1))
        } else if (face.modY != 0) {
            Pair(Vector(1, 0, 0), Vector(0, 0, 1))
        } else {
            Pair(Vector(1, 0, 0), Vector(0, 1, 0))
        }
        val locations: MutableList<Location> = mutableListOf()
        for (x in -range .. range) {
            for (y in -range .. range) {
                locations.add(origin.clone().add(xOffset.clone().multiply(x)).add(yOffset.clone().multiply(y)))
            }
        }
        return locations
    }

}
package me.weiwen.blanktopia.actions

import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class InfinityAction : Action {
    private val infiniteWaterBucketAction = WaterBucketAction()
    private val throwEnderPearlAction = ThrowEnderPearlAction()
    private val placeTorchAction = PlaceBlockAction(Material.TORCH)

    override fun run(player: Player, item: ItemStack) {
        when (item.type) {
            Material.WATER_BUCKET -> infiniteWaterBucketAction.run(player, item)
            Material.ENDER_PEARL -> throwEnderPearlAction.run(player, item)
        }
    }

    override fun run(player: Player, item: ItemStack, block: Block, face: BlockFace) {
        when (item.type) {
            Material.WATER_BUCKET -> infiniteWaterBucketAction.run(player, item)
            Material.ENDER_PEARL -> throwEnderPearlAction.run(player, item)
            Material.TORCH -> placeTorchAction.run(player, item, block, face)
        }
    }
}

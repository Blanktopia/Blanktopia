package me.weiwen.blanktopia.actions

import me.weiwen.blanktopia.canMineBlock
import me.weiwen.blanktopia.playSoundAt
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class CycleToolAction(private val materials: List<Material>) : Action {
    override fun run(player: Player, item: ItemStack) {
        val index = materials.indexOf(item.type)
        val offset = if (player.isSneaking) { 1 } else { -1 }
        item.type = materials[(index + offset) % materials.size]
        player.playSoundAt(Sound.ITEM_ARMOR_EQUIP_DIAMOND, SoundCategory.PLAYERS, 1.0f, 1.0f)
    }

    override fun run(player: Player, item: ItemStack, block: Block) {
        if (item.type.canMineBlock(block)) return

        for (material in materials) {
            if (material.canMineBlock(block)) {
                item.type = material
                player.playSoundAt(Sound.ITEM_ARMOR_EQUIP_DIAMOND, SoundCategory.PLAYERS, 1.0f, 1.0f)
                return
            }
        }
    }
}
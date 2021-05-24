package me.weiwen.blanktopia.actions

import me.weiwen.blanktopia.playSoundAt
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.entity.Entity
import org.bukkit.entity.ItemFrame
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class ToggleItemFrameVisibilityAction : Action {
    override fun run(player: Player, item: ItemStack, entity: Entity) {
        val itemFrame = entity as? ItemFrame ?: return
        itemFrame.isVisible = !itemFrame.isVisible
        entity.playSoundAt(Sound.BLOCK_GRINDSTONE_USE, SoundCategory.PLAYERS, 1.0f, 2.0f)
    }
}

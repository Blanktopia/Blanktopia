package me.weiwen.blanktopia.actions

import me.weiwen.blanktopia.BLACKLISTED_BLOCKS
import me.weiwen.blanktopia.EMPTY_BLOCKS
import me.weiwen.blanktopia.playSoundAt
import me.weiwen.blanktopia.playSoundTo
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

class PlaceRandomBlockAction : Action {
    override fun run(player: Player, item: ItemStack, block: Block, face: BlockFace) {
        val target = block.getRelative(face)
        if (!EMPTY_BLOCKS.contains(target.type)) return
        if (BLACKLISTED_BLOCKS.contains(block.type)) return
        for (slot in (0..8).toMutableList().shuffled()) {
            val item = player.inventory.getItem(slot) ?: continue
            if (!item.type.isBlock) continue

            val state = target.state
            state.type = item.type
            val data = Bukkit.getServer().createBlockData(item.type)
            state.blockData = data
            val cost = item.clone()
            cost.amount = 1
            val buildEvent = BlockPlaceEvent(
                target,
                state,
                block,
                cost,
                player,
                true,
                EquipmentSlot.HAND
            )
            Bukkit.getPluginManager().callEvent(buildEvent)
            if (buildEvent.isCancelled) {
                continue
            }
            if (player.gameMode != GameMode.CREATIVE) player.inventory.removeItem(cost)
            state.update(true)
            target.playSoundAt(target.soundGroup.placeSound, SoundCategory.BLOCKS, 1.0f, 1.0f)
            return
        }
        player.playSoundTo(Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, SoundCategory.PLAYERS, 1.0f, 1.0f)
    }
}

package me.weiwen.blanktopia.actions

import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class AllPlayersAction(private val actions: List<Action>) : Action {
    override fun run(player: Player, item: ItemStack) {
        player.server.onlinePlayers.forEach { other ->
            actions.forEach { it.run(other, item) }
        }
    }

    override fun run(player: Player, item: ItemStack, block: Block) {
        player.server.onlinePlayers.forEach { other ->
            actions.forEach { it.run(other, item, block) }
        }
    }

    override fun run(player: Player, item: ItemStack, entity: Entity) {
        player.server.onlinePlayers.forEach { other ->
            actions.forEach { it.run(other, item, entity) }
        }
    }

    override fun run(player: Player, item: ItemStack, block: Block, face: BlockFace) {
        player.server.onlinePlayers.forEach { other ->
            actions.forEach { it.run(other, item, block, face) }
        }
    }
}

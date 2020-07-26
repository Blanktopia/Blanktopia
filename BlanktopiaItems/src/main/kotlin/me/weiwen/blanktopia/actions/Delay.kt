package me.weiwen.blanktopia.actions

import me.weiwen.blanktopia.BlanktopiaItems
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class DelayAction(private val ticks: Long, private val actions: List<Action>) : Action {
    override fun run(player: Player, item: ItemStack) {
        BlanktopiaItems.INSTANCE.server.scheduler.runTaskLater(BlanktopiaItems.INSTANCE, { ->
            actions.forEach { it.run(player, item) }
        }, ticks)
    }

    override fun run(player: Player, item: ItemStack, block: Block) {
        BlanktopiaItems.INSTANCE.server.scheduler.runTaskLater(BlanktopiaItems.INSTANCE, { ->
            actions.forEach { it.run(player, item, block) }
        }, ticks)
    }

    override fun run(player: Player, item: ItemStack, entity: Entity) {
        BlanktopiaItems.INSTANCE.server.scheduler.runTaskLater(BlanktopiaItems.INSTANCE, { ->
            actions.forEach { it.run(player, item, entity) }
        }, ticks)
    }

    override fun run(player: Player, item: ItemStack, block: Block, face: BlockFace) {
        BlanktopiaItems.INSTANCE.server.scheduler.runTaskLater(BlanktopiaItems.INSTANCE, { ->
            actions.forEach { it.run(player, item, block, face) }
        }, ticks)
    }
}

package me.weiwen.blanktopia.actions

import me.weiwen.blanktopia.BlanktopiaItems
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class RepeatAction(private val delay: Int, private val interval: Int, private val count: Int, private val actions: List<Action>) : Action {
    override fun run(player: Player, item: ItemStack) {
        BlanktopiaItems.INSTANCE.server.scheduler.runTaskLater(BlanktopiaItems.INSTANCE, { ->
            actions.forEach { it.run(player, item) }
        }, delay.toLong())
        for (i in 0..count) {
            BlanktopiaItems.INSTANCE.server.scheduler.runTaskLater(BlanktopiaItems.INSTANCE, { ->
                actions.forEach { it.run(player, item) }
            }, delay.toLong() + interval + i * interval)
        }
    }

    override fun run(player: Player, item: ItemStack, block: Block) {
        BlanktopiaItems.INSTANCE.server.scheduler.runTaskLater(BlanktopiaItems.INSTANCE, { ->
            actions.forEach { it.run(player, item, block) }
        }, delay.toLong())
        for (i in 0..count) {
            BlanktopiaItems.INSTANCE.server.scheduler.runTaskLater(BlanktopiaItems.INSTANCE, { ->
                actions.forEach { it.run(player, item, block) }
            }, delay.toLong() + interval + i * interval)
        }
    }

    override fun run(player: Player, item: ItemStack, entity: Entity) {
        BlanktopiaItems.INSTANCE.server.scheduler.runTaskLater(BlanktopiaItems.INSTANCE, { ->
            actions.forEach { it.run(player, item, entity) }
        }, delay.toLong())
        for (i in 0..count) {
            BlanktopiaItems.INSTANCE.server.scheduler.runTaskLater(BlanktopiaItems.INSTANCE, { ->
                actions.forEach { it.run(player, item, entity) }
            }, delay.toLong() + interval + i * interval)
        }
    }

    override fun run(player: Player, item: ItemStack, block: Block, face: BlockFace) {
        BlanktopiaItems.INSTANCE.server.scheduler.runTaskLater(BlanktopiaItems.INSTANCE, { ->
            actions.forEach { it.run(player, item, block, face) }
        }, delay.toLong())
        for (i in 0..count) {
            BlanktopiaItems.INSTANCE.server.scheduler.runTaskLater(BlanktopiaItems.INSTANCE, { ->
                actions.forEach { it.run(player, item, block, face) }
            }, delay.toLong() + interval + i * interval)
        }
    }
}

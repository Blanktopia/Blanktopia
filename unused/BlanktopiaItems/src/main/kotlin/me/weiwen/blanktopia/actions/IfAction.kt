package me.weiwen.blanktopia.actions

import me.weiwen.blanktopia.conditions.Condition
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class IfAction(private val condition: Condition, private val ifTrue: List<Action>, private val ifFalse: List<Action>) : Action {
    override fun run(player: Player, item: ItemStack) {
        if (condition.test(player, item)) {
            ifTrue.forEach { it.run(player, item) }
        } else {
            ifFalse.forEach { it.run(player, item) }
        }
    }

    override fun run(player: Player, item: ItemStack, block: Block) {
        if (condition.test(player, item)) {
            ifTrue.forEach { it.run(player, item, block) }
        } else {
            ifFalse.forEach { it.run(player, item, block) }
        }
    }

    override fun run(player: Player, item: ItemStack, entity: Entity) {
        if (condition.test(player, item)) {
            ifTrue.forEach { it.run(player, item, entity) }
        } else {
            ifFalse.forEach { it.run(player, item, entity) }
        }
    }

    override fun run(player: Player, item: ItemStack, block: Block, face: BlockFace) {
        if (condition.test(player, item)) {
            ifTrue.forEach { it.run(player, item, block, face) }
        } else {
            ifFalse.forEach { it.run(player, item, block, face) }
        }
    }
}

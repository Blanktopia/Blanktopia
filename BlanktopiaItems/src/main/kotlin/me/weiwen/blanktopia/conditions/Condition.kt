package me.weiwen.blanktopia.conditions

import me.weiwen.blanktopia.BlanktopiaItems
import me.weiwen.blanktopia.Node
import me.weiwen.blanktopia.tryGet
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.logging.Level

interface Condition {
    fun test(player: Player, item: ItemStack): Boolean
}

fun parseConditions(nodes: List<Node>): List<Condition> {
    return nodes.mapNotNull { parseCondition(it) }
}

fun parseCondition(node: Node): Condition? {
    return when (val condition = node.tryGet<String>("condition")) {
        "not" -> node.tryGet<Node>("not")?.let {
            parseCondition(it)?.let { NotCondition(it) }
        }
        "any" -> node.tryGet<List<Node>>("any")?.let { AnyCondition(parseConditions(it)) }
        "all" -> node.tryGet<List<Node>>("all")?.let { AllCondition(parseConditions(it)) }

        "is-sneaking" -> IsSneakingCondition()
        "item-cooldown" -> ItemCooldownCondition()
        else -> {
            BlanktopiaItems.INSTANCE.logger.log(Level.WARNING, "Unrecognized condition '$condition' when parsing custom item")
            null
        }
    }
}


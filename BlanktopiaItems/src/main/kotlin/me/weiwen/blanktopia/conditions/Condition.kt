package me.weiwen.blanktopia.conditions

import me.weiwen.blanktopia.BlanktopiaItems
import me.weiwen.blanktopia.Node
import me.weiwen.blanktopia.tryGet
import org.bukkit.Material
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

        "is-in-world" -> {
            val world = node.tryGet<String>("world") ?: return null
            IsInWorld(world)
        }

        "is-sneaking" -> IsSneakingCondition()
        "is-sprinting" -> IsSneakingCondition()
        "is-flying" -> IsSneakingCondition()
        "is-on-ground" -> IsSneakingCondition()

        "can-build-at" -> CanBuildCondition()
        "has-build-trust" -> {
            val material = node.tryGet<String?>("material", null)?.let { Material.valueOf(it) } ?: Material.DIRT
            HasBuildTrustCondition(material)
        }

        "item-cooldown" -> ItemCooldownCondition()
        else -> {
            BlanktopiaItems.INSTANCE.logger.log(Level.WARNING, "Unrecognized condition '$condition' when parsing custom item")
            null
        }
    }
}


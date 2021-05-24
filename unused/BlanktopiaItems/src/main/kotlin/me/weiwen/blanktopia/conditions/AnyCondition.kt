package me.weiwen.blanktopia.conditions

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class AnyCondition(private val conditions: List<Condition>) : Condition {
    override fun test(player: Player, item: ItemStack): Boolean {
        return conditions.any { it.test(player, item) }
    }
}
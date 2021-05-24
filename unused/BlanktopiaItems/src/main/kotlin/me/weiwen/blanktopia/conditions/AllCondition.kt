package me.weiwen.blanktopia.conditions

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class AllCondition(private val conditions: List<Condition>) : Condition {
    override fun test(player: Player, item: ItemStack): Boolean {
        return conditions.all { it.test(player, item) }
    }
}
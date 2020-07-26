package me.weiwen.blanktopia.conditions

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class NotCondition(private val condition: Condition) : Condition {
    override fun test(player: Player, item: ItemStack): Boolean {
        return !condition.test(player, item)
    }
}
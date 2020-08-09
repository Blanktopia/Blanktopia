package me.weiwen.blanktopia.conditions

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class IsOnGroundCondition : Condition {
    override fun test(player: Player, item: ItemStack): Boolean {
        return player.velocity.y == 0.0
    }
}
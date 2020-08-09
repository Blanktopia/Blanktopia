package me.weiwen.blanktopia.conditions

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class IsFlyingCondition : Condition {
    override fun test(player: Player, item: ItemStack): Boolean {
        return player.isFlying
    }
}
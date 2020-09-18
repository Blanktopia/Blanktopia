package me.weiwen.blanktopia.conditions

import me.weiwen.blanktopia.canBuildAt
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class CanBuildCondition() : Condition {
    override fun test(player: Player, item: ItemStack): Boolean {
        return player.canBuildAt(player.location)
    }
}
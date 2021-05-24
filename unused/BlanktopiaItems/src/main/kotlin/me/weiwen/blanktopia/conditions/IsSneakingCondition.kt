package me.weiwen.blanktopia.conditions

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class IsSneakingCondition : Condition {
    override fun test(player: Player, item: ItemStack): Boolean {
        return player.isSneaking
    }
}
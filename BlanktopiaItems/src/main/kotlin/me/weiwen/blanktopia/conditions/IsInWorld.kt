package me.weiwen.blanktopia.conditions

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class IsInWorld(val world: String) : Condition {
    override fun test(player: Player, item: ItemStack): Boolean {
        return player.world.name == world
    }
}
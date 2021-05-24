package me.weiwen.blanktopia.actions

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class ItemCooldownAction(private val ticks: Int) : Action {
    override fun run(player: Player, item: ItemStack) {
        player.setCooldown(item.type, ticks)
    }
}

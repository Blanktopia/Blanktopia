package me.weiwen.blanktopia.actions

import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player

class HealAction(val amount: Int) : Action {
    override fun run(player: Player) {
        val max = player.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.value ?: 20.0
        if (player.health + amount > max) {
            player.health = max
        } else {
            player.health += amount
        }
    }
}
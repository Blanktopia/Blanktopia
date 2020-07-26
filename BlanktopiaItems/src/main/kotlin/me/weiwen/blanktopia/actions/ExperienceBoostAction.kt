package me.weiwen.blanktopia.actions

import me.weiwen.blanktopia.addExperienceBoost
import org.bukkit.entity.Player

class ExperienceBoostAction(private val multiplier: Double, private val ticks: Long) : Action {
    override fun run(player: Player) {
        player.addExperienceBoost(multiplier, ticks)
    }
}

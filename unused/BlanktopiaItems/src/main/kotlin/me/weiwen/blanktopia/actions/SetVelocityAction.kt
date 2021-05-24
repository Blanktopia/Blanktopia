package me.weiwen.blanktopia.actions

import org.bukkit.entity.Player
import org.bukkit.util.Vector

class SetVelocityAction(private val x: Double?, private val y: Double?, private val z: Double?) : Action {
    override fun run(player: Player) {
        val vec = Vector(x ?: player.velocity.x, y ?: player.velocity.y, z ?: player.velocity.z)
        player.velocity = vec
    }
}
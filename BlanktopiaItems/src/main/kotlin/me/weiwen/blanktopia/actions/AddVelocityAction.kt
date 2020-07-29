package me.weiwen.blanktopia.actions

import org.bukkit.entity.Player
import org.bukkit.util.Vector

class AddVelocityAction(private val x: Double, private val y: Double, private val z: Double) : Action {
    override fun run(player: Player) {
        val vec = Vector(x, y, z)
        vec.rotateAroundY(player.location.yaw.toDouble())
        player.velocity = player.velocity.add(vec)
    }
}
package me.weiwen.blanktopia.actions

import org.bukkit.Particle
import org.bukkit.entity.Player

class SpawnParticleAction(
        particle: String,
        private val x: Double,
        private val y: Double,
        private val z: Double,
        private val count: Int,
        private val offsetX: Double,
        private val offsetY: Double,
        private val offsetZ: Double,
        private val extra: Double) : Action {
    private val particle = Particle.valueOf(particle)

    override fun run(player: Player) {
        player.world.spawnParticle(
            particle,
            player.location.add(x, y, z),
            count,
            offsetX,
            offsetY,
            offsetZ,
            extra
        )
    }
}
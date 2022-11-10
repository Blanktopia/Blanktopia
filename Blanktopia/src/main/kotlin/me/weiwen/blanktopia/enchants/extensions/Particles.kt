package me.weiwen.moromoro.extensions

import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.block.Block
import org.bukkit.entity.Entity

fun Location.spawnParticle(particle: Particle, count: Int, speed: Double) {
    world.spawnParticle(particle, x, y, z, count, 0.4, 0.4, 0.4, speed)
}

fun Block.spawnParticle(particle: Particle, count: Int, speed: Double) {
    world.spawnParticle(particle, x + 0.5, y + 0.5, z + 0.6, count, 0.4, 0.4, 0.4, speed)
}

fun Entity.spawnParticle(particle: Particle, count: Int, speed: Double) {
    world.spawnParticle(particle, location.x, location.y + height/2, location.z, count, 0.3, height/2, 0.0, speed)
}
package me.weiwen.moromoro.extensions

import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

fun Location.playSoundAt(sound: Sound, category: SoundCategory, volume: Float, pitch: Float) {
    world.playSound(this, sound, category, volume, pitch)
}

fun Entity.playSoundAt(sound: Sound, category: SoundCategory, volume: Float, pitch: Float) {
    world.playSound(Location(world, location.x, location.y + height/2, location.z), sound, category, volume, pitch)
}

fun Block.playSoundAt(sound: Sound, category: SoundCategory, volume: Float, pitch: Float) {
    world.playSound(Location(world, x + 0.5, y + 0.5, z + 0.5), sound, category, volume, pitch)
}

fun Player.playSoundTo(sound: Sound, category: SoundCategory, volume: Float, pitch: Float) {
    this.playSound(location.clone().add(0.0, height/2, 0.0), sound, category, volume, pitch)
}

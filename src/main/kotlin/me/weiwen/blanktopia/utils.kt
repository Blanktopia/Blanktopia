package me.weiwen.blanktopia

import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.entity.ExperienceOrb
import kotlin.math.sqrt

var ExperienceOrb.level: Double
    get() = if (experience < 352) {
        sqrt(experience + 9.0) - 3
    } else if (experience < 1507) {
        (sqrt(40 * experience - 7839.0) + 81) / 10
    } else {
        (sqrt(72 * experience - 54215.0) + 325) / 18
    }
    set(level: Double) {
        if (level < 16) {
            level * level + 6 * level
        } else if (level < 31) {
            2.5 * level * level - 40.5 * level + 360
        } else {
            4.5 * level * level - 162 * level + 2220
        }
    }

fun Int.toRomanNumerals(): String {
    val symbols =
        arrayOf("M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I")
    val numbers = intArrayOf(1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1)
    for (i in numbers.indices) {
        if (this >= numbers[i]) {
            return symbols[i] + (this - numbers[i]).toRomanNumerals()
        }
    }
    return ""
}

data class Trie<S, T>(var value: T?) {
    var children: MutableMap<S, Trie<S, T>> = mutableMapOf()

    operator fun set(key: S, child: Trie<S, T>) {
        children[key] = child
    }

    operator fun get(key: S): Trie<S, T>? {
        return children[key]
    }

    fun toList(): List<T> {
        if (value != null) {
            return listOf(value!!)
        } else {
            return children.values.flatMap { it.toList() }
        }
    }
}

fun spawnParticleAt(particle: Particle, entity: Entity, count: Int, speed: Double) {
    entity.world.spawnParticle(particle, entity.location.x, entity.location.y + entity.height/2, entity.location.z, count, 0.3, entity.height / 2, 0.0, speed)
}

fun spawnParticleAt(particle: Particle, block: Block, count: Int, speed: Double) {
    block.world.spawnParticle(particle, block.x + 0.5, block.y + 0.5, block.z + 0.6, count, 0.4, 0.4, 0.4, speed)
}

fun playSoundAt(sound: Sound, entity: Entity, category: SoundCategory, volume: Float, pitch: Float) {
    entity.world.playSound(Location(entity.world, entity.location.x, entity.location.y + entity.height/2, entity.location.z), sound, category, volume, pitch)
}

fun playSoundAt(sound: Sound, block: Block, category: SoundCategory, volume: Float, pitch: Float) {
    block.world.playSound(Location(block.world, block.x + 0.5, block.y + 0.5, block.z + 0.5), sound, category, volume, pitch)
}
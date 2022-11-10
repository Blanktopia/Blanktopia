package me.weiwen.blanktopia.enchants.extensions

import org.bukkit.entity.ExperienceOrb
import kotlin.math.sqrt
import kotlin.random.Random

var ExperienceOrb.level: Double
    get() = if (experience < 352) {
        sqrt(experience + 9.0) - 3
    } else if (experience < 1507) {
        (sqrt(40 * experience - 7839.0) + 81) / 10
    } else {
        (sqrt(72 * experience - 54215.0) + 325) / 18
    }
    set(level) {
        experience = (if (level < 16) {
            level * level + 6 * level
        } else if (level < 31) {
            2.5 * level * level - 40.5 * level + 360
        } else {
            4.5 * level * level - 162 * level + 2220
        }).toInt()
    }

fun ExperienceOrb.setExperience(experience: Double) {
    this.experience = experience.toInt() + if (Random.nextDouble() < experience % 1) { 1 } else { 0 }
}


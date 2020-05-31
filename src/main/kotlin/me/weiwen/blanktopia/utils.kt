package me.weiwen.blanktopia

import org.bukkit.block.Block
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Entity
import org.bukkit.entity.ExperienceOrb
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import kotlin.math.sqrt
import kotlin.random.Random
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.*
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import java.util.*

var ExperienceOrb.level: Double
    get() = if (experience < 352) {
        sqrt(experience + 9.0) - 3
    } else if (experience < 1507) {
        (sqrt(40 * experience - 7839.0) + 81) / 10
    } else {
        (sqrt(72 * experience - 54215.0) + 325) / 18
    }
    set(level: Double) {
        experience = (if (level < 16) {
            level * level + 6 * level
        } else if (level < 31) {
            2.5 * level * level - 40.5 * level + 360
        } else {
            4.5 * level * level - 162 * level + 2220
        }).toInt()
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

fun ItemStack.damage(damage: Int) {
    val meta = this.itemMeta ?: return
    val unbreaking = meta.getEnchantLevel(Enchantment.DURABILITY)
    if (meta is Damageable && (1.0 / (unbreaking + 1)) > Random.nextDouble()) {
            meta.damage += damage
    }
    this.itemMeta = meta
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

fun isInOwnClaim(player: Player, location: Location): Boolean {
    val claim = GriefPrevention.instance.dataStore.getClaimAt(location, true, null) ?: return false
    return claim.ownerID == player.uniqueId
}

fun isInTrustedClaim(player: Player, location: Location): Boolean {
    val claim = GriefPrevention.instance.dataStore.getClaimAt(location, true, null) ?: return false
    return claim.allowContainers(player) == null
}

fun canBuild(player: Player, location: Location): Boolean {
    return GriefPrevention.instance.allowBuild(player, location) == null
}

fun locationsInRange(origin: Location, face: BlockFace, range: Int): MutableList<Location> {
    val (xOffset, yOffset) = if (face.modX != 0) {
        Pair(Vector(0, 1, 0), Vector(0, 0, 1))
    } else if (face.modY != 0) {
        Pair(Vector(1, 0, 0), Vector(0, 0, 1))
    } else {
        Pair(Vector(1, 0, 0), Vector(0, 1, 0))
    }
    val locations: MutableList<Location> = mutableListOf()
    for (x in -range .. range) {
        for (y in -range .. range) {
            locations.add(origin.clone().add(xOffset.clone().multiply(x)).add(yOffset.clone().multiply(y)))
        }
    }
    return locations
}

fun playerHeadFromTexture(name: String, texture: String): ItemStack {
    val skull = ItemStack(Material.PLAYER_HEAD)
    val uuid = UUID(texture.hashCode().toLong(), texture.hashCode().toLong())
    Bukkit.getUnsafe().modifyItemStack(skull,
        "{SkullOwner:{Name:\"$name\",Id:\"$uuid\",Properties:{textures:[{Value:\"$texture\"}]}}}"
        // TODO: change in 1.16 {UUID:[I;1498693494,1027158888,1898994005,860320107]}
    )
    return skull
}

fun playerHeadFromUrl(name: String, url: String): ItemStack {
    val data = Base64.getEncoder().encode("{textures:{SKIN:{url:\"http://textures.minecraft.net/texture/$url\"}}}".toByteArray())
    return playerHeadFromTexture(name, String(data))
}


package me.weiwen.blanktopia.enchants.enchantments

import me.weiwen.blanktopia.enchants.*
import me.weiwen.moromoro.extensions.playSoundAt
import me.weiwen.moromoro.extensions.spawnParticle
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Entity
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

val FROST = CustomEnchantment(
    "frost",
    "Frost",
    3,
    SWORDS + AXES + BOOKS,
    NONE,
    0.2,
    10,
    15,
    4,
    { setOf<Enchantment>(STING, Enchantment.FIRE_ASPECT) },
    Frost
)

object Frost : Listener {
    init {}

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
        if (event.damage == 0.0) return
        val entity: Entity = event.entity
        val damager = event.damager
        if (event.cause != EntityDamageEvent.DamageCause.ENTITY_ATTACK && event.cause != EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) return
        if (entity is HumanEntity && entity.isBlocking) return
        if (entity is LivingEntity && damager is LivingEntity) {
            val weapon = damager.equipment?.itemInMainHand ?: return
            if (weapon.enchantments.containsKey(FROST)) {
                val level = weapon.enchantments[FROST] ?: 1
                entity.spawnParticle(Particle.SNOWBALL, 20, 0.01)
                if (event.cause == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                    entity.playSoundAt(Sound.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 0.5f, 0.1f)
                    entity.playSoundAt(Sound.ENTITY_SNOW_GOLEM_HURT, SoundCategory.PLAYERS, 0.5f, 1.5f)
                    entity.fireTicks = 0
                    entity.freezeTicks = minOf(entity.maxFreezeTicks, 60 * level)
                }
            }
        }
    }
}

package me.weiwen.blanktopia.enchants.enchantments

import me.weiwen.blanktopia.enchants.*
import me.weiwen.moromoro.extensions.spawnParticle
import org.bukkit.Particle
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

val STING: CustomEnchantment = CustomEnchantment(
    "sting",
    "Sting",
    3,
    SWORDS + AXES + BOOKS,
    NONE,
    0.2,
    10,
    15,
    4,
    { setOf<Enchantment>(FROST, Enchantment.FIRE_ASPECT) },
    Sting
)

object Sting : Listener {
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
            if (weapon.enchantments.containsKey(STING)) {
                val level = weapon.enchantments[STING]
                entity.spawnParticle(Particle.SNEEZE, 10, 0.01)
                entity.addPotionEffect(PotionEffect(PotionEffectType.POISON, 80,
                    when (level) {
                        0, 1 -> 0
                        2 -> 1
                        3 -> 4
                        else -> 4
                    })
                )
            }
        }
    }
}

package me.weiwen.blanktopia.enchants.enchantments

import me.weiwen.blanktopia.enchants.*
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Entity
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

val SPECTRAL = CustomEnchantment(
    "spectral",
    "Spectral",
    3,
    SWORDS + AXES + BOOKS,
    NONE,
    0.1,
    10,
    15,
    4,
    { setOf<Enchantment>() },
    Spectral
)

object Spectral : Listener {
    init {}

    @EventHandler
    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
        val entity: Entity = event.entity
        val damager = event.damager
        if (event.cause != EntityDamageEvent.DamageCause.ENTITY_ATTACK && event.cause != EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) return
        if (entity is HumanEntity && entity.isBlocking) return
        if (event.isCancelled) return
        if (entity is LivingEntity && damager is LivingEntity) {
            val weapon = damager.equipment?.itemInMainHand ?: return
            if (weapon.containsEnchantment(SPECTRAL)) {
                val level = weapon.getEnchantmentLevel(SPECTRAL)
                entity.addPotionEffect(PotionEffect(PotionEffectType.GLOWING, 20 + level * 20, 0, true))
            }
        }
    }
}

package me.weiwen.blanktopia.enchants.enchantments

import me.weiwen.blanktopia.enchants.*
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
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
        if (entity is LivingEntity && damager is LivingEntity) {
            val weapon = damager.equipment?.itemInMainHand ?: return
            if (weapon.containsEnchantment(SPECTRAL)) {
                val level = weapon.getEnchantmentLevel(SPECTRAL)
                entity.addPotionEffect(PotionEffect(PotionEffectType.GLOWING, 20 + level * 20, 0, true))
            }
        }
    }
}

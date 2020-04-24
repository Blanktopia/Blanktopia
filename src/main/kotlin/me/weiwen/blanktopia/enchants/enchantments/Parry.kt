package me.weiwen.blanktopia.enchants.enchantments

import me.weiwen.blanktopia.enchants.BOOKS
import me.weiwen.blanktopia.enchants.CustomEnchantment
import me.weiwen.blanktopia.enchants.NONE
import me.weiwen.blanktopia.enchants.SHIELDS
import me.weiwen.blanktopia.spawnParticleAt
import org.bukkit.Particle
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Entity
import org.bukkit.entity.HumanEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

val PARRY: CustomEnchantment = CustomEnchantment(
    "parry",
    "Parry",
    2,
    SHIELDS + BOOKS,
    NONE,
    0.2,
    10,
    15,
    4,
    { setOf<Enchantment>(RUSH) },
    Parry
)

object Parry : Listener {
    init {}

    @EventHandler
    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
        val entity: Entity = event.entity
        if (entity is HumanEntity && entity.isBlocking) {
            val weapon = entity.equipment?.itemInOffHand ?: return
            if (weapon.containsEnchantment(PARRY)) {
                val level = weapon.getEnchantmentLevel(PARRY)
                spawnParticleAt(Particle.CRIT, entity, 10, 0.01)
                if (event.isCancelled) return
                entity.addPotionEffect(PotionEffect(PotionEffectType.INCREASE_DAMAGE, 40, level - 1))
            }
        }
    }
}

package me.weiwen.blanktopia.enchants.enchantments

import me.weiwen.blanktopia.enchants.BOOKS
import me.weiwen.blanktopia.enchants.CustomEnchantment
import me.weiwen.blanktopia.enchants.NONE
import me.weiwen.blanktopia.enchants.SHIELDS
import me.weiwen.moromoro.extensions.spawnParticle
import org.bukkit.Particle
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Entity
import org.bukkit.entity.HumanEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

val RUSH: CustomEnchantment = CustomEnchantment(
    "rush",
    "Rush",
    2,
    SHIELDS + BOOKS,
    NONE,
    0.2,
    10,
    15,
    4,
    { setOf<Enchantment>(PARRY) },
    Rush
)

object Rush : Listener {
    init {}

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
        val entity: Entity = event.entity
        if (entity is HumanEntity && entity.isBlocking) {
            val weapon = entity.equipment?.itemInOffHand ?: return
            if (weapon.enchantments.containsKey(RUSH)) {
                val level = weapon.getEnchantmentLevel(RUSH)
                entity.spawnParticle(Particle.CRIT, 10, 0.01)
                if (event.isCancelled) return
                entity.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 40, level - 1))
            }
        }
    }
}

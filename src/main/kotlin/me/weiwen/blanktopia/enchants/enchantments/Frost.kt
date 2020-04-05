package me.weiwen.blanktopia.enchants.enchantments

import me.weiwen.blanktopia.enchants.*
import me.weiwen.blanktopia.playSoundAt
import me.weiwen.blanktopia.spawnParticleAt
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
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

    @EventHandler
    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
        val entity: Entity = event.entity
        val damager = event.damager
        if (entity is LivingEntity && damager is LivingEntity) {
            val weapon = damager.equipment?.itemInMainHand ?: return
            if (weapon.containsEnchantment(FROST)) {
                val level = weapon.getEnchantmentLevel(FROST)
                spawnParticleAt(Particle.SNOWBALL, entity, 20, 0.01)
                playSoundAt(Sound.BLOCK_GLASS_BREAK, entity, SoundCategory.PLAYERS, 0.5f, 0.1f)
                playSoundAt(Sound.ENTITY_SNOW_GOLEM_HURT, entity, SoundCategory.PLAYERS, 0.5f, 1.5f)
                entity.fireTicks = 0
                entity.addPotionEffect(PotionEffect(PotionEffectType.SLOW, 20 + level * 20, level))
            }
        }
    }
}

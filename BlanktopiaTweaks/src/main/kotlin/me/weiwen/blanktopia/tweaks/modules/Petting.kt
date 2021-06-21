package me.weiwen.blanktopia.tweaks.modules

import me.weiwen.blanktopia.tweaks.Module
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.attribute.Attributable
import org.bukkit.attribute.Attribute
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.util.Vector
import kotlin.math.min

class Petting(private val plugin: JavaPlugin) :
    Listener, Module {

    private var pettableEntities: Set<EntityType> = setOf()

    override fun enable() {
        plugin.server.pluginManager.registerEvents(this, plugin)
        reload()
    }

    override fun disable() {}

    override fun reload() {
        pettableEntities = plugin.config.getStringList("pettable-entities").map {
            EntityType.valueOf(it)
        }.toSet()
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onPlayerInteractEntity(event: PlayerInteractEntityEvent) {
        val entity = event.rightClicked

        if (entity.type !in pettableEntities) return

        if (!event.player.isSneaking) return

        if (entity is Damageable && entity is Attributable) {
            val maxHealth = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.value ?: return
            entity.health = min(entity.health + 1, maxHealth)
        }

        entity.world.spawnParticle(Particle.HEART, entity.location.add(0.0, entity.height + 0.2, 0.0), 1)

        event.isCancelled = true
    }
}

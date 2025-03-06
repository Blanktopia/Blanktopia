package me.weiwen.blanktopia.tweaks.modules

import me.weiwen.blanktopia.tweaks.Module
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDamageEvent.DamageCause
import org.bukkit.plugin.java.JavaPlugin

class ProtectNamedEntities(private val plugin: JavaPlugin) :
    Listener, Module {

    private var noDryoutEntities: Set<EntityType> = setOf()
    private var noDrownEntities: Set<EntityType> = setOf()
    private var noSuffocateEntities: Set<EntityType> = setOf()

    override fun enable() {
        plugin.server.pluginManager.registerEvents(this, plugin)
        reload()
    }

    override fun disable() {}

    override fun reload() {
        noDrownEntities = plugin.config.getStringList("no-drown-named-entities").map {
            EntityType.valueOf(it)
        }.toSet()
        noDryoutEntities = plugin.config.getStringList("no-dryout-named-entities").map {
            EntityType.valueOf(it)
        }.toSet()
        noSuffocateEntities = plugin.config.getStringList("no-suffocate-named-entities").map {
            EntityType.valueOf(it)
        }.toSet()
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onEntityDamage(event: EntityDamageEvent) {
        val entity = event.entity
        when (event.cause) {
            DamageCause.DROWNING -> if (entity.type !in noDrownEntities) return
            DamageCause.DRYOUT -> if (entity.type !in noDryoutEntities) return
            DamageCause.SUFFOCATION -> if (entity.type !in noSuffocateEntities) return
            else -> return
        }

        if (entity.customName() == null) return

        event.isCancelled = true
    }
}

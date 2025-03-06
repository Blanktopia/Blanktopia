package me.weiwen.blanktopia.tweaks.modules

import me.weiwen.blanktopia.tweaks.Module
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.entity.EntityBreedEvent
import org.bukkit.plugin.java.JavaPlugin
import java.util.concurrent.ThreadLocalRandom

class RandomSizedEntities(private val plugin: JavaPlugin) :
    Listener, Module {

    private var entitySpawnReasons: Set<CreatureSpawnEvent.SpawnReason> = setOf(
        CreatureSpawnEvent.SpawnReason.NATURAL,
        CreatureSpawnEvent.SpawnReason.BREEDING
    )
    private var randomSizedEntities: Set<EntityType> = setOf()

    override fun enable() {
        plugin.server.pluginManager.registerEvents(this, plugin)
        reload()
    }

    override fun disable() {}

    override fun reload() {
        randomSizedEntities = plugin.config.getStringList("random-sized-entities").map {
            EntityType.valueOf(it)
        }.toSet()
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onCreatureSpawn(event: CreatureSpawnEvent) {
        val entity = event.entity

        if (entity.type !in randomSizedEntities) return
        if (entity.entitySpawnReason !in entitySpawnReasons) return
        if (entity.entitySpawnReason == CreatureSpawnEvent.SpawnReason.BREEDING) return

        val scale = ThreadLocalRandom.current().nextGaussian(0.0, 0.08)
        val attribute = entity.getAttribute(Attribute.SCALE) ?: return
        attribute.addModifier(AttributeModifier(NamespacedKey(plugin, "scale"), scale, AttributeModifier.Operation.MULTIPLY_SCALAR_1))
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onBreedEvent(event: EntityBreedEvent) {
        val entity = event.entity

        if (entity.type !in randomSizedEntities) return
        if (CreatureSpawnEvent.SpawnReason.BREEDING !in entitySpawnReasons) return

        val fatherScale = event.father.getAttribute(Attribute.SCALE) ?: return
        val motherScale = event.mother.getAttribute(Attribute.SCALE) ?: return
        val baseScale = ((fatherScale.value + motherScale.value) / 2 - 1) * 0.5
        val scale = ThreadLocalRandom.current().nextGaussian(baseScale, 0.04)
        val attribute = entity.getAttribute(Attribute.SCALE) ?: return
        attribute.addModifier(AttributeModifier(NamespacedKey(plugin, "scale"), scale, AttributeModifier.Operation.MULTIPLY_SCALAR_1))
    }
}

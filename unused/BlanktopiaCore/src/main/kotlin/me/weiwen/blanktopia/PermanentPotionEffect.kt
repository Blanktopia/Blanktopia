package me.weiwen.blanktopia

import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitTask
import java.util.*

class PermanentPotionEffect(val plugin: JavaPlugin): Module {
    lateinit var task: BukkitTask

    companion object {
        lateinit var INSTANCE: PermanentPotionEffect
    }

    val potionEffectGroups: MutableMap<UUID, MutableMap<String, Map<PotionEffectType, Int>>> = mutableMapOf()

    override fun enable() {
        INSTANCE = this
        task = plugin.server.scheduler.runTaskTimer(plugin, ::applyToAllPlayers as (() -> Unit), 100, 100)
    }

    override fun disable() {
        task.cancel()
    }

    override fun reload() {}

    private fun applyToAllPlayers() {
        for (world in plugin.server.worlds) {
            if (world.name.startsWith("DXL_Game_")) {
                continue
            }
            for (player in world.players) {
                val playerPotionEffectGroups = potionEffectGroups[player.uniqueId] ?: continue
                for (potionEffects in playerPotionEffectGroups.values) {
                    for ((type, level) in potionEffects.entries) {
                        player.addPotionEffect(
                            PotionEffect(
                                type,
                                619,
                                level,
                                true
                            )
                        )
                    }
                }
            }
        }
    }
}

fun Player.addPermanentPotionEffects(key: String, effects: Map<PotionEffectType, Int>) {
    PermanentPotionEffect.INSTANCE.potionEffectGroups.getOrPut(uniqueId, { mutableMapOf() })[key] = effects
    for ((type, level) in effects.entries) {
        addPotionEffect(
            PotionEffect(
                type,
                619,
                level,
                true
            )
        )
    }
}

fun Player.removePermanentPotionEffects(key: String) {
    val effects = PermanentPotionEffect.INSTANCE.potionEffectGroups[uniqueId]?.get(key) ?: return
    for ((type, level) in effects.entries) {
        if (getPotionEffect(type)?.amplifier == level) {
            removePotionEffect(type)
        }
    }
    PermanentPotionEffect.INSTANCE.potionEffectGroups[uniqueId]?.remove(key)
}

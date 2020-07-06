package me.weiwen.blanktopia.items.listeners

import me.weiwen.blanktopia.Blanktopia
import me.weiwen.blanktopia.enchants.enchantments.NightVision
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitTask
import java.util.*

class PotionEffect(val plugin: Blanktopia) {
    lateinit var task: BukkitTask

    val potionEffectGroups: MutableMap<UUID, MutableMap<String, Map<PotionEffectType, Int>>> = mutableMapOf()

    fun enable() {
        task = plugin.server.scheduler.runTaskTimer(plugin, ::applyToAllPlayers as (() -> Unit), 100, 100)

        NightVision.enable(plugin)
    }

    fun disable() {
        task.cancel()
    }

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

    fun addPotionEffects(player: Player, key: String, effects: Map<PotionEffectType, Int>) {
        potionEffectGroups.getOrPut(player.uniqueId, { mutableMapOf() })[key] = effects
        for ((type, level) in effects.entries) {
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

    fun removePotionEffects(player: Player, key: String) {
        val effects = potionEffectGroups[player.uniqueId]?.get(key) ?: return
        for ((type, level) in effects.entries) {
            if (player.getPotionEffect(type)?.amplifier == level) {
                player.removePotionEffect(type)
            }
        }
        potionEffectGroups[player.uniqueId]?.remove(key)
    }
}

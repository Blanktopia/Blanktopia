package me.weiwen.blanktopia.items.listeners

import me.weiwen.blanktopia.Blanktopia
import org.bukkit.NamespacedKey
import org.bukkit.persistence.PersistentDataType
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitTask

class PotionEffect(val plugin: Blanktopia) {
    lateinit var task: BukkitTask

    fun enable() {
        task = plugin.server.scheduler.runTaskTimer(plugin, ::applyToAllPlayers as (() -> Unit), 100, 100)
    }

    fun disable() {
        task.cancel()
    }

    private fun applyToAllPlayers() {
        for (player in plugin.server.onlinePlayers) {
            val potionEffectType = player.persistentDataContainer.get(NamespacedKey(plugin, "potion-effect-type"), PersistentDataType.STRING) ?: continue
            val potionEffectLevel = player.persistentDataContainer.get(NamespacedKey(plugin, "potion-effect-level"), PersistentDataType.INTEGER) ?: 0
            player.addPotionEffect(PotionEffect(PotionEffectType.getByName(potionEffectType) ?: continue, 619, potionEffectLevel, true))
        }
    }
}

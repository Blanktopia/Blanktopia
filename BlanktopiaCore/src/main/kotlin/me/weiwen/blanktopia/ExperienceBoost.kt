package me.weiwen.blanktopia

import com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

data class ExperienceBoostData(
    val multiplier: Double,
    val expiry: Long
)

class ExperienceBoost(val plugin: JavaPlugin): Module, Listener {
    val experienceBoosts: MutableMap<UUID, ExperienceBoostData> = mutableMapOf()

    companion object {
        lateinit var INSTANCE: ExperienceBoost
    }

    override fun enable() {
        INSTANCE = this
        plugin.server.pluginManager.registerEvents(this, plugin)
    }

    override fun disable() {}

    override fun reload() {}

    @EventHandler
    fun onExperiencePickUp(event: PlayerPickupExperienceEvent) {
        val player = event.player
        val experienceBoost = experienceBoosts[player.uniqueId] ?: return

        if (experienceBoost.expiry < System.currentTimeMillis()) {
            experienceBoosts.remove(player.uniqueId)
            return
        }

        event.experienceOrb.setExperience(event.experienceOrb.experience * experienceBoost.multiplier)
    }
}

fun Player.addExperienceBoost(multiplier: Double, ticks: Long) {
    ExperienceBoost.INSTANCE.experienceBoosts.put(
        uniqueId,
        ExperienceBoostData(multiplier, System.currentTimeMillis() + ticks * 50)
    )
}
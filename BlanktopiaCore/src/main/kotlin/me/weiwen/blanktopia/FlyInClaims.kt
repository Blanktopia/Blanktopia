package me.weiwen.blanktopia

import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerTeleportEvent
import java.util.*

class FlyInClaims(val plugin: BlanktopiaCore) : Listener, Module {
    val canFlyPlayers: MutableSet<UUID> = mutableSetOf()

    companion object {
        lateinit var INSTANCE: FlyInClaims
    }

    override fun enable() {
        INSTANCE = this
        plugin.server.pluginManager.registerEvents(this, plugin)
    }

    override fun disable() {}

    override fun reload() {}

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        checkCanFly(event.player, event.to, event.from)
    }

    @EventHandler
    fun onPlayerTeleport(event: PlayerTeleportEvent) {
        checkCanFly(event.player, event.to, event.from)
    }

    private fun checkCanFly(player: Player, to: Location?, from: Location) {
        if (to == null) return
        if (to.blockX == from.blockX && to.blockZ == from.blockZ) return
        if (player.gameMode == GameMode.CREATIVE || player.gameMode == GameMode.SPECTATOR) return
        if (player.canFlyInClaims) {
            player.allowFlight = player.hasAccessTrust(to)
        }
    }
}

var Player.canFlyInClaims: Boolean
    get() = FlyInClaims.INSTANCE.canFlyPlayers.contains(uniqueId)
    set(canFly) {
        if (canFly) {
            FlyInClaims.INSTANCE.canFlyPlayers.add(uniqueId)
        } else {
            FlyInClaims.INSTANCE.canFlyPlayers.remove(uniqueId)
        }
    }

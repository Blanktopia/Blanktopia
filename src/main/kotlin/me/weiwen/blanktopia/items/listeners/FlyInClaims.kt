package me.weiwen.blanktopia.items.listeners

import me.weiwen.blanktopia.Blanktopia
import me.weiwen.blanktopia.isInTrustedClaim
import org.bukkit.Location
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerTeleportEvent
import org.bukkit.persistence.PersistentDataType

class FlyInClaims(val plugin: Blanktopia) : Listener {
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
        if (to.blockX == from.blockX && to.blockY == from.blockY && to.blockZ == from.blockZ) return
        if (player.persistentDataContainer.get(NamespacedKey(plugin, "fly-in-claims"), PersistentDataType.BYTE) == 1.toByte()) {
            player.allowFlight = isInTrustedClaim(player, to)
        }
    }
}
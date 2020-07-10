package me.weiwen.blanktopia

import io.papermc.lib.PaperLib
import me.weiwen.blanktopia.storage.Storage
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerPortalEvent
import org.bukkit.event.player.PlayerTeleportEvent
import org.bukkit.plugin.java.JavaPlugin

class EnderDragonRespawn(val plugin: JavaPlugin, val storage: Storage) : Module, Listener {
    override fun enable() {
        plugin.server.pluginManager.registerEvents(this, plugin)
    }

    override fun disable() {}

    override fun reload() {}

    @EventHandler
    fun onPlayerPortalEvent(event: PlayerPortalEvent) {
        if (event.cause != PlayerTeleportEvent.TeleportCause.END_PORTAL) return
        val world = event.to?.world ?: return
        val player = event.player
        storage.storage?.loadPlayer(player.uniqueId) {
            data ->
            if (!data.hasSpawnedDragon) {
                PaperLib.getChunkAtAsync(world, 0, 0, false).thenAccept {
                    val enderDragonBattle = world.enderDragonBattle ?: return@thenAccept
                    if (enderDragonBattle.hasBeenPreviouslyKilled()
                        && enderDragonBattle.enderDragon == null
                    ) {
                        val loc = enderDragonBattle.endPortalLocation ?: return@thenAccept
                        world.spawnEntity(loc.clone().add(3.5, 1.0, 0.5), EntityType.ENDER_CRYSTAL)
                        world.spawnEntity(loc.clone().add(-2.5, 1.0, 0.5), EntityType.ENDER_CRYSTAL)
                        world.spawnEntity(loc.clone().add(0.5, 1.0, 3.5), EntityType.ENDER_CRYSTAL)
                        world.spawnEntity(loc.clone().add(0.5, 1.0, -2.5), EntityType.ENDER_CRYSTAL)
                        enderDragonBattle.initiateRespawn()
                    }
                }
            }
            data.hasSpawnedDragon = true
            storage.storage?.savePlayer(player.uniqueId, data)
        }
    }
}


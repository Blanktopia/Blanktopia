package me.weiwen.blanktopia

import io.papermc.lib.PaperLib
import me.weiwen.blanktopia.storage.Storage
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.player.PlayerPortalEvent
import org.bukkit.event.player.PlayerTeleportEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector

class EnderDragon(val plugin: Blanktopia, val storage: Storage) : Module, Listener {
    override fun enable() {
        plugin.server.pluginManager.registerEvents(this, plugin)
    }

    override fun disable() {}

    override fun reload() {}

    @EventHandler
    fun onPlayerPortalEvent(event: PlayerPortalEvent) {
        if (event.cause != PlayerTeleportEvent.TeleportCause.END_PORTAL) return
        val world = event.to.world
        val enderDragonBattle = world.enderDragonBattle ?: return
        val player = event.player
        val config = storage.player(player)
        if (!config.getBoolean("has-spawned-dragon")
            && enderDragonBattle.hasBeenPreviouslyKilled()
            && enderDragonBattle.enderDragon == null) {
            val loc = enderDragonBattle.endPortalLocation
            PaperLib.getChunkAtAsync(loc).thenAccept {
                world.spawnEntity(loc.clone().add(Vector(3.5, 1.0, 0.5)), EntityType.ENDER_CRYSTAL)
                world.spawnEntity(loc.clone().add(Vector(-2.5, 1.0, 0.5)), EntityType.ENDER_CRYSTAL)
                world.spawnEntity(loc.clone().add(Vector(0.5, 1.0, 3.5)), EntityType.ENDER_CRYSTAL)
                world.spawnEntity(loc.clone().add(Vector(0.5, 1.0, -2.5)), EntityType.ENDER_CRYSTAL)
                enderDragonBattle.initiateRespawn()
            }
        }
        config.set("has-spawned-dragon", true)
    }

    @EventHandler
    fun onEntityDeath(event: EntityDeathEvent) {
        if (event.entityType != EntityType.ENDER_DRAGON) return
        val world = event.entity.world
        val enderDragonBattle = world.enderDragonBattle ?: return
        val player = event.entity.killer
        Bukkit.getScheduler().runTaskLater(plugin, runTaskLater@{
            player?.inventory?.addItem(ItemStack(Material.ELYTRA))
            player?.giveExp(11500, true)
            val eggLocation = enderDragonBattle.endPortalLocation.add(Vector(0, 5, 0))
            val egg = world.getBlockAt(eggLocation)
            if (egg.type == Material.AIR) egg.type = Material.DRAGON_EGG
        } as () -> Unit, 200)
    }
}


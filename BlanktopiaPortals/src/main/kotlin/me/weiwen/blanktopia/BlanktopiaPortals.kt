package me.weiwen.blanktopia

import io.papermc.lib.PaperLib
import org.bukkit.*
import org.bukkit.block.Beacon
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerTeleportEvent
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class BlanktopiaPortals : JavaPlugin(), Listener {
    val incompletePortals: MutableMap<UUID, Location> = mutableMapOf()

    companion object {
        lateinit var INSTANCE: BlanktopiaPortals
            private set
    }

    override fun onLoad() {
        INSTANCE = this
    }

    override fun onEnable() {
        server.pluginManager.registerEvents(this, this)
        logger.info("BlanktopiaPortals is enabled")
    }

    override fun onDisable() {
        logger.info("BlanktopiaPortals is disabled")
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun onPlayerMove(event: PlayerMoveEvent) {
        if (event.to.blockX == event.from.blockX && event.to.blockZ == event.from.blockZ && event.to.blockY == event.from.blockY) {
            return
        }
        if (!event.player.hasPermission("blanktopia.portals.use")) return
        val block = event.to.clone().add(0.0, -1.0, 0.0).block
        if (block.type != Material.BEACON) return
        val state = block.state as? Beacon ?: return
        val beacon = state.persistentDataContainer
        val world = beacon.get(NamespacedKey(this, "world"), PersistentDataType.STRING) ?: return
        val x = beacon.get(NamespacedKey(this, "x"), PersistentDataType.INTEGER) ?: return
        val y = beacon.get(NamespacedKey(this, "y"), PersistentDataType.INTEGER) ?: return
        val z = beacon.get(NamespacedKey(this, "z"), PersistentDataType.INTEGER) ?: return
        val loc = Location(server.getWorld(world), x.toDouble(), y.toDouble() + 1, z.toDouble())
        loc.pitch = event.player.location.pitch
        loc.yaw = event.player.location.yaw
        PaperLib.getChunkAtAsync(loc, false).thenAccept {
            event.player.playSound(event.to, Sound.ITEM_CHORUS_FRUIT_TELEPORT, 1.0f, 1.0f)
            event.player.teleport(loc.add(0.5, 0.0, 0.5), PlayerTeleportEvent.TeleportCause.PLUGIN)
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun onPlayerInteract(event: PlayerInteractEvent) {
        val block = event.clickedBlock ?: return
        val player = event.player
        val item = event.hand?.let { player.inventory.getItem(it) } ?: return
        if (item.type != Material.NETHER_STAR) return
        if (block.type != Material.BEACON) return
        if (event.action != Action.RIGHT_CLICK_BLOCK) return
        if (!player.hasPermission("blanktopia.portals.create")) return
        if (!player.canBuildAt(block.location)) return
        event.isCancelled = true
        val state = block.state as? Beacon ?: return
        val beacon = state.persistentDataContainer
        val world = beacon.get(NamespacedKey(this, "world"), PersistentDataType.STRING)
        if (world != null) {
            player.sendMessage("${ChatColor.RED}A portal already exists here.")
            return
        }

        val uuid = player.uniqueId
        val entrance = incompletePortals[uuid]
        if (entrance != null) {
            if (entrance == block.location) {
                player.sendMessage("${ChatColor.GOLD}Portal entrance set.")
                return
            }
            PaperLib.getChunkAtAsync(entrance, false).thenAccept {
                if (it == null) return@thenAccept

                if (entrance.block.type != Material.BEACON) {
                    incompletePortals[uuid] = entrance.block.location
                    player.sendMessage("${ChatColor.GOLD}Portal entrance set.")
                }

                val entranceState = entrance.block.state as? Beacon ?: return@thenAccept
                val entranceBeacon = entranceState.persistentDataContainer
                entranceBeacon.set(NamespacedKey(this, "world"), PersistentDataType.STRING, block.world.name)
                entranceBeacon.set(NamespacedKey(this, "x"), PersistentDataType.INTEGER, block.x)
                entranceBeacon.set(NamespacedKey(this, "y"), PersistentDataType.INTEGER, block.y)
                entranceBeacon.set(NamespacedKey(this, "z"), PersistentDataType.INTEGER, block.z)
                entranceState.update()

                val exitState = block.state as? Beacon ?: return@thenAccept
                val exitBeacon = exitState.persistentDataContainer
                exitBeacon.set(NamespacedKey(this, "world"), PersistentDataType.STRING, entrance.world.name)
                exitBeacon.set(NamespacedKey(this, "x"), PersistentDataType.INTEGER, entrance.blockX)
                exitBeacon.set(NamespacedKey(this, "y"), PersistentDataType.INTEGER, entrance.blockY)
                exitBeacon.set(NamespacedKey(this, "z"), PersistentDataType.INTEGER, entrance.blockZ)
                exitState.update()

                incompletePortals.remove(uuid)
                player.sendMessage("${ChatColor.GOLD}Portal created!")
            }
        } else {
            incompletePortals[uuid] = block.location
            player.sendMessage("${ChatColor.GOLD}Portal entrance set.")
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun onBlockBreak(event: BlockBreakEvent) {
        if (event.block.type != Material.BEACON) return
        val state = event.block.state as? Beacon ?: return
        val beacon = state.persistentDataContainer
        val world = beacon.get(NamespacedKey(this, "world"), PersistentDataType.STRING) ?: return
        val x = beacon.get(NamespacedKey(this, "x"), PersistentDataType.INTEGER) ?: return
        val y = beacon.get(NamespacedKey(this, "y"), PersistentDataType.INTEGER) ?: return
        val z = beacon.get(NamespacedKey(this, "z"), PersistentDataType.INTEGER) ?: return
        val loc = Location(server.getWorld(world), x.toDouble(), y.toDouble(), z.toDouble())

        PaperLib.getChunkAtAsync(loc, false).thenAccept {
            if (it == null) return@thenAccept

            val otherState = loc.block.state as? Beacon ?: return@thenAccept
            val otherBeacon = otherState.persistentDataContainer
            otherBeacon.remove(NamespacedKey(this, "world"))
            otherBeacon.remove(NamespacedKey(this, "x"))
            otherBeacon.remove(NamespacedKey(this, "y"))
            otherBeacon.remove(NamespacedKey(this, "z"))
            otherState.update()

            event.player.sendMessage("${ChatColor.GOLD}Portal broken.")
        }
    }
}
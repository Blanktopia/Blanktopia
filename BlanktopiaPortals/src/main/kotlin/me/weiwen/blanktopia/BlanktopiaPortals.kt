package me.weiwen.blanktopia

import com.mineinabyss.idofront.platforms.IdofrontPlatforms
import io.papermc.lib.PaperLib
import me.ryanhamshire.GriefPrevention.GriefPrevention
import org.bukkit.*
import org.bukkit.block.Beacon
import org.bukkit.entity.Player
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
    private val incompletePortals: MutableMap<UUID, Location> = mutableMapOf()

    companion object {
        lateinit var INSTANCE: BlanktopiaPortals
            private set
    }

    override fun onLoad() {
        INSTANCE = this
        IdofrontPlatforms.load(this, "mineinabyss")
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
        val yaw = beacon.get(NamespacedKey(this, "yaw"), PersistentDataType.INTEGER) ?: 0
        val loc = Location(
            server.getWorld(world),
            x.toDouble() + 0.5,
            y.toDouble() + 1,
            z.toDouble() + 0.5,
            event.player.location.yaw + yaw,
            event.player.location.pitch,
        )

        event.player.teleportAsync(loc, PlayerTeleportEvent.TeleportCause.PLUGIN).thenAccept {
            event.from.world.playSound(event.from, Sound.ITEM_CHORUS_FRUIT_TELEPORT, SoundCategory.PLAYERS, 1.0f, 1.0f)
            event.to.world.playSound(event.to, Sound.ITEM_CHORUS_FRUIT_TELEPORT, SoundCategory.PLAYERS, 1.0f, 1.0f)
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun onPlayerInteract(event: PlayerInteractEvent) {
        val exit = event.clickedBlock ?: return
        val player = event.player
        val item = event.hand?.let { player.inventory.getItem(it) } ?: return
        if (item.type != Material.NETHER_STAR) return
        if (exit.type != Material.BEACON) return
        if (event.action != Action.RIGHT_CLICK_BLOCK) return
        if (!player.hasPermission("blanktopia.portals.create")) return
        if (!player.canBuildAt(exit.location)) return
        event.isCancelled = true
        val state = exit.state as? Beacon ?: return
        val beacon = state.persistentDataContainer
        val world = beacon.get(NamespacedKey(this, "world"), PersistentDataType.STRING)
        if (world != null) {
            player.sendMessage("${ChatColor.RED}A portal already exists here.")
            return
        }

        val uuid = player.uniqueId
        val entrance = incompletePortals[uuid]
        if (entrance != null) {
            if (exit.world == entrance.world && entrance.distance(exit.location) < 0.5) {
                player.sendMessage("${ChatColor.GOLD}Portal entrance set.")
                return
            }
            PaperLib.getChunkAtAsync(entrance, false).thenAccept {
                if (it == null) return@thenAccept

                if (entrance.block.type != Material.BEACON) {
                    incompletePortals[uuid] = entrance.block.location
                    player.sendMessage("${ChatColor.GOLD}Portal entrance set.")
                }

                val entranceYaw = ((entrance.yaw + 180 + 45) / 90).toInt() * 90
                val exitYaw = ((player.location.yaw + 180 + 45) / 90).toInt() * 90
                val yaw = (exitYaw - entranceYaw) % 90

                val entranceState = entrance.block.state as? Beacon ?: return@thenAccept
                val entranceBeacon = entranceState.persistentDataContainer
                entranceBeacon.set(NamespacedKey(this, "world"), PersistentDataType.STRING, exit.world.name)
                entranceBeacon.set(NamespacedKey(this, "x"), PersistentDataType.INTEGER, exit.x)
                entranceBeacon.set(NamespacedKey(this, "y"), PersistentDataType.INTEGER, exit.y)
                entranceBeacon.set(NamespacedKey(this, "z"), PersistentDataType.INTEGER, exit.z)
                entranceBeacon.set(NamespacedKey(this, "yaw"), PersistentDataType.INTEGER, -yaw)
                entranceState.update()

                val exitState = exit.state as? Beacon ?: return@thenAccept
                val exitBeacon = exitState.persistentDataContainer
                exitBeacon.set(NamespacedKey(this, "world"), PersistentDataType.STRING, entrance.world.name)
                exitBeacon.set(NamespacedKey(this, "x"), PersistentDataType.INTEGER, entrance.blockX)
                exitBeacon.set(NamespacedKey(this, "y"), PersistentDataType.INTEGER, entrance.blockY)
                exitBeacon.set(NamespacedKey(this, "z"), PersistentDataType.INTEGER, entrance.blockZ)
                exitBeacon.set(NamespacedKey(this, "yaw"), PersistentDataType.INTEGER, yaw)
                exitState.update()

                incompletePortals.remove(uuid)
                entrance.world.playSound(
                    entrance.clone().add(0.5, 0.0, 0.5),
                    Sound.ITEM_CHORUS_FRUIT_TELEPORT,
                    1.0f,
                    1.0f
                )
                exit.world.playSound(
                    exit.location.clone().add(0.5, 0.0, 0.5),
                    Sound.ITEM_CHORUS_FRUIT_TELEPORT,
                    1.0f,
                    1.0f
                )
                player.sendMessage("${ChatColor.GOLD}Portal created!")
            }
        } else {
            incompletePortals[uuid] = exit.location.clone().apply {
                yaw = player.location.yaw
            }
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

fun Player.canBuildAt(location: Location): Boolean {
    return GriefPrevention.instance.allowBuild(this, location) == null
}

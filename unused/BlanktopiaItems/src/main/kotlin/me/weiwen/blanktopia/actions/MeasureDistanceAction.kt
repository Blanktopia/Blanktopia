package me.weiwen.blanktopia.actions

import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.*

class MeasureDistanceAction(private val isOrigin: Boolean) : Action {
    companion object {
        val locations: MutableMap<UUID, Location> = mutableMapOf()
    }

    override fun run(player: Player, item: ItemStack, block: Block) {
        val location = locations[player.uniqueId]
        if (!isOrigin && location != null) {
            val distance = location.distance(block.location)
            val d = location.toVector().subtract(block.location.toVector())
            player.sendActionBar("${ChatColor.GOLD}The distance measures $distance blocks. (x: ${d.x}, y: ${d.y}, z: ${d.z})")
        } else {
            player.sendActionBar("${ChatColor.GOLD}Right click another block to measure the distance.")
            locations[player.uniqueId] = block.location
        }
    }
}
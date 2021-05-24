package me.weiwen.blanktopia.actions

import me.weiwen.blanktopia.BLACKLISTED_BLOCKS
import me.weiwen.blanktopia.PARTIAL_BLOCKS
import me.weiwen.blanktopia.playSoundTo
import org.bukkit.*
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.block.data.Ageable
import org.bukkit.block.data.type.*
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector

class BuildersWandAction(private val range: Int) : Action {
    override fun run(player: Player, item: ItemStack, block: Block, face: BlockFace) {
        if (PARTIAL_BLOCKS.contains(block.type)) return
        if (BLACKLISTED_BLOCKS.contains(block.type)) return
        val locations = buildersWandLocations(block, face, range)
        var canBuild = false
        for ((base, location) in locations) {
            val state = location.block.state
            state.type = base.type
            val blockData = base.blockData.clone()
            val cost = ItemStack(block.type, when (blockData) {
                is Ageable -> { blockData.age = 0; 1 }
                is Beehive -> { blockData.honeyLevel = 0; 1 }
                is Cake -> { blockData.bites = 0; 1 }
                is BrewingStand -> {
                    blockData.setBottle(0, false)
                    blockData.setBottle(1, false)
                    blockData.setBottle(2, false)
                    1
                }
                is EndPortalFrame -> { blockData.setEye(false); 1 }
                is Furnace -> { blockData.isLit = false; 1 }
                is Sapling -> { blockData.stage = 0; 1 }
                is Slab -> if (blockData.type == Slab.Type.DOUBLE) 2 else 1
                is SeaPickle -> blockData.pickles
                is Snow -> blockData.layers
                is TurtleEgg -> { blockData.hatch = 0; blockData.eggs }
                else -> 1
            })
            state.blockData = blockData
            if (player.gameMode != GameMode.CREATIVE && !player.inventory.containsAtLeast(cost, 1)) {
                if (!canBuild) {
                    block.world.playSound(block.location, Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 1.0f, 1.0f)
                }
                return
            }
            if (player.location.block.location == location || player.location.add( 0.0, 1.0, 0.0 ).block.location == location) {
                continue
            }

            val buildEvent = BlockPlaceEvent(
                location.block,
                state,
                location.block.getRelative(face.oppositeFace),
                cost,
                player,
                true,
                EquipmentSlot.HAND
            )
            Bukkit.getPluginManager().callEvent(buildEvent)
            if (buildEvent.isCancelled) {
                continue
            }
            if (player.gameMode != GameMode.CREATIVE) player.inventory.removeItem(cost)
            state.update(true)
            canBuild = true
        }
        if (canBuild) {
            block.world.playSound(block.location, block.soundGroup.placeSound, 1.0f, 1.0f)
        } else {
            player.playSoundTo(Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, SoundCategory.PLAYERS, 1.0f, 1.0f)
        }
    }

    private fun buildersWandLocations(block: Block, face: BlockFace, range: Int): MutableList<Pair<Block, Location>> {
        val material = block.type
        val locations: MutableList<Pair<Block, Location>> = mutableListOf()
        for (base in locationsInRange(block.location, face, range)) {
            if (base.block.type != material) continue
            val other = base.clone().add(face.direction)
            if (other.block.type != Material.AIR && other.block.type != Material.WATER && other.block.type != Material.LAVA) continue
            locations.add(Pair(base.block, other))
        }
        return locations
    }

    private fun locationsInRange(origin: Location, face: BlockFace, range: Int): MutableList<Location> {
        val (xOffset, yOffset) = if (face.modX != 0) {
            Pair(Vector(0, 1, 0), Vector(0, 0, 1))
        } else if (face.modY != 0) {
            Pair(Vector(1, 0, 0), Vector(0, 0, 1))
        } else {
            Pair(Vector(1, 0, 0), Vector(0, 1, 0))
        }
        val locations: MutableList<Location> = mutableListOf()
        for (x in -range .. range) {
            for (y in -range .. range) {
                locations.add(origin.clone().add(xOffset.clone().multiply(x)).add(yOffset.clone().multiply(y)))
            }
        }
        return locations
    }

}
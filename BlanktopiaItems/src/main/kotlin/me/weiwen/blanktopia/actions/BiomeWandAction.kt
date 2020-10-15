package me.weiwen.blanktopia.actions

import me.weiwen.blanktopia.canBuildAt
import me.weiwen.blanktopia.nms.send
import me.weiwen.blanktopia.playSoundTo
import me.weiwen.blanktopia.spawnParticleAt
import org.bukkit.*
import org.bukkit.block.Biome
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack


class BiomeWandAction(private val biome: Biome, private val range: Int) : Action {
    override fun run(player: Player, item: ItemStack) {
        val result = player.rayTraceBlocks(5.0, FluidCollisionMode.ALWAYS) ?: return
        val block = result.hitBlock ?: return
        run(player, item, block)
    }

    override fun run(player: Player, item: ItemStack, block: Block) {
        val chunks = mutableSetOf<Chunk>()
        for (location in locationsInRange(block.location, range)) {
            if (player.canBuildAt(location)) {
                location.world.setBiome(location.blockX, location.blockY, location.blockZ, biome)
                location.block.getRelative(0, 1, 0).spawnParticleAt(Particle.VILLAGER_HAPPY, 2, 0.01)
                chunks.add(location.chunk)
            }
        }
        if (chunks.size > 0) {
            val players = player.world.getNearbyPlayers(player.location, 64.0, 256.0)
            chunks.forEach { chunk -> players.forEach { chunk.send(it) } }
            block.world.playSound(block.location, Sound.BLOCK_GRASS_PLACE, 1.0f, 1.0f)
        } else {
            player.playSoundTo(Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, SoundCategory.PLAYERS, 1.0f, 1.0f)
        }
    }

    private fun locationsInRange(origin: Location, range: Int) = sequence {
        for (x in -range..range) {
            for (y in -range..range) {
                for (z in -range..range) {
                    yield(origin.clone().add(x.toDouble(), y.toDouble(), z.toDouble()))
                }
            }
        }
    }
}


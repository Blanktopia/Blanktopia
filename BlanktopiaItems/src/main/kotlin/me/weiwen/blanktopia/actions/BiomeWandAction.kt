package me.weiwen.blanktopia.actions

import me.weiwen.blanktopia.canBuildAt
import me.weiwen.blanktopia.playSoundTo
import net.minecraft.server.v1_16_R3.PacketPlayOutMapChunk
import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.block.Biome
import org.bukkit.block.Block
import org.bukkit.craftbukkit.v1_16_R3.CraftChunk
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack


class BiomeWandAction(private val biome: Biome, private val range: Int) : Action {
    override fun run(player: Player, item: ItemStack, block: Block) {
        val chunks = mutableSetOf<Chunk>()
        for (location in locationsInRange(block.location, range)) {
            if (player.canBuildAt(location)) {
                location.world.setBiome(location.blockX, location.blockY, location.blockZ, biome)
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

fun Chunk.send(player: Player) {
    (player as CraftPlayer).handle.playerConnection.sendPacket(
            PacketPlayOutMapChunk((this as CraftChunk).handle, 20, true))
}

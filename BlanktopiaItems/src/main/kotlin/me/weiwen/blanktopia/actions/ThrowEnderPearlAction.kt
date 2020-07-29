package me.weiwen.blanktopia.actions

import me.weiwen.blanktopia.playSoundAt
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.entity.EnderPearl
import org.bukkit.entity.Player

class ThrowEnderPearlAction : Action {
    override fun run(player: Player) {
        player.playSoundAt(Sound.ENTITY_ENDER_PEARL_THROW, SoundCategory.PLAYERS, 1.0f, 0.5f)
        player.launchProjectile(EnderPearl::class.java)
    }
}
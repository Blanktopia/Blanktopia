package me.weiwen.blanktopia.actions

import me.weiwen.blanktopia.playSoundAt
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.entity.Player

class PlaySoundAction(sound: String, private val pitch: Float, private val volume: Float) : Action {
    private val sound = Sound.valueOf(sound)

    override fun run(player: Player) {
        player.playSoundAt(sound, SoundCategory.PLAYERS, volume, pitch)
    }
}
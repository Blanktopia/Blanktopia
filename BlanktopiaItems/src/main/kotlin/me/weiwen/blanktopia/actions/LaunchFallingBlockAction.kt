package me.weiwen.blanktopia.actions

import me.libraryaddict.disguise.disguisetypes.Disguise
import me.weiwen.blanktopia.pitch
import org.bukkit.Bukkit.createBlockData
import org.bukkit.Material
import org.bukkit.entity.*

class LaunchFallingBlockAction(private val material: Material, private val canDropItem: Boolean, private val canHurtEntities: Boolean, private val magnitude: Double, private val pitch: Double, private val disguise: Disguise?, private val isPitchRelative: Boolean = true) : Action {

    override fun run(player: Player) {
        val entity = player.world.spawnFallingBlock(player.eyeLocation, createBlockData(material))
        entity.setHurtEntities(canHurtEntities)
        entity.dropItem = canDropItem

        val v = player.location.direction

        if (!isPitchRelative) {
            v.pitch = 0.0
        }
        v.pitch += pitch
        v.normalize().multiply(magnitude)
        entity.velocity = v

        if (disguise != null) {
            val disguise = disguise.clone()
            disguise.entity = entity
            disguise.startDisguise()
        }
    }
}

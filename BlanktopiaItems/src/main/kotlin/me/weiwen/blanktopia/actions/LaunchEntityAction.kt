package me.weiwen.blanktopia.actions

import me.libraryaddict.disguise.disguisetypes.Disguise
import me.weiwen.blanktopia.pitch
import me.weiwen.blanktopia.yaw
import org.bukkit.entity.*

class LaunchEntityAction(type: String, private val magnitude: Double, private val pitch: Double, private val disguise: Disguise?, private val isPitchRelative: Boolean = true) : Action {
    private val type = EntityType.valueOf(type.toUpperCase())

    override fun run(player: Player) {
        val entity = player.world.spawnEntity(player.eyeLocation, type)

        if (entity is Projectile) {
            entity.shooter = player
            if (entity is AbstractArrow) {
                entity.pickupStatus = AbstractArrow.PickupStatus.CREATIVE_ONLY
            }
        }
        if (entity is Item) {
            entity.pickupDelay = 20
            entity.setCanMobPickup(false)
        }

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

package me.weiwen.blanktopia.actions

import me.libraryaddict.disguise.disguisetypes.Disguise
import me.weiwen.blanktopia.BlanktopiaCore
import me.weiwen.blanktopia.pitch
import me.weiwen.blanktopia.projectile.ItemProjectile
import me.weiwen.blanktopia.yaw
import org.bukkit.Material
import org.bukkit.entity.*
import org.bukkit.inventory.ItemStack

class LaunchItemProjectileAction(material: Material, private val amount: Int, private val magnitude: Double, private val pitch: Double, private val isPitchRelative: Boolean = true) : Action {
    private val item = ItemStack(material, amount)

    override fun run(player: Player) {
        val velocity = player.location.direction
        if (!isPitchRelative) {
            velocity.pitch = 0.0
        }
        velocity.pitch += pitch
        velocity.normalize().multiply(magnitude)

        val projectile = ItemProjectile(
                item,
                player.eyeLocation,
                player,
                velocity
        )
        BlanktopiaCore.INSTANCE.projectileManager.add(projectile)
    }
}

package me.weiwen.blanktopia.actions

import me.weiwen.blanktopia.BlanktopiaCore
import me.weiwen.blanktopia.pitch
import me.weiwen.blanktopia.projectile.ItemProjectile
import net.minecraft.server.v1_16_R1.Items.it
import org.bukkit.FluidCollisionMode
import org.bukkit.Material
import org.bukkit.entity.*
import org.bukkit.inventory.ItemStack
import java.util.function.Predicate

class LaunchItemProjectileAction(
        material: Material,
        private val amount: Int,
        private val magnitude: Double,
        private val pitch: Double,
        private val isPitchRelative: Boolean = true,
        private val bounce: Double = 0.0,
        private val drag: Double = 0.02,
        private val gravity: Double = 0.08
) : Action {

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
                velocity,
                bounce,
                drag,
                gravity)

        BlanktopiaCore.INSTANCE.projectileManager.add(projectile)
    }
}

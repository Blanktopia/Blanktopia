package me.weiwen.blanktopia.projectile

import me.weiwen.blanktopia.BlanktopiaCore
import org.bukkit.FluidCollisionMode
import org.bukkit.Location
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector
import java.util.function.Predicate
import java.util.logging.Level
import kotlin.math.cos

class ItemProjectile
    : Projectile {

    private val armorStand: ArmorStand
    private val offset: Vector

    constructor(
            itemStack: ItemStack,
            location: Location,
            entity: Entity,
            velocity: Vector,
            bounce: Double = 0.0,
            drag: Double = 0.02,
            gravity: Double = 0.08,
            fluidCollisionMode: FluidCollisionMode = FluidCollisionMode.NEVER,
            ignorePassableBlocks: Boolean = true,
            ignoreOrigin: Boolean = true,
            size: Double = 0.0,
            predicate: Predicate<Entity>? = null) : super(location, entity, velocity, bounce, drag, gravity, fluidCollisionMode, ignorePassableBlocks, ignoreOrigin, size, predicate) {
        val dir = location.direction
        offset = Vector(dir.x * -0.4 + dir.z * 0.55, -1.45, dir.z * -0.4 + dir.x * 0.65)
        armorStand = location.world.spawnEntity(location.add(offset), EntityType.ARMOR_STAND) as ArmorStand
        armorStand.isVisible = false
        armorStand.setGravity(false)
        armorStand.setItem(EquipmentSlot.HAND, itemStack)
        armorStand.setDisabledSlots(EquipmentSlot.HAND, EquipmentSlot.OFF_HAND, EquipmentSlot.LEGS, EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.FEET)
        this.predicate = if (predicate != null) {
            Predicate { it != armorStand && this.predicate!!.test(it) }
        } else {
            Predicate { it != armorStand }
        }

    }

    override fun tick(): Boolean {
        return if (super.tick()) {
            armorStand.velocity = velocity
            armorStand.rightArmPose = armorStand.rightArmPose.add(0.300, 0.0, 0.0)
            armorStand.teleport(location.clone().add(offset).add(0.0, cos(armorStand.rightArmPose.x) * 0.7, 0.0))
            true
        } else {
            armorStand.rightArmPose = armorStand.rightArmPose.setX(0.0)
            armorStand.teleport(location.clone().add(offset).add(0.0, cos(armorStand.rightArmPose.x) * 0.7, 0.0))
            armorStand.velocity.zero()
            BlanktopiaCore.INSTANCE.server.scheduler.runTaskLater(BlanktopiaCore.INSTANCE, { ->
                armorStand.remove()
            }, 300)
            false
        }
    }
}
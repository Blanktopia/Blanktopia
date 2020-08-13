package me.weiwen.blanktopia.projectile

import me.weiwen.blanktopia.BlanktopiaCore
import org.bukkit.FluidCollisionMode
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.Item
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector
import java.util.function.Predicate
import java.util.logging.Level

class ItemProjectile
    : Projectile {

    lateinit var item: Item

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
            predicate: Predicate<Entity?>? = null) : super(location, entity, velocity, bounce, drag, gravity, fluidCollisionMode, ignorePassableBlocks, ignoreOrigin, size, predicate) {
        item = location.world.dropItem(location, itemStack)
        item.pickupDelay = Int.MAX_VALUE
        item.setCanMobPickup(false)
        this.predicate = if (predicate != null) {
            Predicate { it != item && this.predicate!!.test(it) }
        } else {
            Predicate { it != item }
        }
    }

    fun test(entity: Entity): Boolean {
        return entity == item
    }

    override fun tick(): Boolean {
        BlanktopiaCore.INSTANCE.logger.log(Level.INFO, "x: ${location.x}, y: ${location.y}, z: ${location.z}, vx: ${velocity.x}, vy: ${velocity.y}, vz: ${velocity.z}")
        return if (super.tick()) {
            item.teleport(location)
            item.velocity = velocity
            true
        } else {
            item.velocity.zero().add(Vector(0.0, -0.01, 0.0))
            item.teleport(location)
            for (i in 1..10) {
                BlanktopiaCore.INSTANCE.server.scheduler.runTaskLater(BlanktopiaCore.INSTANCE, { ->
                    item.teleport(location)
                    item.velocity.zero().add(Vector(0.0, -0.01, 0.0))
                }, i.toLong())
            }
            BlanktopiaCore.INSTANCE.server.scheduler.runTaskLater(BlanktopiaCore.INSTANCE, { ->
                item.remove()
            }, 300)
            false
        }
    }
}
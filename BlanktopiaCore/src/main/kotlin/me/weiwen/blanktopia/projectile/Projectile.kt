package me.weiwen.blanktopia.projectile

/* Author @aecsocket (https://gitlab.com/aecsocket/unified-framework) */

import me.weiwen.blanktopia.reflect
import org.bukkit.FluidCollisionMode
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.util.RayTraceResult
import org.bukkit.util.Vector
import java.util.function.Predicate

/** An object which has physics and can collide. */
open class Projectile(
        val location: Location,
        val entity: Entity,
        val velocity: Vector,
        private val bounce: Double = 0.0,
        private val drag: Double = 0.02,
        private val gravity: Double = 0.08,
        private val fluidCollisionMode: FluidCollisionMode = FluidCollisionMode.NEVER,
        private val ignorePassableBlocks: Boolean = true,
        private val ignoreOrigin: Boolean = true,
        private var size: Double = 0.0,
        protected var predicate: Predicate<Entity?>?) {

    companion object {
        const val SPEED_THRESHOLD = 0.05
    }

    val world: World = location.world

    var distanceTraveled: Double = 0.0
        private set

    /** Gets the current [Block] the projectile is in, if [Projectile.hit] returned [ProjectileHitResult.CONTINUE].
     * @return The current [Block] the projectile is in, if [Projectile.hit] returned [ProjectileHitResult.CONTINUE].
     */
    var hitBlock: Block? = null
        private set

    /** Gets the current [Entity] the projectile is in, if [Projectile.hit] returned [ProjectileHitResult.CONTINUE].
     * @return The current [Entity] the projectile is in, if [Projectile.hit] returned [ProjectileHitResult.CONTINUE].
     */
    var hitEntity: Entity? = null
        private set

    /** Gets the current [ProjectileHitType] of the object the projectile is in, if [Projectile.hit] returned [ProjectileHitResult.CONTINUE].
     * @return The current [ProjectileHitType] of the object the projectile is in, if [Projectile.hit] returned [ProjectileHitResult.CONTINUE].
     */
    var currentType: ProjectileHitType? = null
        private set

    /** Ticks the projectile's physics. */
    open fun tick(): Boolean {
        val distance = velocity.length()
        if (location.y < 0) {
            return false
        }
        if (distance != 0.0) {
            val ray = world.rayTrace(location, velocity, distance, fluidCollisionMode, ignorePassableBlocks, size, predicate)
            if (ray != null && (!ignoreOrigin || ray.hitEntity !== entity)) {
                var handleHit = true
                if (currentType != null) {
                    if (currentType === ProjectileHitType.BLOCK && ray.hitBlock != null && ray.hitBlock == hitBlock) handleHit = false
                    if (currentType === ProjectileHitType.ENTITY && ray.hitEntity != null && ray.hitEntity == hitEntity) handleHit = false
                }
                if (handleHit) {
                    if (currentType === ProjectileHitType.BLOCK && ray.hitBlock !== hitBlock) hitBlock = ray.hitBlock
                    if (currentType === ProjectileHitType.ENTITY && ray.hitEntity !== hitEntity) hitEntity = ray.hitEntity
                    val hit = hit(ray)
                    val face = ray.hitBlockFace
                    if (face != null && hit === ProjectileHitResult.BOUNCE) {
                        velocity.reflect(face).multiply(bounce)
                        return true
                    }
                    if (hit === ProjectileHitResult.CONTINUE) {
                        if (ray.hitBlock != null) {
                            hitBlock = ray.hitBlock
                            currentType = ProjectileHitType.BLOCK
                        } else if (ray.hitEntity != null) {
                            hitEntity = ray.hitEntity
                            currentType = ProjectileHitType.ENTITY
                        }
                    }
                    if (hit === ProjectileHitResult.DESTROY) {
                        return false
                    }
                }
            }
            distanceTraveled += distance
            location.add(velocity)
        }
        val dragMultiplier = drag
        velocity.x = velocity.x - velocity.x * dragMultiplier
        velocity.z = velocity.z - velocity.z * dragMultiplier
        velocity.y = velocity.y - gravity
        velocity.y = velocity.y - velocity.y * dragMultiplier
        return true
    }

    /** When the projectile hits a block or an entity.
     * @param ray The [RayTraceResult].
     * @param context The [TickContext] of the step.
     * @return The [ProjectileHitResult].
     */
    open fun hit(ray: RayTraceResult?): ProjectileHitResult {
        return if (bounce > 0) {
            if (velocity.length() >= SPEED_THRESHOLD) {
                ProjectileHitResult.BOUNCE
            } else {
                ProjectileHitResult.DESTROY
            }
        } else {
            ProjectileHitResult.DESTROY
        }
    }
}

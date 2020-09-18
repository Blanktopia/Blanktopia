package me.weiwen.blanktopia.projectile

import me.weiwen.blanktopia.Module
import org.bukkit.plugin.java.JavaPlugin

class ProjectileManager(private val plugin: JavaPlugin) : Module {
    val projectiles = mutableSetOf<Projectile>()

    override fun enable() {
        plugin.server.scheduler.scheduleSyncRepeatingTask(plugin, ::tick, 1, 1)
        reload()
    }

    override fun disable() {
    }

    override fun reload() {
    }

    private fun tick() {
        projectiles.retainAll { it.tick() }
    }

    fun add(projectile: Projectile) {
        projectiles.add(projectile)
    }

    fun remove(projectile: Projectile) {
        projectiles.remove(projectile)
    }

}
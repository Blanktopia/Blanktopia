package me.weiwen.blanktopia

import me.weiwen.blanktopia.storage.Storage
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class BlanktopiaCore: JavaPlugin() {
    lateinit var storage: Storage
    lateinit var permanentPotionEffect: PermanentPotionEffect
    lateinit var experienceBoost: ExperienceBoost

    companion object {
        lateinit var INSTANCE: BlanktopiaCore
            private set
    }

    override fun onLoad() {
        INSTANCE = this
        createConfig()
    }

    override fun onEnable() {
        reloadConfig()
        storage = Storage(this)
        storage.enable()
        permanentPotionEffect = PermanentPotionEffect(this)
        permanentPotionEffect.enable()
        experienceBoost = ExperienceBoost(this)
        experienceBoost.enable()
    }

    override fun onDisable() {
        storage.disable()
        permanentPotionEffect.disable()
    }

    private fun createConfig() {
        try {
            if (!dataFolder.exists()) {
                dataFolder.mkdirs()
            }
            val file = File(dataFolder, "config.yml")
            if (!file.exists()) {
                saveDefaultConfig()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
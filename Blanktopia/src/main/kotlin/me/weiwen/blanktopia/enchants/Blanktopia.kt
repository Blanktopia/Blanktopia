package me.weiwen.blanktopia.enchants

import me.weiwen.blanktopia.enchants.listeners.AnvilListener
import me.weiwen.blanktopia.enchants.listeners.EnchantingTableListener
import me.weiwen.blanktopia.enchants.listeners.GrindStoneListener
import me.weiwen.blanktopia.enchants.managers.CustomEnchantsManager
import me.weiwen.blanktopia.enchants.managers.PermanentPotionEffectManager
import org.bukkit.ChatColor
import org.bukkit.plugin.java.JavaPlugin


class Blanktopia : JavaPlugin() {
    lateinit var customEnchants: CustomEnchantsManager
    lateinit var permanentPotionEffectsManager: PermanentPotionEffectManager

    companion object {
        lateinit var INSTANCE: Blanktopia
            private set
    }

    override fun onLoad() {
        INSTANCE = this
    }

    override fun onEnable() {
        reloadConfig()
        getCommand("blanktopia")?.setExecutor { sender, _, _, args ->
            when (args[0]) {
                "reload" -> {
                    customEnchants.reload()
                    sender.sendMessage(ChatColor.GOLD.toString() + "Reloaded configuration!")
                    true
                }
                else -> false
            }
        }

        server.pluginManager.registerEvents(EnchantingTableListener, this)
        server.pluginManager.registerEvents(AnvilListener(this), this)
        server.pluginManager.registerEvents(GrindStoneListener(this), this)

        permanentPotionEffectsManager = PermanentPotionEffectManager(this)
        permanentPotionEffectsManager.enable()

        customEnchants = CustomEnchantsManager(this)
        customEnchants.enable()

        logger.info("BlanktopiaEnchants is enabled")
    }

    override fun onDisable() {
        permanentPotionEffectsManager.disable()
        customEnchants.disable()
        logger.info("BlanktopiaEnchants is disabled")
    }
}

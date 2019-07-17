package com.yourserver.yourplugin

import org.bukkit.plugin.java.JavaPlugin

class YourPlugin : JavaPlugin() {

    override fun onEnable() {
        logger.info("My plugin with Kotlin is enabled")
    }

    override fun onDisable() {
        logger.info("My plugin with Kotlin is disabled")
    }
}
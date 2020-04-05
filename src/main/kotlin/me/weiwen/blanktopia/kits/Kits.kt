package me.weiwen.blanktopia.kits

import me.weiwen.blanktopia.Blanktopia
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class Kits(plugin: Blanktopia) {
    private val config = plugin.config.getConfigurationSection("books")!!
    private val kits = populateKits()

    init {
        val command = plugin.getCommand("kit")
        command?.setExecutor { sender, _, _, args ->
            true
        }
    }

    fun populateKits(): Map<String, List<ItemStack>> {
        return mapOf()
    }
}

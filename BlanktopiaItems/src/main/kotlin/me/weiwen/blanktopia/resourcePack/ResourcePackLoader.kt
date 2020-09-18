package me.weiwen.blanktopia.kits

import me.weiwen.blanktopia.Module
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

class ResourcePackLoader(val plugin: JavaPlugin) : Listener, Module {
    private var kits = mapOf<String, List<Pair<EquipmentSlot?, ItemStack>>>()
    var resourcePackUrl = plugin.config.getString("resource-pack-url")
    var resourcePackHash = plugin.config.getString("resource-pack-hash")

    override fun enable() {
        plugin.server.pluginManager.registerEvents(this, plugin)
        reload()

        val rpCommand = plugin.getCommand("rp")
        rpCommand?.setExecutor { sender, _, _, _ ->
            if (sender !is Player) return@setExecutor false
            val url = resourcePackUrl ?: return@setExecutor false
            val hash = resourcePackHash ?: return@setExecutor false
            sender.setResourcePack(url, hash)
            sender.sendMessage("Sending resource pack.")
            true
        }
    }

    override fun disable() {}

    override fun reload() {
        resourcePackUrl = plugin.config.getString("resource-pack-url")
        resourcePackHash = plugin.config.getString("resource-pack-hash")
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val url = resourcePackUrl ?: return
        val hash = resourcePackHash ?: return
        event.player.setResourcePack(url, hash)
    }

}

package me.weiwen.blanktopia.tweaks.modules

import io.papermc.paper.connection.PlayerConfigurationConnection
import me.weiwen.blanktopia.tweaks.Module
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerLinksSendEvent
import org.bukkit.plugin.java.JavaPlugin
import java.net.URI

/** Adds the `server-links` from the config to the Server Links screen. `{player}` in a URL becomes the player name. */
class ServerLinks(private val plugin: JavaPlugin) :
    Listener, Module {

    private var links: Map<String, String> = mapOf()

    override fun enable() {
        plugin.server.pluginManager.registerEvents(this, plugin)
        reload()
    }

    override fun disable() {}

    override fun reload() {
        val section = plugin.config.getConfigurationSection("server-links") ?: return
        links = section.getKeys(false).associateWith { section.getString(it)!! }
    }

    @EventHandler
    fun onPlayerLinksSend(event: PlayerLinksSendEvent) {
        val name = (event.connection as PlayerConfigurationConnection).profile.name ?: return
        for ((label, url) in links) {
            event.links.addLink(label, URI(url.replace("{player}", name)))
        }
    }
}

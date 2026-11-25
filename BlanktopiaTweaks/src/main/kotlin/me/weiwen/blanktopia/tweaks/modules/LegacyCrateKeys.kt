package me.weiwen.blanktopia.tweaks.modules

import me.weiwen.blanktopia.tweaks.Module
import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.Inventory
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin

// ExcellentCrates 7 reads the key ID from "key.id"; 6.x wrote it to "crate_key.id".
class LegacyCrateKeys(val plugin: JavaPlugin) : Module, Listener {
    private val legacyKey = NamespacedKey("excellentcrates", "crate_key.id")
    private val key = NamespacedKey("excellentcrates", "key.id")

    override fun enable() {
        plugin.server.pluginManager.registerEvents(this, plugin)
    }

    override fun disable() {}

    override fun reload() {}

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        retag(event.player.inventory)
        retag(event.player.enderChest)
    }

    @EventHandler
    fun onInventoryOpen(event: InventoryOpenEvent) {
        retag(event.inventory)
    }

    private fun retag(inventory: Inventory) {
        for (item in inventory.contents) {
            if (item == null) continue
            val id = item.persistentDataContainer.get(legacyKey, PersistentDataType.STRING) ?: continue
            item.editPersistentDataContainer {
                it.set(key, PersistentDataType.STRING, id)
                it.remove(legacyKey)
            }
        }
    }
}

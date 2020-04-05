package me.weiwen.blanktopia.items

import me.weiwen.blanktopia.Blanktopia
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataHolder
import org.bukkit.persistence.PersistentDataType

class CustomItems(private val plugin: Blanktopia) :
    Listener {
    private val config = plugin.config.getConfigurationSection("items")!!
    private var items: Map<String, CustomItemType> = populateItems()

    init {
        plugin.server.pluginManager.registerEvents(this, plugin)
        val command = plugin.getCommand("witem")
        command?.setExecutor { sender, _, _, args ->
            if (sender is Player) {
                val key = args[0]
                val item = sender.inventory.itemInMainHand
                val meta = item.itemMeta
                meta?.persistentDataContainer?.set(NamespacedKey(plugin, "type"), PersistentDataType.STRING, key)
                item.itemMeta = meta
                sender.playSound(sender.location, Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 1.0f)
                true
            } else {
                false
            }
        }
        command?.setTabCompleter {
                _, _, _, args ->
            config.getKeys(false).toList()
        }
    }

    fun populateItems(): Map<String, CustomItemType> {
        val items = mutableMapOf<String, CustomItemType>()
        for (type in config.getKeys(false)) {
            items[type] = CustomItemType(type, config.getConfigurationSection(type)!!)
        }
        return items
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onPlayerInteract(event: PlayerInteractEvent) {
        val data = event.player.inventory.itemInMainHand.itemMeta?.persistentDataContainer ?: return
        val type = data.get(NamespacedKey(plugin, "type"), PersistentDataType.STRING) ?: return
        val item = items[type] ?: return
        when (event.action) {
            Action.LEFT_CLICK_AIR -> item.leftClickAir?.let { it.run(event.player); event.isCancelled = true }
            Action.LEFT_CLICK_BLOCK -> item.leftClickBlock?.let { it.run(event.player); event.isCancelled = true }
            Action.RIGHT_CLICK_AIR -> item.rightClickAir?.let { it.run(event.player); event.isCancelled = true }
            Action.RIGHT_CLICK_BLOCK -> item.rightClickBlock?.let { it.run(event.player); event.isCancelled = true }
            else -> Unit
        }
    }
}

fun ItemStack.makeCustom(key: String) {
    val meta = itemMeta
    meta?.persistentDataContainer?.set(NamespacedKey(Blanktopia.INSTANCE, "type"), PersistentDataType.STRING, key)
    itemMeta = meta
}

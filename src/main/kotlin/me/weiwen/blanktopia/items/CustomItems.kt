package me.weiwen.blanktopia.items

import me.weiwen.blanktopia.Blanktopia
import me.weiwen.blanktopia.Module
import org.bukkit.NamespacedKey
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

class CustomItems(private val plugin: Blanktopia) :
    Listener, Module {
    private val config = plugin.config.getConfigurationSection("items")!!
    private lateinit var items: Map<String, CustomItemType>

    override fun enable() {
        items = populateItems()
        plugin.server.pluginManager.registerEvents(this, plugin)
        val command = plugin.getCommand("witem")
        command?.setExecutor { sender, _, _, args ->
            if (sender is Player) {
                sender.inventory.addItem(buildItem(args[0]))
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

    override fun disable() {}

    override fun reload() {
        items = populateItems()
    }

    fun buildItem(type: String): ItemStack? {
        return items[type]?.build()
    }

    fun populateItems(): Map<String, CustomItemType> {
        val items = mutableMapOf<String, CustomItemType>()
        for (type in config.getKeys(false)) {
            items[type] = CustomItemType(type, config.getConfigurationSection(type)!!)
        }
        return items
    }

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        val data = event.player.inventory.itemInMainHand.itemMeta?.persistentDataContainer ?: return
        val type = data.get(NamespacedKey(plugin, "type"), PersistentDataType.STRING) ?: return
        event.isCancelled = true
        val item = items[type] ?: return
        when (event.action) {
            Action.LEFT_CLICK_AIR -> item.leftClickAir?.run(event.player)
            Action.LEFT_CLICK_BLOCK -> item.leftClickBlock?.run(event.player)
            Action.RIGHT_CLICK_AIR -> item.rightClickAir?.run(event.player)
            Action.RIGHT_CLICK_BLOCK -> item.rightClickBlock?.run(event.player)
            else -> Unit
        }
    }
}

fun ItemStack.makeCustom(key: String) {
    val meta = itemMeta
    meta?.persistentDataContainer?.set(NamespacedKey(Blanktopia.INSTANCE, "type"), PersistentDataType.STRING, key)
    itemMeta = meta
}

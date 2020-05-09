package me.weiwen.blanktopia.items

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent
import me.weiwen.blanktopia.Blanktopia
import me.weiwen.blanktopia.Module
import me.weiwen.blanktopia.items.listeners.FlyInClaims
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
    private var config = plugin.config.getConfigurationSection("items")!!
    private lateinit var items: Map<String, CustomItemType>

    override fun enable() {
        items = populateItems()
        plugin.server.pluginManager.registerEvents(this, plugin)
        plugin.server.pluginManager.registerEvents(FlyInClaims(plugin), plugin)
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
                _, _, _, _ ->
            config.getKeys(false).toList()
        }
    }

    override fun disable() {}

    override fun reload() {
        config = plugin.config.getConfigurationSection("items")!!
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
        val item = items[type] ?: return
        when (event.action) {
            Action.LEFT_CLICK_AIR -> item.leftClickAir?.run(event.player, null, null)
            Action.LEFT_CLICK_BLOCK -> item.leftClickBlock?.run(event.player, event.clickedBlock, event.blockFace)
            Action.RIGHT_CLICK_AIR -> item.rightClickAir?.run(event.player, null, null)
            Action.RIGHT_CLICK_BLOCK -> item.rightClickBlock?.run(event.player, event.clickedBlock, event.blockFace)
            else -> return
        }
        event.isCancelled = true
    }

    @EventHandler
    fun onPlayerArmorChange(event: PlayerArmorChangeEvent) {
        if (event.newItem == event.oldItem) return

        event.newItem?.let {
            val data = it.itemMeta?.persistentDataContainer ?: return@let
            val type = data.get(NamespacedKey(plugin, "type"), PersistentDataType.STRING) ?: return@let
            val item = items[type] ?: return@let
            item.equipArmor?.run(event.player, null, null)
        }

        event.oldItem?.let {
            val data = it.itemMeta?.persistentDataContainer ?: return@let
            val type = data.get(NamespacedKey(plugin, "type"), PersistentDataType.STRING) ?: return@let
            val item = items[type] ?: return@let
            item.unequipArmor?.run(event.player, null, null)
        }
    }
}

fun ItemStack.makeCustom(key: String) {
    val meta = itemMeta
    meta?.persistentDataContainer?.set(NamespacedKey(Blanktopia.INSTANCE, "type"), PersistentDataType.STRING, key)
    itemMeta = meta
}
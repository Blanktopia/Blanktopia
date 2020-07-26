package me.weiwen.blanktopia.items

import com.comphenix.protocol.PacketType.Play.Client.BLOCK_PLACE
import com.comphenix.protocol.PacketType.Play.Server.BLOCK_BREAK
import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent
import me.weiwen.blanktopia.Blanktopia
import me.weiwen.blanktopia.Module
import me.weiwen.blanktopia.triggers.TriggerType
import org.bukkit.NamespacedKey
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.inventory.PrepareItemCraftEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin

class CustomItems(private val plugin: JavaPlugin) :
    Listener, Module {
    private var config = plugin.config.getConfigurationSection("items")!!
    private lateinit var items: Map<String, CustomItem>

    override fun enable() {
        items = populateItems()
        plugin.server.pluginManager.registerEvents(this, plugin)

        val witemCommand = plugin.getCommand("witem")
        witemCommand?.setExecutor { sender, _, _, args ->
            val name = args[0] ?: return@setExecutor false
            val player = if (args[1] != null) plugin.server.getPlayer(args[1]) else sender as? Player
            if (player == null) return@setExecutor false
            player.inventory.addItem(buildItem(name))
            player.playSound(player.location, Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 1.0f)
            true
        }
        witemCommand?.setTabCompleter {
                _, _, _, _ ->
            config.getConfigurationSection("items")?.getKeys(false)?.toList() ?: listOf()
        }

        for (player in plugin.server.onlinePlayers) {
            for (item in player.inventory.armorContents) {
                if (item == null) continue
                val customItem = getCustomItem(item) ?: continue
                customItem.triggers[TriggerType.EQUIP_ARMOR]?.forEach {
                    it.run(player, item)
                }
            }
        }

    }

    override fun disable() {
    }

    override fun reload() {
        config = plugin.config.getConfigurationSection("items")!!
        items = populateItems()
    }

    fun buildItem(type: String): ItemStack? {
        return items[type]?.build()
    }

    fun populateItems(): Map<String, CustomItem> {
        val items = mutableMapOf<String, CustomItem>()
        for (type in config.getKeys(false)) {
            items[type] = CustomItem(type, config.getConfigurationSection(type)!!)
        }
        return items
    }

    fun getCustomItem(item: ItemStack): CustomItem? {
        val data = item.itemMeta?.persistentDataContainer ?: return null
        val type = data.get(NamespacedKey(Blanktopia.INSTANCE, "type"), PersistentDataType.STRING) ?: return null
        return items[type]
    }

    @EventHandler(ignoreCancelled = true)
    fun onPlayerInteract(event: PlayerInteractEvent) {
        val item = event.item ?: return
        val customItem = getCustomItem(item) ?: return
        val player = event.player
            when (event.action) {
                Action.RIGHT_CLICK_AIR -> customItem.triggers[TriggerType.RIGHT_CLICK_AIR]?.forEach {
                    if (it.test(player, item)) it.run(player, item)
                    event.isCancelled = true
                }
                Action.RIGHT_CLICK_BLOCK -> customItem.triggers[TriggerType.RIGHT_CLICK_BLOCK]?.forEach {
                    if (it.test(player, item)) it.run(
                        player,
                        item,
                        event.clickedBlock ?: return@forEach,
                        event.blockFace
                    )
                    event.isCancelled = true
                }
                Action.LEFT_CLICK_AIR -> customItem.triggers[TriggerType.LEFT_CLICK_AIR]?.forEach {
                    if (it.test(player, item)) it.run(player, item)
                    event.isCancelled = true
                }
                Action.LEFT_CLICK_BLOCK -> customItem.triggers[TriggerType.LEFT_CLICK_BLOCK]?.forEach {
                    if (it.test(player, item)) it.run(
                        player,
                        item,
                        event.clickedBlock ?: return@forEach,
                        event.blockFace
                    )
                    event.isCancelled = true
                }
                else -> return
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onPlayerInteractEntity(event: PlayerInteractEntityEvent) {
        val item = if (event.hand == EquipmentSlot.HAND) event.player.inventory.itemInMainHand else if (event.hand === EquipmentSlot.OFF_HAND) event.player.inventory.itemInOffHand else return
        val customItem = getCustomItem(item) ?: return
        customItem.triggers[TriggerType.RIGHT_CLICK_ENTITY]?.forEach {
            it.run(
                event.player,
                item,
                event.rightClicked
            )
            event.isCancelled = true
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onPrepareItemCraft(event: PrepareItemCraftEvent) {
        for (item in event.inventory.matrix) {
            if (item == null) continue
            if (getCustomItem(item) != null) {
                event.inventory.result = null
                return
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onPlayerArmorChange(event: PlayerArmorChangeEvent) {
        if (event.newItem == event.oldItem) return

        event.oldItem?.let { item ->
            val customItem = getCustomItem(item) ?: return@let
            customItem.triggers[TriggerType.UNEQUIP_ARMOR]?.forEach {
                it.run(event.player, item)
            }
        }

        event.newItem?.let { item ->
            val customItem = getCustomItem(item) ?: return@let
            customItem.triggers[TriggerType.EQUIP_ARMOR]?.forEach {
                it.run(event.player, item)
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onBlockBreak(event: BlockBreakEvent) {
        val item = event.player.inventory.itemInMainHand
        val customItem = getCustomItem(item) ?: return
        customItem.triggers[TriggerType.BREAK_BLOCK]?.forEach {
            it.run(
                event.player,
                item,
                event.block
            )
            event.isCancelled = true
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onBlockPlace(event: BlockPlaceEvent) {
        val item = event.player.inventory.itemInMainHand
        val customItem = getCustomItem(item) ?: return
        customItem.triggers[TriggerType.PLACE_BLOCK]?.forEach {
            it.run(
                event.player,
                item,
                event.block
            )
            event.isCancelled = true
        }
    }
}

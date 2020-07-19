package me.weiwen.blanktopia.items

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent
import me.weiwen.blanktopia.Blanktopia
import me.weiwen.blanktopia.Module
import me.weiwen.blanktopia.items.listeners.FlyInClaims
import me.weiwen.blanktopia.playerHeadFromUrl
import org.bukkit.NamespacedKey
import org.bukkit.Sound
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.inventory.PrepareItemCraftEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import java.io.File

class CustomItems(private val plugin: Blanktopia) :
    Listener, Module {
    private var config = plugin.config.getConfigurationSection("items")!!
    private lateinit var items: Map<String, CustomItemType>
    lateinit var flyInClaims: FlyInClaims

    override fun enable() {
        items = populateItems()
        plugin.server.pluginManager.registerEvents(this, plugin)

        flyInClaims = FlyInClaims(plugin)
        plugin.server.pluginManager.registerEvents(flyInClaims, plugin)

        for (player in plugin.server.onlinePlayers) {
            for (item in player.inventory.armorContents) {
                if (item == null) continue
                val customItem = plugin.customItems.getCustomItem(item) ?: continue
                customItem.equipArmor?.run(customItem.type, player, item)
            }
        }

        val witemCommand = plugin.getCommand("witem")
        witemCommand?.setExecutor { sender, _, _, args ->
            if (sender !is Player) return@setExecutor false
            if (args.size != 1) return@setExecutor false
            sender.inventory.addItem(buildItem(args[0]))
            sender.playSound(sender.location, Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 1.0f)
            true
        }
        witemCommand?.setTabCompleter {
                _, _, _, _ ->
            config.getKeys(false).toList()
        }

        val wheadCommand = plugin.getCommand("whead")
        wheadCommand?.setExecutor { sender, _, _, args ->
            if (sender !is Player) return@setExecutor false
            if (args.size != 2) return@setExecutor false
            sender.inventory.addItem(playerHeadFromUrl(args[0].replace("_", " "), args[1]))
            sender.playSound(sender.location, Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 1.0f)
            true
        }

        val wserializeCommand = plugin.getCommand("wserialize")
        wserializeCommand?.setExecutor { sender, _, _, _ ->
            if (sender !is Player) return@setExecutor false
            val item = sender.inventory.itemInMainHand

            val file = File(plugin.dataFolder, "serialized.yml")
            if (!file.exists()) {
                file.createNewFile()
            }

            val config = YamlConfiguration()
            if (!config.isList("serialized")) {
                config.set("serialized", listOf<Any>());
            }
            (config.getList("serialized") as? MutableList<Any>)?.add(item)

            config.save(file)
            sender.playSound(sender.location, Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 1.0f)
            true
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

    fun populateItems(): Map<String, CustomItemType> {
        val items = mutableMapOf<String, CustomItemType>()
        for (type in config.getKeys(false)) {
            items[type] = CustomItemType(type, config.getConfigurationSection(type)!!)
        }
        return items
    }

    public fun getCustomItem(item: ItemStack): CustomItemType? {
        val data = item.itemMeta?.persistentDataContainer ?: return null
        val type = data.get(NamespacedKey(plugin, "type"), PersistentDataType.STRING) ?: return null
        return items[type]
    }

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        val item = event.item ?: return
        val customItem = getCustomItem(item) ?: return
        val player = event.player
        if (player.isSneaking) {
            when (event.action) {
                Action.LEFT_CLICK_AIR -> customItem.shiftLeftClickAir?.let {
                    it.run(customItem.type, player, item)
                    event.isCancelled = true
                }
                Action.LEFT_CLICK_BLOCK -> customItem.shiftLeftClickBlock?.let {
                    it.run(
                        customItem.type,
                        player,
                        item,
                        event.clickedBlock,
                        event.blockFace
                    )
                    event.isCancelled = true
                }
                Action.RIGHT_CLICK_AIR -> customItem.shiftRightClickAir?.let {
                    it.run(
                        customItem.type,
                        player,
                        item
                    )
                    event.isCancelled = true
                }
                Action.RIGHT_CLICK_BLOCK -> customItem.shiftRightClickBlock?.let {
                    it.run(
                        customItem.type,
                        player,
                        item,
                        event.clickedBlock,
                        event.blockFace
                    )
                    event.isCancelled = true
                }
                else -> return
            }
        } else {
            when (event.action) {
                Action.LEFT_CLICK_AIR -> customItem.leftClickAir?.let {
                    it.run(customItem.type, player, item)
                    event.isCancelled = true
                }
                Action.LEFT_CLICK_BLOCK -> customItem.leftClickBlock?.let {
                    it.run(
                        customItem.type,
                        player,
                        item,
                        event.clickedBlock,
                        event.blockFace
                    )
                    event.isCancelled = true
                }
                Action.RIGHT_CLICK_AIR -> customItem.rightClickAir?.let {
                    it.run(
                        customItem.type,
                        player,
                        item
                    )
                    event.isCancelled = true
                }
                Action.RIGHT_CLICK_BLOCK -> customItem.rightClickBlock?.let {
                    it.run(
                        customItem.type,
                        player,
                        item,
                        event.clickedBlock,
                        event.blockFace
                    )
                    event.isCancelled = true
                }
                else -> return
            }
        }
    }

    @EventHandler
    fun onPlayerInteractEntity(event: PlayerInteractEntityEvent) {
        val item = if (event.hand == EquipmentSlot.HAND) event.player.inventory.itemInMainHand else if (event.hand === EquipmentSlot.OFF_HAND) event.player.inventory.itemInOffHand else return
        val customItem = getCustomItem(item) ?: return
        customItem.rightClickEntity?.let {
            it.run(customItem.type, event.player, item, event.rightClicked)
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onPrepareItemCraft(event: PrepareItemCraftEvent) {
        for (item in event.inventory.matrix) {
            if (item == null) continue
            if (getCustomItem(item) != null) {
                event.inventory.result = null
                return
            }
        }
    }

    @EventHandler
    fun onPlayerArmorChange(event: PlayerArmorChangeEvent) {
        if (event.newItem == event.oldItem) return

        event.oldItem?.let {
            val customItem = getCustomItem(it) ?: return@let
            customItem.unequipArmor?.run(customItem.type, event.player, it)
        }

        event.newItem?.let {
            val customItem = getCustomItem(it) ?: return@let
            customItem.equipArmor?.run(customItem.type, event.player, it)
        }
    }

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        val item = event.player.inventory.itemInMainHand
        val customItem = getCustomItem(item) ?: return
        customItem.breakBlock?.run(customItem.type, event.player, item, event.block)
    }
}

fun ItemStack.makeCustom(key: String) {
    val meta = itemMeta
    meta?.persistentDataContainer?.set(NamespacedKey(Blanktopia.INSTANCE, "type"), PersistentDataType.STRING, key)
    itemMeta = meta
}

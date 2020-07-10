package me.weiwen.blanktopia

import org.bukkit.*
import org.bukkit.block.Block
import org.bukkit.block.Container
import org.bukkit.block.Sign
import org.bukkit.block.data.Directional
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.SignChangeEvent
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.BlockInventoryHolder
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.*

val SIGN_TYPES = setOf(
    Material.ACACIA_WALL_SIGN,
    Material.BIRCH_WALL_SIGN,
    Material.DARK_OAK_WALL_SIGN,
    Material.JUNGLE_WALL_SIGN,
    Material.OAK_WALL_SIGN,
    Material.SPRUCE_WALL_SIGN
)

val CONTAINER_TYPES = setOf(
    Material.CHEST,
    Material.TRAPPED_CHEST,
    Material.BARREL
)

class BlanktopiaShop : JavaPlugin(), Listener {
    companion object {
        lateinit var INSTANCE: BlanktopiaShop
            private set
    }

    override fun onLoad() {
        INSTANCE = this
        createConfig()
    }

    override fun onEnable() {
        reloadConfig()
        server.pluginManager.registerEvents(this, this)
        logger.info("BlanktopiaShop is enabled")
    }

    override fun onDisable() {
        logger.info("BlanktopiaShop is disabled")
    }

    private fun createConfig() {
        try {
            if (!dataFolder.exists()) {
                dataFolder.mkdirs()
            }
            val file = File(dataFolder, "config.yml")
            if (!file.exists()) {
                saveDefaultConfig()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onSignChange(event: SignChangeEvent) {
        if (event.getLine(0) != "[Shop]") return
        event.isCancelled = true
        val player = event.player
        if (!player.hasPermission("ww.shop.create")) {
            player.sendMessage("${ChatColor.RED}You don't have permission to set up shops!")
            event.block.breakNaturally()
            return
        }

        val description = event.getLine(1) ?: ""
        val price = event.getLine(2)
        if (price == null) {
            player.sendMessage("${ChatColor.RED}Put the price in the second line. e.g. \"2 diamonds\"")
            event.block.breakNaturally()
            return
        }
        val splitPrice = price.split(' ', limit = 2)
        if (splitPrice.size != 2) {
            player.sendMessage("${ChatColor.RED}Put the price in the second line. e.g. \"2 diamonds\"")
            event.block.breakNaturally()
            return
        }
        val amount = splitPrice[0].toIntOrNull()
        val item = parseItem(splitPrice[1], amount != 1)
        if (amount == null || item == null) {
            player.sendMessage("${ChatColor.RED}Put the price in the second line. e.g. \"2 diamonds\"")
            event.block.breakNaturally()
            return
        }
        val (material, name) = item

        var owner = event.getLine(3)
        if (owner.isNullOrEmpty()) owner = player.name
        if (owner != player.name && !player.hasPermission("ww.shop.create.others")) {
            player.sendMessage("${ChatColor.RED}Leave the last line blank.")
            event.isCancelled = true
            event.block.breakNaturally()
            return
        }
        val uuid = server.getPlayer(owner)?.uniqueId?.toString() ?: ""

        val chest = getBlockBehindSign(event.block) ?: return
        val state = chest.state as? Container ?: return
        val container = state.persistentDataContainer
        container.set(NamespacedKey(this, "player"), PersistentDataType.STRING, uuid)
        container.set(NamespacedKey(this, "amount"), PersistentDataType.INTEGER, amount)
        container.set(NamespacedKey(this, "item"), PersistentDataType.STRING, material.toString())
        container.set(NamespacedKey(this, "itemName"), PersistentDataType.STRING, name)
        state.update()

        val sign = event.block.state as? Sign ?: return
        sign.setLine(0, "${ChatColor.LIGHT_PURPLE}[Shop]")
        sign.setLine(1, "${ChatColor.GOLD}${description}")
        sign.setLine(2, "${ChatColor.GOLD}${amount} ${name}")
        sign.setLine(3, "${ChatColor.DARK_PURPLE}${player.name}")
        sign.isEditable = false
        sign.update()
    }

    @EventHandler(ignoreCancelled = true)
    fun onBlockBreak(event: BlockBreakEvent) {
        if (!SIGN_TYPES.contains(event.block.type)) return
        val chest = getBlockBehindSign(event.block) ?: return
        val state = chest.state as? Container ?: return
        val container = state.persistentDataContainer
        val uuid = container.get(NamespacedKey(this, "player"), PersistentDataType.STRING)
        if (uuid != null && UUID.fromString(uuid) != event.player.uniqueId && !event.player.hasPermission("ww.shop.break")) {
            event.player.sendMessage("${ChatColor.RED}You don't have permission to break other players' shops!")
            event.isCancelled = true
            return
        }
        container.remove(NamespacedKey(this, "player"))
        container.remove(NamespacedKey(this, "amount"))
        container.remove(NamespacedKey(this, "item"))
        state.update()
    }

    @EventHandler(ignoreCancelled = true)
    fun onInteractSign(event: PlayerInteractEvent) {
        if (event.action != Action.RIGHT_CLICK_BLOCK) return
        val block = event.clickedBlock ?: return
        if (!SIGN_TYPES.contains(block.type)) return
        val player = event.player

        val chest = getBlockBehindSign(block) ?: return
        if (!CONTAINER_TYPES.contains(chest.type)) return
        val state = chest.state as? Container ?: return
        val container = state.persistentDataContainer
        if (container.get(NamespacedKey(this, "item"), PersistentDataType.STRING) == null) return
        player.openInventory(state.inventory)
    }

    @EventHandler
    fun onInventoryOpen(event: InventoryOpenEvent) {
        val block = (event.inventory.holder as? BlockInventoryHolder)?.block ?: return
        val chest = block.state as? Container ?: return
        val container = chest.persistentDataContainer
        val amount = container.get(NamespacedKey(this, "amount"), PersistentDataType.INTEGER) ?: return
        val item = container.get(NamespacedKey(this, "itemName"), PersistentDataType.STRING) ?: return
        val uuid = container.get(NamespacedKey(this, "player"), PersistentDataType.STRING)
        if (uuid != null && UUID.fromString(uuid) == event.player.uniqueId) {
            event.player.sendMessage("${ChatColor.GOLD}Editing your own shop.")
        } else {
            event.player.sendMessage("${ChatColor.GOLD}Each stack costs ${amount} ${item}.")
        }
    }

    @EventHandler
    fun onInventoryDrag(event: InventoryDragEvent) {
        val block = (event.inventory.holder as? BlockInventoryHolder)?.block ?: return
        val chest = block.state as? Container ?: return
        val container = chest.persistentDataContainer
        val uuid = container.get(NamespacedKey(this, "player"), PersistentDataType.STRING) ?: return
        if (uuid != null && UUID.fromString(uuid) == event.whoClicked.uniqueId) {
            return
        }
        event.isCancelled = true
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val block = (event.inventory.holder as? BlockInventoryHolder)?.block ?: return
        val chest = block.state as? Container ?: return
        val container = chest.persistentDataContainer
        val uuid = container.get(NamespacedKey(this, "player"), PersistentDataType.STRING)
        if (uuid != null && UUID.fromString(uuid) == event.whoClicked.uniqueId) {
            return
        }
        event.isCancelled = true
        if (event.click != ClickType.LEFT) return

        val amount = container.get(NamespacedKey(this, "amount"), PersistentDataType.INTEGER) ?: return
        val item = container.get(NamespacedKey(this, "item"), PersistentDataType.STRING) ?: return
        val material = Material.getMaterial(item) ?: return
        val cost = ItemStack(material, amount)

        val clickedItem = event.inventory.getItem(event.slot) ?: return
        if (clickedItem.type == material) return

        val player = event.whoClicked as? Player ?: return
        if (player.inventory.containsAtLeast(cost, amount)) {
            val emptySlot = player.inventory.firstEmpty()
            player.inventory.removeItem(cost)
            event.inventory.setItem(event.slot, cost)
            player.inventory.setItem(emptySlot, clickedItem)
            player.playSoundAt(Sound.BLOCK_NOTE_BLOCK_BELL, SoundCategory.PLAYERS, 1.0f, 0.81f)
            logPurchase(player, UUID.fromString(uuid), block.location, clickedItem, cost)
        } else {
            player.playSoundTo(Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, SoundCategory.PLAYERS, 1.0f, 1.0f)
        }
    }

    fun getBlockBehindSign(block: Block): Block? {
        if (!SIGN_TYPES.contains(block.type)) return null
        val data = block.blockData as? Directional ?: return null
        return block.getRelative(data.facing.oppositeFace)
    }

    fun logPurchase(player: Player, uuid: UUID, location: Location, clickedItem: ItemStack, cost: ItemStack) {
        val owner = Bukkit.getServer().getOfflinePlayer(uuid)
        if (player.isOnline) {
            player.sendMessage("${ChatColor.GOLD}${player.displayName} has bought ${clickedItem} for ${cost}.")
        } else {
            // send mail
        }
    }
}

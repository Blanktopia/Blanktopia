package me.weiwen.blanktopia

import com.earth2me.essentials.Essentials
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
import org.bukkit.event.inventory.*
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.BlockInventoryHolder
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.*
import java.util.logging.Level

val SIGN_TYPES = setOf(
    Material.ACACIA_WALL_SIGN,
    Material.BIRCH_WALL_SIGN,
    Material.DARK_OAK_WALL_SIGN,
    Material.JUNGLE_WALL_SIGN,
    Material.OAK_WALL_SIGN,
    Material.SPRUCE_WALL_SIGN,
    Material.CRIMSON_WALL_SIGN,
    Material.WARPED_WALL_SIGN
)

val CONTAINER_TYPES = setOf(
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
        if (event.getLine(0)?.toLowerCase() != "[shop]") return
        event.isCancelled = true
        val player = event.player
        if (!player.hasPermission("blanktopia.shop.create")) {
            player.sendMessage("${ChatColor.RED}You don't have permission to set up shops!")
            event.block.breakNaturally()
            return
        }

        val description = event.getLine(1) ?: ""
        val price = event.getLine(2)
        if (price == null) {
            player.sendMessage("${ChatColor.RED}Put the price in the third line. e.g. \"2 diamonds\"")
            event.block.breakNaturally()
            return
        }
        val item = parseItem(price)
        if (item == null) {
            player.sendMessage("${ChatColor.RED}Put the price in the third line. e.g. \"2 diamonds\"")
            event.block.breakNaturally()
            return
        }
        val (amount, material, name) = item

        var owner = event.getLine(3)
        if (owner.isNullOrEmpty()) owner = player.name
        if (owner != player.name && !player.hasPermission("blanktopia.shop.create.others")) {
            player.sendMessage("${ChatColor.RED}Leave the last line blank.")
            event.isCancelled = true
            event.block.breakNaturally()
            return
        }
        val uuid = if (owner == "admin") {
            owner = ""
            ""
        } else {
            server.getPlayer(owner)?.uniqueId?.toString() ?: player.uniqueId.toString()
        }

        val chest = getBlockBehindSign(event.block) ?: return
        val state = chest.state as? Container ?: return
        val container = state.persistentDataContainer
        container.set(NamespacedKey(this, "owner"), PersistentDataType.STRING, uuid)
        container.set(NamespacedKey(this, "amount"), PersistentDataType.INTEGER, amount)
        container.set(NamespacedKey(this, "item"), PersistentDataType.STRING, material.toString())
        container.set(NamespacedKey(this, "cost"), PersistentDataType.STRING, name)
        state.update()

        val sign = event.block.state as? Sign ?: return
        sign.setLine(0, "${ChatColor.DARK_PURPLE}[Shop]")
        sign.setLine(1, "${ChatColor.GOLD}${ChatColor.BOLD}${description}")
        sign.setLine(2, "${ChatColor.GOLD}${name}")
        sign.setLine(3, "${ChatColor.DARK_PURPLE}${owner}")
        sign.isEditable = false
        sign.update()
    }

    @EventHandler(ignoreCancelled = true)
    fun onBlockBreak(event: BlockBreakEvent) {
        if (!SIGN_TYPES.contains(event.block.type)) return
        val chest = getBlockBehindSign(event.block) ?: return
        val state = chest.state as? Container ?: return
        val container = state.persistentDataContainer
        val uuid = container.get(NamespacedKey(this, "owner"), PersistentDataType.STRING)
        if (uuid != null && uuid != "" && UUID.fromString(uuid) != event.player.uniqueId && !event.player.hasPermission("blanktopia.shop.break")) {
            event.player.sendMessage("${ChatColor.RED}You don't have permission to break other players' shops!")
            event.isCancelled = true
            return
        }
        container.remove(NamespacedKey(this, "owner"))
        container.remove(NamespacedKey(this, "amount"))
        container.remove(NamespacedKey(this, "cost"))
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
        if (container.get(NamespacedKey(this, "item"), PersistentDataType.STRING) == null) {
            val sign = block.state as? Sign ?: return
            if (sign.getLine(0) == "${ChatColor.DARK_PURPLE}[Shop]") {
                // Migrate Skript shop
                val price = sign.getLine(2).substring(2)
                val (amount, material, name) = parseItem(price) ?: return
                val owner = sign.getLine(3).substring(2)
                val uuid = server.getOfflinePlayer(owner)?.uniqueId?.toString() ?: ""
                container.set(NamespacedKey(this, "owner"), PersistentDataType.STRING, uuid)
                container.set(NamespacedKey(this, "amount"), PersistentDataType.INTEGER, amount)
                container.set(NamespacedKey(this, "item"), PersistentDataType.STRING, material.toString())
                container.set(NamespacedKey(this, "cost"), PersistentDataType.STRING, name)
                state.update()
                if (!player.hasPermission("blanktopia.shop.buy")) {
                    player.sendMessage("${ChatColor.RED}You don't have permission to buy from shops!")
                    return
                }
                player.openInventory(state.inventory)
            }
            return
        }

        if (!player.hasPermission("blanktopia.shop.buy")) {
            player.sendMessage("${ChatColor.RED}You don't have permission to buy from shops!")
            return
        }
        player.openInventory(state.inventory)
    }

    @EventHandler(ignoreCancelled = true)
    fun onInventoryOpen(event: InventoryOpenEvent) {
        val block = (event.inventory.holder as? BlockInventoryHolder)?.block ?: return
        val chest = block.state as? Container ?: return
        val container = chest.persistentDataContainer
        val cost = container.get(NamespacedKey(this, "cost"), PersistentDataType.STRING) ?: return
        val uuid = container.get(NamespacedKey(this, "owner"), PersistentDataType.STRING) ?: return
        val player = event.player as? Player ?: return
        if (uuid != "" && UUID.fromString(uuid) == player.uniqueId) {
            player.sendMessage("${ChatColor.GOLD}Editing your own shop.")
        } else if (player.hasContainerTrust(block.location)) {
            player.sendMessage("${ChatColor.GOLD}Editing someone else's shop.")
        }
        event.player.sendMessage("${ChatColor.GOLD}Each stack costs ${cost}.")
    }

    @EventHandler
    fun onInventoryDrag(event: InventoryDragEvent) {
        val block = (event.inventory.holder as? BlockInventoryHolder)?.block ?: return
        val chest = block.state as? Container ?: return
        val container = chest.persistentDataContainer
        val uuid = container.get(NamespacedKey(this, "owner"), PersistentDataType.STRING) ?: return
        val player = event.whoClicked as? Player ?: return
        if ((uuid != "" && UUID.fromString(uuid) == player.uniqueId) || player.hasContainerTrust(block.location)) {
            return
        }
        event.isCancelled = true
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val block = (event.inventory.holder as? BlockInventoryHolder)?.block ?: return
        val chest = block.state as? Container ?: return
        val container = chest.persistentDataContainer
        val uuid = container.get(NamespacedKey(this, "owner"), PersistentDataType.STRING) ?: return
        val player = event.whoClicked as? Player ?: return
        if ((uuid != "" && UUID.fromString(uuid) == player.uniqueId) || player.hasContainerTrust(block.location)) {
            return
        }
        event.isCancelled = true
        if (event.click != ClickType.LEFT) return
        val inventory = event.clickedInventory ?: return
        if (inventory.type == InventoryType.PLAYER) return

        val amount = container.get(NamespacedKey(this, "amount"), PersistentDataType.INTEGER) ?: return
        val item = container.get(NamespacedKey(this, "item"), PersistentDataType.STRING) ?: return
        val material = Material.getMaterial(item) ?: return
        val cost = ItemStack(material, amount)

        val clickedItem = inventory.getItem(event.slot) ?: return
        if (clickedItem.type == material || clickedItem.type == Material.AIR) return

        if (!player.inventory.containsAtLeast(cost, amount)) {
            player.playSoundTo(Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, SoundCategory.PLAYERS, 1.0f, 1.0f)
            return
        }

        val emptySlot = player.inventory.firstEmpty()
        if (emptySlot == -1) {
            player.playSoundTo(Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, SoundCategory.PLAYERS, 1.0f, 1.0f)
            return
        }

        player.inventory.removeItem(cost)
        player.inventory.setItem(emptySlot, clickedItem)
        player.playSoundAt(Sound.BLOCK_NOTE_BLOCK_BELL, SoundCategory.PLAYERS, 1.0f, 0.81f)
        if (uuid != "") {
            inventory.setItem(event.slot, cost)
            val price = container.get(NamespacedKey(this, "cost"), PersistentDataType.STRING) ?: toString(cost)
            logPurchase(player, UUID.fromString(uuid), clickedItem, price)
        }
    }

    fun getBlockBehindSign(block: Block): Block? {
        if (!SIGN_TYPES.contains(block.type)) return null
        val data = block.blockData as? Directional ?: return null
        return block.getRelative(data.facing.oppositeFace)
    }


    private fun logPurchase(player: Player, uuid: UUID, clickedItem: ItemStack, price: String) {
        val owner = Bukkit.getServer().getOfflinePlayer(uuid)
        val boughtItem = toString(clickedItem)
        player.sendMessage("${ChatColor.GOLD}You have bought ${boughtItem}${ChatColor.GOLD} for ${price}.${ChatColor.GOLD}")

        val essentials = server.pluginManager.getPlugin("Essentials") as? Essentials ?: return
        val user = essentials.getUser(owner.uniqueId) ?: return
        logger.log(Level.INFO, "${ChatColor.GOLD}${player.displayName}${ChatColor.GOLD} has bought ${boughtItem}${ChatColor.GOLD} from ${owner.name} for ${price}${ChatColor.GOLD}.")
        if (!owner.isOnline || user.isAfk) {
            user.addMail("${ChatColor.GOLD}${player.displayName}${ChatColor.GOLD} has bought ${boughtItem}${ChatColor.GOLD} for ${price}${ChatColor.GOLD}.")
        } else {
            owner.player?.let {
                it.sendMessage("${ChatColor.GOLD}${player.displayName}${ChatColor.GOLD} has bought ${boughtItem}${ChatColor.GOLD} for ${price}.${ChatColor.GOLD}.")
                it.playSoundTo(Sound.BLOCK_NOTE_BLOCK_BELL, SoundCategory.PLAYERS, 1.0f, 0.81f)
            }
        }
    }
}

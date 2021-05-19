package me.weiwen.blanktopia

import com.earth2me.essentials.Essentials
import org.bukkit.*
import org.bukkit.block.Block
import org.bukkit.block.Container
import org.bukkit.block.Sign
import org.bukkit.block.data.Directional
import org.bukkit.block.data.type.Chest
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
    Material.BARREL,
    Material.CHEST,
    Material.SHULKER_BOX
)

class BlanktopiaShop : JavaPlugin(), Listener {
    val canBuyFromOwnShop: MutableSet<UUID> = mutableSetOf()

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

        getCommand("shopedit")?.setExecutor { sender, _, _, _ ->
            if (sender !is Player) return@setExecutor false
            if (canBuyFromOwnShop.contains(sender.uniqueId)) {
                canBuyFromOwnShop.remove(sender.uniqueId)
                sender.sendMessage("${ChatColor.GOLD}You can no longer buy from shops you are trusted in.")
            } else {
                canBuyFromOwnShop.add(sender.uniqueId)
                sender.sendMessage("${ChatColor.GOLD}You can now buy from shops you are trusted in.")
            }
            true
        }

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
        if (event.getLine(0)?.toLowerCase() == "[shop]") {
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
            container.set(NamespacedKey(this, "type"), PersistentDataType.STRING, "shop")
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

        } else if (event.getLine(0)?.toLowerCase() == "[mail]") {
            event.isCancelled = true
            val player = event.player
            if (!player.hasPermission("blanktopia.mail.create")) {
                player.sendMessage("${ChatColor.RED}You don't have permission to set up mailboxes!")
                event.block.breakNaturally()
                return
            }

            var owner = event.getLine(3)
            if (owner.isNullOrEmpty()) owner = player.name
            if (owner != player.name && !player.hasPermission("blanktopia.mail.create.others")) {
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
            container.set(NamespacedKey(this, "type"), PersistentDataType.STRING, "mail")
            container.set(NamespacedKey(this, "owner"), PersistentDataType.STRING, uuid)
            state.update()

            val sign = event.block.state as? Sign ?: return
            sign.setLine(0, "${ChatColor.GREEN}[Mail]")
            sign.setLine(1, "${ChatColor.GOLD}${event.getLine(1)}")
            sign.setLine(2, "${ChatColor.GOLD}${event.getLine(2)}")
            sign.setLine(3, "${ChatColor.GREEN}${owner}")
            sign.isEditable = false
            sign.update()
        }
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
        val blockData = chest.blockData
        if (blockData is Chest && blockData.type != Chest.Type.SINGLE) return
        val state = chest.state as? Container ?: return
        val container = state.persistentDataContainer

        var type = container.get(NamespacedKey(this, "type"), PersistentDataType.STRING)

        // Migrate chest type
        if (type == null && container.get(NamespacedKey(this, "cost"), PersistentDataType.STRING) != null) {
            container.set(NamespacedKey(this, "type"), PersistentDataType.STRING, "shop")
            type = "shop"
        }

        if (type == "shop") {
            // Migrate chest names
            val cost = container.get(NamespacedKey(this, "cost"), PersistentDataType.STRING) ?: return
            state.customName = "${cost} per stack"
            state.update()

            if (!player.hasPermission("blanktopia.shop.buy")) {
                player.sendMessage("${ChatColor.RED}You don't have permission to buy from shops!")
                return
            }

        } else if (type == "mail") {
            if (!player.hasPermission("blanktopia.mail.send")) {
                player.sendMessage("${ChatColor.RED}You don't have permission to send mail!")
                return
            }
        }

        player.openInventory(state.inventory)
    }

    @EventHandler(ignoreCancelled = true)
    fun onInventoryOpen(event: InventoryOpenEvent) {
        val block = (event.inventory.holder as? BlockInventoryHolder)?.block ?: return
        val chest = block.state as? Container ?: return
        val container = chest.persistentDataContainer
        val type = container.get(NamespacedKey(this, "type"), PersistentDataType.STRING) ?: return
        if (type == "shop") {
            val cost = container.get(NamespacedKey(this, "cost"), PersistentDataType.STRING) ?: return
            val uuid = container.get(NamespacedKey(this, "owner"), PersistentDataType.STRING) ?: return
            val player = event.player as? Player ?: return
            if (!player.hasPermission("blanktopia.shop.buy")) {
                player.sendMessage("${ChatColor.RED}You don't have permission to buy from shops!")
                event.isCancelled = true
                return
            }
            if (!canBuyFromOwnShop.contains(player.uniqueId)) {
                if (uuid != "" && UUID.fromString(uuid) == player.uniqueId || player.hasContainerTrust(block.location)) {
                    player.sendActionBar("${ChatColor.GOLD}Type /shopedit to buy from shops you are trusted in.")
                }
            }
        }
    }

    @EventHandler
    fun onInventoryDrag(event: InventoryDragEvent) {
        val block = (event.inventory.holder as? BlockInventoryHolder)?.block ?: return
        val chest = block.state as? Container ?: return
        val container = chest.persistentDataContainer
        val type = container.get(NamespacedKey(this, "type"), PersistentDataType.STRING) ?: return
        if (type == "shop") {
            val uuid = container.get(NamespacedKey(this, "owner"), PersistentDataType.STRING) ?: return
            val player = event.whoClicked as? Player ?: return
            if (!canBuyFromOwnShop.contains(player.uniqueId) &&
                ((uuid != "" && UUID.fromString(uuid) == player.uniqueId) ||
                        player.hasContainerTrust(block.location))
            ) {
                return
            }
            event.isCancelled = true
        } else if (type == "mail") {
            val uuid = container.get(NamespacedKey(this, "owner"), PersistentDataType.STRING) ?: return
            val player = event.whoClicked as? Player ?: return
            if ((uuid != "" && UUID.fromString(uuid) == player.uniqueId) || player.hasContainerTrust(block.location)) {
                return
            }
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val block = (event.inventory.holder as? BlockInventoryHolder)?.block ?: return
        val chest = block.state as? Container ?: return
        val container = chest.persistentDataContainer
        val type = container.get(NamespacedKey(this, "type"), PersistentDataType.STRING) ?: return
        if (type == "shop") {
            val uuid = container.get(NamespacedKey(this, "owner"), PersistentDataType.STRING) ?: return
            val player = event.whoClicked as? Player ?: return
            if (!canBuyFromOwnShop.contains(player.uniqueId) &&
                ((uuid != "" && UUID.fromString(uuid) == player.uniqueId) ||
                        player.hasContainerTrust(block.location))
            ) {
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

            val price = container.get(NamespacedKey(this, "cost"), PersistentDataType.STRING) ?: toString(cost)
            if (uuid != "") {
                inventory.setItem(event.slot, cost)
                logPurchase(player, UUID.fromString(uuid), clickedItem, price)
            } else {
                logger.log(
                    Level.INFO,
                    "${ChatColor.GOLD}${player.displayName}${ChatColor.GOLD} has bought ${toString(clickedItem)}${ChatColor.GOLD} for ${price}${ChatColor.GOLD}."
                )
            }
        } else if (type == "mail") {
            val uuid = container.get(NamespacedKey(this, "owner"), PersistentDataType.STRING) ?: return
            val player = event.whoClicked as? Player ?: return
            if ((uuid != "" && UUID.fromString(uuid) == player.uniqueId) || player.hasContainerTrust(block.location)) {
                return
            }
            val inventory = event.clickedInventory ?: return
            if (inventory.type == InventoryType.PLAYER
                && (event.click != ClickType.SHIFT_LEFT || event.click != ClickType.SHIFT_RIGHT)) {
                return
            }

            // Allow only if:
            // - non-empty cursor
            // - left click
            // - on an empty slot
            val item = event.cursor
            val clickedItem = inventory.getItem(event.slot)
            if (item == null
                || event.click != ClickType.LEFT
                || (clickedItem != null && clickedItem.type != Material.AIR)) {
                player.playSoundTo(Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, SoundCategory.PLAYERS, 1.0f, 1.0f)
                event.isCancelled = true
                return
            }

            player.playSoundAt(Sound.BLOCK_NOTE_BLOCK_BELL, SoundCategory.PLAYERS, 1.0f, 0.81f)

            if (uuid != "") {
                logMail(player, UUID.fromString(uuid), item)
            } else {
                logger.log(
                    Level.INFO,
                    "${ChatColor.GOLD}${player.displayName}${ChatColor.GOLD} has sent ${toString(item)}${ChatColor.GOLD}."
                )
            }
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

    private fun logMail(player: Player, uuid: UUID, item: ItemStack) {
        val owner = Bukkit.getServer().getOfflinePlayer(uuid)
        val item = toString(item)
        player.sendMessage("${ChatColor.GOLD}You have sent ${item}${ChatColor.GOLD}")

        val essentials = server.pluginManager.getPlugin("Essentials") as? Essentials ?: return
        val user = essentials.getUser(owner.uniqueId) ?: return
        logger.log(Level.INFO, "${ChatColor.GOLD}${player.displayName}${ChatColor.GOLD} has sent ${item}${ChatColor.GOLD} to ${owner.name}.")
        if (!owner.isOnline || user.isAfk) {
            user.addMail("${ChatColor.GOLD}${player.displayName}${ChatColor.GOLD} has sent you ${item}${ChatColor.GOLD}.")
        } else {
            owner.player?.let {
                it.sendMessage("${ChatColor.GOLD}${player.displayName}${ChatColor.GOLD} has sent you ${item}${ChatColor.GOLD}.")
                it.playSoundTo(Sound.BLOCK_NOTE_BLOCK_BELL, SoundCategory.PLAYERS, 1.0f, 0.81f)
            }
        }
    }
}

package me.weiwen.blanktopia.items

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent
import com.destroystokyo.paper.event.player.PlayerJumpEvent
import me.weiwen.blanktopia.*
import me.weiwen.blanktopia.recipes.build
import me.weiwen.blanktopia.triggers.EQUIPPED_TRIGGERS
import me.weiwen.blanktopia.triggers.Trigger
import me.weiwen.blanktopia.triggers.TriggerType
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityToggleGlideEvent
import org.bukkit.event.entity.EntityToggleSwimEvent
import org.bukkit.event.inventory.*
import org.bukkit.event.player.*
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin
import java.util.*
import java.util.logging.Level

class CustomItems(private val plugin: JavaPlugin) :
    Listener, Module {
    lateinit var items: Map<String, CustomItem>
    lateinit var recipes: Set<Recipe>

    private val equippedItems: MutableMap<UUID,
            MutableMap<TriggerType,
                    MutableMap<PlayerArmorChangeEvent.SlotType, Pair<ItemStack, List<Trigger>>>>> = mutableMapOf()

    override fun enable() {
        items = populateItems()
        recipes = populateRecipes()
        plugin.server.pluginManager.registerEvents(this, plugin)

        val witemCommand = plugin.getCommand("witem")
        witemCommand?.setExecutor { sender, _, _, args ->
            val name = args[0] ?: return@setExecutor false
            val player = if (args.size > 1) plugin.server.getPlayer(args[1]) else sender as? Player
            if (player == null) return@setExecutor false
            player.inventory.addItem(buildItem(name))
            player.playSound(player.location, Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 1.0f)
            true
        }
        witemCommand?.setTabCompleter { _, _, _, _ -> items.keys.toList() }

        runEquipTriggers()
    }

    override fun disable() {
    }

    override fun reload() {
        items = populateItems()
        recipes = populateRecipes()

        runEquipTriggers()
    }

    private fun runEquipTriggers() {
        equippedItems.clear()
        for (player in plugin.server.onlinePlayers) {
            val slots: MutableMap<PlayerArmorChangeEvent.SlotType, ItemStack> = mutableMapOf()
            player.inventory.helmet?.let { slots[PlayerArmorChangeEvent.SlotType.HEAD] = it }
            player.inventory.chestplate?.let { slots[PlayerArmorChangeEvent.SlotType.CHEST] = it }
            player.inventory.leggings?.let { slots[PlayerArmorChangeEvent.SlotType.LEGS] = it }
            player.inventory.boots?.let { slots[PlayerArmorChangeEvent.SlotType.FEET] = it }
            for ((slot, item) in slots.entries) {
                val customItem = item.getCustomItem() ?: continue
                customItem.triggers.forEach { triggerType, triggers ->
                    if (triggerType in EQUIPPED_TRIGGERS) {
                        equippedItems
                            .getOrPut(player.uniqueId, { mutableMapOf() })
                            .getOrPut(triggerType, { mutableMapOf() })[slot] = Pair(item, triggers)
                    }
                }
                customItem.triggers[TriggerType.EQUIP_ARMOR]?.forEach {
                    it.run(player, item)
                }
            }
        }
    }

    fun buildItem(type: String): ItemStack? {
        return items[type]?.build()
    }

    private fun populateItems(): Map<String, CustomItem> {
        val items = mutableMapOf<String, CustomItem>()
        val config = plugin.config.getConfigurationSectionOrError("items") ?: return items
        for (type in config.getKeys(false)) {
            items[type] = CustomItem(type, config.getConfigurationSection(type)!!)
        }
        return items
    }

    private fun populateRecipes(): Set<Recipe> {
        val recipes = mutableSetOf<Recipe>()
        val config = plugin.config.getConfigurationSectionOrError("recipes") ?: return recipes
        for ((key, node) in config.asNode()) {
            if (node == null) {
                BlanktopiaItems.INSTANCE.logger.log(Level.WARNING, "Invalid recipe detected at key '$key'")
                continue
            }
            val recipe = parseRecipe(node as Node) ?: continue
            recipes.add(recipe)
        }
        return recipes
    }

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        val item = event.item ?: return
        val customItem = item.getCustomItem() ?: return
        val player = event.player

        if ((event.action == Action.RIGHT_CLICK_AIR ||
                        event.action == Action.RIGHT_CLICK_BLOCK) &&
                event.hand != EquipmentSlot.HAND &&
                event.player.inventory.itemInMainHand.type == Material.CROSSBOW) {
            return
        }

        var removeItem = false
        when (event.action) {
            Action.RIGHT_CLICK_AIR -> customItem.triggers[TriggerType.RIGHT_CLICK_AIR]?.forEach {
                if (it.test(player, item)) {
                    it.run(player, item)
                    if (it.cancel) event.isCancelled = true
                    if (it.removeItem) removeItem = true
                }
            }
            Action.RIGHT_CLICK_BLOCK -> customItem.triggers[TriggerType.RIGHT_CLICK_BLOCK]?.forEach {
                if (!it.force && !event.player.isSneaking && event.clickedBlock!!.type.canBeInteractedWith) return@forEach
                if (it.test(player, item)) {
                    it.run(
                        player,
                        item,
                        event.clickedBlock ?: return@forEach,
                        event.blockFace
                    )
                    if (it.cancel) event.isCancelled = true
                    if (it.removeItem) removeItem = true
                }
            }
            Action.LEFT_CLICK_AIR -> customItem.triggers[TriggerType.LEFT_CLICK_AIR]?.forEach {
                if (it.test(player, item)) {
                    it.run(player, item)
                    if (it.cancel) event.isCancelled = true
                    if (it.removeItem) removeItem = true
                }
            }
            Action.LEFT_CLICK_BLOCK -> customItem.triggers[TriggerType.LEFT_CLICK_BLOCK]?.forEach {
                if (it.test(player, item)) {
                    it.run(
                        player,
                        item,
                        event.clickedBlock ?: return@forEach,
                        event.blockFace
                    )
                    if (it.cancel) event.isCancelled = true
                    if (it.removeItem) removeItem = true
                }
            }
            else -> return
        }

        if (removeItem) {
            event.hand?.let { removeOne(player, it) }
        }
    }

    @EventHandler
    fun onPlayerInteractAtEntity(event: PlayerInteractAtEntityEvent) {
        val item = if (event.hand == EquipmentSlot.HAND) event.player.inventory.itemInMainHand else if (event.hand === EquipmentSlot.OFF_HAND) event.player.inventory.itemInOffHand else return
        val customItem = item.getCustomItem() ?: return

        var removeItem = false
        customItem.triggers[TriggerType.RIGHT_CLICK_ENTITY]?.forEach {
            if (it.test(event.player, item)) {
                it.run(event.player, item, event.rightClicked)
                if (it.cancel) event.isCancelled = true
                if (it.removeItem) removeItem = true
            }
        }
        if (removeItem) {
            removeOne(event.player, event.hand)
        }
    }

    @EventHandler
    fun onEntityDamageByPlayer(event: EntityDamageByEntityEvent) {
        if (event.cause != EntityDamageEvent.DamageCause.ENTITY_ATTACK) return

        val player = event.damager as? Player ?: return
        val item = player.inventory.itemInMainHand
        val customItem = item.getCustomItem() ?: return


        var removeItem = false
        customItem.triggers[TriggerType.DAMAGE_ENTITY]?.forEach {
            if (it.test(player, item)) {
                it.run(player, item, event.entity)
                if (it.cancel) event.isCancelled = true
                if (it.removeItem) removeItem = true
            }
        }
        if (removeItem) {
            player.inventory.setItemInMainHand(removeOne(item))
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onInventoryClick(event: InventoryClickEvent) {
        val inventory = event.clickedInventory ?: return
        val item = inventory.getItem(event.slot) ?: return
        val customItem = item.getCustomItem() ?: return
        val player = event.whoClicked as? Player ?: return
        val trigger = when (event.click) {
            ClickType.RIGHT -> TriggerType.RIGHT_CLICK_INVENTORY
            ClickType.LEFT -> TriggerType.LEFT_CLICK_INVENTORY
            ClickType.MIDDLE -> TriggerType.MIDDLE_CLICK_INVENTORY
            ClickType.SHIFT_RIGHT -> TriggerType.SHIFT_RIGHT_CLICK_INVENTORY
            ClickType.SHIFT_LEFT -> TriggerType.SHIFT_LEFT_CLICK_INVENTORY
            ClickType.DOUBLE_CLICK -> TriggerType.DOUBLE_CLICK_INVENTORY
            ClickType.DROP -> TriggerType.DROP_INVENTORY
            ClickType.CONTROL_DROP -> TriggerType.CONTROL_DROP_INVENTORY
            ClickType.WINDOW_BORDER_LEFT -> TriggerType.LEFT_BORDER_INVENTORY
            ClickType.WINDOW_BORDER_RIGHT -> TriggerType.RIGHT_BORDER_INVENTORY
            ClickType.NUMBER_KEY -> when (event.hotbarButton) {
                0 -> TriggerType.NUMBER_KEY_1_INVENTORY
                1 -> TriggerType.NUMBER_KEY_2_INVENTORY
                2 -> TriggerType.NUMBER_KEY_3_INVENTORY
                3 -> TriggerType.NUMBER_KEY_4_INVENTORY
                4 -> TriggerType.NUMBER_KEY_5_INVENTORY
                5 -> TriggerType.NUMBER_KEY_6_INVENTORY
                6 -> TriggerType.NUMBER_KEY_7_INVENTORY
                7 -> TriggerType.NUMBER_KEY_8_INVENTORY
                8 -> TriggerType.NUMBER_KEY_9_INVENTORY
                else -> TriggerType.NUMBER_KEY_1_INVENTORY
            }
            ClickType.CREATIVE -> TriggerType.CREATIVE_INVENTORY
            ClickType.SWAP_OFFHAND -> TriggerType.SWAP_OFFHAND_INVENTORY
            ClickType.UNKNOWN -> null
        } ?: return

        var removeItem = false
        customItem.triggers[trigger]?.forEach {
            if (it.test(player, item)) {
                it.run(player, item)
                if (it.cancel) event.isCancelled = true
                if (it.removeItem) removeItem = true
            }
        }
        if (removeItem) {
            inventory.setItem(event.slot, removeOne(item))
        }
    }

    @EventHandler
    fun onPlayerDropItem(event: PlayerDropItemEvent) {
        val item = event.itemDrop.itemStack
        val customItem = item.getCustomItem() ?: return

        customItem.triggers[TriggerType.DROP]?.forEach {
            if (it.test(event.player, item)) it.run(event.player, item)
            if (it.cancel) event.isCancelled = true
        }
    }

    @EventHandler
    fun onPlayerItemConsume(event: PlayerItemConsumeEvent) {
        val item = event.item
        val customItem = item.getCustomItem() ?: return
        customItem.triggers[TriggerType.CONSUME]?.forEach {
            if (it.test(event.player, item)) {
                it.run(event.player, item)
                if (it.cancel) event.isCancelled = true
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onPrepareAnvil(event: PrepareAnvilEvent) {
        val item = event.inventory.secondItem ?: return
        item.getCustomItem() ?: return
        event.result = null
    }

    @EventHandler(ignoreCancelled = true)
    fun onPrepareItemCraft(event: PrepareItemCraftEvent) {
        for (recipe in recipes) {
            if (recipe.validate(event.inventory)) {
                event.inventory.result = recipe.result.build()
                return
            }
        }

        for (item in event.inventory.matrix) {
            if (item == null) continue
            if (item.getCustomItem() != null) {
                event.inventory.result = null
                return
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onFurnaceBurn(event: FurnaceBurnEvent) {
        if (event.fuel.getCustomItem() != null) {
            event.isCancelled = true
            return
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onPlayerArmorChange(event: PlayerArmorChangeEvent) {
        if (event.newItem == event.oldItem) return

        event.oldItem?.let { item ->
            val customItem = item.getCustomItem() ?: return@let
            customItem.triggers.forEach { triggerType, _ ->
                if (triggerType in EQUIPPED_TRIGGERS) {
                    val triggersByType = equippedItems.get(event.player.uniqueId) ?: return@forEach
                    val triggers = triggersByType.get(triggerType) ?: return@forEach
                    triggers.remove(event.slotType)
                    if (triggers.isEmpty()) {
                        triggersByType.remove(triggerType)
                    }
                }
            }
            customItem.triggers[TriggerType.UNEQUIP_ARMOR]?.forEach {
                if (it.test(event.player, item)) {
                    it.run(event.player, item)
                }
            }
        }

        event.newItem?.let { item ->
            val customItem = item.getCustomItem() ?: return@let
            customItem.triggers.forEach { triggerType, triggers ->
                if (triggerType in EQUIPPED_TRIGGERS) {
                    equippedItems
                        .getOrPut(event.player.uniqueId, { mutableMapOf() })
                        .getOrPut(triggerType, { mutableMapOf() })[event.slotType] = Pair(item, triggers)
                }
            }
            customItem.triggers[TriggerType.EQUIP_ARMOR]?.forEach {
                if (it.test(event.player, item)) {
                    it.run(event.player, item)
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onBlockBreak(event: BlockBreakEvent) {
        val item = event.player.inventory.itemInMainHand
        val customItem = item.getCustomItem() ?: return
        customItem.triggers[TriggerType.BREAK_BLOCK]?.forEach {
            if (it.test(event.player, item)) {
                it.run(event.player, item, event.block)
                if (it.cancel) event.isCancelled = true
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onBlockPlace(event: BlockPlaceEvent) {
        val item = event.player.inventory.itemInMainHand
        val customItem = item.getCustomItem() ?: return
        customItem.triggers[TriggerType.PLACE_BLOCK]?.forEach {
            it.run(event.player, item, event.block)
            if (it.cancel) event.isCancelled = true
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onPlayerMove(event: PlayerMoveEvent) {
        equippedItems[event.player.uniqueId]?.get(TriggerType.MOVE)?.values?.forEach { (item, triggers) ->
            triggers.forEach {
                if (it.test(event.player, item)) {
                    it.run(event.player, item)
                    if (it.cancel) event.isCancelled = true
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onPlayerJump(event: PlayerJumpEvent) {
        equippedItems[event.player.uniqueId]?.get(TriggerType.JUMP)?.values?.forEach { (item, triggers) ->
            triggers.forEach {
                if (it.test(event.player, item)) {
                    it.run(event.player, item)
                    if (it.cancel) event.isCancelled = true
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onPlayerToggleSneak(event: PlayerToggleSneakEvent) {
        val triggerType = if (event.isSneaking) TriggerType.SNEAK else TriggerType.UNSNEAK
        equippedItems[event.player.uniqueId]?.get(triggerType)?.values?.forEach { (item, triggers) ->
            triggers.forEach {
                if (it.test(event.player, item)) {
                    it.run(event.player, item)
                    if (it.cancel) event.isCancelled = true
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onPlayerToggleSprint(event: PlayerToggleSprintEvent) {
        val triggerType = if (event.isSprinting) TriggerType.SPRINT else TriggerType.UNSPRINT
        equippedItems[event.player.uniqueId]?.get(triggerType)?.values?.forEach { (item, triggers) ->
            triggers.forEach {
                if (it.test(event.player, item)) {
                    it.run(event.player, item)
                    if (it.cancel) event.isCancelled = true
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onPlayerToggleFlight(event: PlayerToggleFlightEvent) {
        val triggerType = if (event.isFlying) TriggerType.FLY else TriggerType.UNFLY
        equippedItems[event.player.uniqueId]?.get(triggerType)?.values?.forEach { (item, triggers) ->
            triggers.forEach {
                if (it.test(event.player, item)) {
                    it.run(event.player, item)
                    if (it.cancel) event.isCancelled = true
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onPlayerToggleGlide(event: EntityToggleGlideEvent) {
        val player = event.entity as? Player ?: return
        val triggerType = if (event.isGliding) TriggerType.GLIDE else TriggerType.UNGLIDE
        equippedItems[player.uniqueId]?.get(triggerType)?.values?.forEach { (item, triggers) ->
            triggers.forEach {
                if (it.test(player, item)) {
                    it.run(player, item)
                    if (it.cancel) event.isCancelled = true
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onPlayerToggleSwim(event: EntityToggleSwimEvent) {
        val player = event.entity as? Player ?: return
        val triggerType = if (event.isSwimming) TriggerType.SWIM else TriggerType.UNSWIM
        equippedItems[player.uniqueId]?.get(triggerType)?.values?.forEach { (item, triggers) ->
            triggers.forEach {
                if (it.test(player, item)) {
                    it.run(player, item)
                    if (it.cancel) event.isCancelled = true
                }
            }
        }
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        // Cleanup
        equippedItems.remove(event.player.uniqueId)
    }

    private fun removeOne(item: ItemStack): ItemStack? {
        if (item.amount > 1) {
            item.amount -= 1
            return item
        } else {
            return null
        }
    }

    private fun removeOne(player: Player, slot: EquipmentSlot) {
        player.inventory.setItem(slot, removeOne(player.inventory.getItem(slot)))
    }
}

fun ItemStack.getCustomItem(): CustomItem? {
    val meta = itemMeta ?: return null
    val data = meta.persistentDataContainer ?: return null
    val type = data.get(NamespacedKey(Blanktopia.INSTANCE, "type"), PersistentDataType.STRING) ?: return null
    val customItem = BlanktopiaItems.INSTANCE.customItems.items[type] ?: return null

    // Custom model data migration
    if (customItem.customModelData != null &&
            (!meta.hasCustomModelData() || meta.customModelData != customItem.customModelData)) {
        meta.setCustomModelData(customItem.customModelData)
        itemMeta = meta
    }

    return customItem
}

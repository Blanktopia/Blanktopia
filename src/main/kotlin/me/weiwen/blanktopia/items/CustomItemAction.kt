package me.weiwen.blanktopia.items

import me.weiwen.blanktopia.Blanktopia
import org.bukkit.*
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector
import org.bukkit.Bukkit
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.event.block.BlockPlaceEvent
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

class CustomItemAction(config: ConfigurationSection) {
    private var message: String? = config.getString("message")
    private var playerCommand: String? = config.getString("player-command")
    private var flyInClaims: Boolean? = if(config.isSet("fly-in-claims")) config.getBoolean("fly-in-claims") else null
    private var portableBeacon: Boolean = config.isSet("portable-beacon")
    private var buildersWandBuild: Boolean = config.getString("builders-wand") == "build"
    private var paintBrushPick: Boolean = config.getString("paint-brush") == "pick"
    private var paintBrushPaint: Boolean = config.getString("paint-brush") == "paint"

    fun run(player: Player, item: ItemStack, block: Block?, face: BlockFace?) {
        message?.let { player.sendMessage(it) }
        playerCommand?.let { player.performCommand(it) }
        flyInClaims?.let {flyInClaims(player, it) }
        if (portableBeacon) portableBeacon(player)
        if (block != null && face != null) {
            if (buildersWandBuild) buildersWandBuild(player, block, face)
            if (paintBrushPick) paintBrushPick(player, item, block)
            if (paintBrushPaint) paintBrushPaint(player, item, block, face)
        }
    }
}

fun flyInClaims(player: Player, canFly: Boolean) {
    if (canFly) {
        player.persistentDataContainer.set(
            NamespacedKey(Blanktopia.INSTANCE, "fly-in-claims"),
            PersistentDataType.BYTE,
            1
        )
    } else {
        player.persistentDataContainer.remove(
            NamespacedKey(Blanktopia.INSTANCE, "fly-in-claims")
        )
        player.allowFlight = false
    }
}

fun portableBeacon(player: Player) {
    val haste = player.getPotionEffect(PotionEffectType.FAST_DIGGING)
    if (haste != null && haste.amplifier == 2 && haste.duration >= 10800) return
    val item = ItemStack(Material.NETHER_STAR, 1)
    if (player.inventory.containsAtLeast(item, 1)) {
        val world = player.location.world ?: return
        player.inventory.removeItem(item)
        player.sendMessage("A nether star disappears into the beacon.")
        for (other in world.getNearbyEntities(player.location, 64.0, 64.0, 64.0, { it is HumanEntity })) {
            if (other !is HumanEntity) continue
            world.playSound(other.location, Sound.BLOCK_BEACON_ACTIVATE, 1.0f, 1.0f)
            world.playSound(other.location, Sound.BLOCK_BEACON_POWER_SELECT, 1.0f, 1.0f)
            world.spawnParticle(Particle.SPELL, other.location, 20)
            other.addPotionEffect(PotionEffect(PotionEffectType.FAST_DIGGING, 12000, 2, true, false))
            other.sendMessage(player.name + " has granted you " + ChatColor.AQUA + "Haste III" + ChatColor.RESET + " for " + ChatColor.AQUA + "10 minutes" + ChatColor.RESET + ".")
        }
    }
}

fun buildersWandLocations(block: Block, face: BlockFace): MutableList<Location> {
    val range = 1
    val (xOffset, yOffset) = if (face.modX != 0) {
        Pair(Vector(0, 1, 0), Vector(0, 0, 1))
    } else if (face.modY != 0) {
        Pair(Vector(1, 0, 0), Vector(0, 0, 1))
    } else {
        Pair(Vector(1, 0, 0), Vector(0, 1, 0))
    }
    val location = block.location
    val material = block.type
    val locations: MutableList<Location> = mutableListOf()
    for (x in -range .. range) {
        for (y in -range .. range) {
            val base = location.clone().add(xOffset.clone().multiply(x)).add(yOffset.clone().multiply(y))
            if (base.block.type != material) continue
            val other = base.add(face.direction)
            if (other.block.type != Material.AIR && other.block.type != Material.WATER && other.block.type != Material.LAVA) continue
            locations.add(other)
        }
    }
    return locations
}

fun buildersWandBuild(player: Player, block: Block, face: BlockFace) {
    val locations = buildersWandLocations(block, face)
    val item = ItemStack(block.type, 1)
   var canBuild = false
    for (location in locations) {
        if (player.gameMode != GameMode.CREATIVE && !player.inventory.containsAtLeast(item, 1)) {
            if (!canBuild) {
                block.world.playSound(block.location, Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 1.0f, 1.0f)
            }
            return
        }
        if (player.location.block.location == location || player.location.add( 0.0, 1.0, 0.0 ).block.location == location) {
            continue
        }
        val state = location.block.state
        state.type = block.type
        state.blockData = block.blockData

        val buildEvent = BlockPlaceEvent(
            location.block,
            state,
            location.block.getRelative(face.oppositeFace),
            item,
            player,
            true,
            EquipmentSlot.HAND
        )
        Bukkit.getPluginManager().callEvent(buildEvent)
        if (buildEvent.isCancelled) {
            continue
        }
        if (player.gameMode != GameMode.CREATIVE) player.inventory.removeItem(item)
        state.update(true)
        canBuild = true
    }
    if (canBuild) {
        block.world.playSound(block.location, Sound.BLOCK_METAL_PLACE, 1.0f, 1.0f)
    } else {
        player.playSound(block.location, Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 1.0f, 1.0f)
    }
}

val BLOCK_COLOUR_MAP: Map<Material, DyeColor> = mapOf(
    Pair(Material.WHITE_WOOL, DyeColor.WHITE),
    Pair(Material.WHITE_STAINED_GLASS, DyeColor.WHITE),
    Pair(Material.WHITE_STAINED_GLASS_PANE, DyeColor.WHITE),
    Pair(Material.WHITE_TERRACOTTA, DyeColor.WHITE),
    Pair(Material.WHITE_GLAZED_TERRACOTTA, DyeColor.WHITE),
    Pair(Material.WHITE_CONCRETE, DyeColor.WHITE),
    Pair(Material.WHITE_CONCRETE_POWDER, DyeColor.WHITE),
    Pair(Material.WHITE_BED, DyeColor.WHITE),

    Pair(Material.ORANGE_WOOL, DyeColor.ORANGE),
    Pair(Material.ORANGE_STAINED_GLASS, DyeColor.ORANGE),
    Pair(Material.ORANGE_STAINED_GLASS_PANE, DyeColor.ORANGE),
    Pair(Material.ORANGE_TERRACOTTA, DyeColor.ORANGE),
    Pair(Material.ORANGE_GLAZED_TERRACOTTA, DyeColor.ORANGE),
    Pair(Material.ORANGE_CONCRETE, DyeColor.ORANGE),
    Pair(Material.ORANGE_CONCRETE_POWDER, DyeColor.ORANGE),
    Pair(Material.ORANGE_BED, DyeColor.ORANGE),

    Pair(Material.MAGENTA_WOOL, DyeColor.MAGENTA),
    Pair(Material.MAGENTA_STAINED_GLASS, DyeColor.MAGENTA),
    Pair(Material.MAGENTA_STAINED_GLASS_PANE, DyeColor.MAGENTA),
    Pair(Material.MAGENTA_TERRACOTTA, DyeColor.MAGENTA),
    Pair(Material.MAGENTA_GLAZED_TERRACOTTA, DyeColor.MAGENTA),
    Pair(Material.MAGENTA_CONCRETE, DyeColor.MAGENTA),
    Pair(Material.MAGENTA_CONCRETE_POWDER, DyeColor.MAGENTA),
    Pair(Material.MAGENTA_BED, DyeColor.MAGENTA),

    Pair(Material.LIGHT_BLUE_WOOL, DyeColor.LIGHT_BLUE),
    Pair(Material.LIGHT_BLUE_STAINED_GLASS, DyeColor.LIGHT_BLUE),
    Pair(Material.LIGHT_BLUE_STAINED_GLASS_PANE, DyeColor.LIGHT_BLUE),
    Pair(Material.LIGHT_BLUE_TERRACOTTA, DyeColor.LIGHT_BLUE),
    Pair(Material.LIGHT_BLUE_GLAZED_TERRACOTTA, DyeColor.LIGHT_BLUE),
    Pair(Material.LIGHT_BLUE_CONCRETE, DyeColor.LIGHT_BLUE),
    Pair(Material.LIGHT_BLUE_CONCRETE_POWDER, DyeColor.LIGHT_BLUE),
    Pair(Material.LIGHT_BLUE_BED, DyeColor.LIGHT_BLUE),

    Pair(Material.YELLOW_WOOL, DyeColor.YELLOW),
    Pair(Material.YELLOW_STAINED_GLASS, DyeColor.YELLOW),
    Pair(Material.YELLOW_STAINED_GLASS_PANE, DyeColor.YELLOW),
    Pair(Material.YELLOW_TERRACOTTA, DyeColor.YELLOW),
    Pair(Material.YELLOW_GLAZED_TERRACOTTA, DyeColor.YELLOW),
    Pair(Material.YELLOW_CONCRETE, DyeColor.YELLOW),
    Pair(Material.YELLOW_CONCRETE_POWDER, DyeColor.YELLOW),
    Pair(Material.YELLOW_BED, DyeColor.YELLOW),

    Pair(Material.LIME_WOOL, DyeColor.LIME),
    Pair(Material.LIME_STAINED_GLASS, DyeColor.LIME),
    Pair(Material.LIME_STAINED_GLASS_PANE, DyeColor.LIME),
    Pair(Material.LIME_TERRACOTTA, DyeColor.LIME),
    Pair(Material.LIME_GLAZED_TERRACOTTA, DyeColor.LIME),
    Pair(Material.LIME_CONCRETE, DyeColor.LIME),
    Pair(Material.LIME_CONCRETE_POWDER, DyeColor.LIME),
    Pair(Material.LIME_BED, DyeColor.LIME),

    Pair(Material.PINK_WOOL, DyeColor.PINK),
    Pair(Material.PINK_STAINED_GLASS, DyeColor.PINK),
    Pair(Material.PINK_STAINED_GLASS_PANE, DyeColor.PINK),
    Pair(Material.PINK_TERRACOTTA, DyeColor.PINK),
    Pair(Material.PINK_GLAZED_TERRACOTTA, DyeColor.PINK),
    Pair(Material.PINK_CONCRETE, DyeColor.PINK),
    Pair(Material.PINK_CONCRETE_POWDER, DyeColor.PINK),
    Pair(Material.PINK_BED, DyeColor.PINK),

    Pair(Material.GRAY_WOOL, DyeColor.GRAY),
    Pair(Material.GRAY_STAINED_GLASS, DyeColor.GRAY),
    Pair(Material.GRAY_STAINED_GLASS_PANE, DyeColor.GRAY),
    Pair(Material.GRAY_TERRACOTTA, DyeColor.GRAY),
    Pair(Material.GRAY_GLAZED_TERRACOTTA, DyeColor.GRAY),
    Pair(Material.GRAY_CONCRETE, DyeColor.GRAY),
    Pair(Material.GRAY_CONCRETE_POWDER, DyeColor.GRAY),
    Pair(Material.GRAY_BED, DyeColor.GRAY),

    Pair(Material.LIGHT_GRAY_WOOL, DyeColor.LIGHT_GRAY),
    Pair(Material.LIGHT_GRAY_STAINED_GLASS, DyeColor.LIGHT_GRAY),
    Pair(Material.LIGHT_GRAY_STAINED_GLASS_PANE, DyeColor.LIGHT_GRAY),
    Pair(Material.LIGHT_GRAY_TERRACOTTA, DyeColor.LIGHT_GRAY),
    Pair(Material.LIGHT_GRAY_GLAZED_TERRACOTTA, DyeColor.LIGHT_GRAY),
    Pair(Material.LIGHT_GRAY_CONCRETE, DyeColor.LIGHT_GRAY),
    Pair(Material.LIGHT_GRAY_CONCRETE_POWDER, DyeColor.LIGHT_GRAY),
    Pair(Material.LIGHT_GRAY_BED, DyeColor.LIGHT_GRAY),

    Pair(Material.CYAN_WOOL, DyeColor.CYAN),
    Pair(Material.CYAN_STAINED_GLASS, DyeColor.CYAN),
    Pair(Material.CYAN_STAINED_GLASS_PANE, DyeColor.CYAN),
    Pair(Material.CYAN_TERRACOTTA, DyeColor.CYAN),
    Pair(Material.CYAN_GLAZED_TERRACOTTA, DyeColor.CYAN),
    Pair(Material.CYAN_CONCRETE, DyeColor.CYAN),
    Pair(Material.CYAN_CONCRETE_POWDER, DyeColor.CYAN),
    Pair(Material.CYAN_BED, DyeColor.CYAN),

    Pair(Material.PURPLE_WOOL, DyeColor.PURPLE),
    Pair(Material.PURPLE_STAINED_GLASS, DyeColor.PURPLE),
    Pair(Material.PURPLE_STAINED_GLASS_PANE, DyeColor.PURPLE),
    Pair(Material.PURPLE_TERRACOTTA, DyeColor.PURPLE),
    Pair(Material.PURPLE_GLAZED_TERRACOTTA, DyeColor.PURPLE),
    Pair(Material.PURPLE_CONCRETE, DyeColor.PURPLE),
    Pair(Material.PURPLE_CONCRETE_POWDER, DyeColor.PURPLE),
    Pair(Material.PURPLE_BED, DyeColor.PURPLE),

    Pair(Material.BLUE_WOOL, DyeColor.BLUE),
    Pair(Material.BLUE_STAINED_GLASS, DyeColor.BLUE),
    Pair(Material.BLUE_STAINED_GLASS_PANE, DyeColor.BLUE),
    Pair(Material.BLUE_TERRACOTTA, DyeColor.BLUE),
    Pair(Material.BLUE_GLAZED_TERRACOTTA, DyeColor.BLUE),
    Pair(Material.BLUE_CONCRETE, DyeColor.BLUE),
    Pair(Material.BLUE_CONCRETE_POWDER, DyeColor.BLUE),
    Pair(Material.BLUE_BED, DyeColor.BLUE),

    Pair(Material.BROWN_WOOL, DyeColor.BROWN),
    Pair(Material.BROWN_STAINED_GLASS, DyeColor.BROWN),
    Pair(Material.BROWN_STAINED_GLASS_PANE, DyeColor.BROWN),
    Pair(Material.BROWN_TERRACOTTA, DyeColor.BROWN),
    Pair(Material.BROWN_GLAZED_TERRACOTTA, DyeColor.BROWN),
    Pair(Material.BROWN_CONCRETE, DyeColor.BROWN),
    Pair(Material.BROWN_CONCRETE_POWDER, DyeColor.BROWN),
    Pair(Material.BROWN_BED, DyeColor.BROWN),

    Pair(Material.GREEN_WOOL, DyeColor.GREEN),
    Pair(Material.GREEN_STAINED_GLASS, DyeColor.GREEN),
    Pair(Material.GREEN_STAINED_GLASS_PANE, DyeColor.GREEN),
    Pair(Material.GREEN_TERRACOTTA, DyeColor.GREEN),
    Pair(Material.GREEN_GLAZED_TERRACOTTA, DyeColor.GREEN),
    Pair(Material.GREEN_CONCRETE, DyeColor.GREEN),
    Pair(Material.GREEN_CONCRETE_POWDER, DyeColor.GREEN),
    Pair(Material.GREEN_BED, DyeColor.GREEN),

    Pair(Material.RED_WOOL, DyeColor.RED),
    Pair(Material.RED_STAINED_GLASS, DyeColor.RED),
    Pair(Material.RED_STAINED_GLASS_PANE, DyeColor.RED),
    Pair(Material.RED_TERRACOTTA, DyeColor.RED),
    Pair(Material.RED_GLAZED_TERRACOTTA, DyeColor.RED),
    Pair(Material.RED_CONCRETE, DyeColor.RED),
    Pair(Material.RED_CONCRETE_POWDER, DyeColor.RED),
    Pair(Material.RED_BED, DyeColor.RED),

    Pair(Material.BLACK_WOOL, DyeColor.BLACK),
    Pair(Material.BLACK_STAINED_GLASS, DyeColor.BLACK),
    Pair(Material.BLACK_STAINED_GLASS_PANE, DyeColor.BLACK),
    Pair(Material.BLACK_TERRACOTTA, DyeColor.BLACK),
    Pair(Material.BLACK_GLAZED_TERRACOTTA, DyeColor.BLACK),
    Pair(Material.BLACK_CONCRETE, DyeColor.BLACK),
    Pair(Material.BLACK_CONCRETE_POWDER, DyeColor.BLACK),
    Pair(Material.BLACK_BED, DyeColor.BLACK)
)
fun paintBrushPick(
    player: Player,
    item: ItemStack,
    block: Block
) {
    val colour = BLOCK_COLOUR_MAP[block.type]
    if (colour == null) {
        player.playSound(block.location, Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 1.0f, 1.0f)
        return
    }
    val paint = when (colour) {
        DyeColor.WHITE -> "WHITE"
        DyeColor.ORANGE -> "ORANGE"
        DyeColor.MAGENTA -> "MAGENTA"
        DyeColor.LIGHT_BLUE -> "LIGHT BLUE"
        DyeColor.YELLOW -> "YELLOW"
        DyeColor.LIME -> "LIME"
        DyeColor.PINK -> "LIME"
        DyeColor.GRAY -> "GRAY"
        DyeColor.LIGHT_GRAY -> "LIGHT GRAY"
        DyeColor.CYAN -> "CYAN"
        DyeColor.PURPLE -> "PURPLE"
        DyeColor.BLUE -> "BLUE"
        DyeColor.BROWN -> "BROWN"
        DyeColor.GREEN -> "GREEN"
        DyeColor.RED -> "RED"
        DyeColor.BLACK -> "BLACK"
    }
    val meta = item.itemMeta ?: return
    val lore = meta.lore ?: return
    lore.set(2, "§7Paint: ${paint}")
    meta.lore = lore
    val data = meta.persistentDataContainer
    data.set(NamespacedKey(Blanktopia.INSTANCE, "paint"), PersistentDataType.STRING, paint)
    item.itemMeta = meta
    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent("Paint: ${paint}"))
}

val WOOL: List<Material> = listOf(Material.WHITE_WOOL, Material.ORANGE_WOOL, Material.MAGENTA_WOOL, Material.LIGHT_BLUE_WOOL, Material.YELLOW_WOOL, Material.LIME_WOOL, Material.PINK_WOOL, Material.GRAY_WOOL, Material.LIGHT_GRAY_WOOL, Material.CYAN_WOOL, Material.PURPLE_WOOL, Material.BLUE_WOOL, Material.BROWN_WOOL, Material.GREEN_WOOL, Material.RED_WOOL, Material.BLACK_WOOL)
val WOOL_MAP: Map<DyeColor, Material> = mapOf(
    Pair(DyeColor.WHITE, Material.WHITE_WOOL),
    Pair(DyeColor.ORANGE, Material.ORANGE_WOOL),
    Pair(DyeColor.MAGENTA, Material.MAGENTA_WOOL),
    Pair(DyeColor.LIGHT_BLUE, Material.LIGHT_BLUE_WOOL),
    Pair(DyeColor.YELLOW, Material.YELLOW_WOOL),
    Pair(DyeColor.LIME, Material.LIME_WOOL),
    Pair(DyeColor.GRAY, Material.GRAY_WOOL),
    Pair(DyeColor.LIGHT_GRAY, Material.LIGHT_GRAY_WOOL),
    Pair(DyeColor.CYAN, Material.CYAN_WOOL),
    Pair(DyeColor.PURPLE, Material.PURPLE_WOOL),
    Pair(DyeColor.BLUE, Material.BLUE_WOOL),
    Pair(DyeColor.BROWN, Material.BROWN_WOOL),
    Pair(DyeColor.GREEN, Material.GREEN_WOOL),
    Pair(DyeColor.RED, Material.RED_WOOL),
    Pair(DyeColor.BLACK, Material.BLACK_WOOL)
)
val STAINED_GLASS: List<Material> = listOf(Material.GLASS, Material.WHITE_STAINED_GLASS, Material.ORANGE_STAINED_GLASS, Material.MAGENTA_STAINED_GLASS, Material.LIGHT_BLUE_STAINED_GLASS, Material.YELLOW_STAINED_GLASS, Material.LIME_STAINED_GLASS, Material.PINK_STAINED_GLASS, Material.GRAY_STAINED_GLASS, Material.LIGHT_GRAY_STAINED_GLASS, Material.CYAN_STAINED_GLASS, Material.PURPLE_STAINED_GLASS, Material.BLUE_STAINED_GLASS, Material.BROWN_STAINED_GLASS, Material.GREEN_STAINED_GLASS, Material.RED_STAINED_GLASS, Material.BLACK_STAINED_GLASS)
val STAINED_GLASS_MAP: Map<DyeColor, Material> = mapOf(
    Pair(DyeColor.WHITE, Material.WHITE_STAINED_GLASS),
    Pair(DyeColor.ORANGE, Material.ORANGE_STAINED_GLASS),
    Pair(DyeColor.MAGENTA, Material.MAGENTA_STAINED_GLASS),
    Pair(DyeColor.LIGHT_BLUE, Material.LIGHT_BLUE_STAINED_GLASS),
    Pair(DyeColor.YELLOW, Material.YELLOW_STAINED_GLASS),
    Pair(DyeColor.LIME, Material.LIME_STAINED_GLASS),
    Pair(DyeColor.GRAY, Material.GRAY_STAINED_GLASS),
    Pair(DyeColor.LIGHT_GRAY, Material.LIGHT_GRAY_STAINED_GLASS),
    Pair(DyeColor.CYAN, Material.CYAN_STAINED_GLASS),
    Pair(DyeColor.PURPLE, Material.PURPLE_STAINED_GLASS),
    Pair(DyeColor.BLUE, Material.BLUE_STAINED_GLASS),
    Pair(DyeColor.BROWN, Material.BROWN_STAINED_GLASS),
    Pair(DyeColor.GREEN, Material.GREEN_STAINED_GLASS),
    Pair(DyeColor.RED, Material.RED_STAINED_GLASS),
    Pair(DyeColor.BLACK, Material.BLACK_STAINED_GLASS)
)
val STAINED_GLASS_PANE: List<Material> = listOf(Material.GLASS_PANE, Material.WHITE_STAINED_GLASS_PANE, Material.ORANGE_STAINED_GLASS_PANE, Material.MAGENTA_STAINED_GLASS_PANE, Material.LIGHT_BLUE_STAINED_GLASS_PANE, Material.YELLOW_STAINED_GLASS_PANE, Material.LIME_STAINED_GLASS_PANE, Material.PINK_STAINED_GLASS_PANE, Material.GRAY_STAINED_GLASS_PANE, Material.LIGHT_GRAY_STAINED_GLASS_PANE, Material.CYAN_STAINED_GLASS_PANE, Material.PURPLE_STAINED_GLASS_PANE, Material.BLUE_STAINED_GLASS_PANE, Material.BROWN_STAINED_GLASS_PANE, Material.GREEN_STAINED_GLASS_PANE, Material.RED_STAINED_GLASS_PANE)
val STAINED_GLASS_PANE_MAP: Map<DyeColor, Material> = mapOf(
    Pair(DyeColor.WHITE, Material.WHITE_STAINED_GLASS_PANE),
    Pair(DyeColor.ORANGE, Material.ORANGE_STAINED_GLASS_PANE),
    Pair(DyeColor.MAGENTA, Material.MAGENTA_STAINED_GLASS_PANE),
    Pair(DyeColor.LIGHT_BLUE, Material.LIGHT_BLUE_STAINED_GLASS_PANE),
    Pair(DyeColor.YELLOW, Material.YELLOW_STAINED_GLASS_PANE),
    Pair(DyeColor.LIME, Material.LIME_STAINED_GLASS_PANE),
    Pair(DyeColor.GRAY, Material.GRAY_STAINED_GLASS_PANE),
    Pair(DyeColor.LIGHT_GRAY, Material.LIGHT_GRAY_STAINED_GLASS_PANE),
    Pair(DyeColor.CYAN, Material.CYAN_STAINED_GLASS_PANE),
    Pair(DyeColor.PURPLE, Material.PURPLE_STAINED_GLASS_PANE),
    Pair(DyeColor.BLUE, Material.BLUE_STAINED_GLASS_PANE),
    Pair(DyeColor.BROWN, Material.BROWN_STAINED_GLASS_PANE),
    Pair(DyeColor.GREEN, Material.GREEN_STAINED_GLASS_PANE),
    Pair(DyeColor.RED, Material.RED_STAINED_GLASS_PANE)
)
val TERRACOTTA: List<Material> = listOf(Material.TERRACOTTA, Material.WHITE_TERRACOTTA, Material.ORANGE_TERRACOTTA, Material.MAGENTA_TERRACOTTA, Material.LIGHT_BLUE_TERRACOTTA, Material.YELLOW_TERRACOTTA, Material.LIME_TERRACOTTA, Material.PINK_TERRACOTTA, Material.GRAY_TERRACOTTA, Material.LIGHT_GRAY_TERRACOTTA, Material.CYAN_TERRACOTTA, Material.PURPLE_TERRACOTTA, Material.BLUE_TERRACOTTA, Material.BROWN_TERRACOTTA, Material.GREEN_TERRACOTTA, Material.RED_TERRACOTTA, Material.BLACK_TERRACOTTA)
val TERRACOTTA_MAP: Map<DyeColor, Material> = mapOf(
    Pair(DyeColor.WHITE, Material.WHITE_TERRACOTTA),
    Pair(DyeColor.ORANGE, Material.ORANGE_TERRACOTTA),
    Pair(DyeColor.MAGENTA, Material.MAGENTA_TERRACOTTA),
    Pair(DyeColor.LIGHT_BLUE, Material.LIGHT_BLUE_TERRACOTTA),
    Pair(DyeColor.YELLOW, Material.YELLOW_TERRACOTTA),
    Pair(DyeColor.LIME, Material.LIME_TERRACOTTA),
    Pair(DyeColor.GRAY, Material.GRAY_TERRACOTTA),
    Pair(DyeColor.LIGHT_GRAY, Material.LIGHT_GRAY_TERRACOTTA),
    Pair(DyeColor.CYAN, Material.CYAN_TERRACOTTA),
    Pair(DyeColor.PURPLE, Material.PURPLE_TERRACOTTA),
    Pair(DyeColor.BLUE, Material.BLUE_TERRACOTTA),
    Pair(DyeColor.BROWN, Material.BROWN_TERRACOTTA),
    Pair(DyeColor.GREEN, Material.GREEN_TERRACOTTA),
    Pair(DyeColor.RED, Material.RED_TERRACOTTA),
    Pair(DyeColor.BLACK, Material.BLACK_TERRACOTTA)
)
val GLAZED_TERRACOTTA: List<Material> = listOf(Material.WHITE_GLAZED_TERRACOTTA, Material.ORANGE_GLAZED_TERRACOTTA, Material.MAGENTA_GLAZED_TERRACOTTA, Material.LIGHT_BLUE_GLAZED_TERRACOTTA, Material.YELLOW_GLAZED_TERRACOTTA, Material.LIME_GLAZED_TERRACOTTA, Material.PINK_GLAZED_TERRACOTTA, Material.GRAY_GLAZED_TERRACOTTA, Material.LIGHT_GRAY_GLAZED_TERRACOTTA, Material.CYAN_GLAZED_TERRACOTTA, Material.PURPLE_GLAZED_TERRACOTTA, Material.BLUE_GLAZED_TERRACOTTA, Material.BROWN_GLAZED_TERRACOTTA, Material.GREEN_GLAZED_TERRACOTTA, Material.RED_GLAZED_TERRACOTTA, Material.BLACK_GLAZED_TERRACOTTA)
val GLAZED_TERRACOTTA_MAP: Map<DyeColor, Material> = mapOf(
    Pair(DyeColor.WHITE, Material.WHITE_GLAZED_TERRACOTTA),
    Pair(DyeColor.ORANGE, Material.ORANGE_GLAZED_TERRACOTTA),
    Pair(DyeColor.MAGENTA, Material.MAGENTA_GLAZED_TERRACOTTA),
    Pair(DyeColor.LIGHT_BLUE, Material.LIGHT_BLUE_GLAZED_TERRACOTTA),
    Pair(DyeColor.YELLOW, Material.YELLOW_GLAZED_TERRACOTTA),
    Pair(DyeColor.LIME, Material.LIME_GLAZED_TERRACOTTA),
    Pair(DyeColor.GRAY, Material.GRAY_GLAZED_TERRACOTTA),
    Pair(DyeColor.LIGHT_GRAY, Material.LIGHT_GRAY_GLAZED_TERRACOTTA),
    Pair(DyeColor.CYAN, Material.CYAN_GLAZED_TERRACOTTA),
    Pair(DyeColor.PURPLE, Material.PURPLE_GLAZED_TERRACOTTA),
    Pair(DyeColor.BLUE, Material.BLUE_GLAZED_TERRACOTTA),
    Pair(DyeColor.BROWN, Material.BROWN_GLAZED_TERRACOTTA),
    Pair(DyeColor.GREEN, Material.GREEN_GLAZED_TERRACOTTA),
    Pair(DyeColor.RED, Material.RED_GLAZED_TERRACOTTA)
)
val CONCRETE: List<Material> = listOf(Material.WHITE_CONCRETE, Material.ORANGE_CONCRETE, Material.MAGENTA_CONCRETE, Material.LIGHT_BLUE_CONCRETE, Material.YELLOW_CONCRETE, Material.LIME_CONCRETE, Material.PINK_CONCRETE, Material.GRAY_CONCRETE, Material.LIGHT_GRAY_CONCRETE, Material.CYAN_CONCRETE, Material.PURPLE_CONCRETE, Material.BLUE_CONCRETE, Material.BROWN_CONCRETE, Material.GREEN_CONCRETE, Material.RED_CONCRETE)
val CONCRETE_MAP: Map<DyeColor, Material> = mapOf(
    Pair(DyeColor.WHITE, Material.WHITE_CONCRETE),
    Pair(DyeColor.ORANGE, Material.ORANGE_CONCRETE),
    Pair(DyeColor.MAGENTA, Material.MAGENTA_CONCRETE),
    Pair(DyeColor.LIGHT_BLUE, Material.LIGHT_BLUE_CONCRETE),
    Pair(DyeColor.YELLOW, Material.YELLOW_CONCRETE),
    Pair(DyeColor.LIME, Material.LIME_CONCRETE),
    Pair(DyeColor.GRAY, Material.GRAY_CONCRETE),
    Pair(DyeColor.LIGHT_GRAY, Material.LIGHT_GRAY_CONCRETE),
    Pair(DyeColor.CYAN, Material.CYAN_CONCRETE),
    Pair(DyeColor.PURPLE, Material.PURPLE_CONCRETE),
    Pair(DyeColor.BLUE, Material.BLUE_CONCRETE),
    Pair(DyeColor.BROWN, Material.BROWN_CONCRETE),
    Pair(DyeColor.GREEN, Material.GREEN_CONCRETE),
    Pair(DyeColor.RED, Material.RED_CONCRETE),
    Pair(DyeColor.BLACK, Material.BLACK_CONCRETE)
)
val CONCRETE_POWDER: List<Material> = listOf(Material.WHITE_CONCRETE_POWDER, Material.ORANGE_CONCRETE_POWDER, Material.MAGENTA_CONCRETE_POWDER, Material.LIGHT_BLUE_CONCRETE_POWDER, Material.YELLOW_CONCRETE_POWDER, Material.LIME_CONCRETE_POWDER, Material.PINK_CONCRETE_POWDER, Material.GRAY_CONCRETE_POWDER, Material.LIGHT_GRAY_CONCRETE_POWDER, Material.CYAN_CONCRETE_POWDER, Material.PURPLE_CONCRETE_POWDER, Material.BLUE_CONCRETE_POWDER, Material.BROWN_CONCRETE_POWDER, Material.GREEN_CONCRETE_POWDER, Material.RED_CONCRETE_POWDER, Material.BLACK_CONCRETE_POWDER)
val CONCRETE_POWDER_MAP: Map<DyeColor, Material> = mapOf(
    Pair(DyeColor.WHITE, Material.WHITE_CONCRETE_POWDER),
    Pair(DyeColor.ORANGE, Material.ORANGE_CONCRETE_POWDER),
    Pair(DyeColor.MAGENTA, Material.MAGENTA_CONCRETE_POWDER),
    Pair(DyeColor.LIGHT_BLUE, Material.LIGHT_BLUE_CONCRETE_POWDER),
    Pair(DyeColor.YELLOW, Material.YELLOW_CONCRETE_POWDER),
    Pair(DyeColor.LIME, Material.LIME_CONCRETE_POWDER),
    Pair(DyeColor.GRAY, Material.GRAY_CONCRETE_POWDER),
    Pair(DyeColor.LIGHT_GRAY, Material.LIGHT_GRAY_CONCRETE_POWDER),
    Pair(DyeColor.CYAN, Material.CYAN_CONCRETE_POWDER),
    Pair(DyeColor.PURPLE, Material.PURPLE_CONCRETE_POWDER),
    Pair(DyeColor.BLUE, Material.BLUE_CONCRETE_POWDER),
    Pair(DyeColor.BROWN, Material.BROWN_CONCRETE_POWDER),
    Pair(DyeColor.GREEN, Material.GREEN_CONCRETE_POWDER),
    Pair(DyeColor.RED, Material.RED_CONCRETE_POWDER),
    Pair(DyeColor.BLACK, Material.BLACK_CONCRETE_POWDER)
)
val BED: List<Material> = listOf(Material.WHITE_BED, Material.ORANGE_BED, Material.MAGENTA_BED, Material.LIGHT_BLUE_BED, Material.YELLOW_BED, Material.LIME_BED, Material.PINK_BED, Material.GRAY_BED, Material.LIGHT_GRAY_BED, Material.CYAN_BED, Material.PURPLE_BED, Material.BLUE_BED, Material.BROWN_BED, Material.GREEN_BED, Material.RED_BED, Material.BLACK_BED)
val BED_MAP: Map<DyeColor, Material> = mapOf(
    Pair(DyeColor.WHITE, Material.WHITE_BED),
    Pair(DyeColor.ORANGE, Material.ORANGE_BED),
    Pair(DyeColor.MAGENTA, Material.MAGENTA_BED),
    Pair(DyeColor.LIGHT_BLUE, Material.LIGHT_BLUE_BED),
    Pair(DyeColor.YELLOW, Material.YELLOW_BED),
    Pair(DyeColor.LIME, Material.LIME_BED),
    Pair(DyeColor.GRAY, Material.GRAY_BED),
    Pair(DyeColor.LIGHT_GRAY, Material.LIGHT_GRAY_BED),
    Pair(DyeColor.CYAN, Material.CYAN_BED),
    Pair(DyeColor.PURPLE, Material.PURPLE_BED),
    Pair(DyeColor.BLUE, Material.BLUE_BED),
    Pair(DyeColor.BROWN, Material.BROWN_BED),
    Pair(DyeColor.GREEN, Material.GREEN_BED),
    Pair(DyeColor.RED, Material.RED_BED),
    Pair(DyeColor.BLACK, Material.BLACK_BED)
)

fun paintBrushPaint(player: Player, item: ItemStack, block: Block, face: BlockFace) {
    val data = item.itemMeta?.persistentDataContainer ?: return
    val paint = data.get(NamespacedKey(Blanktopia.INSTANCE, "paint"), PersistentDataType.STRING) ?: return
    val colour = when (paint) {
        "WHITE" -> DyeColor.WHITE
        "ORANGE" -> DyeColor.ORANGE
        "MAGENTA" -> DyeColor.MAGENTA
        "LIGHT BLUE" -> DyeColor.LIGHT_BLUE
        "YELLOW" -> DyeColor.YELLOW
        "LIME" -> DyeColor.LIME
        "PINK" -> DyeColor.PINK
        "GRAY" -> DyeColor.GRAY
        "LIGHT GRAY" -> DyeColor.LIGHT_GRAY
        "CYAN" -> DyeColor.CYAN
        "PURPLE" -> DyeColor.PURPLE
        "BLUE" -> DyeColor.BLUE
        "BROWN" -> DyeColor.BROWN
        "GREEN" -> DyeColor.GREEN
        "RED" -> DyeColor.RED
        "BLACK" -> DyeColor.BLACK
        else -> {
            player.playSound(block.location, Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 1.0f, 1.0f)
            return
        }
    }

    if (block.type in WOOL) {
        block.type = WOOL_MAP[colour]!!
    } else if (block.type in STAINED_GLASS) {
        block.type = STAINED_GLASS_MAP[colour]!!
    } else if (block.type in STAINED_GLASS_PANE) {
        block.type = STAINED_GLASS_PANE_MAP[colour]!!
    } else if (block.type in TERRACOTTA) {
        block.type = TERRACOTTA_MAP[colour]!!
    } else if (block.type in GLAZED_TERRACOTTA) {
        block.type = GLAZED_TERRACOTTA_MAP[colour]!!
    } else if (block.type in CONCRETE) {
        block.type = CONCRETE_MAP[colour]!!
    } else if (block.type in CONCRETE_POWDER) {
        block.type = CONCRETE_POWDER_MAP[colour]!!
    } else if (block.type in BED) {
        block.type = BED_MAP[colour]!!
    }
}
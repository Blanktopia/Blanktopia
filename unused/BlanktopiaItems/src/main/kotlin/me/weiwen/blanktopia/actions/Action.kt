package me.weiwen.blanktopia.actions

import me.libraryaddict.disguise.disguisetypes.*
import me.weiwen.blanktopia.*
import me.weiwen.blanktopia.conditions.parseCondition
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.block.Biome
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffectType
import java.util.logging.Level

interface Action {
    fun run(player: Player) {}
    fun run(player: Player, item: ItemStack) {
        run(player)
    }
    fun run(player: Player, item: ItemStack, block: Block) {
        run(player, item)
    }
    fun run(player: Player, item: ItemStack, block: Block, face: BlockFace) {
        run(player, item, block)
    }
    fun run(player: Player, item: ItemStack, entity: Entity) {
        run(player, item)
    }
}

fun parseActions(nodes: List<Node>): List<Action> {
    return nodes.mapNotNull { parseAction(it) }
}

fun parseAction(node: Node): Action? {
    return when (val action = node.tryGet<String>("action")) {
        "action-bar" -> node.tryGet<String>("message")?.let { ActionBarAction(it) }
        "add-permanent-potion-effects" -> {
            val key = node.tryGet<String>("key") ?: return null
            val effects = node.tryGet<Node>("effects")?.let {
                it.mapNotNull { (key, _) ->
                    val type = PotionEffectType.getByName(key)
                    if (type == null) {
                        BlanktopiaCore.INSTANCE.logger.log(Level.SEVERE, "Invalid potion effect type ${key}")
                        null
                    } else {
                        type to it.tryGet<Int>(key, 0)
                    }
                }
            }?.toMap() ?: return null
            AddPermanentPotionEffectAction(key, effects)
        }
        "add-potion-effect" -> {
            val type = node.tryGet<String>("type")?.let { PotionEffectType.getByName(it) }
            if (type == null) {
                BlanktopiaCore.INSTANCE.logger.log(Level.SEVERE, "Invalid potion effect type")
                return null
            }
            val ticks = node.tryGet<Int>("ticks") ?: return null
            val level = node.tryGet<Int>("level") ?: return null
            AddPotionEffectAction(type, ticks, level)
        }
        "add-velocity" -> { 
            val x = node.tryGet<Double>("x", 0.0)
            val y = node.tryGet<Double>("y", 0.0)
            val z = node.tryGet<Double>("z", 0.0)
            AddVelocityAction(x, y, z)
        }
        "all-players" -> {
            val actions = node.tryGet<List<Node>>("actions")?.let { parseActions(it) } ?: return null
            AllPlayersAction(actions)
        }
        "builders-wand" -> node.tryGet<Int>("range")?.let { BuildersWandAction(it) }
        "biome-wand" -> {
            val biome = node.tryGet<String>("biome")?.let { Biome.valueOf(it) } ?: return null
            val range = node.tryGet<Int>("range") ?: return null
            BiomeWandAction(biome, range)
        }
        "delay" -> {
            val ticks = node.tryGet<Int>("ticks") ?: return null
            val actions = node.tryGet<List<Node>>("actions")?.let { parseActions(it) } ?: return null
            DelayAction(ticks, actions)
        }
        "console-command" -> node.tryGet<String>("command")?.let { ConsoleCommandAction(it) }
        "if" -> {
            val condition = node.tryGet<Node>("condition")?.let { parseCondition(it) } ?: return null
            val ifTrue = node.tryGet<List<Node>>("if-true")?.let { parseActions(it) } ?: return null
            val ifFalse = node.tryGet<List<Node>>("if-false")?.let { parseActions(it) } ?: return null
            IfAction(condition, ifTrue, ifFalse)
        }
        "cycle-tool" -> node.tryGet<List<String>>("materials")?.map { Material.valueOf(it) }?.let { CycleToolAction(it) }
        "disguise" -> parseDisguise(node)?.let { DisguiseAction(it)}
        "experience-boost" -> {
            val multiplier = node.tryGet<Double>("multiplier", 1.0)
            val ticks = node.tryGet<Int>("ticks", 0)
            ExperienceBoostAction(multiplier, ticks)
        }
        "equip-item" -> {
            val slot = EquipmentSlot.valueOf(node.tryGet<String>("slot") ?: return null)
            EquipItemAction(slot)
        }
        "feed" -> {
            val amount = node.tryGet<Int>("amount") ?: 0
            val saturation = node.tryGet<Double>("saturation")?.toFloat() ?: 0.0f
            FeedAction(amount, saturation)
        }
        "fly-in-claims" -> node.tryGet<Boolean>("can-fly")?.let { FlyInClaimsAction(it) }
        "hammer" -> {
            val range = node.tryGet<Int>("range", 0)
            val depth = node.tryGet<Int>("depth", 0)
            HammerAction(range, depth)
        }
        "hammer-strip" -> node.tryGet<Int>("range")?.let { HammerStripAction(it) }
        "heal" -> node.tryGet<Int>("amount")?.let { HealAction(it) }
        "item-cooldown" -> node.tryGet<Int>("ticks")?.let { ItemCooldownAction(it) }
        "launch-entity" -> {
            val type = node.tryGet<String>("type") ?: "ARROW"
            val magnitude = node.tryGet<Double>("magnitude", 1.5)
            val pitch = node.tryGet<Double>("pitch", 0.0)
            val disguise = node.tryGet<Node?>("disguise", null)?.let { parseDisguise(it) }
            val isPitchRelative = node.tryGet<Boolean>("is-pitch-relative", true)
            LaunchEntityAction(type, magnitude, pitch, disguise, isPitchRelative)
        }
        "launch-falling-block" -> {
            val material = node.tryGet<String?>("material")?.let { Material.valueOf(it) } ?: Material.DIRT
            val canDropItem = node.tryGet<Boolean?>("can-drop-item") ?: true
            val canHurtEntities = node.tryGet<Boolean?>("can-hurt-entities") ?: true
            val magnitude = node.tryGet<Double>("magnitude", 1.5)
            val pitch = node.tryGet<Double>("pitch", 0.0)
            val disguise = node.tryGet<Node?>("disguise", null)?.let { parseDisguise(it) }
            val isPitchRelative = node.tryGet<Boolean>("is-pitch-relative", true)
            LaunchFallingBlockAction(material, canDropItem, canHurtEntities, magnitude, pitch, disguise, isPitchRelative)
        }
        "lava-bucket" -> LavaBucketAction()
        "message" -> node.tryGet<String>("message")?.let { MessageAction(it) }
        "measure-distance" -> MeasureDistanceAction(node.tryGet<Boolean>("is-origin", false))
        "paint-brush-pick" -> PaintBrushPickAction()
        "paint-brush-paint" -> PaintBrushPaintAction()
        "play-sound" -> {
            val sound = node.tryGet<String>("sound") ?: return null
            val pitch = node.tryGet("pitch", 1.0).toFloat()
            val volume = node.tryGet("volume", 1.0).toFloat()
            PlaySoundAction(sound, pitch, volume)
        }
        "player-command" -> node.tryGet<String>("command")?.let { PlayerCommandAction(it) }
        "place-block" -> node.tryGet<String>("material")?.let {
            val material = Material.matchMaterial(it)
            if (material == null) {
                BlanktopiaItems.INSTANCE.logger.log(
                    Level.WARNING,
                    "Unrecognized material '$it' when parsing action 'place-block'"
                )
                null
            } else {
                PlaceBlockAction(material)
            }
        }
        "place-random-block" -> PlaceRandomBlockAction()
        "remove-permanent-potion-effects" -> node.tryGet<String>("key")?.let { RemovePermanentPotionEffectAction(it) }
        "repeat" -> {
            val delay = node.tryGet<Int>("delay", 0)
            val interval = node.tryGet<Int>("interval") ?: 0
            val count = node.tryGet<Int>("count") ?: 0
            val actions = node.tryGet<List<Node>>("actions")?.let { parseActions(it) } ?: return null
            RepeatAction(delay, interval, count, actions)
        }
        "rotate" -> RotateAction(node.tryGet<Boolean>("is-reversed", false))
        "set-velocity" -> {
            val x = node.tryGet<Double?>("x", null)
            val y = node.tryGet<Double?>("y", null)
            val z = node.tryGet<Double?>("z", null)
            SetVelocityAction(x, y, z)
        }
        "spawn-particle" -> {
            val particle = node.tryGet<String>("particle") ?: return null
            val x = node.tryGet("x", 0.0)
            val y = node.tryGet("y", 0.0)
            val z = node.tryGet("z", 0.0)
            val count = node.tryGet("count", 1)
            val offsetX = node.tryGet("offset-x", 0.0)
            val offsetY = node.tryGet("offset-y", 0.0)
            val offsetZ = node.tryGet("offset-z", 0.0)
            val extra = node.tryGet("extra", 0.0)
            SpawnParticleAction(particle, x, y, z, count, offsetX, offsetY, offsetZ, extra)
        }
        "sudo-command" -> {
            val command = node.tryGet<String>("command") ?: return null
            val permissions = node.tryGet<List<String>>("permissions") ?: ArrayList()
            SudoCommandAction(command, permissions)
        }
        "toggle-item-frame-visibility" -> ToggleItemFrameVisibilityAction()
        "toggle-enchantment" -> {
            val enchantment = node.tryGet<String>("enchantment")?.let {
                Enchantment.getByKey(NamespacedKey.minecraft(it)) ?: Enchantment.getByKey(
                    NamespacedKey(
                        Blanktopia.INSTANCE,
                        it
                    )
                )
            }
            val level = node.tryGet<Int>("level") ?: 1
            val name = node.tryGet<String>("name") ?: "enchantment"
            if (enchantment != null) {
                ToggleEnchantmentAction(enchantment, level, name)
            } else {
                BlanktopiaItems.INSTANCE.logger.log(Level.WARNING, "Unrecognized enchantment when parsing custom item")
                null
            }
        }
        "undisguise" -> UndisguiseAction()
        "water-bucket" -> WaterBucketAction()
        else -> {
            BlanktopiaItems.INSTANCE.logger.log(Level.WARNING, "Unrecognized action '$action' when parsing custom item")
            null
        }
    }
}

fun parseDisguise(node: Node): Disguise? {
    if (!BlanktopiaItems.INSTANCE.server.pluginManager.isPluginEnabled("LibsDisguises")) {
        return null
    }
    return when (node.tryGet<String>("kind")) {
        "mob" -> {
            val type = node.tryGet<String>("type") ?: "ZOMBIE"
            val isBaby = node.tryGet("baby", false)
            MobDisguise(DisguiseType.valueOf(type), !isBaby)
        }
        "player" -> node.tryGet<String>("name")?.let { PlayerDisguise(it) }
        "item" -> {
            val material = node.tryGet<String>("material")?.let { Material.valueOf(it) } ?: Material.STICK
            val amount = node.tryGet("amount", 1)
            MiscDisguise(DisguiseType.DROPPED_ITEM, ItemStack(material, amount))
        }
        "block" -> {
            val material = node.tryGet<String>("material")?.let { Material.valueOf(it) } ?: Material.DIRT
            val data = node.tryGet("data", 0)
            MiscDisguise(DisguiseType.FALLING_BLOCK, material, data)
        }
        else -> return null
    }
}
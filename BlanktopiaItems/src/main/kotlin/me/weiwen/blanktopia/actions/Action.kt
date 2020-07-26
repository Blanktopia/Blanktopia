package me.weiwen.blanktopia.actions

import me.libraryaddict.disguise.disguisetypes.DisguiseType
import me.libraryaddict.disguise.disguisetypes.MobDisguise
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise
import me.weiwen.blanktopia.*
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
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
        "add-potion-effects" -> {
            val key = node.tryGet<String>("key") ?: return null
            val effects = node.tryGet<Node>("effects")?.let {
                it.mapNotNull { (key, value) ->
                    val type = PotionEffectType.getByName(key)
                    if (type == null) {
                        BlanktopiaCore.INSTANCE.logger.log(Level.SEVERE, "Invalid potion effect type ${key}")
                        null
                    } else {
                        type to it.tryGet<Int>(key, 0)
                    }
                }
            }?.toMap() ?: return null
            AddPotionEffectAction(key, effects)
        }
        "builders-wand" -> node.tryGet<Int>("range")?.let { BuildersWandAction(it) }
        "console-command" -> node.tryGet<String>("command")?.let { ConsoleCommandAction(it) }
        "cycle-tool" -> node.tryGet<List<String>>("materials")?.map { Material.valueOf(it as String) }?.let { CycleToolAction(it) }
        "delay" -> {
            val ticks = node.tryGet<Long>("ticks") ?: return null
            val actions = node.tryGet<List<Node>>("actions")?.let { parseActions(it) } ?: return null
            DelayAction(ticks, actions)
        }
        "disguise" -> {
            when (node.tryGet<String>("kind")) {
                "mob" -> {
                    val type = node.tryGet<String>("type") ?: "ZOMBIE"
                    val isBaby = node.tryGet<Boolean>("baby", false)
                    DisguiseAction(MobDisguise(DisguiseType.valueOf(type), !isBaby))
                }
                "player" -> node.tryGet<String>("name")?.let { DisguiseAction(PlayerDisguise(it)) }
                else -> null
            }
        }
        "experience-boost" -> {
            val multiplier = node.tryGet<Double>("multiplier", 1.0)
            val ticks = node.tryGet<Long>("ticks", 0)
            ExperienceBoostAction(multiplier, ticks)
        }
        "fly-in-claims" -> node.tryGet<Boolean>("can-fly")?.let { FlyInClaimsAction(it) }
        "hammer" -> node.tryGet<Int>("range")?.let { HammerAction(it) }
        "item-cooldown" -> node.tryGet<Int>("ticks")?.let { ItemCooldownAction(it) }
        "message-action" -> node.tryGet<String>("message")?.let { MessageAction(it) }
        "paint-brush-pick" -> PaintBrushPickAction()
        "paint-brush-paint" -> PaintBrushPaintAction()
        "player-command" -> node.tryGet<String>("command")?.let { PlayerCommandAction(it) }
        "place-block" -> node.tryGet<String>("material")?.let { 
            val material = Material.matchMaterial(it)
            if (material == null) {
                BlanktopiaItems.INSTANCE.logger.log(Level.WARNING, "Unrecognized material '$it' when parsing action 'place-block'")
                null
            } else {
                PlaceBlockAction(material)
            }
        }
        "place-random-block" -> PlaceRandomBlockAction()
        "remove-potion-effects" -> node.tryGet<String>("key")?.let { RemovePotionEffectAction(it) }
        "rotate" -> RotateAction(node.tryGet<Boolean>("is-reversed", false))
        "sudo-command" -> node.tryGet<String>("command")?.let { SudoCommandAction(it) }
        "throw-ender-pearl" -> ThrowEnderPearlAction()
        "toggle-item-frame-visibility" -> ToggleItemFrameVisibilityAction()
        "undisguise" -> UndisguiseAction()
        "water-bucket" -> WaterBucketAction()
            else -> {
            BlanktopiaItems.INSTANCE.logger.log(Level.WARNING, "Unrecognized action '$action' when parsing custom item")
            null
        }
    }
}


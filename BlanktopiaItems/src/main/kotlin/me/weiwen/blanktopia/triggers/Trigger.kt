package me.weiwen.blanktopia.triggers

import me.weiwen.blanktopia.BlanktopiaItems
import me.weiwen.blanktopia.Node
import me.weiwen.blanktopia.actions.Action
import me.weiwen.blanktopia.actions.parseActions
import me.weiwen.blanktopia.conditions.Condition
import me.weiwen.blanktopia.conditions.parseConditions
import me.weiwen.blanktopia.tryGet
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.logging.Level

class Trigger(
    val triggerTypes: Set<TriggerType>,
    private val actions: List<Action>,
    private val conditions: List<Condition>) {

    fun test(player: Player, item: ItemStack): Boolean {
        return conditions.all { it.test(player, item) }
    }

    fun run(player: Player, item: ItemStack) {
        actions.forEach { it.run(player, item) }
    }

    fun run(player: Player, item: ItemStack, block: Block) {
        actions.forEach { it.run(player, item, block) }
    }

    fun run(player: Player, item: ItemStack, block: Block, face: BlockFace) {
        actions.forEach { it.run(player, item, block, face) }
    }

    fun run(player: Player, item: ItemStack, entity: Entity) {
        actions.forEach { it.run(player, item, entity) }
    }
}

fun parseTriggers(nodes: List<Node>): List<Trigger> {
    val triggers = mutableListOf<Trigger>()
    for (node in nodes) {
        val triggerTypes = node.tryGet<String>("trigger")?.let { parseTriggerTypes(it) } ?: continue
        val actions = node.tryGet<List<Node>>("actions")?.let { parseActions(it) } ?: continue
        val conditions = node.tryGet<List<Node>>("conditions", listOf()).let { parseConditions(it) }
        triggers.add(Trigger(triggerTypes, actions, conditions))
    }
    return triggers
}

fun parseTriggerTypes(type: String): Set<TriggerType> {
    return when (type) {
        "right-click" -> setOf(TriggerType.RIGHT_CLICK_AIR, TriggerType.RIGHT_CLICK_BLOCK, TriggerType.RIGHT_CLICK_ENTITY)
        "right-click-air" -> setOf(TriggerType.RIGHT_CLICK_AIR)
        "right-click-block" -> setOf(TriggerType.RIGHT_CLICK_BLOCK)
        "right-click-entity" -> setOf(TriggerType.RIGHT_CLICK_ENTITY)
        
        "left-click" -> setOf(TriggerType.LEFT_CLICK_AIR, TriggerType.LEFT_CLICK_BLOCK)
        "left-click-air" -> setOf(TriggerType.LEFT_CLICK_AIR)
        "left-click-block" -> setOf(TriggerType.LEFT_CLICK_BLOCK)

        "middle-click" -> setOf(TriggerType.MIDDLE_CLICK_AIR, TriggerType.MIDDLE_CLICK_BLOCK)
        "middle-click-air" -> setOf(TriggerType.MIDDLE_CLICK_AIR)
        "middle-click-block" -> setOf(TriggerType.MIDDLE_CLICK_BLOCK)

        "damage-entity" -> setOf(TriggerType.DAMAGE_ENTITY)

        "equip-armor" -> setOf(TriggerType.EQUIP_ARMOR)
        "unequip-armor" -> setOf(TriggerType.UNEQUIP_ARMOR)

        "consume" -> setOf(TriggerType.CONSUME)

        "break-block" -> setOf(TriggerType.BREAK_BLOCK)
        "place-block" -> setOf(TriggerType.PLACE_BLOCK)

        "drop" -> setOf(TriggerType.DROP)

        "click-inventory" -> setOf(
                TriggerType.RIGHT_CLICK_INVENTORY,
                TriggerType.LEFT_CLICK_INVENTORY,
                TriggerType.MIDDLE_CLICK_INVENTORY,
                TriggerType.SHIFT_RIGHT_CLICK_INVENTORY,
                TriggerType.SHIFT_LEFT_CLICK_INVENTORY)
        "right-click-inventory" -> setOf(TriggerType.RIGHT_CLICK_INVENTORY)
        "left-click-inventory" -> setOf(TriggerType.LEFT_CLICK_INVENTORY)
        "middle-click-inventory" -> setOf(TriggerType.MIDDLE_CLICK_INVENTORY)
        "shift-click-inventory" -> setOf(TriggerType.SHIFT_LEFT_CLICK_INVENTORY, TriggerType.SHIFT_RIGHT_CLICK_INVENTORY)
        "shift-right-click-inventory" -> setOf(TriggerType.SHIFT_RIGHT_CLICK_INVENTORY)
        "shift-left-click-inventory" -> setOf(TriggerType.SHIFT_LEFT_CLICK_INVENTORY)
        "double-click-inventory" -> setOf(TriggerType.DOUBLE_CLICK_INVENTORY)
        "drop-inventory" -> setOf(TriggerType.DROP_INVENTORY)
        "control-drop-inventory" -> setOf(TriggerType.CONTROL_DROP_INVENTORY)
        "left-border-inventory" -> setOf(TriggerType.LEFT_BORDER_INVENTORY)
        "right-border-inventory" -> setOf(TriggerType.RIGHT_BORDER_INVENTORY)
        "number-key-1-inventory" -> setOf(TriggerType.NUMBER_KEY_1_INVENTORY)
        "number-key-2-inventory" -> setOf(TriggerType.NUMBER_KEY_2_INVENTORY)
        "number-key-3-inventory" -> setOf(TriggerType.NUMBER_KEY_3_INVENTORY)
        "number-key-4-inventory" -> setOf(TriggerType.NUMBER_KEY_4_INVENTORY)
        "number-key-5-inventory" -> setOf(TriggerType.NUMBER_KEY_5_INVENTORY)
        "number-key-6-inventory" -> setOf(TriggerType.NUMBER_KEY_6_INVENTORY)
        "number-key-7-inventory" -> setOf(TriggerType.NUMBER_KEY_7_INVENTORY)
        "number-key-8-inventory" -> setOf(TriggerType.NUMBER_KEY_8_INVENTORY)
        "number-key-9-inventory" -> setOf(TriggerType.NUMBER_KEY_9_INVENTORY)

        // These triggers will register when armor is equiped and unregistered when removed
        "move" -> setOf(TriggerType.MOVE)
        "jump" -> setOf(TriggerType.JUMP)
        "toggle-sneak" -> setOf(TriggerType.TOGGLE_SNEAK)
        "toggle-sprint" -> setOf(TriggerType.TOGGLE_SPRINT)
        "toggle-flight" -> setOf(TriggerType.TOGGLE_FLIGHT)

        
        else -> {
            BlanktopiaItems.INSTANCE.logger.log(Level.WARNING, "Unrecognized trigger '$type' when parsing custom item")
            setOf()
        }
    }
}


package me.weiwen.blanktopia.items

import me.weiwen.blanktopia.*
import me.weiwen.blanktopia.recipes.*
import org.bukkit.Material
import org.bukkit.inventory.CraftingInventory
import org.bukkit.inventory.ItemStack
import java.util.logging.Level

interface Recipe {
    val result: HybridItemStack
    fun validate(inventory: CraftingInventory): Boolean
}

class ShapedRecipe(pattern: String, keys: Map<Char, HybridItem>, override val result: HybridItemStack) : Recipe {
    val matrix = pattern.replace("\n", "").map {
        if (it == ' ') {
            null
        } else if (keys[it] == null) {
            BlanktopiaItems.INSTANCE.logger.log(Level.WARNING, "Invalid recipe '$pattern'. No ingredient named '$it'")
            Invalid
        } else {
            keys[it]!!
        }
    }

    val flipped = listOf(matrix[2], matrix[1], matrix[0], matrix[5], matrix[4], matrix[3], matrix[8], matrix[7], matrix[6])

    override fun validate(inventory: CraftingInventory): Boolean {
        val ingredients = inventory.matrix.map { it?.toHybridItem() }
        return matrix == ingredients || flipped == ingredients
    }
}

fun parseItem(node: Node): HybridItem? {
    return when (node.tryGet<String?>("kind", null)) {
        "custom" -> node.tryGet<String>("type")?.let { Custom(it) }
        else -> node.tryGet<String>("material")?.let { Vanilla(Material.valueOf(it)) }
    } ?: return null
}

fun parseRecipe(node: Node): Recipe? {
    val pattern = node.tryGet<String>("pattern") ?: return null
    val keys = node.tryGet<Map<String, Node>>("keys")?.mapValues { (_, v) ->
        parseItem(v) ?: return null
    }?.mapKeys { (k, _) ->
        k.first()
    } ?: return null
    val result = node.tryGet<Node>("result")?.let {
        val item = parseItem(it) ?: return null
        val count = it.tryGet<Int>("count", 1)
        HybridItemStack(item, count)
    } ?: return null
    return ShapedRecipe(pattern, keys, result)
}
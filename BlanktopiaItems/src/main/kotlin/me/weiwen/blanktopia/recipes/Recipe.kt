package me.weiwen.blanktopia.items

import me.weiwen.blanktopia.*
import me.weiwen.blanktopia.recipes.*
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.block.Campfire
import org.bukkit.inventory.*
import java.util.logging.Level

fun parseItem(node: Node): HybridItem? {
    return when (node.tryGet<String?>("kind", null)) {
        "custom" -> node.tryGet<String>("type")?.let { Custom(it) }
        else -> node.tryGet<String>("material")?.let { Vanilla(Material.valueOf(it)) }
    } ?: return null
}

fun parseItemStack(node: Node): ItemStack? {
    val item = parseItem(node) ?: return null
    val count = node.tryGet<Int>("count", 1)
    return HybridItemStack(item, count).build()
}

fun parseRecipe(key: NamespacedKey, node: Node): Recipe? {
    val result = node.tryGet<Node>("result")?.let {
        parseItemStack(it)
    } ?: return null

    return when (node.tryGet<String>("type")) {
        "shaped" -> {
            val recipe = ShapedRecipe(key, result)
            node.tryGet<List<String>>("shape")?.let {
                recipe.shape(*it.toTypedArray())
            }
            node.tryGet<Map<String, Node>>("ingredients")?.mapValues { (_, v) ->
                parseItemStack(v) ?: return null
            }?.mapKeys { (k, item) ->
                recipe.setIngredient(k.first(), item)
            } ?: return null
            recipe
        }
        "shapeless" -> {
            val recipe = ShapelessRecipe(key, result)
            node.tryGet<List<Node>>("ingredients")?.map {
                parseItemStack(it) ?: return null
            }?.forEach {
                recipe.addIngredient(RecipeChoice.ExactChoice(it))
            }
            recipe
        }
        "furnace" -> {
            val input = node.tryGet<Node>("input")?.let { parseItemStack(it) }?.let { RecipeChoice.ExactChoice(it) }
                    ?: return null
            val experience = node.tryGet<Double>("experience", 0.0).toFloat()
            val cookingTime = node.tryGet<Int>("cooking-time", 200)
            FurnaceRecipe(key, result, input, experience, cookingTime)
        }
        "campfire" -> {
            val input = node.tryGet<Node>("input")?.let { parseItemStack(it) }?.let { RecipeChoice.ExactChoice(it) }
                    ?: return null
            val experience = node.tryGet<Double>("experience", 0.0).toFloat()
            val cookingTime = node.tryGet<Int>("cooking-time", 600)
            CampfireRecipe(key, result, input, experience, cookingTime)
        }
        "smoking" -> {
            val input = node.tryGet<Node>("input")?.let { parseItemStack(it) }?.let { RecipeChoice.ExactChoice(it) }
                    ?: return null
            val experience = node.tryGet<Double>("experience", 0.0).toFloat()
            val cookingTime = node.tryGet<Int>("cooking-time", 100)
            SmokingRecipe(key, result, input, experience, cookingTime)
        }
        "blasting" -> {
            val input = node.tryGet<Node>("input")?.let { parseItemStack(it) }?.let { RecipeChoice.ExactChoice(it) }
                    ?: return null
            val experience = node.tryGet<Double>("experience", 0.0).toFloat()
            val cookingTime = node.tryGet<Int>("cooking-time", 100)
            SmokingRecipe(key, result, input, experience, cookingTime)
            BlastingRecipe(key, result, input, experience, cookingTime)
        }
        else -> null
    }
}
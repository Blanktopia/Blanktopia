package me.weiwen.blanktopia

import ch.njol.skript.aliases.Aliases
import ch.njol.skript.aliases.ItemType
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

data class Item(val amount: Int, val material: Material, val name: String)

fun parseItem(str: String): Item? {
    if (Bukkit.getPluginManager().getPlugin("Skript") != null) {
        val itemType = Aliases.parseItemType(str) ?: return null
        val itemData = itemType.types[0]
        return Item(itemType.amount, itemType.material, itemType.toString(ch.njol.skript.localization.Language.F_INDEFINITE_ARTICLE))
    } else {
        val splitPrice = str.split(' ', limit = 2)
        if (splitPrice.size != 2) return null
        val amount = splitPrice[0].toIntOrNull() ?: return null
        val material = Material.matchMaterial(splitPrice[1]) ?: return null
        return Item(amount, material, "${amount} ${material.toString()}")
    }
}

fun toString(item: ItemStack): String {
    if (Bukkit.getPluginManager().getPlugin("Skript") != null) {
        return ItemType.toString(item, ch.njol.skript.localization.Language.F_INDEFINITE_ARTICLE)
    } else {
        return "${item.amount} ${item.type}"
    }
}
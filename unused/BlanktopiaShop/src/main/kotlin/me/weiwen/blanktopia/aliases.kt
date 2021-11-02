package me.weiwen.blanktopia

import ch.njol.skript.aliases.Aliases
import ch.njol.skript.aliases.ItemType
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

data class Item(val amount: Int, val material: Material, val name: String)

fun parseItem(str: String): Item? {
    if (str.toLowerCase() == "free") {
        return Item(0, Material.DIAMOND, "FREE")
    } else if (Bukkit.getPluginManager().getPlugin("Skript") != null) {
        val itemType = Aliases.parseItemType(str) ?: return null
        val item = ItemType(itemType.material)
        item.amount = itemType.amount
        var name = item.toString(ch.njol.skript.localization.Language.F_INDEFINITE_ARTICLE)
        if (name.first() !in '0'..'9') {
            name = "1 $name"
        }
        if (item.amount == 0) {
            name = "FREE"
        }
        return Item(item.amount, item.material, name)
    } else {
        val splitPrice = str.split(' ', limit = 2)
        if (splitPrice.size != 2) return null
        val amount = splitPrice[0].toIntOrNull() ?: return null
        val material = Material.matchMaterial(splitPrice[1]) ?: return null
        val name = if (amount == 0) {
            "FREE"
        } else {
            material.toString()
        }
        return Item(amount, material, "${amount} ${name}")
    }
}

fun toString(item: ItemStack): String {
    if (Bukkit.getPluginManager().getPlugin("Skript") != null) {
        return ItemType.toString(item, ch.njol.skript.localization.Language.F_INDEFINITE_ARTICLE)
    } else {
        return "${item.amount} ${item.type}"
    }
}
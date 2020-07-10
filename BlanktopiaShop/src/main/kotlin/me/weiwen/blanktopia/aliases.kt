package me.weiwen.blanktopia

import ch.njol.skript.aliases.Aliases
import org.bukkit.Bukkit
import org.bukkit.Material

fun parseItem(str: String, plural: Boolean): Pair<Material, String>? {
    if (Bukkit.getPluginManager().getPlugin("Skript") != null) {
        val itemType = Aliases.parseItemType(str) ?: return null
        val itemData = itemType.types[0]
        return Pair(itemType.material, itemData.toString(false, plural))
    } else {
        val material = Material.matchMaterial(str) ?: return null
        return Pair(material, material.toString())
    }
}

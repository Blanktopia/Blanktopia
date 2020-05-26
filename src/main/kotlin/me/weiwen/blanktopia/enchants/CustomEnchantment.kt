package me.weiwen.blanktopia.enchants

import me.weiwen.blanktopia.Blanktopia
import me.weiwen.blanktopia.toRomanNumerals
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.enchantments.EnchantmentTarget
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta

open class CustomEnchantment(
    key: String,
    val _name: String,
    val _maxLevel: Int,
    val primaryItems: Set<Material>,
    val secondaryItems: Set<Material>,
    val probability: Double,
    val enchantCostBase: Int,
    val enchantCostPerLevel: Int,
    val anvilCostPerLevel: Int,
    val conflicts: () -> Set<Enchantment>,
    val listener: Listener) :
    Enchantment(
        NamespacedKey(
            Blanktopia.INSTANCE,
            key
        )
    ) {
    var _isTreasure = false
    var _startLevel = 1

    override fun getName(): String {
        return _name
    }

    override fun isCursed(): Boolean {
        return false
    }

    override fun canEnchantItem(item: ItemStack): Boolean {
        return primaryItems.contains(item.type)
    }

    fun canAnvilItem(item: ItemStack): Boolean {
        return primaryItems.contains(item.type) || secondaryItems.contains(item.type)
    }

    override fun conflictsWith(other: Enchantment): Boolean {
        return other == this || conflicts().contains(other)
    }

    override fun getItemTarget(): EnchantmentTarget {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getMaxLevel(): Int {
        return _maxLevel
    }

    override fun getStartLevel(): Int {
        return _startLevel
    }

    override fun isTreasure(): Boolean {
        return _isTreasure
    }
}

fun ItemStack.enchant(enchantment: Enchantment, level: Int) {
    val meta = this.itemMeta ?: Bukkit.getItemFactory().getItemMeta(this.type)!!
    if (meta is EnchantmentStorageMeta) {
        meta.addStoredEnchant(enchantment, level, true)
    } else {
        meta.addEnchant(enchantment, level, true)
    }
    if (enchantment is CustomEnchantment) {
        val enchantmentLore = StringBuilder()
        enchantmentLore.append(ChatColor.RESET)
        enchantmentLore.append(ChatColor.GRAY)
        enchantmentLore.append(enchantment.name)
        if (enchantment.maxLevel != 1) {
            enchantmentLore.append(" ")
            enchantmentLore.append(level.toRomanNumerals())
        }
        val lore = meta.lore ?: ArrayList<String>()
        var found = false
        for (i in lore.indices) {
            if (lore[i].startsWith(ChatColor.RESET.toString() + ChatColor.GRAY + enchantment.name)) {
                lore[i] = enchantmentLore.toString()
                found = true
                break
            }
        }
        if (!found) {
            lore.add(0, enchantmentLore.toString())
        }
        meta.lore = lore
    }
    this.itemMeta = meta
}

fun ItemStack.disenchant(enchantment: Enchantment) {
    val meta = this.itemMeta ?: return
    if (meta is EnchantmentStorageMeta) {
        meta.removeStoredEnchant(enchantment)
    } else {
        meta.removeEnchant(enchantment)
    }
    if (enchantment is CustomEnchantment) {
        val lore = meta.lore ?: return
        for (i in lore.indices) {
            if (lore[i].startsWith(ChatColor.RESET.toString() + ChatColor.GRAY + enchantment.name)) {
                lore.removeAt(i)
                break
            }
        }
        meta.lore = lore
    }
    this.itemMeta = meta
}
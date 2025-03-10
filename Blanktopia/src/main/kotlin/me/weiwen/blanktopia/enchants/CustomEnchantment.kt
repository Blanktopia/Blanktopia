package me.weiwen.blanktopia.enchants

import io.papermc.paper.enchantments.EnchantmentRarity
import me.weiwen.blanktopia.enchants.extensions.toRomanNumerals
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Registry
import org.bukkit.enchantments.Enchantment
import org.bukkit.enchantments.EnchantmentTarget
import org.bukkit.entity.EntityCategory
import org.bukkit.event.Listener
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta

open class CustomEnchantment(
    val _key: String,
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
    Enchantment() {

    var _isTreasure = false
    var _startLevel = 1

    override fun translationKey(): String {
        return _key
    }

    override fun getName(): String {
        return _name
    }

    override fun displayName(level: Int): Component {
        return if (maxLevel != 1) {
            Component.text("$name ${level.toRomanNumerals()}")
        } else {
            Component.text(name)
        }
    }

    override fun isTradeable(): Boolean {
        return false
    }

    override fun isDiscoverable(): Boolean {
        return false
    }

    override fun getRarity(): EnchantmentRarity {
        return EnchantmentRarity.RARE
    }

    override fun getDamageIncrease(level: Int, entityCategory: EntityCategory): Float {
        return 0f
    }

    override fun getActiveSlots(): MutableSet<EquipmentSlot> {
        return mutableSetOf()
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
        enchantmentLore.append(ChatColor.GRAY)
        enchantmentLore.append(enchantment.name)
        if (enchantment.maxLevel != 1) {
            enchantmentLore.append(" ")
            enchantmentLore.append(level.toRomanNumerals())
        }
        val lore = meta.lore ?: ArrayList<String>()
        var found = false
        for (i in lore.indices) {
            if (lore[i].startsWith("${ChatColor.RESET}${ChatColor.GRAY}${enchantment.name}")
                    || lore[i].startsWith("${ChatColor.GRAY}${enchantment.name}")) {
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
            if (lore[i].startsWith("${ChatColor.RESET}${ChatColor.GRAY}${enchantment.name}")
                || lore[i].startsWith("${ChatColor.GRAY}${enchantment.name}")) {
                    lore.removeAt(i)
                break
            }
        }
        meta.lore = lore
    }
    this.itemMeta = meta
}
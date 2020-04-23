package me.weiwen.blanktopia.enchants

import org.bukkit.Material
import org.bukkit.enchantments.Enchantment

fun Enchantment.getAnvilCostPerLevel(): Int {
    if (this is CustomEnchantment) return this.anvilCostPerLevel
    return when (this) {
        Enchantment.PROTECTION_ENVIRONMENTAL -> 1
        Enchantment.PROTECTION_FIRE -> 2
        Enchantment.PROTECTION_FALL -> 2
        Enchantment.PROTECTION_EXPLOSIONS -> 4
        Enchantment.PROTECTION_PROJECTILE -> 2
        Enchantment.THORNS -> 8
        Enchantment.OXYGEN -> 4
        Enchantment.DEPTH_STRIDER -> 4
        Enchantment.WATER_WORKER -> 4
        Enchantment.DAMAGE_ALL -> 1
        Enchantment.DAMAGE_UNDEAD -> 2
        Enchantment.DAMAGE_ARTHROPODS -> 2
        Enchantment.KNOCKBACK -> 2
        Enchantment.FIRE_ASPECT -> 4
        Enchantment.LOOT_BONUS_MOBS -> 4
        Enchantment.DIG_SPEED -> 1
        Enchantment.SILK_TOUCH -> 8
        Enchantment.DURABILITY -> 2
        Enchantment.LOOT_BONUS_BLOCKS -> 4
        Enchantment.ARROW_DAMAGE -> 1
        Enchantment.ARROW_KNOCKBACK -> 4
        Enchantment.ARROW_FIRE -> 4
        Enchantment.ARROW_INFINITE -> 8
        Enchantment.LUCK -> 4
        Enchantment.LURE -> 4
        Enchantment.FROST_WALKER -> 4
        Enchantment.MENDING -> 4
        Enchantment.BINDING_CURSE -> 8
        Enchantment.VANISHING_CURSE -> 8
        Enchantment.IMPALING -> 4
        Enchantment.RIPTIDE -> 4
        Enchantment.LOYALTY -> 1
        Enchantment.CHANNELING -> 8
        Enchantment.PIERCING -> 1
        Enchantment.QUICK_CHARGE -> 2
        Enchantment.SWEEPING_EDGE -> 4
        else -> 2
    }
}

fun Material.getEnchantability(): Int {
    return when (this) {
        in LEATHER_ARMOR -> 15
        in CHAINMAIL_ARMOR -> 12
        in IRON_ARMOR -> 9
        in GOLDEN_ARMOR -> 25
        in DIAMOND_ARMOR -> 9
        in WOODEN_TOOLS -> 15
        in STONE_TOOLS -> 5
        in IRON_TOOLS -> 14
        in GOLDEN_TOOLS -> 22
        in DIAMOND_TOOLS -> 10
        Material.BOOK -> 1
        Material.TURTLE_HELMET -> 9
        else -> 1
    }
}

val NONE = setOf<Material>()
val LEATHER_ARMOR = setOf(
    Material.LEATHER_HELMET,
    Material.LEATHER_CHESTPLATE,
    Material.LEATHER_LEGGINGS,
    Material.LEATHER_BOOTS
)
val CHAINMAIL_ARMOR = setOf(
    Material.CHAINMAIL_HELMET,
    Material.CHAINMAIL_CHESTPLATE,
    Material.CHAINMAIL_LEGGINGS,
    Material.CHAINMAIL_BOOTS
    )
val IRON_ARMOR = setOf(
    Material.IRON_HELMET,
    Material.IRON_CHESTPLATE,
    Material.IRON_LEGGINGS,
    Material.IRON_BOOTS
    )
val GOLDEN_ARMOR = setOf(
    Material.GOLDEN_HELMET,
    Material.GOLDEN_CHESTPLATE,
    Material.GOLDEN_LEGGINGS,
    Material.GOLDEN_BOOTS
    )
val DIAMOND_ARMOR = setOf(
    Material.DIAMOND_HELMET,
    Material.DIAMOND_CHESTPLATE,
    Material.DIAMOND_LEGGINGS,
    Material.DIAMOND_BOOTS
    )
val HELMET = setOf(
    Material.LEATHER_HELMET,
    Material.CHAINMAIL_HELMET,
    Material.IRON_HELMET,
    Material.GOLDEN_HELMET,
    Material.DIAMOND_HELMET,
    Material.TURTLE_HELMET
)
val CHESTPLATE = setOf(
    Material.LEATHER_CHESTPLATE,
    Material.CHAINMAIL_CHESTPLATE,
    Material.IRON_CHESTPLATE,
    Material.GOLDEN_CHESTPLATE,
    Material.DIAMOND_CHESTPLATE
)
val LEGGINGS = setOf(
    Material.LEATHER_LEGGINGS,
    Material.CHAINMAIL_LEGGINGS,
    Material.IRON_LEGGINGS,
    Material.GOLDEN_LEGGINGS,
    Material.DIAMOND_LEGGINGS
)
val BOOTS = setOf(
    Material.LEATHER_BOOTS,
    Material.CHAINMAIL_BOOTS,
    Material.IRON_BOOTS,
    Material.GOLDEN_BOOTS,
    Material.DIAMOND_BOOTS
)
val WOODEN_TOOLS = setOf(
    Material.WOODEN_SHOVEL,
    Material.WOODEN_AXE,
    Material.WOODEN_PICKAXE,
    Material.WOODEN_HOE,
    Material.WOODEN_SWORD
)
val STONE_TOOLS = setOf(
    Material.STONE_SHOVEL,
    Material.STONE_AXE,
    Material.STONE_PICKAXE,
    Material.STONE_HOE,
    Material.STONE_SWORD
)
val IRON_TOOLS = setOf(
    Material.IRON_SHOVEL,
    Material.IRON_AXE,
    Material.IRON_PICKAXE,
    Material.IRON_HOE,
    Material.IRON_SWORD
)
val GOLDEN_TOOLS = setOf(
    Material.GOLDEN_SHOVEL,
    Material.GOLDEN_AXE,
    Material.GOLDEN_PICKAXE,
    Material.GOLDEN_HOE,
    Material.GOLDEN_SWORD
)
val DIAMOND_TOOLS = setOf(
    Material.DIAMOND_SHOVEL,
    Material.DIAMOND_AXE,
    Material.DIAMOND_PICKAXE,
    Material.DIAMOND_HOE,
    Material.DIAMOND_SWORD
)
val SWORDS = setOf(
    Material.WOODEN_SWORD,
    Material.STONE_SWORD,
    Material.IRON_SWORD,
    Material.GOLDEN_SWORD,
    Material.DIAMOND_SWORD
)
val AXES = setOf(
    Material.WOODEN_AXE,
    Material.STONE_AXE,
    Material.IRON_AXE,
    Material.GOLDEN_AXE,
    Material.DIAMOND_AXE
)
val PICKAXES = setOf(
    Material.WOODEN_PICKAXE,
    Material.STONE_PICKAXE,
    Material.IRON_PICKAXE,
    Material.GOLDEN_PICKAXE,
    Material.DIAMOND_PICKAXE
)
val SHOVELS = setOf(
    Material.WOODEN_SHOVEL,
    Material.STONE_SHOVEL,
    Material.IRON_SHOVEL,
    Material.GOLDEN_SHOVEL,
    Material.DIAMOND_SHOVEL
)
val HOES = setOf(
    Material.WOODEN_HOE,
    Material.STONE_HOE,
    Material.IRON_HOE,
    Material.GOLDEN_HOE,
    Material.DIAMOND_HOE
)
val BOOKS = setOf(
    Material.BOOK
)
val ELYTRA = setOf(
    Material.ELYTRA
)
val BOWS = setOf(Material.BOW, Material.CROSSBOW)
val FISHING_ROD = setOf(Material.FISHING_ROD)
val TRIDENT = setOf(Material.TRIDENT)
val ARMOR = HELMET + CHESTPLATE + LEGGINGS + BOOTS
val TOOLS = SWORDS + AXES + PICKAXES + SHOVELS + HOES
val ALL = ARMOR + TOOLS + TRIDENT + BOWS + FISHING_ROD + ELYTRA

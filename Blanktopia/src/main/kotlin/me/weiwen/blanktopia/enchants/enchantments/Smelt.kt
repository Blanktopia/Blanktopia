package me.weiwen.blanktopia.enchants.enchantments

import me.weiwen.blanktopia.enchants.*
import me.weiwen.blanktopia.playSoundAt
import me.weiwen.blanktopia.spawnParticleAt
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.entity.EntityType
import org.bukkit.entity.ExperienceOrb
import org.bukkit.entity.Item
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockDropItemEvent
import org.bukkit.inventory.ItemStack
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.random.Random


val SMELT = CustomEnchantment(
    "smelt",
    "Smelt",
    1,
    PICKAXES + SHOVELS + HOES + BOOKS,
    AXES,
    0.1,
    30,
    30,
    12,
    { setOf() },
    Smelt
)

object Smelt : Listener {
    init {}

    @EventHandler
    fun onBlockDropItem(event: BlockDropItemEvent) {
        val player = event.player
        val tool = player.inventory.itemInMainHand
        if (!tool.containsEnchantment(SMELT)) return
        val block = event.block
        var smelted = false
        var experience = 0.0
        val items = event.items
        val removedItems = mutableSetOf<Item>()
        for (item in items) {
            val smeltedItem = getSmeltedDrops(item.itemStack)
            if (smeltedItem != null) {
                removedItems.add(item)
                smelted = true
                block.world.dropItemNaturally(block.location, smeltedItem.first)
                experience += smeltedItem.second
            }
        }
        items.removeAll(removedItems)
        if (smelted) {
            block.spawnParticleAt(Particle.FLAME, 1, 0.01)
            block.playSoundAt(Sound.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 0.5f, 0.8f)
        }
        if (experience > 0) {
            val orb = block.world.spawnEntity(block.location, EntityType.EXPERIENCE_ORB) as ExperienceOrb
            orb.experience = if (Random.nextDouble() > experience.rem(1.0)) {
                ceil(experience)
            } else {
                floor(experience)
            }.toInt()
        }
    }

    private fun getSmeltedDrops(item: ItemStack): Pair<ItemStack, Double>? {
        val amount = item.amount
        return when (item.type) {
            Material.PORKCHOP -> Pair(ItemStack(Material.COOKED_PORKCHOP, amount), 0.0)
            Material.BEEF -> Pair(ItemStack(Material.COOKED_BEEF, amount), 0.0)
            Material.CHICKEN -> Pair(ItemStack(Material.COOKED_CHICKEN, amount), 0.0)
            Material.COD -> Pair(ItemStack(Material.COOKED_COD, amount), 0.0)
            Material.SALMON -> Pair(ItemStack(Material.COOKED_SALMON, amount), 0.0)
            Material.POTATO -> Pair(ItemStack(Material.BAKED_POTATO, amount), 0.0)
            Material.MUTTON -> Pair(ItemStack(Material.COOKED_MUTTON, amount), 0.0)
            Material.RABBIT -> Pair(ItemStack(Material.COOKED_RABBIT, amount), 0.0)
            Material.KELP -> Pair(ItemStack(Material.DRIED_KELP, amount), 0.0)
            Material.KELP_PLANT -> Pair(ItemStack(Material.DRIED_KELP, amount), 0.0)
            Material.IRON_ORE -> Pair(ItemStack(Material.IRON_INGOT, amount), 0.035)
            Material.GOLD_ORE -> Pair(ItemStack(Material.GOLD_INGOT, amount), 0.5)
            Material.SAND -> Pair(ItemStack(Material.GLASS, amount), 0.0)
            Material.COBBLESTONE -> Pair(ItemStack(Material.STONE, amount), 0.00)
            Material.STONE -> Pair(ItemStack(Material.SMOOTH_STONE, amount), 0.0)
            Material.SANDSTONE -> Pair(ItemStack(Material.SMOOTH_SANDSTONE, amount), 0.0)
            Material.RED_SANDSTONE -> Pair(ItemStack(Material.SMOOTH_RED_SANDSTONE, amount), 0.0)
            Material.QUARTZ_BLOCK -> Pair(ItemStack(Material.SMOOTH_QUARTZ, amount), 0.0)
            Material.NETHERRACK -> Pair(ItemStack(Material.NETHER_BRICK, amount), 0.0)
            Material.CLAY_BALL -> Pair(ItemStack(Material.BRICK, amount), 0.0)
            Material.CLAY -> Pair(ItemStack(Material.TERRACOTTA, amount), 0.0)
            Material.STONE_BRICKS -> Pair(ItemStack(Material.CRACKED_STONE_BRICKS, amount), 0.0)
            Material.WHITE_TERRACOTTA -> Pair(ItemStack(Material.WHITE_GLAZED_TERRACOTTA, amount), 0.0)
            Material.ORANGE_TERRACOTTA -> Pair(ItemStack(Material.ORANGE_GLAZED_TERRACOTTA, amount), 0.0)
            Material.MAGENTA_TERRACOTTA -> Pair(ItemStack(Material.MAGENTA_GLAZED_TERRACOTTA, amount), 0.0)
            Material.LIGHT_BLUE_TERRACOTTA -> Pair(ItemStack(Material.LIGHT_BLUE_GLAZED_TERRACOTTA, amount), 0.0)
            Material.YELLOW_TERRACOTTA -> Pair(ItemStack(Material.YELLOW_GLAZED_TERRACOTTA, amount), 0.0)
            Material.LIME_TERRACOTTA -> Pair(ItemStack(Material.LIME_GLAZED_TERRACOTTA, amount), 0.0)
            Material.PINK_TERRACOTTA -> Pair(ItemStack(Material.PINK_GLAZED_TERRACOTTA, amount), 0.0)
            Material.GRAY_TERRACOTTA -> Pair(ItemStack(Material.GRAY_GLAZED_TERRACOTTA, amount), 0.0)
            Material.LIGHT_GRAY_TERRACOTTA -> Pair(ItemStack(Material.LIGHT_GRAY_GLAZED_TERRACOTTA, amount), 0.0)
            Material.CYAN_TERRACOTTA -> Pair(ItemStack(Material.CYAN_GLAZED_TERRACOTTA, amount), 0.0)
            Material.BLUE_TERRACOTTA -> Pair(ItemStack(Material.BLUE_GLAZED_TERRACOTTA, amount), 0.0)
            Material.PURPLE_TERRACOTTA -> Pair(ItemStack(Material.PURPLE_GLAZED_TERRACOTTA, amount), 0.0)
            Material.GREEN_TERRACOTTA -> Pair(ItemStack(Material.GREEN_GLAZED_TERRACOTTA, amount), 0.0)
            Material.BROWN_TERRACOTTA -> Pair(ItemStack(Material.BROWN_GLAZED_TERRACOTTA, amount), 0.0)
            Material.RED_TERRACOTTA -> Pair(ItemStack(Material.RED_GLAZED_TERRACOTTA, amount), 0.0)
            Material.BLACK_TERRACOTTA -> Pair(ItemStack(Material.BLACK_GLAZED_TERRACOTTA, amount), 0.0)
            Material.CACTUS -> Pair(ItemStack(Material.GREEN_DYE, amount), 0.0)
            Material.ACACIA_LOG -> Pair(ItemStack(Material.CHARCOAL, amount), 0.0)
            Material.BIRCH_LOG -> Pair(ItemStack(Material.CHARCOAL, amount), 0.0)
            Material.DARK_OAK_LOG -> Pair(ItemStack(Material.CHARCOAL, amount), 0.0)
            Material.JUNGLE_LOG -> Pair(ItemStack(Material.CHARCOAL, amount), 0.0)
            Material.OAK_LOG -> Pair(ItemStack(Material.CHARCOAL, amount), 0.0)
            Material.SPRUCE_LOG -> Pair(ItemStack(Material.CHARCOAL, amount), 0.0)
            Material.ACACIA_WOOD -> Pair(ItemStack(Material.CHARCOAL, amount), 0.0)
            Material.BIRCH_WOOD -> Pair(ItemStack(Material.CHARCOAL, amount), 0.0)
            Material.DARK_OAK_WOOD -> Pair(ItemStack(Material.CHARCOAL, amount), 0.0)
            Material.JUNGLE_WOOD -> Pair(ItemStack(Material.CHARCOAL, amount), 0.0)
            Material.OAK_WOOD -> Pair(ItemStack(Material.CHARCOAL, amount), 0.0)
            Material.SPRUCE_WOOD -> Pair(ItemStack(Material.CHARCOAL, amount), 0.0)
            Material.STRIPPED_ACACIA_LOG -> Pair(ItemStack(Material.CHARCOAL, amount), 0.0)
            Material.STRIPPED_BIRCH_LOG -> Pair(ItemStack(Material.CHARCOAL, amount), 0.0)
            Material.STRIPPED_DARK_OAK_LOG -> Pair(ItemStack(Material.CHARCOAL, amount), 0.0)
            Material.STRIPPED_JUNGLE_LOG -> Pair(ItemStack(Material.CHARCOAL, amount), 0.0)
            Material.STRIPPED_OAK_LOG -> Pair(ItemStack(Material.CHARCOAL, amount), 0.0)
            Material.STRIPPED_SPRUCE_LOG -> Pair(ItemStack(Material.CHARCOAL, amount), 0.0)
            Material.STRIPPED_ACACIA_WOOD -> Pair(ItemStack(Material.CHARCOAL, amount), 0.0)
            Material.STRIPPED_BIRCH_WOOD -> Pair(ItemStack(Material.CHARCOAL, amount), 0.0)
            Material.STRIPPED_DARK_OAK_WOOD -> Pair(ItemStack(Material.CHARCOAL, amount), 0.0)
            Material.STRIPPED_JUNGLE_WOOD -> Pair(ItemStack(Material.CHARCOAL, amount), 0.0)
            Material.STRIPPED_OAK_WOOD -> Pair(ItemStack(Material.CHARCOAL, amount), 0.0)
            Material.STRIPPED_SPRUCE_WOOD -> Pair(ItemStack(Material.CHARCOAL, amount), 0.0)
            Material.CHORUS_FRUIT -> Pair(ItemStack(Material.POPPED_CHORUS_FRUIT, amount), 0.0)
            Material.WET_SPONGE -> Pair(ItemStack(Material.SPONGE, amount), 0.0)
            Material.SEA_PICKLE -> Pair(ItemStack(Material.LIME_DYE, amount), 0.0)
            else -> null
        }
    }
}

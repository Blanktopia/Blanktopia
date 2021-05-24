package me.weiwen.blanktopia.enchants.enchantments

import me.weiwen.blanktopia.enchants.*
import me.weiwen.moromoro.extensions.playSoundAt
import me.weiwen.moromoro.extensions.spawnParticle
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.entity.EntityType
import org.bukkit.entity.ExperienceOrb
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
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

data class SmeltResult(val material: Material, val experience: Double)

object Smelt : Listener {
    init {}

    @EventHandler(priority = EventPriority.LOW)
    fun onBlockDropItem(event: BlockDropItemEvent) {
        val player = event.player
        val tool = player.inventory.itemInMainHand
        if (!tool.containsEnchantment(SMELT)) return
        val block = event.block
        var smelted = false
        var experience = 0.0
        val items = event.items
        for (item in items) {
            val smeltedItem = getSmeltedDrops(item.itemStack) ?: continue
            smelted = true
            experience += smeltedItem.experience

            val itemStack = item.itemStack
            itemStack.type = smeltedItem.material
            item.itemStack = itemStack
        }
        if (smelted) {
            block.spawnParticle(Particle.FLAME, 1, 0.01)
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

    private fun getSmeltedDrops(item: ItemStack): SmeltResult? {
        return when (item.type) {
            Material.PORKCHOP -> SmeltResult(Material.COOKED_PORKCHOP, 0.0)
            Material.BEEF -> SmeltResult(Material.COOKED_BEEF, 0.0)
            Material.CHICKEN -> SmeltResult(Material.COOKED_CHICKEN, 0.0)
            Material.COD -> SmeltResult(Material.COOKED_COD, 0.0)
            Material.SALMON -> SmeltResult(Material.COOKED_SALMON, 0.0)
            Material.POTATO -> SmeltResult(Material.BAKED_POTATO, 0.0)
            Material.MUTTON -> SmeltResult(Material.COOKED_MUTTON, 0.0)
            Material.RABBIT -> SmeltResult(Material.COOKED_RABBIT, 0.0)
            Material.KELP -> SmeltResult(Material.DRIED_KELP, 0.0)
            Material.KELP_PLANT -> SmeltResult(Material.DRIED_KELP, 0.0)
            Material.IRON_ORE -> SmeltResult(Material.IRON_INGOT, 0.035)
            Material.GOLD_ORE -> SmeltResult(Material.GOLD_INGOT, 0.5)
            Material.SAND -> SmeltResult(Material.GLASS, 0.0)
            Material.COBBLESTONE -> SmeltResult(Material.STONE, 0.00)
            Material.STONE -> SmeltResult(Material.SMOOTH_STONE, 0.0)
            Material.SANDSTONE -> SmeltResult(Material.SMOOTH_SANDSTONE, 0.0)
            Material.RED_SANDSTONE -> SmeltResult(Material.SMOOTH_RED_SANDSTONE, 0.0)
            Material.QUARTZ_BLOCK -> SmeltResult(Material.SMOOTH_QUARTZ, 0.0)
            Material.NETHERRACK -> SmeltResult(Material.NETHER_BRICK, 0.0)
            Material.CLAY_BALL -> SmeltResult(Material.BRICK, 0.0)
            Material.CLAY -> SmeltResult(Material.TERRACOTTA, 0.0)
            Material.STONE_BRICKS -> SmeltResult(Material.CRACKED_STONE_BRICKS, 0.0)
            Material.WHITE_TERRACOTTA -> SmeltResult(Material.WHITE_GLAZED_TERRACOTTA, 0.0)
            Material.ORANGE_TERRACOTTA -> SmeltResult(Material.ORANGE_GLAZED_TERRACOTTA, 0.0)
            Material.MAGENTA_TERRACOTTA -> SmeltResult(Material.MAGENTA_GLAZED_TERRACOTTA, 0.0)
            Material.LIGHT_BLUE_TERRACOTTA -> SmeltResult(Material.LIGHT_BLUE_GLAZED_TERRACOTTA, 0.0)
            Material.YELLOW_TERRACOTTA -> SmeltResult(Material.YELLOW_GLAZED_TERRACOTTA, 0.0)
            Material.LIME_TERRACOTTA -> SmeltResult(Material.LIME_GLAZED_TERRACOTTA, 0.0)
            Material.PINK_TERRACOTTA -> SmeltResult(Material.PINK_GLAZED_TERRACOTTA, 0.0)
            Material.GRAY_TERRACOTTA -> SmeltResult(Material.GRAY_GLAZED_TERRACOTTA, 0.0)
            Material.LIGHT_GRAY_TERRACOTTA -> SmeltResult(Material.LIGHT_GRAY_GLAZED_TERRACOTTA, 0.0)
            Material.CYAN_TERRACOTTA -> SmeltResult(Material.CYAN_GLAZED_TERRACOTTA, 0.0)
            Material.BLUE_TERRACOTTA -> SmeltResult(Material.BLUE_GLAZED_TERRACOTTA, 0.0)
            Material.PURPLE_TERRACOTTA -> SmeltResult(Material.PURPLE_GLAZED_TERRACOTTA, 0.0)
            Material.GREEN_TERRACOTTA -> SmeltResult(Material.GREEN_GLAZED_TERRACOTTA, 0.0)
            Material.BROWN_TERRACOTTA -> SmeltResult(Material.BROWN_GLAZED_TERRACOTTA, 0.0)
            Material.RED_TERRACOTTA -> SmeltResult(Material.RED_GLAZED_TERRACOTTA, 0.0)
            Material.BLACK_TERRACOTTA -> SmeltResult(Material.BLACK_GLAZED_TERRACOTTA, 0.0)
            Material.CACTUS -> SmeltResult(Material.GREEN_DYE, 0.0)
            Material.ACACIA_LOG -> SmeltResult(Material.CHARCOAL, 0.0)
            Material.BIRCH_LOG -> SmeltResult(Material.CHARCOAL, 0.0)
            Material.DARK_OAK_LOG -> SmeltResult(Material.CHARCOAL, 0.0)
            Material.JUNGLE_LOG -> SmeltResult(Material.CHARCOAL, 0.0)
            Material.OAK_LOG -> SmeltResult(Material.CHARCOAL, 0.0)
            Material.SPRUCE_LOG -> SmeltResult(Material.CHARCOAL, 0.0)
            Material.ACACIA_WOOD -> SmeltResult(Material.CHARCOAL, 0.0)
            Material.BIRCH_WOOD -> SmeltResult(Material.CHARCOAL, 0.0)
            Material.DARK_OAK_WOOD -> SmeltResult(Material.CHARCOAL, 0.0)
            Material.JUNGLE_WOOD -> SmeltResult(Material.CHARCOAL, 0.0)
            Material.OAK_WOOD -> SmeltResult(Material.CHARCOAL, 0.0)
            Material.SPRUCE_WOOD -> SmeltResult(Material.CHARCOAL, 0.0)
            Material.STRIPPED_ACACIA_LOG -> SmeltResult(Material.CHARCOAL, 0.0)
            Material.STRIPPED_BIRCH_LOG -> SmeltResult(Material.CHARCOAL, 0.0)
            Material.STRIPPED_DARK_OAK_LOG -> SmeltResult(Material.CHARCOAL, 0.0)
            Material.STRIPPED_JUNGLE_LOG -> SmeltResult(Material.CHARCOAL, 0.0)
            Material.STRIPPED_OAK_LOG -> SmeltResult(Material.CHARCOAL, 0.0)
            Material.STRIPPED_SPRUCE_LOG -> SmeltResult(Material.CHARCOAL, 0.0)
            Material.STRIPPED_ACACIA_WOOD -> SmeltResult(Material.CHARCOAL, 0.0)
            Material.STRIPPED_BIRCH_WOOD -> SmeltResult(Material.CHARCOAL, 0.0)
            Material.STRIPPED_DARK_OAK_WOOD -> SmeltResult(Material.CHARCOAL, 0.0)
            Material.STRIPPED_JUNGLE_WOOD -> SmeltResult(Material.CHARCOAL, 0.0)
            Material.STRIPPED_OAK_WOOD -> SmeltResult(Material.CHARCOAL, 0.0)
            Material.STRIPPED_SPRUCE_WOOD -> SmeltResult(Material.CHARCOAL, 0.0)
            Material.CHORUS_FRUIT -> SmeltResult(Material.POPPED_CHORUS_FRUIT, 0.0)
            Material.WET_SPONGE -> SmeltResult(Material.SPONGE, 0.0)
            Material.SEA_PICKLE -> SmeltResult(Material.LIME_DYE, 0.0)
            Material.ANCIENT_DEBRIS -> SmeltResult(Material.NETHERITE_SCRAP, 0.0)
            Material.WARPED_HYPHAE -> SmeltResult(Material.CHARCOAL, 0.0)
            Material.WARPED_STEM -> SmeltResult(Material.CHARCOAL, 0.0)
            Material.STRIPPED_WARPED_HYPHAE -> SmeltResult(Material.CHARCOAL, 0.0)
            Material.STRIPPED_WARPED_STEM -> SmeltResult(Material.CHARCOAL, 0.0)
            Material.CRIMSON_HYPHAE -> SmeltResult(Material.CHARCOAL, 0.0)
            Material.CRIMSON_STEM -> SmeltResult(Material.CHARCOAL, 0.0)
            Material.STRIPPED_CRIMSON_HYPHAE -> SmeltResult(Material.CHARCOAL, 0.0)
            Material.STRIPPED_CRIMSON_STEM -> SmeltResult(Material.CHARCOAL, 0.0)
            else -> null
        }
    }
}

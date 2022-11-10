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
import org.bukkit.inventory.FurnaceRecipe
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

data class SmeltResult(val item: ItemStack, val experience: Float)

object Smelt : Listener {
    init {
    }

    val smeltRecipes: Map<Material, FurnaceRecipe> by lazy {
        val map = mutableMapOf<Material, FurnaceRecipe>()
        Blanktopia.INSTANCE.server.recipeIterator().forEach {
            if (it is FurnaceRecipe) {
                map[it.input.type] = it
            }
        }
        map
    }

    @EventHandler(priority = EventPriority.LOW)
    fun onBlockDropItem(event: BlockDropItemEvent) {
        val player = event.player
        val tool = player.inventory.itemInMainHand
        if (!tool.enchantments.containsKey(SMELT)) return
        val block = event.block
        var smelted = false
        var experience = 0.0
        val items = event.items
        for (item in items) {
            val smeltedItem = getSmeltedDrops(item.itemStack) ?: continue
            smelted = true
            experience += smeltedItem.experience

            val itemStack = item.itemStack
            itemStack.type = smeltedItem.item.type
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
        if (item.type in BLACKLISTED_ITEMS) {
            return null
        }

        val recipe = smeltRecipes[item.type] ?: return null

        val result = recipe.result.clone().apply {
            amount *= item.amount
        }

        val experience = if (recipe.experience <= 0.15) {
            0f
        } else {
            recipe.experience * item.amount
        }

        return SmeltResult(result, experience)
    }
}


val BLACKLISTED_ITEMS = setOf(
    Material.DIAMOND_ORE,
    Material.EMERALD_ORE,
    Material.REDSTONE_ORE,
    Material.COPPER_ORE,
    Material.IRON_ORE,
    Material.GOLD_ORE,
    Material.DEEPSLATE_DIAMOND_ORE,
    Material.DEEPSLATE_EMERALD_ORE,
    Material.DEEPSLATE_REDSTONE_ORE,
    Material.DEEPSLATE_COPPER_ORE,
    Material.DEEPSLATE_IRON_ORE,
    Material.DEEPSLATE_GOLD_ORE,

)
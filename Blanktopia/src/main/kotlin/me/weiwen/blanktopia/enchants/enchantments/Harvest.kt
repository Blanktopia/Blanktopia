package me.weiwen.blanktopia.enchants.enchantments

import me.weiwen.blanktopia.enchants.BOOKS
import me.weiwen.blanktopia.enchants.CustomEnchantment
import me.weiwen.blanktopia.enchants.HOES
import me.weiwen.blanktopia.enchants.NONE
import me.weiwen.blanktopia.enchants.extensions.damage
import me.weiwen.moromoro.extensions.spawnParticle
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.block.data.Ageable
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent

val HARVEST = CustomEnchantment(
    "harvest",
    "Harvest",
    1,
    HOES + BOOKS,
    NONE,
    0.1,
    20,
    20,
    10,
    { setOf<Enchantment>(STING, Enchantment.FIRE_ASPECT) },
    Harvest
)

val HARVESTABLE_BLOCKS = setOf(Material.WHEAT, Material.CARROTS, Material.POTATOES, Material.BEETROOTS, Material.COCOA, Material.NETHER_WART)
val HARVESTABLE_ITEMS = setOf(Material.WHEAT_SEEDS, Material.CARROT, Material.POTATO, Material.BEETROOT_SEEDS, Material.COCOA_BEANS, Material.NETHER_WART)

object Harvest : Listener {
    init {}

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        val player = event.player
        val tool = player.inventory.itemInMainHand
        if (tool.enchantments.containsKey(HARVEST)) {
            val blockData = event.block.blockData as? Ageable ?: return
            if (!HARVESTABLE_BLOCKS.contains(event.block.type)) return

            event.isCancelled = true

            tool.damage(1)

            if (blockData.age < blockData.maximumAge) return

            for (drop in event.block.getDrops(tool)) {
                if (HARVESTABLE_ITEMS.contains(drop.type)) {
                    drop.amount -= 1
                }
                if (drop.amount > 0) {
                    event.block.world.dropItemNaturally(event.block.location, drop)
                }
            }

            event.block.spawnParticle(Particle.VILLAGER_HAPPY, 4, 0.01)
            blockData.age = 0
            event.block.blockData = blockData
        }
    }
}

package me.weiwen.blanktopia.enchants.enchantments

import me.weiwen.blanktopia.Blanktopia
import me.weiwen.blanktopia.enchants.BOOKS
import me.weiwen.blanktopia.enchants.CustomEnchantment
import me.weiwen.blanktopia.enchants.HOES
import me.weiwen.blanktopia.enchants.NONE
import me.weiwen.blanktopia.spawnParticleAt
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Particle
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

object Harvest : Listener {
    init {}

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        val player = event.player
        val tool = player.inventory.itemInMainHand ?: return
        if (tool.containsEnchantment(HARVEST)) {
            spawnParticleAt(Particle.VILLAGER_HAPPY, event.block, 4, 0.01)
            val material = when (event.block.type) {
                Material.WHEAT -> Material.WHEAT
                Material.CARROTS -> Material.CARROTS
                Material.POTATOES -> Material.POTATOES
                Material.BEETROOTS -> Material.BEETROOTS
                else -> null
            }
            event.isDropItems = false
            for (drop in event.block.getDrops(tool)) {
                when (drop.type) {
                    Material.WHEAT_SEEDS,
                        Material.CARROT,
                        Material.POTATO,
                        Material.BEETROOT_SEEDS -> drop.amount -= 1
                    else -> Unit
                }
                if (drop.amount > 0) {
                    event.block.world.dropItemNaturally(event.block.location, drop)
                }
            }
            if (material != null) {
                Bukkit.getScheduler().runTask(Blanktopia.INSTANCE, {
                    event.block.setType(material)
                } as () -> Unit)
            }
        }
    }
}

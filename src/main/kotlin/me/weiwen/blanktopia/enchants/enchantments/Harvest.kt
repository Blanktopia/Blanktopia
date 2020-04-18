package me.weiwen.blanktopia.enchants.enchantments

import me.weiwen.blanktopia.Blanktopia
import me.weiwen.blanktopia.damage
import me.weiwen.blanktopia.enchants.BOOKS
import me.weiwen.blanktopia.enchants.CustomEnchantment
import me.weiwen.blanktopia.enchants.HOES
import me.weiwen.blanktopia.enchants.NONE
import me.weiwen.blanktopia.spawnParticleAt
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.block.data.Ageable
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.meta.Damageable

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
            val blockData = event.block.blockData as? Ageable ?: return
            event.isCancelled = true

            tool.damage(1)

            for (drop in event.block.getDrops(tool)) {
                when (drop.type) {
                    Material.WHEAT_SEEDS,
                        Material.CARROT,
                        Material.POTATO,
                        Material.BEETROOT_SEEDS,
                        Material.COCOA_BEANS -> drop.amount -= 1
                    else -> Unit
                }
                if (drop.amount > 0) {
                    event.block.world.dropItemNaturally(event.block.location, drop)
                }
            }

            spawnParticleAt(Particle.VILLAGER_HAPPY, event.block, 4, 0.01)
            blockData.age = 0
            event.block.blockData = blockData
        }
    }
}

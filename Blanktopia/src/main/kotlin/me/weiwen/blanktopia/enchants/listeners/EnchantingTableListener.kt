package me.weiwen.blanktopia.enchants.listeners

import me.weiwen.blanktopia.enchants.Blanktopia
import me.weiwen.blanktopia.enchants.CustomEnchantment
import me.weiwen.blanktopia.enchants.enchant
import me.weiwen.blanktopia.enchants.getEnchantability
import me.weiwen.blanktopia.enchants.managers.ENCHANTMENTS
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.enchantment.EnchantItemEvent
import kotlin.math.pow
import kotlin.math.round
import kotlin.random.Random

object EnchantingTableListener : Listener {
    @EventHandler
    private fun onEnchantItem(event: EnchantItemEvent) {
        val item = event.item
        val enchants = event.enchantsToAdd.keys

        val enchantability = item.type.getEnchantability()
        val randEnchantability = 1 + Random.nextInt(enchantability / 4 + 1) + Random.nextInt(enchantability / 4 + 1)
        val level = event.expLevelCost + randEnchantability
        val modifier = Random.nextDouble(0.85, 1.15)
        val modifiedLevel = maxOf(round(level * modifier), 1.0).toInt()

        val customEnchants = mutableSetOf<CustomEnchantment>()
        for (enchant in ENCHANTMENTS.shuffled()) {
            if (enchants.any { enchant.conflictsWith(it) }) continue
            if (customEnchants.any { enchant.conflictsWith(it) }) continue
            if (enchant.primaryItems.contains(item.type) && modifiedLevel > enchant.enchantCostBase) {
                customEnchants.add(enchant)
            }
        }
        val totalWeight = customEnchants.map { it.probability }.sum()

        val enchantsToAdd = mutableMapOf<CustomEnchantment, Int>()
        var remainingLevels = modifiedLevel / (2.0).pow(enchants.size)
        while ((1 + remainingLevels) / 50 > Random.nextDouble()) {
            var weight = Random.nextDouble() * totalWeight
            for (enchant in customEnchants) {
                weight -= enchant.probability
                if (weight < 0) {
                    var enchantmentLevel = round(Random.nextDouble(0.5, 1.5) * modifiedLevel).toInt()
                    enchantmentLevel = ((enchantmentLevel - enchant.enchantCostBase) / enchant.enchantCostPerLevel + enchant.startLevel).toInt().coerceIn(enchant.startLevel, enchant.maxLevel)
                    enchantsToAdd[enchant] = enchantmentLevel
                    break
                }
            }
            remainingLevels /= 2
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(Blanktopia.INSTANCE, {
            val item = event.inventory.getItem(0) ?: return@scheduleSyncDelayedTask
            for ((enchant, level) in enchantsToAdd.entries) {
                item.enchant(enchant, level)
            }
        }, 1)
    }
}


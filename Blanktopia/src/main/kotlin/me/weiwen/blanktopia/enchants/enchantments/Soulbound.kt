package me.weiwen.blanktopia.enchants.enchantments

import me.weiwen.blanktopia.enchants.ALL
import me.weiwen.blanktopia.enchants.BOOKS
import me.weiwen.blanktopia.enchants.CustomEnchantment
import me.weiwen.blanktopia.enchants.NONE
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.inventory.ItemStack

val SOULBOUND = CustomEnchantment(
    "soulbound",
    "Soulbound",
    1,
    ALL + BOOKS,
    NONE,
    0.05,
    30,
    30,
    15,
    { setOf(Enchantment.VANISHING_CURSE, Enchantment.BINDING_CURSE) },
    Soulbound
)

object Soulbound : Listener {
    init {
    }

    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent): Boolean {
        if (event.keepInventory) {
            return false
        }

        val items = event.entity.inventory.contents?.map {
            if (it != null && (it.enchantments.containsKey(SOULBOUND) ||
                        it.itemMeta?.lore?.contains(ChatColor.GRAY.toString() + "Soulbound") == true) // compat
            ) {
                event.drops.remove(it)
                it
            } else {
                ItemStack(Material.AIR)
            }
        } ?: return true

        event.keepInventory = true
        event.entity.inventory.setContents(items.toTypedArray())
        return true
    }
}

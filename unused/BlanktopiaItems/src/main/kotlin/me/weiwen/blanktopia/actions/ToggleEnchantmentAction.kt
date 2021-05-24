package me.weiwen.blanktopia.actions

import me.weiwen.blanktopia.enchants.disenchant
import me.weiwen.blanktopia.enchants.enchant
import me.weiwen.blanktopia.playSoundAt
import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class ToggleEnchantmentAction(private val enchantment: Enchantment, private val level: Int, private val name: String) : Action {
    override fun run(player: Player, item: ItemStack) {
        if (item.enchantments.containsKey(enchantment)) {
            item.disenchant(enchantment)
            player.sendActionBar("${ChatColor.RED}Disabled ${name}.")
        } else {
            item.enchant(enchantment, level)
            player.sendActionBar("${ChatColor.GREEN}Enabled ${name}.")
        }
        player.playSoundAt(Sound.ITEM_ARMOR_EQUIP_NETHERITE, SoundCategory.PLAYERS, 1.0f, 1.0f)
    }
}
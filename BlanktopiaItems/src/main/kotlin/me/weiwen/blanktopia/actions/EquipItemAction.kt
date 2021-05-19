package me.weiwen.blanktopia.actions

import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

class EquipItemAction(private val slot: EquipmentSlot) : Action {
    override fun run(player: Player, item: ItemStack) {
        val equippedItem: ItemStack? = player.inventory.getItem(slot)
        if (equippedItem == item) {
            player.inventory.setItem(slot, null)
            player.inventory.addItem(item)
        } else if (equippedItem == null) {
            player.inventory.removeItem(item)
            player.inventory.setItem(slot, item)
        }
    }
}

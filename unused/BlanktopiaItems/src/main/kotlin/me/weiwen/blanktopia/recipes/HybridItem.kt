package me.weiwen.blanktopia.recipes

import me.weiwen.blanktopia.BlanktopiaItems
import me.weiwen.blanktopia.items.getCustomItem
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

data class HybridItemStack(val item: HybridItem, val amount: Int)
fun HybridItemStack.build(): ItemStack? {
    return when (item) {
        is Vanilla -> ItemStack(item.material, amount)
        is Custom -> BlanktopiaItems.INSTANCE.customItems.buildItem(item.type)
    }
}

sealed class HybridItem
data class Vanilla(val material: Material) : HybridItem()
data class Custom(val type: String) : HybridItem()

fun ItemStack.toHybridItem(): HybridItem {
    val customItem = getCustomItem()
    if (customItem != null) {
        return Custom(customItem.type)
    } else {
        return Vanilla(type)
    }
}

package me.weiwen.blanktopia.items

import org.bukkit.configuration.ConfigurationSection

class CustomItemType(val type: String, config: ConfigurationSection) {
    var leftClickBlock: CustomItemAction? = null
    var rightClickBlock: CustomItemAction? = null
    var leftClickAir: CustomItemAction? = null
    var rightClickAir: CustomItemAction? = null

    init {
        config.getConfigurationSection("left-click")?.let {
            leftClickBlock = CustomItemAction(it)
            leftClickAir = leftClickBlock
        }
        config.getConfigurationSection("right-click")?.let {
            rightClickBlock = CustomItemAction(it)
            rightClickAir = rightClickBlock
        }
    }
}


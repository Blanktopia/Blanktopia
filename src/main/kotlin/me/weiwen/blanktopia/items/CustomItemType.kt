package me.weiwen.blanktopia.items

import me.weiwen.blanktopia.Blanktopia
import me.weiwen.blanktopia.enchants.CustomEnchantment
import me.weiwen.blanktopia.enchants.enchant
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

class CustomItemType(val type: String, config: ConfigurationSection) {
    val material: Material = Material.matchMaterial(config.getString("material") ?: "STICK") ?: Material.STICK
    val name: String? = config.getString("name")
    val lore: List<String>? = config.getStringList("lore")
    val enchantments: MutableMap<Enchantment, Int> = mutableMapOf()
    var leftClickBlock: CustomItemAction? = null
    var rightClickBlock: CustomItemAction? = null
    var leftClickAir: CustomItemAction? = null
    var rightClickAir: CustomItemAction? = null

    init {
        config.getConfigurationSection("enchantments")?.let {
            for ((name, level) in it.getValues(false)) {
                val enchant = Enchantment.getByKey(NamespacedKey.minecraft(name)) ?: Enchantment.getByKey(
                    NamespacedKey(
                        Blanktopia.INSTANCE,
                        name
                    )
                )
                if (enchant != null) enchantments[enchant] = level as Int
            }
        }
        config.getConfigurationSection("left-click")?.let {
            leftClickBlock = CustomItemAction(it)
            leftClickAir = leftClickBlock
        }
        config.getConfigurationSection("right-click")?.let {
            rightClickBlock = CustomItemAction(it)
            rightClickAir = rightClickBlock
        }
    }

    fun build(): ItemStack {
        val item = ItemStack(material)
        val meta = item.itemMeta ?: Bukkit.getItemFactory().getItemMeta(material)!!
        if (name != null) meta.setDisplayName(name)
        if (lore != null) meta.lore = lore
        meta.persistentDataContainer.set(NamespacedKey(Blanktopia.INSTANCE, "type"), PersistentDataType.STRING, type)
        item.itemMeta = meta
        for ((enchant, level) in enchantments) {
            item.enchant(enchant, level)
        }
        return item
    }
}


package me.weiwen.blanktopia.items

import me.weiwen.blanktopia.Blanktopia
import me.weiwen.blanktopia.enchants.CustomEnchantment
import me.weiwen.blanktopia.enchants.enchant
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

class CustomItemType(val type: String, config: ConfigurationSection) {
    val material: Material = Material.matchMaterial(config.getString("material") ?: "STICK") ?: Material.STICK
    val name: String? = config.getString("name")
    val lore: List<String>? = config.getStringList("lore")
    val enchantments: MutableMap<Enchantment, Int> = mutableMapOf()
    val unbreakable: Boolean = config.getBoolean("unbreakable")
    var flags: MutableList<ItemFlag> = mutableListOf()
    var leftClickBlock: CustomItemAction? = null
    var rightClickBlock: CustomItemAction? = null
    var leftClickAir: CustomItemAction? = null
    var rightClickAir: CustomItemAction? = null
    var equipArmor: CustomItemAction? = null
    var unequipArmor: CustomItemAction? = null

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
        config.getConfigurationSection("flags")?.let { flags ->
            if (flags.getBoolean("hide-enchants")) this.flags.add(ItemFlag.HIDE_ENCHANTS)
            if (flags.getBoolean("hide-attributes")) this.flags.add(ItemFlag.HIDE_ATTRIBUTES)
            if (flags.getBoolean("hide-unbreakable")) this.flags.add(ItemFlag.HIDE_UNBREAKABLE)
            if (flags.getBoolean("hide-destroys")) this.flags.add(ItemFlag.HIDE_DESTROYS)
            if (flags.getBoolean("hide-placed-on")) this.flags.add(ItemFlag.HIDE_PLACED_ON)
            if (flags.getBoolean("hide-potion-effects")) this.flags.add(ItemFlag.HIDE_POTION_EFFECTS)
        }
        config.getConfigurationSection("actions")?.let { actions ->
            actions.getConfigurationSection("left-click")?.let {
                leftClickBlock = CustomItemAction(it)
                leftClickAir = leftClickBlock
            }
            actions.getConfigurationSection("right-click")?.let {
                rightClickBlock = CustomItemAction(it)
                rightClickAir = rightClickBlock
            }
            actions.getConfigurationSection("equip-armor")?.let {
                equipArmor = CustomItemAction(it)
            }
            actions.getConfigurationSection("unequip-armor")?.let {
                unequipArmor = CustomItemAction(it)
            }
        }
    }

    fun build(): ItemStack {
        val item = ItemStack(material)
        val meta = item.itemMeta ?: Bukkit.getItemFactory().getItemMeta(material)!!
        if (name != null) meta.setDisplayName(name)
        if (lore != null) meta.lore = lore
        if (unbreakable) meta.isUnbreakable = true
        meta.persistentDataContainer.set(NamespacedKey(Blanktopia.INSTANCE, "type"), PersistentDataType.STRING, type)
        item.itemMeta = meta
        for ((enchant, level) in enchantments) {
            item.enchant(enchant, level)
        }
        return item
    }
}


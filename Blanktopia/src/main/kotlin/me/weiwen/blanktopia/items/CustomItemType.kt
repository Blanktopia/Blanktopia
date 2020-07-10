package me.weiwen.blanktopia.items

import me.weiwen.blanktopia.Blanktopia
import me.weiwen.blanktopia.enchants.enchant
import me.weiwen.blanktopia.playerHeadFromUrl
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import java.util.*

class CustomItemType(val type: String, config: ConfigurationSection) {
    val material: Material = Material.matchMaterial(config.getString("material") ?: "STICK") ?: Material.STICK
    val name: String? = config.getString("name")
    val head: String? = config.getString("head")
    val lore: List<String>? = config.getStringList("lore")
    val enchantments: MutableMap<Enchantment, Int> = mutableMapOf()
    val attributeModifiers: MutableList<Pair<Attribute, AttributeModifier>> = mutableListOf()
    val unbreakable: Boolean = config.getBoolean("unbreakable")
    var flags: MutableList<ItemFlag> = mutableListOf()
    var leftClickBlock: CustomItemAction? = null
    var rightClickBlock: CustomItemAction? = null
    var leftClickAir: CustomItemAction? = null
    var rightClickAir: CustomItemAction? = null
    var rightClickEntity: CustomItemAction? = null
    var shiftLeftClickBlock: CustomItemAction? = null
    var shiftRightClickBlock: CustomItemAction? = null
    var shiftLeftClickAir: CustomItemAction? = null
    var shiftRightClickAir: CustomItemAction? = null
    var shiftRightClickEntity: CustomItemAction? = null
    var breakBlock: CustomItemAction? = null
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
        config.getMapList("attribute-modifiers")?.forEach {
            attributeModifiers.add(Pair(
                Attribute.valueOf(it["attribute"] as String),
                AttributeModifier(
                    UUID.fromString(it["uuid"] as String),
                    it["name"] as String,
                    it["amount"] as Double,
                    AttributeModifier.Operation.valueOf(it["operation"] as String),
                    EquipmentSlot.valueOf(it["slot"] as String)
                )
            ))
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
                shiftLeftClickBlock = CustomItemAction(it)
                shiftLeftClickAir = shiftLeftClickBlock
            }
            actions.getConfigurationSection("right-click")?.let {
                rightClickBlock = CustomItemAction(it)
                rightClickAir = rightClickBlock
                rightClickEntity = rightClickBlock
                shiftRightClickBlock = CustomItemAction(it)
                shiftRightClickAir = shiftRightClickBlock
                shiftRightClickEntity = shiftRightClickBlock
            }
            actions.getConfigurationSection("left-click-block")?.let {
                leftClickBlock = CustomItemAction(it)
                shiftLeftClickBlock = CustomItemAction(it)
            }
            actions.getConfigurationSection("left-click-air")?.let {
                leftClickAir = CustomItemAction(it)
                shiftLeftClickAir = CustomItemAction(it)
            }
            actions.getConfigurationSection("right-click-block")?.let {
                rightClickBlock = CustomItemAction(it)
                shiftRightClickBlock = CustomItemAction(it)
            }
            actions.getConfigurationSection("right-click-air")?.let {
                rightClickAir = CustomItemAction(it)
                shiftRightClickAir = CustomItemAction(it)
            }
            actions.getConfigurationSection("right-click-entity")?.let {
                rightClickEntity = CustomItemAction(it)
                shiftRightClickEntity = CustomItemAction(it)
            }
            actions.getConfigurationSection("shift-left-click")?.let {
                shiftLeftClickBlock = CustomItemAction(it)
                shiftLeftClickAir = shiftLeftClickBlock
            }
            actions.getConfigurationSection("shift-right-click")?.let {
                shiftRightClickBlock = CustomItemAction(it)
                shiftRightClickAir = shiftRightClickBlock
                shiftRightClickEntity = shiftRightClickBlock
            }
            actions.getConfigurationSection("shift-left-click-block")?.let {
                shiftLeftClickBlock = CustomItemAction(it)
            }
            actions.getConfigurationSection("shift-left-click-air")?.let {
                shiftLeftClickAir = CustomItemAction(it)
            }
            actions.getConfigurationSection("shift-right-click-block")?.let {
                shiftRightClickBlock = CustomItemAction(it)
            }
            actions.getConfigurationSection("shift-right-click-air")?.let {
                shiftRightClickAir = CustomItemAction(it)
            }
            actions.getConfigurationSection("shift-right-click-entity")?.let {
                shiftRightClickEntity = CustomItemAction(it)
            }
            actions.getConfigurationSection("break-block")?.let {
                breakBlock = CustomItemAction(it)
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
        val item = if (head != null) {
            playerHeadFromUrl(name ?: "Anonymous", head)
        } else {
            ItemStack(material)
        }
        val meta = item.itemMeta ?: Bukkit.getItemFactory().getItemMeta(material)!!
        if (name != null) meta.setDisplayName(name)
        if (lore != null) meta.lore = lore
        if (unbreakable) meta.isUnbreakable = true
        meta.persistentDataContainer.set(NamespacedKey(Blanktopia.INSTANCE, "type"), PersistentDataType.STRING, type)
        for ((attribute, attributeModifier) in  attributeModifiers) {
            meta.addAttributeModifier(attribute, attributeModifier)
        }
        item.itemMeta = meta
        for ((enchant, level) in enchantments) {
            item.enchant(enchant, level)
        }
        return item
    }
}

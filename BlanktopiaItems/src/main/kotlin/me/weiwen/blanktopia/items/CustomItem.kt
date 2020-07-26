package me.weiwen.blanktopia.items

import me.weiwen.blanktopia.*
import me.weiwen.blanktopia.actions.Action
import me.weiwen.blanktopia.enchants.enchant
import me.weiwen.blanktopia.triggers.Trigger
import me.weiwen.blanktopia.triggers.TriggerType
import me.weiwen.blanktopia.triggers.parseTriggers
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

class CustomItem(val type: String, config: ConfigurationSection) {
    private val material: Material = Material.matchMaterial(config.getStringOrError("material") ?: "STICK") ?: Material.STICK
    private val name: String? = config.getString("name")
    private val head: String? = config.getString("head")
    private val lore: List<String>? = config.getStringList("lore")
    private val enchantments: MutableMap<Enchantment, Int> = mutableMapOf()
    private val attributeModifiers: MutableList<Pair<Attribute, AttributeModifier>> = mutableListOf()
    private val unbreakable: Boolean = config.getBoolean("unbreakable")
    private val flags: Set<ItemFlag> = config.getConfigurationSection("flags")?.let {
        val flags: MutableSet<ItemFlag> = mutableSetOf()
        if (it.getBoolean("hide-enchants")) flags.add(ItemFlag.HIDE_ENCHANTS)
        if (it.getBoolean("hide-attributes")) flags.add(ItemFlag.HIDE_ATTRIBUTES)
        if (it.getBoolean("hide-unbreakable")) flags.add(ItemFlag.HIDE_UNBREAKABLE)
        if (it.getBoolean("hide-destroys")) flags.add(ItemFlag.HIDE_DESTROYS)
        if (it.getBoolean("hide-placed-on")) flags.add(ItemFlag.HIDE_PLACED_ON)
        if (it.getBoolean("hide-potion-effects")) flags.add(ItemFlag.HIDE_POTION_EFFECTS)
        flags
    } ?: setOf()
    val triggers: Map<TriggerType, List<Trigger>> = config.getList("triggers")?.let {
        val map: MutableMap<TriggerType, MutableList<Trigger>> = mutableMapOf()
        parseTriggers(it as List<Node>).forEach { trigger ->
            trigger.triggerTypes.forEach {  type ->
                if (map[type] != null) {
                    map[type]?.add(trigger)
                } else {
                    map[type] = mutableListOf(trigger)
                }
            }
        }
        map
    } ?: mapOf()

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
        config.getMapList("attribute-modifiers").forEach {
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


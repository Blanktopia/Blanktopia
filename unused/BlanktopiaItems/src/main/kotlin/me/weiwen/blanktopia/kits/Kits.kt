package me.weiwen.blanktopia.kits

import me.weiwen.blanktopia.Blanktopia
import me.weiwen.blanktopia.Module
import me.weiwen.blanktopia.enchants.enchant
import me.weiwen.blanktopia.items.CustomItems
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Sound
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

class Kits(val plugin: JavaPlugin, val customItems: CustomItems) : Listener, Module {
    private var kits = mapOf<String, List<Pair<EquipmentSlot?, ItemStack>>>()

    override fun enable() {
        plugin.server.pluginManager.registerEvents(this, plugin)
        reload()

        val command = plugin.getCommand("wkit")
        command?.setExecutor { sender, _, _, args ->
            val name = args[0] ?: return@setExecutor false
            val player = if (args[1] != null) plugin.server.getPlayer(args[1]) else sender as? Player
            if (player == null) return@setExecutor false
            giveKit(player, name)
            player.playSound(player.location, Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 1.0f)
            true
        }
    }

    override fun disable() {}

    override fun reload() {
        val config = plugin.config.getConfigurationSection("kits")!!
        kits = populateKits(config)
    }

    fun giveKit(player: Player, name: String) {
        if (kits[name] != null) {
            for ((slot, item) in kits[name]!!) {
                when (slot) {
                    EquipmentSlot.HEAD -> if (player.inventory.helmet == null) {
                        player.inventory.helmet = item.clone()
                    } else {
                        player.inventory.addItem(item.clone())
                    }
                    EquipmentSlot.CHEST -> if (player.inventory.chestplate == null) {
                        player.inventory.chestplate = item.clone()
                    } else {
                        player.inventory.addItem(item.clone())
                    }
                    EquipmentSlot.LEGS -> if (player.inventory.leggings == null) {
                        player.inventory.leggings = item.clone()
                    } else {
                        player.inventory.addItem(item.clone())
                    }
                    EquipmentSlot.FEET -> if (player.inventory.boots == null) {
                        player.inventory.boots = item.clone()
                    } else {
                        player.inventory.addItem(item.clone())
                    }
                    EquipmentSlot.OFF_HAND -> if (player.inventory.itemInOffHand.type == Material.AIR) {
                        player.inventory.setItemInOffHand(item.clone())
                    } else {
                        player.inventory.addItem(item.clone())
                    }

                    null -> player.inventory.addItem(item.clone())
                }
            }
        }
    }

    private fun populateKits(config: ConfigurationSection): Map<String, List<Pair<EquipmentSlot?, ItemStack>>> {
        val kits = mutableMapOf<String, List<Pair<EquipmentSlot?, ItemStack>>>()
        for (name in config.getKeys(false)) {
            val kitConfig = config.getMapList(name)
            val kit = mutableListOf<Pair<EquipmentSlot?, ItemStack>>()
            for (itemConfig in kitConfig) {
                val customItem = itemConfig["custom-item"] as? String
                if (customItem != null) {
                    customItems.buildItem(customItem)?.let {
                        it.amount = itemConfig["amount"] as? Int ?: 1
                        kit.add(Pair(null, it))
                    }
                } else {
                    val slot = (itemConfig["slot"] as? String? ?: null)?.let { EquipmentSlot.valueOf(it) }
                    val material: Material =
                        Material.matchMaterial(itemConfig["material"] as? String ?: "STICK") ?: Material.STICK
                    val amount: Int = itemConfig["amount"] as? Int ?: 1
                    val item = ItemStack(material, amount)
                    val meta = item.itemMeta ?: Bukkit.getItemFactory().getItemMeta(material)!!
                    meta.setDisplayName(itemConfig["name"] as? String)
                    meta.lore = itemConfig["lore"] as? List<String>
                    item.itemMeta = meta
                    val enchantmentsConfig = itemConfig["enchantments"] as? Map<String, Int>
                    if (enchantmentsConfig != null) {
                        for ((enchantName, level) in enchantmentsConfig.entries) {
                            val enchant =
                                Enchantment.getByKey(NamespacedKey.minecraft(enchantName)) ?: Enchantment.getByKey(
                                    NamespacedKey(
                                        Blanktopia.INSTANCE,
                                        enchantName
                                    )
                                )
                            if (enchant != null) {
                                item.enchant(enchant, level)
                            }
                        }
                    }
                    kit.add(Pair(slot, item))
                }
            }
            kits[name] = kit
        }
        return kits
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        if (!event.player.hasPlayedBefore()) {
            giveKit(event.player, "starter")
        }
    }

}

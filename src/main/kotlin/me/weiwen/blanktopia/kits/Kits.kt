package me.weiwen.blanktopia.kits

import me.weiwen.blanktopia.Blanktopia
import me.weiwen.blanktopia.Module
import me.weiwen.blanktopia.enchants.CustomEnchantment
import me.weiwen.blanktopia.enchants.enchant
import me.weiwen.blanktopia.items.CustomItems
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.ItemStack

class Kits(val plugin: Blanktopia, val customItems: CustomItems) : Listener, Module {
    private var kits = mapOf<String, List<ItemStack>>()

    override fun enable() {
        plugin.server.pluginManager.registerEvents(this, plugin)
        reload()

        val command = plugin.getCommand("wkit")
        command?.setExecutor { sender, _, _, args ->
            val name = args[0] ?: return@setExecutor false
            val player = if (args[1] != null) plugin.server.getPlayer(args[1]) else sender as? Player
            if (player == null) return@setExecutor false
            giveKit(player, name)
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
            for (item in kits[name]!!) {
                player.inventory.addItem(item.clone())
            }
        }
    }

    private fun populateKits(config: ConfigurationSection): Map<String, List<ItemStack>> {
        val kits = mutableMapOf<String, List<ItemStack>>()
        for (name in config.getKeys(false)) {
            val kitConfig = config.getMapList(name)
            val kit = mutableListOf<ItemStack>()
            for (itemConfig in kitConfig) {
                val customItem = itemConfig["custom-item"] as? String
                if (customItem != null) {
                    customItems.buildItem(customItem)?.let { kit.add(it) }
                } else {
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
                                        plugin,
                                        enchantName
                                    )
                                )
                            if (enchant != null) {
                                item.enchant(enchant, level)
                            }
                        }
                    }
                    kit.add(item)
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

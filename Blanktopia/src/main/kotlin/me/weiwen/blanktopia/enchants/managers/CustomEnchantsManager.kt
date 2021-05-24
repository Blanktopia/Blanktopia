package me.weiwen.blanktopia.enchants.managers

import me.weiwen.blanktopia.enchants.Blanktopia
import me.weiwen.blanktopia.enchants.disenchant
import me.weiwen.blanktopia.enchants.enchant
import me.weiwen.blanktopia.enchants.enchantments.*
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import java.lang.reflect.Field
import java.util.*

val ENCHANTMENTS = setOf(
    BEHEADING,
    FINAL,
    FROST,
    HARVEST,
    NIGHT_VISION,
    PARRY,
    RUSH,
    SMELT,
    SNIPER,
    SOULBOUND,
    SPECTRAL,
    SPRING,
    STING,
    STRIDE
)

class CustomEnchantsManager(private val plugin: Blanktopia) :
    Listener {

    fun enable() {
        plugin.server.pluginManager.registerEvents(this, plugin)

        registerCommands()
        registerEnchantments()

        NightVision.enable(plugin)
    }

    fun disable() {}

    fun reload() {}

    private fun registerCommands() {
        val command = plugin.getCommand("wenchant")
        command?.setExecutor { sender, _, _, args ->
            if (sender is Player) {
                val enchantName = args.getOrNull(0) ?: return@setExecutor false
                val enchant = ENCHANTMENTS.filter { it.key.toString().startsWith(enchantName) }.firstOrNull()
                if (enchant != null) {
                    val level =
                        args.getOrNull(1)?.toIntOrNull() ?: enchant.maxLevel
                    if (level == 0) {
                        sender.inventory.itemInMainHand.disenchant(enchant)
                    } else {
                        sender.inventory.itemInMainHand.enchant(enchant, level)
                    }
                } else {
                    sender.sendMessage("Unknown enchantment.")
                }
                true
            } else {
                false
            }
        }
        command?.setTabCompleter { _, _, _, args ->
            when (args.size) {
                1 -> ENCHANTMENTS.map { it.key.toString() }
                2 -> listOf("0", "1")
                else -> listOf()
            }
        }
    }

    private fun registerEnchantments() {
        unregisterEnchantments()
        try {
            val f: Field = Enchantment::class.java.getDeclaredField("acceptingNew")
            f.setAccessible(true)
            f.set(null, true)
            for (enchant in ENCHANTMENTS) {
                Enchantment.registerEnchantment(enchant)
                plugin.server.pluginManager.registerEvents(enchant.listener, plugin)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        plugin.logger.info("Registered custom enchantments")
    }

    private fun unregisterEnchantments() {
        try {
            val keyField = Enchantment::class.java.getDeclaredField("byKey")
            keyField.isAccessible = true
            val byKey = keyField[null] as HashMap<NamespacedKey, Enchantment?>
            for (enchant in ENCHANTMENTS) {
                if (byKey.containsKey(enchant.key)) {
                    byKey.remove(enchant.key)
                }
            }
            val nameField = Enchantment::class.java.getDeclaredField("byName")
            nameField.isAccessible = true
            val byName =
                nameField[null] as HashMap<String, Enchantment?>
            for (enchant in ENCHANTMENTS) {
                if (byName.containsKey(enchant.name)) {
                    byName.remove(enchant.name)
                }
            }
        } catch (ignored: java.lang.Exception) {
        }
    }

}

package me.weiwen.blanktopia.books

import de.themoep.minedown.MineDown
import me.weiwen.blanktopia.Blanktopia
import me.weiwen.blanktopia.Trie
import me.weiwen.blanktopia.enchants.enchantments.SOULBOUND
import me.weiwen.blanktopia.items.makeCustom
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BookMeta

class Books(val plugin: Blanktopia) : Listener {
    private val config = plugin.config.getConfigurationSection("books")!!
    private val books = populateBooks()
    private val bookTitles = populateBookTitles()

    init {
        plugin.server.pluginManager.registerEvents(this, plugin)
        val command = plugin.getCommand("help")
        command?.setExecutor { sender, _, _, args ->
            val book = books[if (args.size > 0) args.joinToString(" ") else "help"]
            if (sender is Player && book != null) {
                sender.openBook(book)
                sender.playSound(sender.location, "item.book.page_turn", 1.0f, 1.0f)
                true
            } else {
                false
            }
        }
        command?.setTabCompleter {
            _, _, _, args ->
            var node = bookTitles
            for (word in args) {
                if (word.isBlank()) continue
                for (char in word) {
                    node = node[char] ?: return@setTabCompleter node.toList()
                }
                node = node[' '] ?: return@setTabCompleter node.toList()
            }
            node.toList()
        }
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val storage = plugin.storage.player(event.player)
        if (storage.get("received-guidebook") != true) {
            event.player.inventory.addItem(ItemStack(Material.KNOWLEDGE_BOOK).apply {
                val meta = itemMeta
                meta?.setDisplayName(ChatColor.BLUE.toString() + "Beginner's Guide")
                itemMeta = meta
                SOULBOUND.enchantItem(this, 1)
                makeCustom("guidebook")
            })
            storage.set("received-guidebook", true)
        }
    }

    private fun populateBooks(): Map<String, ItemStack> {
        val title = config.getString("title")
        val author = config.getString("author")
        val books = mutableMapOf<String, ItemStack>()

        val pages = config.getConfigurationSection("pages")
        if (pages == null) {
            plugin.logger.warning("No pages found. Is the configuration set correctly?")
            return mapOf()
        }
        for (key in pages.getKeys(false)) {
            books[key] = ItemStack(Material.WRITTEN_BOOK).apply {
                val meta = Bukkit.getItemFactory().getItemMeta(Material.WRITTEN_BOOK) as BookMeta
                for (page in pages.getStringList(key)) {
                    meta.spigot().addPage(MineDown.parse(page))
                }
                meta.title = title
                meta.author = author
                itemMeta = meta
            }
        }
        return books
    }

    private fun populateBookTitles(): Trie<Char, String> {
        val trie = Trie<Char, String>(null)
        val pages = config.getConfigurationSection("pages")
        if (pages == null) {
            plugin.logger.warning("No pages found. Is the configuration set correctly?")
            return trie
        }
        for (key in pages.getKeys(false)) {
            for (word in key.split(" ")) {
                var node = trie
                for (char in key) {
                    if (node[char] == null) {
                        node[char] = Trie(null)
                    }
                    node = node[char]!!
                }
                node.value = word
            }
        }
        return trie
    }
}

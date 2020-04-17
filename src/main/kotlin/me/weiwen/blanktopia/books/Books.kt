package me.weiwen.blanktopia.books

import de.themoep.minedown.MineDown
import me.weiwen.blanktopia.Blanktopia
import me.weiwen.blanktopia.Module
import me.weiwen.blanktopia.Trie
import me.weiwen.blanktopia.items.CustomItems
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BookMeta

class Books(val plugin: Blanktopia, val customItems: CustomItems) : Listener, Module {
    private val config = plugin.config.getConfigurationSection("books")!!
    private lateinit var books: Map<String, ItemStack>
    private lateinit var bookTitles: Trie<Char, String>

    override fun enable() {
        books = populateBooks()
        bookTitles = populateBookTitles()
        plugin.server.pluginManager.registerEvents(this, plugin)

        // Help
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

        // Rules
        plugin.getCommand("rules")?.setExecutor { sender, _, _, _ ->
            val book = books["rules"]
            if (sender is Player && book != null) {
                sender.openBook(book)
                sender.playSound(sender.location, "item.book.page_turn", 1.0f, 1.0f)
                true
            } else {
                false
            }
        }

        // About
        plugin.getCommand("about")?.setExecutor { sender, _, _, _ ->
            val book = books["about"]
            if (sender is Player && book != null) {
                sender.openBook(book)
                sender.playSound(sender.location, "item.book.page_turn", 1.0f, 1.0f)
                true
            } else {
                false
            }
        }

        // Shop
        plugin.getCommand("shop")?.setExecutor { sender, _, _, _ ->
            val book = books["shop"]
            if (sender is Player && book != null) {
                sender.openBook(book)
                sender.playSound(sender.location, "item.book.page_turn", 1.0f, 1.0f)
                true
            } else {
                false
            }
        }

        // Ranks
        plugin.getCommand("ranks")?.setExecutor { sender, _, _, _ ->
            val book = books["ranks"]
            if (sender is Player && book != null) {
                sender.openBook(book)
                sender.playSound(sender.location, "item.book.page_turn", 1.0f, 1.0f)
                true
            } else {
                false
            }
        }
    }

    override fun disable() {}

    override fun reload() {
        books = populateBooks()
        bookTitles = populateBookTitles()
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

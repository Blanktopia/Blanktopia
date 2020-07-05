import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("net.minecrell.plugin-yml.bukkit")
    id("com.github.johnrengelman.shadow")
}
val kotlin_version: String by project
val bukkit_version: String by project
val spigot_version: String by project
val paper_version: String by project

val plugin_group: String by project
val plugin_version: String by project

group = plugin_group
version = plugin_version

repositories {
    jcenter()
    maven { url = uri("https://papermc.io/repo/repository/maven-public/") }
    maven { url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") }
    maven { url = uri("http://repo.minebench.de/")}
    maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots/") }
    maven { url = uri("https://jitpack.io") }
    maven { url = uri("https://repo.dmulloy2.net/nexus/repository/public/") }
    maven { url = uri("http://repo.md-5.net/content/repositories/releases/") }
}

dependencies {
    compile(kotlin("stdlib-jdk8", kotlin_version))

    compileOnly("com.destroystokyo.paper:paper-api:$paper_version")
    compileOnly("org.spigotmc:spigot-api:$spigot_version")
    compileOnly("com.github.TechFortress:GriefPrevention:16.7.1")
    compileOnly("LibsDisguises:LibsDisguises:10.0.12")
    compile("de.themoep:minedown:1.6.1-SNAPSHOT")
    compile("io.papermc:paperlib:1.0.2")
}

val plugin_main: String by project
val plugin_name: String by project
val plugin_description: String by project
val plugin_api_version: String by project
val plugin_author: String by project
val plugin_website: String by project

bukkit {
    main = plugin_main
    name = plugin_name
    version = plugin_version
    description = plugin_description
    apiVersion = plugin_api_version
    author = plugin_author
    website = plugin_website
    depend = listOf("GriefPrevention")
    softDepend = listOf("LibsDisguises")
    loadBefore = listOf("GoldenCrates")
    commands {
        register("blanktopia") {
            description = "Manages the Blanktopia plugin"
            usage = "/<command>"
            permission = "blanktopia.admin"
        }
        register("help") {
            description = "Find out more about Blanktopia!"
            usage = "/<command> <page>"
        }
        register("rules") {
            description = "Read the rules of the server."
            usage = "/<command>"
        }
        register("about") {
            description = "Find out what makes Blanktopia unique!"
            usage = "/<command>"
        }
        register("shop") {
            description = "Learn how to set up your own shops!"
            usage = "/<command>"
        }
        register("ranks") {
            description = "Rank up and gain more perks!"
            usage = "/<command>"
        }
        register("wenchant") {
            description = "Enchants held item with custom enchantments"
            usage = "/<command> <enchant> [level]"
            permission = "blanktopia.enchant"
        }
        register("witem") {
            description = "Gives the player a custom item"
            usage = "/<command> <type>"
            permission = "blanktopia.item"
        }
        register("whead") {
            description = "Gives the player a custom player head"
            usage = "/<command> <name> <base64>"
            permission = "blanktopia.head"
        }
        register("wkit") {
            description = "Gives user a kit"
            usage = "/<command> <kit> [player]"
            permission = "blanktopia.kit"
        }
        register("wserialize") {
            description = "Serializes the player's held item and writes it to serialized.yml"
            usage = "/<command>"
            permission = "blanktopia.serialize"
        }
    }
    permissions {
        register("blanktopia.admin") { }
        register("blanktopia.enchant") { }
        register("blanktopia.item") { }
        register("blanktopia.kit") { }
        register("blanktopia.serialize") { }
        register("blanktopia.help") {
            default = net.minecrell.pluginyml.bukkit.BukkitPluginDescription.Permission.Default.TRUE
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<ShadowJar> {
    classifier = null
}
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
}

dependencies {
    compile(kotlin("stdlib-jdk8", kotlin_version))

    compileOnly("com.destroystokyo.paper:paper-api:$paper_version")
    compileOnly("org.spigotmc:spigot-api:$spigot_version")
    compileOnly("org.bukkit:bukkit:$bukkit_version")
    compile("de.themoep:minedown:1.5-SNAPSHOT")
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
        register("wenchant") {
            description = "Enchants held item with custom enchantments"
            usage = "/<command> <enchant> [level]"
            permission = "blanktopia.enchant"
        }
        register("witem") {
            description = "Makes the held item a custom item"
            usage = "/<command> <type>"
            permission = "blanktopia.item"
        }
    }
    permissions {
        register("blanktopia.admin") { }
        register("blanktopia.enchant") { }
        register("blanktopia.item") { }
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
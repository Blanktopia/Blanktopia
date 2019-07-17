import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("net.minecrell.plugin-yml.bukkit")
    id("com.github.johnrengelman.shadow")
}
val kotlin_version: String by project
val spigot_version: String by project

val plugin_group: String by project
val plugin_version: String by project

group = plugin_group
version = plugin_version

repositories {
    jcenter()
    maven { url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") }
    maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots/") }
}

dependencies {
    compile(kotlin("stdlib-jdk8", kotlin_version))

    compileOnly("org.spigotmc:spigot-api:$spigot_version")
}

val plugin_main: String by project
val plugin_name: String by project
val plugin_description: String by project
val plugin_author: String by project

bukkit {
    name = plugin_name
    version = plugin_version
    main = plugin_main
    author = plugin_author
    description = plugin_description
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<ShadowJar> {
    classifier = null
}
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.serialization") version "1.9.22"
    id("maven-publish")
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0" apply false
    id("io.github.goooler.shadow") version "8.1.7"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://repo.minebench.de/")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    maven("https://repo.dmulloy2.net/nexus/repository/public/")
    maven("https://repo.md-5.net/content/repositories/releases/")
    maven("https://ci.ender.zone/plugin/repository/everything/")
}

dependencies {
    subprojects
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.serialization")
    apply(plugin = "net.minecrell.plugin-yml.bukkit")

    dependencies {
        compileOnly(kotlin("stdlib"))
        compileOnly("io.papermc.paper:paper-api:1.21-R0.1-SNAPSHOT")
    }

    tasks.withType<ShadowJar> {
        fun reloc(pkg: String) = relocate(pkg, "$group.dependency.$pkg")

        reloc("org.bstats")
        reloc("de.themoep.minedown")
        reloc("cloud.commandframework")
        reloc("com.github.stefvanschie.inventoryframework")
    }
}

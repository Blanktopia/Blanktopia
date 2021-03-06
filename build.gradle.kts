import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.0" apply false
    id("net.minecrell.plugin-yml.bukkit") apply false
    id("com.github.johnrengelman.shadow") apply false
}

subprojects {
    apply {
        plugin("kotlin")
        plugin("net.minecrell.plugin-yml.bukkit")
        plugin("com.github.johnrengelman.shadow")
    }

    group = "me.weiwen.blanktopia"
    version = "1.0.0"

    apply {
        plugin("org.jetbrains.kotlin.jvm")
    }

    repositories {
        jcenter()
        mavenCentral()
        maven { url = uri("https://papermc.io/repo/repository/maven-public/") }
        maven { url = uri("https://repo.minebench.de/") }
        maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots/") }
        maven { url = uri("https://repo.dmulloy2.net/nexus/repository/public/") }
        maven { url = uri("https://repo.md-5.net/content/repositories/releases/") }
        maven { url = uri("https://ci.ender.zone/plugin/repository/everything/") }
        maven { url = uri("https://jitpack.io") }
        mavenLocal()
    }

    val implementation by configurations
    val compileOnly by configurations
    val api by configurations

    dependencies {
        compileOnly("io.papermc.paper:paper-api:1.17-R0.1-SNAPSHOT")
        compileOnly("net.kyori:adventure-api:4.8.1")
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    tasks.withType<ShadowJar> {
        classifier = null
    }
}

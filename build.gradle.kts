import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.mineinabyss.conventions.kotlin")
    id("com.mineinabyss.conventions.copyjar") apply false
    id("net.minecrell.plugin-yml.bukkit") version "0.5.2" apply false
    id("com.github.johnrengelman.shadow") version "7.1.0" apply false
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "com.mineinabyss.conventions.kotlin")
    apply(plugin = "com.mineinabyss.conventions.copyjar")
    apply(plugin = "net.minecrell.plugin-yml.bukkit")
    apply(plugin = "com.github.johnrengelman.shadow")


    tasks.withType<KotlinCompile> {
        kotlinOptions.freeCompilerArgs = listOf("-opt-in=kotlinx.serialization.ExperimentalSerializationApi")
    }

    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://repo.mineinabyss.com")
        maven("https://repo.minebench.de/")
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
        maven("https://repo.dmulloy2.net/nexus/repository/public/")
        maven("https://repo.md-5.net/content/repositories/releases/")
        maven("https://ci.ender.zone/plugin/repository/everything/")
    }


    dependencies {
        val libs = rootProject.libs

        implementation(libs.idofront.platform.loader)
        compileOnly(libs.kotlin.stdlib)
        compileOnly("io.papermc.paper:paper-api:1.19.2-R0.1-SNAPSHOT")
    }

    tasks.withType<ShadowJar> {
        classifier = null
    }
}

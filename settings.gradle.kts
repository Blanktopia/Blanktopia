rootProject.name = "Blanktopia"
include("Blanktopia")
include("BlanktopiaTweaks")
include("BlanktopiaPortals")

pluginManagement {
    repositories {
        gradlePluginPortal()
    }
    resolutionStrategy {
        eachPlugin {
            val id = requested.id.id

            if (id.startsWith("org.jetbrains.kotlin"))
                useVersion("1.6.0")

            if(id.startsWith("net.minecrell.plugin-yml.bukkit"))
                useVersion("0.5.1")

            if(id.startsWith("com.github.johnrengelman.shadow"))
                useVersion("7.1.0")
        }
    }
}


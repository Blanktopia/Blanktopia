rootProject.name = "Blanktopia"
include("BlanktopiaCore")
include("Blanktopia")
include("BlanktopiaTweaks")
include("BlanktopiaShop")
include("BlanktopiaPortals")

pluginManagement {
    repositories {
        gradlePluginPortal()
    }
    resolutionStrategy {
        eachPlugin {
            val id = requested.id.id

            if (id.startsWith("org.jetbrains.kotlin"))
                useVersion("1.3.72")

            if(id.startsWith("net.minecrell.plugin-yml.bukkit"))
                useVersion("0.3.0")

            if(id.startsWith("com.github.johnrengelman.shadow"))
                useVersion("5.2.0")
        }
    }
}


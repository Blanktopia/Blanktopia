rootProject.name = "Blanktopia"
include("BlanktopiaCore")
include("Blanktopia")
include("BlanktopiaItems")
include("BlanktopiaTweaks")
include("BlanktopiaShop")
include("BlanktopiaPortals")
include("BlanktopiaLikes")
include("BlanktopiaTutorial")
include("BlanktopiaFurniture")

pluginManagement {
    repositories {
        gradlePluginPortal()
    }
    resolutionStrategy {
        eachPlugin {
            val id = requested.id.id

            if (id.startsWith("org.jetbrains.kotlin"))
                useVersion("1.4.21")

            if(id.startsWith("net.minecrell.plugin-yml.bukkit"))
                useVersion("0.3.0")

            if(id.startsWith("com.github.johnrengelman.shadow"))
                useVersion("5.2.0")
        }
    }
}


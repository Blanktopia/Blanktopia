rootProject.name = "Blanktopia"

include(
    "BlanktopiaTweaks",
    "BlanktopiaPortals",
)

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}
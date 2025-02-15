rootProject.name = "Blanktopia"

include(
    "BlanktopiaTweaks",
    "BlanktopiaPortals",
)

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://papermc.io/repo/repository/maven-public/")
    }
}
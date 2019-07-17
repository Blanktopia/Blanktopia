val kotlin_version: String by settings
val plugin_yml_version: String by settings
val shadow_version: String by settings

val plugin_name: String by settings

rootProject.name = plugin_name

pluginManagement {
    repositories {
        gradlePluginPortal()
    }
    resolutionStrategy {
        eachPlugin {
            val id = requested.id.id

            if (id.startsWith("org.jetbrains.kotlin"))
                useVersion(kotlin_version)

            if(id.startsWith("net.minecrell.plugin-yml.bukkit"))
                useVersion(plugin_yml_version)

            if(id.startsWith("com.github.johnrengelman.shadow"))
                useVersion(shadow_version)
        }
    }
}


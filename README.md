# SpigotKotlinStarter
A Kotlin starter project for Spigot API.

The project embeds the **Kotlin stdlib** inside your plugin.

### Working with
- [**Shadow gradle plugin**](https://github.com/johnrengelman/shadow) for embed Kotlin stdlib inside the final jar
- [**Plugin-YML gradle plugin**](https://github.com/Minecrell/plugin-yml) for create Plugin.yml easy in ``build.gradle.kts`` using Kotlin
- **Kotlin** 1.3.41

## Configuration
All configuration of the project is in ``gradle.properties``

You can change the Kotlin version, Spigot version (actual 1.8.8), Plugin name, group, main class, version
```properties
kotlin_version=1.3.41

spigot_version=1.8.8-R0.1-SNAPSHOT

plugin_group=com.yourserver
plugin_main=com.yourserver.yourplugin.YourPlugin

plugin_name=YourPluginName
plugin_version=1.0.0
plugin_description=Your plugin description
plugin_author=Your name
```

## Build
```
./gradlew shadowJar
```

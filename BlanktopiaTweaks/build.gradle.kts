repositories {
    // ChestSort
    maven { url = uri("https://repo.jeff-media.com/public/") }

    // WorldEdit
    maven { url = uri("https://maven.enginehub.org/repo/") }
}

dependencies {
    compileOnly("de.jeff_media", "ChestSortAPI", "13.0.0-SNAPSHOT")
    compileOnly("com.sk89q.worldedit", "worldedit-bukkit", "7.3.0-SNAPSHOT")
}

bukkit {
    main = "me.weiwen.blanktopia.tweaks.BlanktopiaTweaks"
    name = "BlanktopiaTweaks"
    version = "1.0.0"
    description = "Blanktopia's custom tweaks"
    apiVersion = "1.21.4"
    author = "Goh Wei Wen <goweiwen@gmail.com>"
    website = "www.blanktopia.com"
    depend = listOf()
    softDepend = listOf("ChestSort")
    commands {
        register("blanktopiatweaks") {
            description = "Manages the Blanktopia plugin"
            usage = "/<command>"
            permission = "blanktopia.admin"
        }
    }
    permissions {
        register("blanktopia.admin") { }
    }
}

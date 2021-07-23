repositories {
    // ChestSort
    maven { url = uri("https://repo.jeff-media.de/maven2") }
}

dependencies {
    compileOnly("de.jeff_media", "ChestSortAPI", "11.0.0-SNAPSHOT")
}

bukkit {
    main = "me.weiwen.blanktopia.tweaks.BlanktopiaTweaks"
    name = "BlanktopiaTweaks"
    version = "1.0.0"
    description = "Blanktopia's custom tweaks"
    apiVersion = "1.16"
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

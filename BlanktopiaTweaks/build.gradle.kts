dependencies {
}

bukkit {
    main = "me.weiwen.blanktopia.BlanktopiaTweaks"
    name = "BlanktopiaTweaks"
    version = "1.0.0"
    description = "Blanktopia's custom tweaks"
    apiVersion = "1.16"
    author = "Goh Wei Wen <goweiwen@gmail.com>"
    website = "www.blanktopia.com"
    depend = listOf()
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

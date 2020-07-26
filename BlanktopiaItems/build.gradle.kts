dependencies {
    compileOnly(project(":BlanktopiaCore"))
    compileOnly(project(":Blanktopia"))
    compileOnly("LibsDisguises:LibsDisguises:10.0.15")
}

bukkit {
    main = "me.weiwen.blanktopia.BlanktopiaItems"
    name = "BlanktopiaItems"
    version = "1.0.0"
    description = "Blanktopia's custom plugin"
    apiVersion = "1.16"
    author = "Goh Wei Wen <goweiwen@gmail.com>"
    website = "www.blanktopia.com"
    depend = listOf("BlanktopiaCore")
    softDepend = listOf("LibsDisguises")
    commands {
        register("blanktopiaitems") {
            description = "Manages the BlanktopiaItems plugin"
            usage = "/<command> reload"
            permission = "blanktopia.admin"
        }
        register("witem") {
            description = "Gives the player a custom item"
            usage = "/<command> <type>"
            permission = "blanktopia.item"
        }
    }
    permissions {
        register("blanktopia.admin") { }
        register("blanktopia.item") { }
    }
}

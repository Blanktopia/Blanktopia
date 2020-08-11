dependencies {
    compileOnly(project(":BlanktopiaCore"))

    compileOnly("com.github.SkriptLang:Skript:3d49509")
    compileOnly("net.ess3:EssentialsX:2.17.2")
}

bukkit {
    main = "me.weiwen.blanktopia.BlanktopiaShop"
    name = "BlanktopiaShop"
    version = "1.0.0"
    description = "Blanktopia's custom plugin"
    apiVersion = "1.16"
    author = "Goh Wei Wen <goweiwen@gmail.com>"
    website = "www.blanktopia.com"
    depend = listOf("BlanktopiaCore")
    softDepend = listOf("Skript", "Essentials")
    commands {
        register("shopedit") {
            description = "Toggles shop edit mode"
            usage = "/<command>"
            permission = "blanktopia.shop.create"
        }
    }
    permissions {
        register("blanktopia.shop.create") { }
        register("blanktopia.shop.create.others") { }
        register("blanktopia.shop.break") { }
        register("blanktopia.shop.buy") { }
    }
}

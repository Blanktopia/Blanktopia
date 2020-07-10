dependencies {
    compileOnly(project(":BlanktopiaCore"))

    compileOnly("com.github.TechFortress:GriefPrevention:16.7.1")
    compileOnly("com.github.SkriptLang:Skript:3d49509")
}

bukkit {
    main = "me.weiwen.blanktopia.BlanktopiaShop"
    name = "BlanktopiaShop"
    version = "1.0.0"
    description = "Blanktopia's custom plugin"
    apiVersion = "1.16"
    author = "Goh Wei Wen <goweiwen@gmail.com>"
    website = "www.blanktopia.com"
    depend = listOf("BlanktopiaCore", "GriefPrevention")
    softDepend = listOf("Skript")
    permissions {
        register("blanktopia.shop.create") { }
        register("blanktopia.shop.break") { }
        register("blanktopia.shop.use") { }
    }
}

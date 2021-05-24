dependencies {
    implementation("io.papermc:paperlib:1.0.6")

    compileOnly("com.github.TechFortress:GriefPrevention:16.7.1")
}

bukkit {
    main = "me.weiwen.blanktopia.BlanktopiaPortals"
    name = "BlanktopiaPortals"
    version = "1.0.0"
    description = "Blanktopia's custom plugin"
    apiVersion = "1.16"
    author = "Goh Wei Wen <goweiwen@gmail.com>"
    website = "www.blanktopia.com"
    depend = listOf("GriefPrevention")
    permissions {
        register("blanktopia.portal.use") { }
        register("blanktopia.portal.create") { }
    }
}

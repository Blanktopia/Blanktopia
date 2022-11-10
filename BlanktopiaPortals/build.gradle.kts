repositories {
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://jitpack.io")
}

dependencies {
    implementation("io.papermc:paperlib:1.0.7")
    compileOnly("com.github.TechFortress:GriefPrevention:16.18")
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

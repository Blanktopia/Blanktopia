dependencies {
    implementation("com.zaxxer:HikariCP:3.4.5")
    implementation("org.mariadb.jdbc:mariadb-java-client:2.6.0")

    api("io.papermc:paperlib:1.0.2")

    compileOnly("com.github.TechFortress:GriefPrevention:16.7.1")
}

bukkit {
    main = "me.weiwen.blanktopia.BlanktopiaCore"
    name = "BlanktopiaCore"
    version = "1.0.0"
    description = "Blanktopia's custom plugin"
    apiVersion = "1.16"
    author = "Goh Wei Wen <goweiwen@gmail.com>"
    website = "www.blanktopia.com"
    depend = listOf("GriefPrevention")
}

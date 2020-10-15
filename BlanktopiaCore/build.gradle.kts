dependencies {
    api(kotlin("stdlib-jdk8", "1.4.0"))
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.9")
    api(kotlin("reflect", "1.4.0"))
    api("io.papermc:paperlib:1.0.2")
    api("de.themoep:minedown:1.6.1-SNAPSHOT")
    api("com.okkero.skedule:skedule:1.2.6")

    implementation("com.zaxxer:HikariCP:3.4.5")
    implementation("org.mariadb.jdbc:mariadb-java-client:2.6.0")

    compileOnly("com.github.TechFortress:GriefPrevention:16.7.1")
    compileOnly("org.spigotmc:spigot:1.16.3-R0.1-SNAPSHOT")
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

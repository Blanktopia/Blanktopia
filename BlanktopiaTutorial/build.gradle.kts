dependencies {
    compileOnly(project(":BlanktopiaCore"))
}

bukkit {
    main = "me.weiwen.blanktopia.BlanktopiaTutorial"
    name = "BlanktopiaTutorial"
    version = "1.0.0"
    description = "Blanktopia's custom plugin"
    apiVersion = "1.16"
    author = "Goh Wei Wen <goweiwen@gmail.com>"
    website = "www.blanktopia.com"
    depend = listOf("BlanktopiaCore")
}

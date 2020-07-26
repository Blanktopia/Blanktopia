dependencies {
    compileOnly(project(":BlanktopiaCore"))
    compileOnly("LibsDisguises:LibsDisguises:10.0.15")
}

bukkit {
    main = "me.weiwen.blanktopia.Blanktopia"
    name = "Blanktopia"
    version = "1.0.0"
    description = "Blanktopia's custom plugin"
    apiVersion = "1.16"
    author = "Goh Wei Wen <goweiwen@gmail.com>"
    website = "www.blanktopia.com"
    depend = listOf("BlanktopiaCore")
    softDepend = listOf("LibsDisguises")
    loadBefore = listOf("GoldenCrates")
    commands {
        register("blanktopia") {
            description = "Manages the Blanktopia plugin"
            usage = "/<command>"
            permission = "blanktopia.admin"
        }
        register("help") {
            description = "Find out more about Blanktopia!"
            usage = "/<command> <page>"
        }
        register("rules") {
            description = "Read the rules of the server."
            usage = "/<command>"
        }
        register("about") {
            description = "Find out what makes Blanktopia unique!"
            usage = "/<command>"
        }
        register("shop") {
            description = "Learn how to set up your own shops!"
            usage = "/<command>"
        }
        register("ranks") {
            description = "Rank up and gain more perks!"
            usage = "/<command>"
        }
        register("wenchant") {
            description = "Enchants held item with custom enchantments"
            usage = "/<command> <enchant> [level]"
            permission = "blanktopia.enchant"
        }
    }
    permissions {
        register("blanktopia.admin") { }
        register("blanktopia.enchant") { }
        register("blanktopia.help") {
            default = net.minecrell.pluginyml.bukkit.BukkitPluginDescription.Permission.Default.TRUE
        }
    }
}

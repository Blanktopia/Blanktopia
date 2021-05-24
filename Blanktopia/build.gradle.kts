dependencies {
}

bukkit {
    main = "me.weiwen.blanktopia.enchants.Blanktopia"
    name = "Blanktopia"
    version = "1.0.0"
    description = "Blanktopia's custom plugin"
    apiVersion = "1.16"
    author = "Goh Wei Wen <goweiwen@gmail.com>"
    website = "www.blanktopia.com"
    depend = listOf()
    softDepend = listOf()
    loadBefore = listOf("GoldenCrates")
    commands {
        register("blanktopia") {
            description = "Manages the Blanktopia plugin"
            usage = "/<command>"
            permission = "blanktopia.admin"
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
    }
}

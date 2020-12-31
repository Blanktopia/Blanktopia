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
    softDepend = listOf("LibsDisguises", "Blanktopia")
    loadBefore = listOf("GoldenCrates")
    commands {
        register("blanktopiaitems") {
            description = "Manages the BlanktopiaItems plugin"
            usage = "/<command> reload"
            permission = "blanktopia.admin"
        }
        register("rp") {
            description = "Downloads the resource pack"
            usage = "/<command>"
            permission = "blanktopia.rp"
        }
        register("witem") {
            description = "Gives the player a custom item"
            usage = "/<command> <type>"
            permission = "blanktopia.item"
        }
        register("whead") {
            description = "Gives the player a custom player head"
            usage = "/<command> <name> <base64>"
            permission = "blanktopia.head"
        }
        register("wkit") {
            description = "Gives user a kit"
            usage = "/<command> <kit> [player]"
            permission = "blanktopia.kit"
        }
        register("wserialize") {
            description = "Serializes the player's held item and writes it to serialized.yml"
            usage = "/<command>"
            permission = "blanktopia.serialize"
        }
    }
    permissions {
        register("blanktopia.admin") { }
        register("blanktopia.rp") { }
        register("blanktopia.item") { }
        register("blanktopia.head") { }
        register("blanktopia.kit") { }
        register("blanktopia.serialize") { }
    }
}

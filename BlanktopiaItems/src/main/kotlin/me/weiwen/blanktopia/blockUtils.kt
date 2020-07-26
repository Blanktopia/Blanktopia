package me.weiwen.blanktopia

import org.bukkit.Material.*
import org.bukkit.block.Block
import org.bukkit.inventory.ItemStack


fun canMineBlock(what: Block, with: ItemStack): Boolean {
    val type = what.getType()
    if (!type.isBlock()) return false

    return when (type) {
        BLACK_CONCRETE_POWDER,
        BLUE_CONCRETE_POWDER,
        BROWN_CONCRETE_POWDER,
        CLAY,
        COARSE_DIRT,
        CRIMSON_NYLIUM,
        CYAN_CONCRETE_POWDER,
        DIRT,
        FARMLAND,
        GRASS_BLOCK,
        GRASS_PATH,
        GRAVEL,
        GRAY_CONCRETE_POWDER,
        GREEN_CONCRETE_POWDER,
        LIGHT_BLUE_CONCRETE_POWDER,
        LIGHT_GRAY_CONCRETE_POWDER,
        LIME_CONCRETE_POWDER,
        MAGENTA_CONCRETE_POWDER,
        MYCELIUM,
        ORANGE_CONCRETE_POWDER,
        PINK_CONCRETE_POWDER,
        PODZOL,
        PURPLE_CONCRETE_POWDER,
        RED_CONCRETE_POWDER,
        RED_SAND,
        SAND,
        SNOW,
        SNOW_BLOCK,
        SOUL_SAND,
        SOUL_SOIL,
        WARPED_NYLIUM,
        WHITE_CONCRETE_POWDER,
        YELLOW_CONCRETE_POWDER ->
            isShovel(with)

        ACTIVATOR_RAIL,
        ANCIENT_DEBRIS,
        ANDESITE,
        ANDESITE_SLAB,
        ANDESITE_STAIRS,
        ANDESITE_WALL,
        ANVIL,
        BASALT,
        BEACON,
        BELL,
        BLACKSTONE,
        BLACKSTONE_SLAB,
        BLACKSTONE_STAIRS,
        BLACKSTONE_WALL,
        BLACK_CONCRETE,
        BLACK_GLAZED_TERRACOTTA,
        BLACK_SHULKER_BOX,
        BLACK_STAINED_GLASS,
        BLACK_STAINED_GLASS_PANE,
        BLACK_TERRACOTTA,
        BLAST_FURNACE,
        BLUE_CONCRETE,
        BLUE_GLAZED_TERRACOTTA,
        BLUE_ICE,
        BLUE_SHULKER_BOX,
        BLUE_STAINED_GLASS,
        BLUE_STAINED_GLASS_PANE,
        BLUE_TERRACOTTA,
        BONE_BLOCK,
        BRAIN_CORAL,
        BRAIN_CORAL_BLOCK,
        BRAIN_CORAL_FAN,
        BRAIN_CORAL_WALL_FAN,
        BREWING_STAND,
        BRICK,
        BRICKS,
        BRICK_SLAB,
        BRICK_STAIRS,
        BRICK_WALL,
        BROWN_CONCRETE,
        BROWN_GLAZED_TERRACOTTA,
        BROWN_MUSHROOM,
        BROWN_SHULKER_BOX,
        BROWN_STAINED_GLASS,
        BROWN_STAINED_GLASS_PANE,
        BROWN_TERRACOTTA,
        BUBBLE_CORAL,
        BUBBLE_CORAL_BLOCK,
        BUBBLE_CORAL_FAN,
        BUBBLE_CORAL_WALL_FAN,
        CAULDRON,
        CHAIN,
        CHIPPED_ANVIL,
        CHISELED_NETHER_BRICKS,
        CHISELED_POLISHED_BLACKSTONE,
        CHISELED_QUARTZ_BLOCK,
        CHISELED_RED_SANDSTONE,
        CHISELED_SANDSTONE,
        CHISELED_STONE_BRICKS,
        COAL_BLOCK,
        COAL_ORE,
        COBBLESTONE,
        COBBLESTONE_SLAB,
        COBBLESTONE_STAIRS,
        COBBLESTONE_WALL,
        CRACKED_NETHER_BRICKS,
        CRACKED_POLISHED_BLACKSTONE_BRICKS,
        CRACKED_STONE_BRICKS,
        CRYING_OBSIDIAN,
        CUT_RED_SANDSTONE,
        CUT_RED_SANDSTONE_SLAB,
        CUT_SANDSTONE,
        CUT_SANDSTONE_SLAB,
        CYAN_CONCRETE,
        CYAN_GLAZED_TERRACOTTA,
        CYAN_SHULKER_BOX,
        CYAN_STAINED_GLASS,
        CYAN_STAINED_GLASS_PANE,
        CYAN_TERRACOTTA,
        DAMAGED_ANVIL,
        DARK_PRISMARINE,
        DARK_PRISMARINE_SLAB,
        DARK_PRISMARINE_STAIRS,
        DAYLIGHT_DETECTOR,
        DEAD_BRAIN_CORAL,
        DEAD_BRAIN_CORAL_BLOCK,
        DEAD_BRAIN_CORAL_FAN,
        DEAD_BRAIN_CORAL_WALL_FAN,
        DEAD_BUBBLE_CORAL,
        DEAD_BUBBLE_CORAL_BLOCK,
        DEAD_BUBBLE_CORAL_FAN,
        DEAD_BUBBLE_CORAL_WALL_FAN,
        DEAD_FIRE_CORAL,
        DEAD_FIRE_CORAL_BLOCK,
        DEAD_FIRE_CORAL_FAN,
        DEAD_FIRE_CORAL_WALL_FAN,
        DEAD_HORN_CORAL,
        DEAD_HORN_CORAL_BLOCK,
        DEAD_HORN_CORAL_FAN,
        DEAD_HORN_CORAL_WALL_FAN,
        DEAD_TUBE_CORAL,
        DEAD_TUBE_CORAL_BLOCK,
        DEAD_TUBE_CORAL_FAN,
        DEAD_TUBE_CORAL_WALL_FAN,
        DETECTOR_RAIL,
        DIAMOND_BLOCK,
        DIAMOND_ORE,
        DIORITE,
        DIORITE_STAIRS,
        DIORITE_WALL,
        DISPENSER,
        DROPPER,
        EMERALD_BLOCK,
        EMERALD_ORE,
        ENCHANTING_TABLE,
        ENDER_CHEST,
        END_CRYSTAL,
        END_GATEWAY,
        END_STONE,
        END_STONE_BRICKS,
        END_STONE_BRICK_SLAB,
        END_STONE_BRICK_STAIRS,
        END_STONE_BRICK_WALL,
        FIRE_CORAL,
        FIRE_CORAL_BLOCK,
        FIRE_CORAL_FAN,
        FIRE_CORAL_WALL_FAN,
        FLOWER_POT,
        FROSTED_ICE,
        FURNACE,
        GILDED_BLACKSTONE,
        GLASS,
        GLASS_PANE,
        GLOWSTONE,
        GLOWSTONE_DUST,
        GOLD_BLOCK,
        GOLD_ORE,
        GRANITE,
        GRANITE_SLAB,
        GRANITE_STAIRS,
        GRANITE_WALL,
        GRAY_CONCRETE,
        GRAY_GLAZED_TERRACOTTA,
        GRAY_SHULKER_BOX,
        GRAY_STAINED_GLASS,
        GRAY_STAINED_GLASS_PANE,
        GRAY_TERRACOTTA,
        GREEN_CONCRETE,
        GREEN_GLAZED_TERRACOTTA,
        GREEN_SHULKER_BOX,
        GREEN_STAINED_GLASS,
        GREEN_STAINED_GLASS_PANE,
        GREEN_TERRACOTTA,
        GRINDSTONE,
        HEAVY_WEIGHTED_PRESSURE_PLATE,
        HOPPER,
        HORN_CORAL,
        HORN_CORAL_BLOCK,
        HORN_CORAL_FAN,
        HORN_CORAL_WALL_FAN,
        ICE,
        INFESTED_CHISELED_STONE_BRICKS,
        INFESTED_COBBLESTONE,
        INFESTED_CRACKED_STONE_BRICKS,
        INFESTED_MOSSY_STONE_BRICKS,
        INFESTED_STONE,
        INFESTED_STONE_BRICKS,
        IRON_BLOCK,
        IRON_ORE,
        IRON_TRAPDOOR,
        LANTERN,
        LAPIS_BLOCK,
        LAPIS_ORE,
        LEVER,
        LIGHT_BLUE_CONCRETE,
        LIGHT_BLUE_GLAZED_TERRACOTTA,
        LIGHT_BLUE_SHULKER_BOX,
        LIGHT_BLUE_STAINED_GLASS,
        LIGHT_BLUE_STAINED_GLASS_PANE,
        LIGHT_BLUE_TERRACOTTA,
        LIGHT_GRAY_CONCRETE,
        LIGHT_GRAY_GLAZED_TERRACOTTA,
        LIGHT_GRAY_SHULKER_BOX,
        LIGHT_GRAY_STAINED_GLASS,
        LIGHT_GRAY_STAINED_GLASS_PANE,
        LIGHT_GRAY_TERRACOTTA,
        LIGHT_WEIGHTED_PRESSURE_PLATE,
        LIME_CONCRETE,
        LIME_GLAZED_TERRACOTTA,
        LIME_SHULKER_BOX,
        LIME_STAINED_GLASS,
        LIME_STAINED_GLASS_PANE,
        LIME_TERRACOTTA,
        LODESTONE,
        MAGENTA_CONCRETE,
        MAGENTA_GLAZED_TERRACOTTA,
        MAGENTA_SHULKER_BOX,
        MAGENTA_STAINED_GLASS,
        MAGENTA_STAINED_GLASS_PANE,
        MAGENTA_TERRACOTTA,
        MAGMA_BLOCK,
        MOSSY_COBBLESTONE,
        MOSSY_COBBLESTONE_SLAB,
        MOSSY_COBBLESTONE_STAIRS,
        MOSSY_COBBLESTONE_WALL,
        MOSSY_STONE_BRICKS,
        MOSSY_STONE_BRICK_SLAB,
        MOSSY_STONE_BRICK_STAIRS,
        MOSSY_STONE_BRICK_WALL,
        NETHERRACK,
        NETHER_BRICKS,
        NETHER_BRICK_FENCE,
        NETHER_BRICK_SLAB,
        NETHER_BRICK_STAIRS,
        NETHER_GOLD_ORE,
        NETHER_QUARTZ_ORE,
        OBSERVER,
        OBSIDIAN,
        ORANGE_CONCRETE,
        ORANGE_GLAZED_TERRACOTTA,
        ORANGE_SHULKER_BOX,
        ORANGE_STAINED_GLASS,
        ORANGE_STAINED_GLASS_PANE,
        ORANGE_TERRACOTTA,
        PACKED_ICE,
        PETRIFIED_OAK_SLAB,
        PINK_CONCRETE,
        PINK_GLAZED_TERRACOTTA,
        PINK_SHULKER_BOX,
        PINK_STAINED_GLASS,
        PINK_STAINED_GLASS_PANE,
        PINK_TERRACOTTA,
        PISTON,
        PISTON_HEAD,
        POLISHED_ANDESITE,
        POLISHED_ANDESITE_SLAB,
        POLISHED_ANDESITE_STAIRS,
        POLISHED_BASALT,
        POLISHED_BLACKSTONE,
        POLISHED_BLACKSTONE_BRICKS,
        POLISHED_BLACKSTONE_BRICK_SLAB,
        POLISHED_BLACKSTONE_BRICK_STAIRS,
        POLISHED_BLACKSTONE_BRICK_WALL,
        POLISHED_BLACKSTONE_BUTTON,
        POLISHED_BLACKSTONE_PRESSURE_PLATE,
        POLISHED_BLACKSTONE_SLAB,
        POLISHED_BLACKSTONE_STAIRS,
        POLISHED_BLACKSTONE_WALL,
        POLISHED_DIORITE,
        POLISHED_DIORITE_SLAB,
        POLISHED_DIORITE_STAIRS,
        POLISHED_GRANITE,
        POLISHED_GRANITE_SLAB,
        POLISHED_GRANITE_STAIRS,
        POWERED_RAIL,
        PRISMARINE,
        PRISMARINE_BRICKS,
        PRISMARINE_BRICK_SLAB,
        PRISMARINE_BRICK_STAIRS,
        PRISMARINE_CRYSTALS,
        PRISMARINE_SLAB,
        PRISMARINE_STAIRS,
        PRISMARINE_WALL,
        PURPLE_CONCRETE,
        PURPLE_GLAZED_TERRACOTTA,
        PURPLE_SHULKER_BOX,
        PURPLE_STAINED_GLASS,
        PURPLE_STAINED_GLASS_PANE,
        PURPLE_TERRACOTTA,
        PURPUR_BLOCK,
        PURPUR_PILLAR,
        PURPUR_SLAB,
        PURPUR_STAIRS,
        QUARTZ_BLOCK,
        QUARTZ_BRICKS,
        QUARTZ_PILLAR,
        QUARTZ_SLAB,
        QUARTZ_STAIRS,
        RAIL,
        REDSTONE_BLOCK,
        REDSTONE_LAMP,
        REDSTONE_ORE,
        RED_CONCRETE,
        RED_GLAZED_TERRACOTTA,
        RED_NETHER_BRICKS,
        RED_NETHER_BRICK_SLAB,
        RED_NETHER_BRICK_STAIRS,
        RED_NETHER_BRICK_WALL,
        RED_SANDSTONE,
        RED_SANDSTONE_SLAB,
        RED_SANDSTONE_STAIRS,
        RED_SANDSTONE_WALL,
        RED_SHULKER_BOX,
        RED_STAINED_GLASS,
        RED_STAINED_GLASS_PANE,
        RED_TERRACOTTA,
        RESPAWN_ANCHOR,
        SANDSTONE,
        SANDSTONE_SLAB,
        SANDSTONE_STAIRS,
        SANDSTONE_WALL,
        SEA_LANTERN,
        SHULKER_BOX,
        SMITHING_TABLE,
        SMOKER,
        SMOOTH_QUARTZ,
        SMOOTH_QUARTZ_SLAB,
        SMOOTH_QUARTZ_STAIRS,
        SMOOTH_RED_SANDSTONE,
        SMOOTH_RED_SANDSTONE_SLAB,
        SMOOTH_RED_SANDSTONE_STAIRS,
        SMOOTH_SANDSTONE,
        SMOOTH_SANDSTONE_SLAB,
        SMOOTH_SANDSTONE_STAIRS,
        SMOOTH_STONE,
        SMOOTH_STONE_SLAB,
        SOUL_LANTERN,
        SPAWNER,
        STICKY_PISTON,
        STONE,
        STONECUTTER,
        STONE_BRICKS,
        STONE_BRICK_SLAB,
        STONE_BRICK_STAIRS,
        STONE_BRICK_WALL,
        STONE_BUTTON,
        STONE_PRESSURE_PLATE,
        STONE_SLAB,
        STONE_STAIRS,
        TERRACOTTA,
        TUBE_CORAL,
        TUBE_CORAL_BLOCK,
        TUBE_CORAL_FAN,
        TUBE_CORAL_WALL_FAN,
        WHITE_CONCRETE,
        WHITE_GLAZED_TERRACOTTA,
        WHITE_SHULKER_BOX,
        WHITE_STAINED_GLASS,
        WHITE_STAINED_GLASS_PANE,
        WHITE_TERRACOTTA,
        YELLOW_CONCRETE,
        YELLOW_GLAZED_TERRACOTTA,
        YELLOW_SHULKER_BOX,
        YELLOW_STAINED_GLASS,
        YELLOW_STAINED_GLASS_PANE,
        YELLOW_TERRACOTTA ->
            isPickaxe(with)

        ACACIA_BUTTON,
        ACACIA_DOOR,
        ACACIA_FENCE,
        ACACIA_FENCE_GATE,
        ACACIA_LOG,
        ACACIA_PLANKS,
        ACACIA_PRESSURE_PLATE,
        ACACIA_SIGN,
        ACACIA_SLAB,
        ACACIA_STAIRS,
        ACACIA_TRAPDOOR,
        ACACIA_WALL_SIGN,
        ACACIA_WOOD,
        BAMBOO,
        BARREL,
        BEEHIVE,
        BEE_NEST,
        BIRCH_BUTTON,
        BIRCH_DOOR,
        BIRCH_FENCE,
        BIRCH_FENCE_GATE,
        BIRCH_LOG,
        BIRCH_PLANKS,
        BIRCH_PRESSURE_PLATE,
        BIRCH_SAPLING,
        BIRCH_SIGN,
        BIRCH_SLAB,
        BIRCH_STAIRS,
        BIRCH_TRAPDOOR,
        BIRCH_WALL_SIGN,
        BIRCH_WOOD,
        BOOKSHELF,
        BROWN_MUSHROOM_BLOCK,
        CACTUS,
        CAMPFIRE,
        CARTOGRAPHY_TABLE,
        CARVED_PUMPKIN,
        CHEST,
        CHORUS_FLOWER,
        CHORUS_FRUIT,
        CHORUS_PLANT,
        COCOA,
        COMPOSTER,
        CRAFTING_TABLE,
        CRIMSON_BUTTON,
        CRIMSON_DOOR,
        CRIMSON_FENCE,
        CRIMSON_FENCE_GATE,
        CRIMSON_FUNGUS,
        CRIMSON_HYPHAE,
        CRIMSON_PLANKS,
        CRIMSON_PRESSURE_PLATE,
        CRIMSON_ROOTS,
        CRIMSON_SIGN,
        CRIMSON_SLAB,
        CRIMSON_STAIRS,
        CRIMSON_STEM,
        CRIMSON_TRAPDOOR,
        CRIMSON_WALL_SIGN,
        DARK_OAK_BUTTON,
        DARK_OAK_DOOR,
        DARK_OAK_FENCE,
        DARK_OAK_FENCE_GATE,
        DARK_OAK_LOG,
        DARK_OAK_PLANKS,
        DARK_OAK_PRESSURE_PLATE,
        DARK_OAK_SIGN,
        DARK_OAK_SLAB,
        DARK_OAK_STAIRS,
        DARK_OAK_TRAPDOOR,
        DARK_OAK_WALL_SIGN,
        DARK_OAK_WOOD,
        DIORITE_SLAB,
        FLETCHING_TABLE,
        HONEYCOMB_BLOCK,
        JACK_O_LANTERN,
        JUKEBOX,
        JUNGLE_BUTTON,
        JUNGLE_DOOR,
        JUNGLE_FENCE,
        JUNGLE_FENCE_GATE,
        JUNGLE_LOG,
        JUNGLE_PLANKS,
        JUNGLE_PRESSURE_PLATE,
        JUNGLE_SIGN,
        JUNGLE_SLAB,
        JUNGLE_STAIRS,
        JUNGLE_TRAPDOOR,
        JUNGLE_WALL_SIGN,
        JUNGLE_WOOD,
        LADDER,
        LECTERN,
        LOOM,
        MELON,
        MUSHROOM_STEM,
        NOTE_BLOCK,
        OAK_BUTTON,
        OAK_DOOR,
        OAK_FENCE,
        OAK_FENCE_GATE,
        OAK_LOG,
        OAK_PLANKS,
        OAK_PRESSURE_PLATE,
        OAK_SIGN,
        OAK_SLAB,
        OAK_STAIRS,
        OAK_TRAPDOOR,
        OAK_WALL_SIGN,
        OAK_WOOD,
        PUMPKIN,
        RED_MUSHROOM_BLOCK,
        SHROOMLIGHT,
        SOUL_CAMPFIRE,
        SPRUCE_BUTTON,
        SPRUCE_DOOR,
        SPRUCE_FENCE,
        SPRUCE_FENCE_GATE,
        SPRUCE_LOG,
        SPRUCE_PLANKS,
        SPRUCE_PRESSURE_PLATE,
        SPRUCE_SIGN,
        SPRUCE_SLAB,
        SPRUCE_STAIRS,
        SPRUCE_TRAPDOOR,
        SPRUCE_WALL_SIGN,
        SPRUCE_WOOD,
        STRIPPED_ACACIA_LOG,
        STRIPPED_ACACIA_WOOD,
        STRIPPED_BIRCH_LOG,
        STRIPPED_BIRCH_WOOD,
        STRIPPED_CRIMSON_HYPHAE,
        STRIPPED_CRIMSON_STEM,
        STRIPPED_DARK_OAK_LOG,
        STRIPPED_DARK_OAK_WOOD,
        STRIPPED_JUNGLE_LOG,
        STRIPPED_JUNGLE_WOOD,
        STRIPPED_OAK_LOG,
        STRIPPED_OAK_WOOD,
        STRIPPED_SPRUCE_LOG,
        STRIPPED_SPRUCE_WOOD,
        STRIPPED_WARPED_HYPHAE,
        STRIPPED_WARPED_STEM,
        TRAPPED_CHEST,
        WARPED_BUTTON,
        WARPED_DOOR,
        WARPED_FENCE,
        WARPED_FENCE_GATE,
        WARPED_FUNGUS,
        WARPED_HYPHAE,
        WARPED_PLANKS,
        WARPED_PRESSURE_PLATE,
        WARPED_ROOTS,
        WARPED_SIGN,
        WARPED_SLAB,
        WARPED_STAIRS,
        WARPED_STEM,
        WARPED_TRAPDOOR,
        WARPED_WALL_SIGN ->
            isAxe(with)

        DRIED_KELP_BLOCK,
        HAY_BLOCK,
        NETHER_WART_BLOCK,
        SPONGE,
        WARPED_WART_BLOCK,
        WET_SPONGE ->
            isHoe(with)

        DARK_OAK_LEAVES,
        JUNGLE_LEAVES,
        OAK_LEAVES,
        SPRUCE_LEAVES,
        ACACIA_LEAVES,
        BIRCH_LEAVES,
        BLACK_CARPET,
        BLACK_WOOL,
        BLUE_CARPET,
        BLUE_WOOL,
        BROWN_CARPET,
        BROWN_WOOL,
        COBWEB,
        CYAN_CARPET,
        CYAN_WOOL,
        GRAY_CARPET,
        GRAY_WOOL,
        GREEN_CARPET,
        GREEN_WOOL,
        LIGHT_BLUE_CARPET,
        LIGHT_BLUE_WOOL,
        LIGHT_GRAY_CARPET,
        LIGHT_GRAY_WOOL,
        LIME_CARPET,
        LIME_WOOL,
        MAGENTA_CARPET,
        MAGENTA_WOOL,
        ORANGE_CARPET,
        ORANGE_WOOL,
        PINK_CARPET,
        PINK_WOOL,
        PURPLE_CARPET,
        PURPLE_WOOL,
        RED_CARPET,
        RED_WOOL,
        WHITE_CARPET,
        WHITE_WOOL,
        YELLOW_CARPET,
        YELLOW_WOOL ->
            isShear(with)

        SLIME_BLOCK,
        HONEY_BLOCK,
        SCAFFOLDING,
        TNT ->
            true

        else -> false
    }
}

fun isShovel(item: ItemStack): Boolean {
    when (item.type) {
        STONE_SHOVEL, DIAMOND_SHOVEL, GOLDEN_SHOVEL, IRON_SHOVEL, WOODEN_SHOVEL, NETHERITE_SHOVEL -> return true
        else -> return false
    }
}

fun isPickaxe(item: ItemStack): Boolean {
    when (item.type) {
        DIAMOND_PICKAXE, GOLDEN_PICKAXE, IRON_PICKAXE, STONE_PICKAXE, WOODEN_PICKAXE, NETHERITE_PICKAXE -> return true
        else -> return false
    }
}

fun isAxe(item: ItemStack): Boolean {
    when (item.type) {
        DIAMOND_AXE, GOLDEN_AXE, IRON_AXE, STONE_AXE, WOODEN_AXE, NETHERITE_AXE -> return true
        else -> return false
    }
}

fun isHoe(item: ItemStack): Boolean {
    when (item.type) {
        DIAMOND_HOE, GOLDEN_HOE, IRON_HOE, STONE_HOE, WOODEN_HOE, NETHERITE_HOE -> return true
        else -> return false
    }
}

fun isShear(item: ItemStack): Boolean {
    return item.type == SHEARS
}

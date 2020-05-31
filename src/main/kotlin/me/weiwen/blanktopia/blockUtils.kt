package me.weiwen.blanktopia

import org.bukkit.inventory.ItemStack
import org.bukkit.block.Block
import org.bukkit.Material.*


fun canMineBlock(what: Block, with: ItemStack): Boolean {
    val type = what.getType()
    if (!type.isBlock()) return false

    return when (type) {
        SLIME_BLOCK, HONEY_BLOCK, SCAFFOLDING, TNT ->
            true
        GLASS, GLASS_PANE, WHITE_STAINED_GLASS, ORANGE_STAINED_GLASS, MAGENTA_STAINED_GLASS, LIGHT_BLUE_STAINED_GLASS, YELLOW_STAINED_GLASS, LIME_STAINED_GLASS, PINK_STAINED_GLASS, GRAY_STAINED_GLASS, LIGHT_GRAY_STAINED_GLASS, CYAN_STAINED_GLASS, PURPLE_STAINED_GLASS, BLUE_STAINED_GLASS, BROWN_STAINED_GLASS, GREEN_STAINED_GLASS, RED_STAINED_GLASS, BLACK_STAINED_GLASS, WHITE_STAINED_GLASS_PANE, ORANGE_STAINED_GLASS_PANE, MAGENTA_STAINED_GLASS_PANE, LIGHT_BLUE_STAINED_GLASS_PANE, YELLOW_STAINED_GLASS_PANE, LIME_STAINED_GLASS_PANE, PINK_STAINED_GLASS_PANE, GRAY_STAINED_GLASS_PANE, LIGHT_GRAY_STAINED_GLASS_PANE, CYAN_STAINED_GLASS_PANE, PURPLE_STAINED_GLASS_PANE, BLUE_STAINED_GLASS_PANE, BROWN_STAINED_GLASS_PANE, GREEN_STAINED_GLASS_PANE, RED_STAINED_GLASS_PANE, BLACK_STAINED_GLASS_PANE ->
            true
        DIRT, GRASS_BLOCK, COARSE_DIRT, FARMLAND, GRASS_PATH, GRAVEL, SAND, MYCELIUM, SOUL_SAND, SNOW, SNOW_BLOCK, BLACK_CONCRETE_POWDER, BLUE_CONCRETE_POWDER, BROWN_CONCRETE_POWDER, CYAN_CONCRETE_POWDER, GRAY_CONCRETE_POWDER, GREEN_CONCRETE_POWDER, LIGHT_BLUE_CONCRETE_POWDER, LIGHT_GRAY_CONCRETE_POWDER, LIME_CONCRETE_POWDER, MAGENTA_CONCRETE_POWDER, ORANGE_CONCRETE_POWDER, PINK_CONCRETE_POWDER, PURPLE_CONCRETE_POWDER, RED_CONCRETE_POWDER, WHITE_CONCRETE_POWDER, YELLOW_CONCRETE_POWDER, CLAY, RED_SAND ->
            isShovel(with)
        STONE_BRICK_WALL, STONE_BRICKS, STONE_STAIRS, CHISELED_STONE_BRICKS, CRACKED_STONE_BRICKS, END_STONE_BRICK_SLAB, END_STONE_BRICK_STAIRS, END_STONE_BRICK_WALL, STONECUTTER, SMOOTH_STONE_SLAB, MOSSY_STONE_BRICK_SLAB, MOSSY_COBBLESTONE_SLAB, MOSSY_COBBLESTONE_STAIRS, MOSSY_STONE_BRICK_STAIRS, MOSSY_STONE_BRICK_WALL, MOSSY_STONE_BRICKS, CHISELED_RED_SANDSTONE, SANDSTONE_SLAB, SANDSTONE_WALL, CHISELED_SANDSTONE, CUT_RED_SANDSTONE, CUT_SANDSTONE, CUT_RED_SANDSTONE_SLAB, CUT_SANDSTONE_SLAB, RED_SANDSTONE_SLAB, RED_SANDSTONE_WALL, SMOOTH_SANDSTONE_STAIRS, SMOOTH_SANDSTONE_SLAB, SMOOTH_RED_SANDSTONE_STAIRS, SMOOTH_RED_SANDSTONE_SLAB, REDSTONE_LAMP, COBBLESTONE_SLAB, GRINDSTONE, STONE, STONE_BUTTON, STONE_PRESSURE_PLATE, STONE_SLAB, STONE_BRICK_SLAB, COBBLESTONE, COBBLESTONE_WALL, MOSSY_COBBLESTONE_WALL, COBBLESTONE_STAIRS, MOSSY_COBBLESTONE, END_STONE, END_STONE_BRICKS, SANDSTONE, GLOWSTONE, RED_SANDSTONE, SANDSTONE_STAIRS, COAL_ORE, DIAMOND_ORE, EMERALD_ORE, GOLD_ORE, IRON_ORE, LAPIS_ORE, NETHER_QUARTZ_ORE, REDSTONE_ORE, RED_SANDSTONE_STAIRS, REDSTONE_BLOCK, COAL_BLOCK, DIAMOND_BLOCK, EMERALD_BLOCK, GOLD_BLOCK, IRON_BLOCK, LAPIS_BLOCK, PURPUR_BLOCK, QUARTZ_BLOCK, ANVIL, ACTIVATOR_RAIL, BEACON, BONE_BLOCK, BREWING_STAND, BRICK, BRICK_STAIRS, SHULKER_BOX, LIGHT_GRAY_SHULKER_BOX, BLACK_SHULKER_BOX, BLUE_SHULKER_BOX, BROWN_SHULKER_BOX, CYAN_SHULKER_BOX, GRAY_SHULKER_BOX, GREEN_SHULKER_BOX, LIGHT_BLUE_SHULKER_BOX, LIME_SHULKER_BOX, MAGENTA_SHULKER_BOX, ORANGE_SHULKER_BOX, PINK_SHULKER_BOX, PURPLE_SHULKER_BOX, RED_SHULKER_BOX, WHITE_SHULKER_BOX, YELLOW_SHULKER_BOX, DAYLIGHT_DETECTOR, CAULDRON, DETECTOR_RAIL, DISPENSER, ENCHANTING_TABLE, END_CRYSTAL, END_GATEWAY, ENDER_CHEST, FURNACE, ICE, FROSTED_ICE, PACKED_ICE, LEVER, DROPPER, FLOWER_POT, HOPPER, OBSIDIAN, NETHER_BRICK, NETHER_BRICK_STAIRS, NETHER_BRICK_FENCE, NETHERRACK, RED_NETHER_BRICKS, SPAWNER, OBSERVER, PISTON, STICKY_PISTON, POWERED_RAIL, PRISMARINE, PURPUR_PILLAR, PURPUR_SLAB, PURPUR_STAIRS, QUARTZ_STAIRS, SEA_LANTERN, SMOOTH_QUARTZ, SMOOTH_RED_SANDSTONE, SMOOTH_SANDSTONE, SMOOTH_STONE, STONE_BRICK_STAIRS, DARK_PRISMARINE_STAIRS, PRISMARINE_BRICK_STAIRS, PRISMARINE_STAIRS, GLASS, BLACK_STAINED_GLASS, BLUE_STAINED_GLASS, BROWN_STAINED_GLASS, CYAN_STAINED_GLASS, GRAY_STAINED_GLASS, GREEN_STAINED_GLASS, LIGHT_BLUE_STAINED_GLASS, LIGHT_GRAY_STAINED_GLASS, LIME_STAINED_GLASS, MAGENTA_STAINED_GLASS, ORANGE_STAINED_GLASS, PINK_STAINED_GLASS, PURPLE_STAINED_GLASS, RED_STAINED_GLASS, WHITE_STAINED_GLASS, YELLOW_STAINED_GLASS, GLASS_PANE, BLACK_STAINED_GLASS_PANE, BLUE_STAINED_GLASS_PANE, BROWN_STAINED_GLASS_PANE, CYAN_STAINED_GLASS_PANE, GRAY_STAINED_GLASS_PANE, GREEN_STAINED_GLASS_PANE, LIGHT_BLUE_STAINED_GLASS_PANE, LIGHT_GRAY_STAINED_GLASS_PANE, LIME_STAINED_GLASS_PANE, MAGENTA_STAINED_GLASS_PANE, ORANGE_STAINED_GLASS_PANE, PINK_STAINED_GLASS_PANE, PURPLE_STAINED_GLASS_PANE, RED_STAINED_GLASS_PANE, WHITE_STAINED_GLASS_PANE, YELLOW_STAINED_GLASS_PANE, IRON_TRAPDOOR, MAGMA_BLOCK, BLACK_CONCRETE, BLUE_CONCRETE, BROWN_CONCRETE, CYAN_CONCRETE, GRAY_CONCRETE, GREEN_CONCRETE, LIGHT_BLUE_CONCRETE, LIGHT_GRAY_CONCRETE, LIME_CONCRETE, MAGENTA_CONCRETE, ORANGE_CONCRETE, PINK_CONCRETE, PURPLE_CONCRETE, RED_CONCRETE, WHITE_CONCRETE, YELLOW_CONCRETE, ANDESITE, POLISHED_ANDESITE, DIORITE, POLISHED_DIORITE, GRANITE, POLISHED_GRANITE, BRICK_SLAB, BRICKS, BRICK_WALL, ANDESITE_SLAB, DARK_PRISMARINE_SLAB, GRANITE_SLAB, NETHER_BRICK_SLAB, PETRIFIED_OAK_SLAB, POLISHED_ANDESITE_SLAB, POLISHED_DIORITE_SLAB, POLISHED_GRANITE_SLAB, PRISMARINE_BRICK_SLAB, PRISMARINE_SLAB, QUARTZ_SLAB, RED_NETHER_BRICK_SLAB, SMOOTH_QUARTZ_SLAB, QUARTZ_PILLAR, CHISELED_QUARTZ_BLOCK, SMOOTH_QUARTZ_STAIRS, PRISMARINE_BRICKS, PRISMARINE_CRYSTALS, PRISMARINE_WALL, DARK_PRISMARINE, HEAVY_WEIGHTED_PRESSURE_PLATE, LIGHT_WEIGHTED_PRESSURE_PLATE, BLAST_FURNACE, SMOKER, PISTON, STICKY_PISTON ->
            isPickaxe(with)
        DARK_OAK_WOOD, STRIPPED_ACACIA_WOOD, STRIPPED_BIRCH_WOOD, STRIPPED_DARK_OAK_WOOD, STRIPPED_JUNGLE_WOOD, STRIPPED_OAK_WOOD, STRIPPED_SPRUCE_WOOD, ACACIA_WOOD, BIRCH_WOOD, JUNGLE_WOOD, OAK_WOOD, SPRUCE_WOOD, BIRCH_STAIRS, JUNGLE_STAIRS, OAK_STAIRS, SPRUCE_STAIRS, BIRCH_FENCE, BIRCH_FENCE_GATE, JUNGLE_FENCE, JUNGLE_FENCE_GATE, ACACIA_FENCE, ACACIA_FENCE_GATE, ACACIA_STAIRS, DARK_OAK_FENCE, DARK_OAK_FENCE_GATE, DARK_OAK_STAIRS, SPRUCE_FENCE, SPRUCE_FENCE_GATE, DARK_OAK_DOOR, ACACIA_DOOR, BIRCH_DOOR, JUNGLE_DOOR, SPRUCE_DOOR, OAK_TRAPDOOR, ACACIA_TRAPDOOR, BIRCH_TRAPDOOR, DARK_OAK_TRAPDOOR, JUNGLE_TRAPDOOR, SPRUCE_TRAPDOOR, OAK_DOOR, MUSHROOM_STEM, BROWN_MUSHROOM_BLOCK, RED_MUSHROOM_BLOCK, BOOKSHELF, OAK_FENCE, OAK_FENCE_GATE, ACACIA_LOG, BIRCH_LOG, JUNGLE_LOG, OAK_LOG, SPRUCE_LOG, CHEST, TRAPPED_CHEST, PUMPKIN, MELON, ACACIA_SLAB, BIRCH_SLAB, DARK_OAK_SLAB, DIORITE_SLAB, JUNGLE_SLAB, OAK_SLAB, SPRUCE_SLAB, DARK_OAK_BUTTON, ACACIA_BUTTON, BIRCH_BUTTON, JUNGLE_BUTTON, OAK_BUTTON, SPRUCE_BUTTON, DARK_OAK_LOG, DARK_OAK_PLANKS, DARK_OAK_PRESSURE_PLATE, DARK_OAK_SIGN, DARK_OAK_WALL_SIGN, ACACIA_PRESSURE_PLATE, BIRCH_PRESSURE_PLATE, JUNGLE_PRESSURE_PLATE, OAK_PRESSURE_PLATE, SPRUCE_PRESSURE_PLATE, STRIPPED_DARK_OAK_LOG, STRIPPED_ACACIA_LOG, STRIPPED_BIRCH_LOG, STRIPPED_JUNGLE_LOG, STRIPPED_OAK_LOG, STRIPPED_SPRUCE_LOG ->
            isAxe(with)
        DRIED_KELP_BLOCK, HAY_BLOCK, NETHER_WART_BLOCK, SPONGE, WET_SPONGE ->
            isHoe(with)
        else -> false
    }
}

fun isShovel(item: ItemStack): Boolean {
    when (item.type) {
        STONE_SHOVEL, DIAMOND_SHOVEL, GOLDEN_SHOVEL, IRON_SHOVEL, WOODEN_SHOVEL -> return true
        else -> return false
    }
}

fun isPickaxe(item: ItemStack): Boolean {
    when (item.type) {
        DIAMOND_PICKAXE, GOLDEN_PICKAXE, IRON_PICKAXE, STONE_PICKAXE, WOODEN_PICKAXE -> return true
        else -> return false
    }
}

fun isAxe(item: ItemStack): Boolean {
    when (item.type) {
        DIAMOND_AXE, GOLDEN_AXE, IRON_AXE, STONE_AXE, WOODEN_AXE -> return true
        else -> return false
    }
}

fun isHoe(item: ItemStack): Boolean {
    when (item.type) {
        DIAMOND_HOE, GOLDEN_HOE, IRON_HOE, STONE_HOE, WOODEN_HOE -> return true
        else -> return false
    }
}

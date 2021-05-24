local colours = ['BLACK', 'RED', 'GREEN', 'BROWN', 'BLUE', 'PURPLE', 'CYAN', 'LIGHT_GRAY', 'GRAY', 'PINK', 'LIME', 'YELLOW', 'LIGHT_BLUE', 'MAGENTA', 'ORANGE', 'WHITE'];

local flatten(arr) = std.foldl(function(acc, x) acc + x, arr, {});

local colourCode(hex) = '§x' + std.join('', std.map(function(c) '§' + c, std.stringChars(hex)));
local biomeWand(biome, colour) = {
  'custom-model-data': 1,
  material: 'WOODEN_HOE',
  name: colourCode(colour) + biome + ' Biome Wand',
  lore: [
    '§8-------------------------',
    '',
    '§bRight-click§f to turn an area',
    '§finto a ' + colourCode(colour) + biome + '§f biome.',
    '',
    '§8-------------------------',
    '§6NATURE Crate Item',
  ],
  enchantments: {
    soulbound: 1,
    final: 1,
  },
  unbreakable: true,
  triggers: [
    {
      trigger: 'right-click-block',
      cancel: true,
      actions: [
        {
          action: 'biome-wand',
          range: 2,
          biome: std.strReplace(std.asciiUpper(biome), ' ', '_'),
        },
      ],
    },
  ],
};

local crateCoupon(name, crate) = {
  material: 'PAPER',
  name: '§6' + name + ' Crate Coupon',
  'custom-model-data': 4,
  lore: [
    '§8-------------------------',
    '',
    '§bRight-click§f to give all online',
    '§fplayers a [' + name + ' Crate Key](gold).',
    '',
    '§8-------------------------',
  ],
  enchantments: {
    final: 1,
  },
  triggers: [
    {
      trigger: 'right-click',
      'remove-item': true,
      actions: [
        {
          action: 'console-command',
          command: 'broadcast §6%p§6 has given everyone a ' + crate + ' Crate Key.',
        },
        {
          action: 'console-command',
          command: 'crates givekey * ' + std.asciiLower(crate),
        },
        {
          action: 'all-players',
          actions: [
            {
              action: 'play-sound',
              sound: 'ENTITY_EXPERIENCE_ORB_PICKUP',
            },
            {
              action: 'play-sound',
              sound: 'BLOCK_ENCHANTMENT_TABLE_USE',
            },
          ],
        },
      ],
    },
  ],
};

local disguise(name, skin, crate, options = {}) = {
  material: 'PLAYER_HEAD',
  head: skin,
  name: '§6' + name + ' Disguise§c ☻',
  lore: [
    '§8-------------------------',
    '',
    '§fDisguises you as a §b' + name + '§f.',
    '',
    '§8-------------------------',
    "§6" + crate + " Crate Item",
  ],
  enchantments: {
    soulbound: 1,
    final: 1,
  },
  triggers: [
    {
      trigger: 'equip-head',
      actions: [
        {
          action: 'disguise',
          kind: 'mob',
          type: std.strReplace(std.asciiUpper(name), ' ', '_'),
          baby: false,
        } + options,
      ],
    },
    {
      trigger: 'unequip-armor',
      actions: [
        {
          action: 'undisguise',
        },
      ],
    },
    {
      trigger: 'right-click',
      cancel: true,
      actions: [
        {
          action: 'equip-item',
          slot: 'HEAD',
        },
      ],
    },
    {
      trigger: 'right-click-block',
      cancel: true,
      actions: [],
    },
  ],
};

local equipHead = [
    {
      trigger: 'shift-click-inventory',
      cancel: true,
      actions: [
        {
          action: 'equip-item',
          slot: 'HEAD',
        },
      ],
    },
    {
      trigger: 'right-click',
      cancel: true,
      actions: [
        {
          action: 'equip-item',
          slot: 'HEAD',
        },
      ],
    },
    {
      trigger: 'right-click-block',
      cancel: true,
      actions: [],
    }
];

local cookingRecipe(input, result, type) = { input: input, result: result, type: type };
local furnaceRecipe(input, result) = cookingRecipe(input, result, 'furnace');
local blastingRecipe(input, result) = cookingRecipe(input, result, 'blasting');
local smokingRecipe(input, result) = cookingRecipe(input, result, 'smoking');
local campfireRecipe(input, result) = cookingRecipe(input, result, 'campfire');

{
  'resource-pack-url': 'http://files.blanktopia.com/Blanktopia-9319d8e5fa45b58fbbbe4ac1376fea3bce9ab128.zip',
  'resource-pack-hash': '9319d8e5fa45b58fbbbe4ac1376fea3bce9ab128',
  kits: {
    starter: [
      {
        'custom-item': 'guidebook',
      },
      {
        material: 'wooden_sword',
        enchantments: {
          soulbound: 1,
          unbreaking: 2,
        },
      },
      {
        material: 'wooden_pickaxe',
        enchantments: {
          soulbound: 1,
          unbreaking: 2,
        },
      },
      {
        material: 'wooden_axe',
        enchantments: {
          soulbound: 1,
          unbreaking: 2,
        },
      },
      {
        material: 'wooden_shovel',
        enchantments: {
          soulbound: 1,
          unbreaking: 2,
        },
      },
      {
        'custom-item': 'cotton-candy-pink',
        amount: 16,
      },
      {
        material: 'golden_shovel',
        name: '§rClaim Shovel',
        enchantments: {
          soulbound: 1,
        },
      },
      {
        material: 'shield',
        slot: 'OFF_HAND',
        enchantments: {
          soulbound: 1,
          unbreaking: 2,
        },
      },
    ],
  },
  recipes: 
    flatten(std.map(function(c) { ['blasting-' + std.asciiLower(c)]: blastingRecipe({ material: c + '_TERRACOTTA' }, { material: c + '_GLAZED_TERRACOTTA' }) }, colours)) + {
    'blasting-cobblestone': blastingRecipe({ material: 'COBBLESTONE' }, { material: 'STONE' }),
    'blasting-sandstone': blastingRecipe({ material: 'SANDSTONE' }, { material: 'SMOOTH_SANDSTONE' }),
    'blasting-red-sandstone': blastingRecipe({ material: 'RED_SANDSTONE' }, { material: 'SMOOTH_RED_SANDSTONE' }),
    'blasting-stone': blastingRecipe({ material: 'STONE' }, { material: 'SMOOTH_STONE' }),
    'blasting-quartz-block': blastingRecipe({ material: 'QUARTZ_BLOCK' }, { material: 'SMOOTH_QUARTZ' }),
    'blasting-clay': blastingRecipe({ material: 'CLAY_BALL' }, { material: 'BRICK' }),
    'blasting-netherrack': blastingRecipe({ material: 'NETHERRACK' }, { material: 'NETHER_BRICK' }),
    'blasting-nether-bricks': blastingRecipe({ material: 'NETHER_BRICKS' }, { material: 'CRACKED_NETHER_BRICKS' }),
    'blasting-clay-block': blastingRecipe({ material: 'CLAY' }, { material: 'TERRACOTTA' }),
    'blasting-stone-bricks': blastingRecipe({ material: 'STONE_BRICKS' }, { material: 'CRACKED_STONE_BRICKS' }),

    'santa-hat': {
      type: 'shaped',
      result: {
        kind: 'custom',
        type: 'santa-hat',
      },
      shape: [
        ' W ',
        'WWW',
        'OOO',
      ],
      ingredients: {
        W: {
          material: 'RED_WOOL',
        },
        O: {
          material: 'WHITE_WOOL',
        },
      },
    },
    'elf-hat': {
      type: 'shaped',
      result: {
        kind: 'custom',
        type: 'elf-hat',
      },
      shape: [
        ' W ',
        'WWW',
        'OOO',
      ],
      ingredients: {
        W: {
          material: 'GREEN_WOOL',
        },
        O: {
          material: 'WHITE_WOOL',
        },
      },
    },

    'oreo': {
      type: 'shaped',
      result: {
        kind: 'custom',
        type: 'oreo',
      },
      shape: [
        'CCC',
        'SSS',
        'CCC',
      ],
      ingredients: {
        S: {
          material: 'SUGAR',
        },
        C: {
          material: 'COOKIE',
        },
      },
    },
    'cotton-candy-pink': {
      type: 'shaped',
      result: {
        kind: 'custom',
        type: 'cotton-candy-pink',
      },
      shape: [
        ' SS',
        ' SS',
        'Z  ',
      ],
      ingredients: {
        S: {
          material: 'SUGAR',
        },
        Z: {
          material: 'STICK',
        },
      },
    },
    'apple-cider': {
      type: 'shapeless',
      result: {
        kind: 'custom',
        type: 'apple-cider',
      },
      ingredients: [
        {
          material: 'APPLE',
        },
        {
          material: 'GLASS_BOTTLE',
        },
        {
          material: 'SUGAR',
        },
      ],
    },
    'apple-pie': {
      type: 'shapeless',
      result: {
        kind: 'custom',
        type: 'apple-pie',
      },
      ingredients: [
        {
          material: 'APPLE',
        },
        {
          material: 'SUGAR',
        },
        {
          material: 'EGG',
        },
      ],
    },
    'sunny-side-up-furnace': furnaceRecipe(
      { material: 'EGG' }, 
      { kind: 'custom', type: 'sunny-side-up' }),
    'sunny-side-up-smoking': smokingRecipe(
      { material: 'EGG' }, 
      { kind: 'custom', type: 'sunny-side-up' }),
    'sunny-side-up-campfire': campfireRecipe(
      { material: 'EGG' }, 
      { kind: 'custom', type: 'sunny-side-up' }),
    cheese: {
      type: 'shapeless',
      result: {
        kind: 'custom',
        type: 'cheese',
      },
      ingredients: [
        {
          material: 'MILK_BUCKET',
        },
        {
          kind: 'custom',
          type: 'vinegar',
        },
      ],
    },
    vinegar: {
      type: 'shapeless',
      result: {
        kind: 'custom',
        type: 'vinegar',
      },
      ingredients: [
        {
          material: 'GLASS_BOTTLE',
        },
        {
          material: 'WHEAT',
        },
      ],
    },
    noodles: {
      type: 'shapeless',
      result: {
        kind: 'custom',
        type: 'noodles',
      },
      ingredients: [
        {
          material: 'WHEAT',
        },
        {
          material: 'WHEAT',
        },
      ],
    },
    'mac-and-cheese': {
      type: 'shapeless',
      result: {
        kind: 'custom',
        type: 'mac-and-cheese',
      },
      ingredients: [
        {
          kind: 'custom',
          type: 'noodles',
        },
        {
          kind: 'custom',
          type: 'cheese',
        },
        {
          material: 'BOWL',
        },
      ],
    },
    'chicken-noodle-soup': {
      type: 'shapeless',
      result: {
        kind: 'custom',
        type: 'chicken-noodle-soup',
      },
      ingredients: [
        {
          kind: 'custom',
          type: 'noodles',
        },
        {
          material: 'COOKED_CHICKEN',
        },
        {
          material: 'BOWL',
        },
      ],
    },
    pasta: {
      type: 'shapeless',
      result: {
        kind: 'custom',
        type: 'pasta',
      },
      ingredients: [
        {
          kind: 'custom',
          type: 'noodles',
        },
        {
          material: 'BOWL',
        },
      ],
    },
    'squid-ink-pasta': {
      type: 'shapeless',
      result: {
        kind: 'custom',
        type: 'squid-ink-pasta',
      },
      ingredients: [
        {
          kind: 'custom',
          type: 'noodles',
        },
        {
          material: 'INK_SAC',
        },
        {
          material: 'BOWL',
        },
      ],
    },
  },
  items: {
    guidebook: {
      material: 'KNOWLEDGE_BOOK',
      name: "Beginner's Guide",
      enchantments: {
        soulbound: 1,
      },
      triggers: [
        {
          trigger: 'right-click',
          cancel: true,
          actions: [
            {
              action: 'player-command',
              command: 'help',
            },
          ],
        },
      ],
    },
    'oreo': {
      material: 'COOKIE',
      'custom-model-data': 7,
      name: '§fOreos',
      triggers: [
        {
          trigger: 'consume',
          cancel: false,
          actions: [
            {
              action: 'add-potion-effect',
              type: 'speed',
              ticks: 1200,
              level: 1,
            },
            {
              action: 'feed',
              amount: 8,
              saturation: 5.6,
            },
          ],
        },
      ],
    },
    'cotton-candy-pink': {
      material: 'COOKIE',
      'custom-model-data': 1,
      name: '§fCotton Candy',
      triggers: [
        {
          trigger: 'consume',
          cancel: false,
          actions: [
            {
              action: 'add-potion-effect',
              type: 'speed',
              ticks: 1200,
              level: 1,
            },
            {
              action: 'feed',
              amount: 3,
              saturation: 5.6,
            },
          ],
        },
      ],
    },
    'cotton-candy-blue': {
      material: 'COOKIE',
      'custom-model-data': 2,
      name: '§fCotton Candy',
      triggers: [
        {
          trigger: 'consume',
          cancel: false,
          actions: [
            {
              action: 'add-potion-effect',
              type: 'speed',
              ticks: 1200,
              level: 1,
            },
            {
              action: 'feed',
              amount: 3,
              saturation: 5.6,
            },
          ],
        },
      ],
    },
    'sunny-side-up': {
      material: 'COOKIE',
      'custom-model-data': 3,
      name: '§fSunny-Side Up',
      triggers: [
        {
          trigger: 'consume',
          cancel: false,
          actions: [
            {
              action: 'add-potion-effect',
              type: 'damage_resistance',
              ticks: 1200,
              level: 1,
            },
            {
              action: 'feed',
              amount: 3,
              saturation: 5.6,
            },
          ],
        },
      ],
    },
    cheese: {
      material: 'COOKIE',
      'custom-model-data': 4,
      name: '§fCheese',
      triggers: [
        {
          trigger: 'consume',
          cancel: false,
          actions: [
            {
              action: 'add-potion-effect',
              type: 'damage_resistance',
              ticks: 1200,
              level: 1,
            },
            {
              action: 'feed',
              amount: 3,
              saturation: 5.6,
            },
          ],
        },
      ],
    },
    noodles: {
      material: 'COOKIE',
      'custom-model-data': 6,
      name: '§fNoodles',
      triggers: [
        {
          trigger: 'consume',
          cancel: false,
          actions: [
            {
              action: 'add-potion-effect',
              type: 'damage_resistance',
              ticks: 1200,
              level: 1,
            },
            {
              action: 'feed',
              amount: 3,
              saturation: 5.6,
            },
          ],
        },
      ],
    },
    pasta: {
      material: 'MUSHROOM_STEW',
      'custom-model-data': 1,
      name: '§fPasta',
      triggers: [
        {
          trigger: 'consume',
          cancel: false,
          actions: [
            {
              action: 'add-potion-effect',
              type: 'damage_resistance',
              ticks: 1200,
              level: 1,
            },
            {
              action: 'feed',
              amount: 3,
              saturation: 5.6,
            },
          ],
        },
      ],
    },
    'squid-ink-pasta': {
      material: 'MUSHROOM_STEW',
      'custom-model-data': 2,
      name: '§fSquid Ink Pasta',
      triggers: [
        {
          trigger: 'consume',
          cancel: false,
          actions: [
            {
              action: 'add-potion-effect',
              type: 'damage_resistance',
              ticks: 1200,
              level: 1,
            },
            {
              action: 'feed',
              amount: 3,
              saturation: 5.6,
            },
          ],
        },
      ],
    },
    'chicken-noodle-soup': {
      material: 'MUSHROOM_STEW',
      'custom-model-data': 3,
      name: '§fChicken Noodle Soup',
      triggers: [
        {
          trigger: 'consume',
          cancel: false,
          actions: [
            {
              action: 'add-potion-effect',
              type: 'damage_resistance',
              ticks: 1200,
              level: 1,
            },
            {
              action: 'feed',
              amount: 3,
              saturation: 5.6,
            },
          ],
        },
      ],
    },
    'mac-and-cheese': {
      material: 'MUSHROOM_STEW',
      'custom-model-data': 4,
      name: '§fMac and Cheese',
      triggers: [
        {
          trigger: 'consume',
          cancel: false,
          actions: [
            {
              action: 'add-potion-effect',
              type: 'damage_resistance',
              ticks: 1200,
              level: 1,
            },
            {
              action: 'feed',
              amount: 3,
              saturation: 5.6,
            },
          ],
        },
      ],
    },
    'apple-pie': {
      material: 'PUMPKIN_PIE',
      'custom-model-data': 1,
      name: '§fApple Pie',
      triggers: [
        {
          trigger: 'consume',
          cancel: false,
          actions: [
            {
              action: 'add-potion-effect',
              type: 'damage_resistance',
              ticks: 1200,
              level: 1,
            },
            {
              action: 'feed',
              amount: 3,
              saturation: 5.6,
            },
          ],
        },
      ],
    },
    'apple-cider': {
      material: 'POTION',
      'custom-model-data': 1,
      name: '§fApple Cider',
      triggers: [
        {
          trigger: 'consume',
          cancel: false,
          actions: [
            {
              action: 'add-potion-effect',
              type: 'increase_damage',
              ticks: 6000,
              level: 2,
            },
            {
              action: 'add-potion-effect',
              type: 'slow',
              ticks: 6000,
              level: 0,
            },
          ],
        },
      ],
    },
    vinegar: {
      material: 'POTION',
      'custom-model-data': 2,
      name: '§fVinegar',
      triggers: [
        {
          trigger: 'consume',
          cancel: false,
          actions: [
            {
              action: 'add-potion-effect',
              type: 'poison',
              ticks: 200,
              level: 2,
            },
          ],
        },
      ],
    },
    '3x-exp-coupon': {
      material: 'PAPER',
      'custom-model-data': 1,
      name: '§b3x EXP Coupon',
      lore: [
        '§8-------------------------',
        '',
        '§bRight-click§f to receive a',
        '§b3x EXP§f buff for §b5 minutes§f.',
        '',
        '§8-------------------------',
      ],
      enchantments: {
        final: 1,
      },
      triggers: [
        {
          trigger: 'right-click',
          'remove-item': true,
          actions: [
            {
              action: 'experience-boost',
              multiplier: 3.0,
              ticks: 6000,
            },
            {
              action: 'message',
              message: '§6Received §b3x EXP§6 buff for 15 minutes.',
            },
            {
              action: 'play-sound',
              sound: 'ENTITY_EXPERIENCE_ORB_PICKUP',
            },
            {
              action: 'play-sound',
              sound: 'BLOCK_ENCHANTMENT_TABLE_USE',
            },
          ],
        },
      ],
    },
    '2x-exp-coupon-server': {
      material: 'PAPER',
      name: '§b2x EXP Coupon (Server-wide)',
      'custom-model-data': 1,
      lore: [
        '§8-------------------------',
        '',
        '§bRight-click§f to grant a §b2x EXP§f',
        '§fbuff to all online §fplayers',
        '§ffor §b15 minutes§f.',
        '',
        '§8-------------------------',
      ],
      enchantments: {
        final: 1,
      },
      triggers: [
        {
          trigger: 'right-click',
          'remove-item': true,
          actions: [
            {
              action: 'console-command',
              command: 'broadcast §6%p§6 has given everyone a §b2x EXP buff§6 for 15 minutes.',
            },
            {
              action: 'all-players',
              actions: [
                {
                  action: 'experience-boost',
                  multiplier: 2.0,
                  ticks: 18000,
                },
                {
                  action: 'play-sound',
                  sound: 'ENTITY_EXPERIENCE_ORB_PICKUP',
                },
                {
                  action: 'play-sound',
                  sound: 'BLOCK_ENCHANTMENT_TABLE_USE',
                },
              ],
            },
          ],
        },
      ],
    },
    'health-coupon-server': {
      material: 'PAPER',
      'custom-model-data': 2,
      name: '§bHealth Coupon (Server-wide)',
      lore: [
        '§8-------------------------',
        '',
        '§bRight-click§f to heal all online',
        '§f§fplayers.',
        '',
        '§8-------------------------',
      ],
      enchantments: {
        final: 1,
      },
      triggers: [
        {
          trigger: 'right-click',
          'remove-item': true,
          actions: [
            {
              action: 'console-command',
              command: 'broadcast §6%p§6 has healed everyone.',
            },
            {
              action: 'all-players',
              actions: [
                {
                  action: 'add-potion-effect',
                  type: 'regeneration',
                  ticks: 60,
                  level: 5,
                },
                {
                  action: 'play-sound',
                  sound: 'ENTITY_EXPERIENCE_ORB_PICKUP',
                },
                {
                  action: 'play-sound',
                  sound: 'BLOCK_ENCHANTMENT_TABLE_USE',
                },
              ],
            },
          ],
        },
      ],
    },
    'food-coupon-server': {
      material: 'PAPER',
      'custom-model-data': 3,
      name: '§bFood Coupon (Server-wide)',
      lore: [
        '§8-------------------------',
        '',
        '§bRight-click§f to feed all online',
        '§fplayers.',
        '',
        '§8-------------------------',
      ],
      enchantments: {
        final: 1,
      },
      triggers: [
        {
          trigger: 'right-click',
          'remove-item': true,
          actions: [
            {
              action: 'console-command',
              command: "broadcast §6%p§6 has refilled everyone's hunger.",
            },
            {
              action: 'all-players',
              actions: [
                {
                  action: 'add-potion-effect',
                  type: 'saturation',
                  ticks: 60,
                  level: 0,
                },
                {
                  action: 'play-sound',
                  sound: 'ENTITY_EXPERIENCE_ORB_PICKUP',
                },
                {
                  action: 'play-sound',
                  sound: 'BLOCK_ENCHANTMENT_TABLE_USE',
                },
              ],
            },
          ],
        },
      ],
    },
    hummingbird: {
      material: 'ELYTRA',
      name: '§dHummingbird§5 ⭐',
      lore: [
        '§8-------------------------',
        '',
        '§fAllows you to hover in claims',
        '§fyou are §b/trusted§f in.',
        '',
        '§8-------------------------',
        "§6BUILDER's Crate Item",
      ],
      enchantments: {
        soulbound: 1,
        final: 1,
      },
      unbreakable: true,
      triggers: [
        {
          trigger: 'equip-chest',
          actions: [
            {
              action: 'fly-in-claims',
              'can-fly': true,
            },
          ],
        },
        {
          trigger: 'unequip-armor',
          actions: [
            {
              action: 'fly-in-claims',
              'can-fly': false,
            },
          ],
        },
      ],
    },
    'builders-wand': {
      material: 'BLAZE_ROD',
      'custom-model-data': 1,
      name: "§dBuilder's Wand§5 ⭐",
      lore: [
        '§8-------------------------',
        '',
        '§bRight-click§f on a block',
        '§fto place blocks in a §b3x3§f',
        '§farea. Requires the block to',
        '§fbe in your inventory.',
        '',
        '§8-------------------------',
        "§6BUILDER's Crate Item",
      ],
      enchantments: {
        soulbound: 1,
        final: 1,
      },
      triggers: [
        {
          trigger: 'right-click-block',
          conditions: [
            {
              condition: 'item-cooldown',
            },
          ],
          cancel: true,
          actions: [
            {
              action: 'builders-wand',
              range: 1,
            },
            {
              action: 'item-cooldown',
              ticks: 5,
            },
          ],
        },
      ],
    },
    'paint-brush': {
      material: 'WHEAT',
      'custom-model-data': 1,
      name: '§dPaint Brush§5 ⭐',
      lore: [
        '§7Paint: NONE',
        '§8-------------------------',
        '',
        '§bLeft-click§f to pick a colour.',
        '§bRight-click§f to paint a block.',
        '',
        '§fWorks on:',
        '§8- §fGlass',
        '§8- §fWool',
        '§8- §fTerracotta',
        '§8- §fConcrete',
        '',
        '§8-------------------------',
        "§6BUILDER's Crate Item",
      ],
      enchantments: {
        soulbound: 1,
        final: 1,
      },
      triggers: [
        {
          trigger: 'left-click-block',
          actions: [
            {
              action: 'paint-brush-pick',
            },
          ],
        },
        {
          trigger: 'right-click-block',
          cancel: true,
          actions: [
            {
              action: 'paint-brush-paint',
            },
          ],
        },
        {
          trigger: 'right-click-entity',
          actions: [],
        },
      ],
    },
    wrench: {
      material: 'BONE',
      'custom-model-data': 1,
      name: '§dWrench §5 ⭐',
      lore: [
        '§8-------------------------',
        '',
        '§bRight-click§f to rotate a block',
        '§for turn an item frame',
        '§finvisible.',
        '',
        '§8-------------------------',
        "§6BUILDER's Crate Item",
      ],
      enchantments: {
        final: 1,
        soulbound: 1,
      },
      triggers: [
        {
          trigger: 'right-click-block',
          force: true,
          cancel: true,
          conditions: [
            {
              condition: 'not',
              not: {
                condition: 'is-sneaking',
              },
            },
          ],
          actions: [
            {
              action: 'rotate',
            },
          ],
        },
        {
          trigger: 'right-click-block',
          force: true,
          cancel: true,
          conditions: [
            {
              condition: 'is-sneaking',
            },
          ],
          actions: [
            {
              action: 'rotate',
              'is-reversed': true,
            },
          ],
        },
        {
          trigger: 'right-click-entity',
          actions: [
            {
              action: 'toggle-item-frame-visibility',
            },
          ],
        },
      ],
    },
    'measuring-tape': {
      material: 'STRING',
      name: '§r§x§a§4§6§b§f§fMeasuring Tape ᛨ',
      lore: [
        '§8-------------------------',
        '',
        '§bLeft-click§f to set a',
        '§fpoint to measure from.',
        '§bRight-click§f to measure the',
        '§flength between two blocks.',
        '',
        '§8-------------------------',
        "§6BUILDER's Crate Item",
      ],
      enchantments: {
        final: 1,
        soulbound: 1,
      },
      triggers: [
        {
          trigger: 'left-click-block',
          actions: [
            {
              action: 'measure-distance',
              'is-origin': true,
            },
          ],
        },
        {
          trigger: 'right-click-block',
          cancel: true,
          actions: [
            {
              action: 'measure-distance',
            },
          ],
        },
      ],
    },
    lumberjack: {
      material: 'IRON_AXE',
      'custom-model-data': 1,
      name: '§6Lumberjack§c ⛏',
      lore: [
        '§8-------------------------',
        '',
        '§fBreaks blocks in a §b3x3§f area.',
        '',
        '§8-------------------------',
        "§6BUILDER's Crate Item",
      ],
      enchantments: {
        soulbound: 1,
        final: 1,
        efficiency: 2,
      },
      unbreakable: true,
      triggers: [
        {
          trigger: 'break-block',
          actions: [
            {
              action: 'hammer',
              range: 1,
            },
          ],
          cancel: false,
        },
        {
          trigger: 'right-click',
          actions: [
            {
              action: 'hammer-strip',
              range: 1,
            },
          ],
        },
      ],
    },
    randomizer: {
      material: 'POPPED_CHORUS_FRUIT',
      name: '§r§x§a§4§6§b§f§fRandomizer ?',
      lore: [
        '§8-------------------------',
        '',
        '§bRight-click§f to place a random',
        '§fblock from your hot bar.',
        '',
        '§8-------------------------',
        "§6BUILDER's Crate Item",
      ],
      enchantments: {
        final: 1,
        soulbound: 1,
      },
      triggers: [
        {
          trigger: 'right-click-block',
          cancel: true,
          actions: [
            {
              action: 'place-random-block',
            },
          ],
        },
      ],
    },
    'rocket-boots': {
      material: 'NETHERITE_BOOTS',
      name: '§dRocket Boots§5 ⭐',
      lore: [
        '§8-------------------------',
        '',
        '§fPress Shift in the air to shoot up like a rocket!',
        '',
        '§8-------------------------',
        "§6MINER's Crate Item",
      ],
      enchantments: {
        soulbound: 1,
        final: 1,
      },
      unbreakable: true,
      triggers: [
        {
          trigger: 'sneak',
          cancel: false,
          actions: [
            {
              action: 'add-permanent-potion-effects',
              key: 'rocket-boots',
              effects: {
                jump: 5,
              },
            },
          ],
        },
        {
          trigger: 'unsneak',
          cancel: false,
          actions: [
            {
              action: 'remove-permanent-potion-effects',
              key: 'rocket-boots',
            },
          ],
        },
        {
          trigger: 'jump',
          cancel: false,
          conditions: [
            {
              condition: 'is-sneaking',
            },
          ],
          actions: [
            {
              action: 'repeat',
              interval: 2,
              count: 3,
              actions: [
                {
                  action: 'spawn-particle',
                  particle: 'FIREWORKS_SPARK',
                  count: 2,
                  'offset-x': 0.3,
                  'offset-y': 0.3,
                  'offset-z': 0.3,
                },
                {
                  action: 'spawn-particle',
                  particle: 'FLAME',
                  count: 2,
                  'offset-x': 0.3,
                  'offset-y': 0.3,
                  'offset-z': 0.3,
                },
              ],
            },
            {
              action: 'play-sound',
              sound: 'ENTITY_FIREWORK_ROCKET_LAUNCH',
              pitch: 1.1,
              volume: 0.5,
            },
          ],
        },
      ],
    },
    'sandys-helmet': {
      material: 'WHITE_STAINED_GLASS',
      name: "§x§e§5§7§b§b§1Sandy's Helmet ⭐",
      lore: [
        '§7Water Breathing',
        '§8-------------------------',
        '',
        '§fUpside down fish bowl.',
        '',
        '§8-------------------------',
        '§6INFINITY Crate Item',
      ],
      enchantments: {
        soulbound: 1,
        final: 1,
        protection: 4,
        aqua_affinity: 1,
        nightvision: 1,
      },
      unbreakable: true,
      'attribute-modifiers': [
        {
          uuid: 'fb096a4d-ad65-4520-9dc0-1107e34c8124',
          name: 'Armor modifier',
          attribute: 'GENERIC_ARMOR',
          amount: 3,
          operation: 'ADD_NUMBER',
          slot: 'HEAD',
        },
        {
          uuid: '549077a2-e23e-4c6e-9235-f20b66b6a08b',
          name: 'Armor toughness',
          attribute: 'GENERIC_ARMOR_TOUGHNESS',
          amount: 3,
          operation: 'ADD_NUMBER',
          slot: 'HEAD',
        },
        {
          uuid: '00fa2498-a7a2-46ac-bf9d-527cbe1b601c',
          name: 'Knockback resistance',
          attribute: 'GENERIC_KNOCKBACK_RESISTANCE',
          amount: 0.1,
          operation: 'ADD_NUMBER',
          slot: 'HEAD',
        },
      ],
      triggers: [
        {
          trigger: 'equip-head',
          actions: [
            {
              action: 'add-permanent-potion-effects',
              key: 'sandys-helmet',
              effects: {
                water_breathing: 0,
              },
            },
          ],
        },
        {
          trigger: 'unequip-armor',
          actions: [
            {
              action: 'remove-permanent-potion-effects',
              key: 'sandys-helmet',
            },
          ],
        },
      ] + equipHead,
    },
    hellfire: {
      material: 'LAVA_BUCKET',
      name: '§x§f§f§8§0§3§eHellfire ≈',
      lore: [
        '§8-------------------------',
        '',
        '§fHot hot hot!',
        '',
        '§8-------------------------',
        '§6INFINITY Crate Item',
      ],
      enchantments: {
        soulbound: 1,
        final: 1,
        infinity: 1,
      },
      triggers: [
        {
          trigger: 'right-click',
          cancel: true,
          actions: [
            {
              action: 'lava-bucket',
            },
          ],
        },
      ],
    },
    'ras-protection': {
      material: 'GOLDEN_CHESTPLATE',
      name: "§6Ra's Protection§c ☲",
      lore: [
        '§7Fire Resistance',
        '§8-------------------------',
        '',
        '§fGrants immunity to fire and',
        '§flava.',
        '',
        '§8-------------------------',
        "§6MINER's Crate Item",
      ],
      enchantments: {
        soulbound: 1,
        final: 1,
        blast_protection: 4,
      },
      unbreakable: true,
      'attribute-modifiers': [
        {
          uuid: 'fb096a4d-ad65-4520-9dc0-1107e34c8124',
          name: 'Armor modifier',
          attribute: 'GENERIC_ARMOR',
          amount: 8,
          operation: 'ADD_NUMBER',
          slot: 'CHEST',
        },
        {
          uuid: '549077a2-e23e-4c6e-9235-f20b66b6a08b',
          name: 'Armor toughness',
          attribute: 'GENERIC_ARMOR_TOUGHNESS',
          amount: 2,
          operation: 'ADD_NUMBER',
          slot: 'CHEST',
        },
      ],
      triggers: [
        {
          trigger: 'equip-chest',
          actions: [
            {
              action: 'add-permanent-potion-effects',
              key: 'ras-protection',
              effects: {
                fire_resistance: 0,
              },
            },
          ],
        },
        {
          trigger: 'unequip-armor',
          actions: [
            {
              action: 'remove-permanent-potion-effects',
              key: 'ras-protection',
            },
          ],
        },
      ],
    },
    'wrecking-ball': {
      material: 'IRON_PICKAXE',
      'custom-model-data': 1,
      name: '§6Wrecking Ball§c ⛏',
      lore: [
        '§8-------------------------',
        '',
        '§fBreaks blocks in a §b3x3§f area.',
        '',
        '§8-------------------------',
        "§6MINER's Crate Item",
      ],
      enchantments: {
        soulbound: 1,
        final: 1,
      },
      unbreakable: true,
      triggers: [
        {
          trigger: 'break-block',
          actions: [
            {
              action: 'hammer',
              range: 1,
            },
          ],
          cancel: false,
        },
      ],
    },
    'pick-shovel': {
      material: 'NETHERITE_SHOVEL',
      name: '§x§7§3§9§2§d§bPick Shovel ⛏',
      lore: [
        '§8-------------------------',
        '',
        '§bRight-click§f to switch between',
        '§fa §bPickaxe§f and §bShovel§f.',
        '',
        '§8-------------------------',
        "§6MINER's Crate Item",
      ],
      enchantments: {
        soulbound: 1,
      },
      triggers: [
        {
          trigger: 'left-click-block',
          cancel: false,
          actions: [
            {
              action: 'cycle-tool',
              materials: [
                'NETHERITE_PICKAXE',
                'NETHERITE_SHOVEL',
              ],
            },
          ],
        },
        {
          trigger: 'right-click',
          cancel: false,
          actions: [
            {
              action: 'cycle-tool',
              materials: [
                'NETHERITE_PICKAXE',
                'NETHERITE_SHOVEL',
              ],
            },
          ],
        },
      ],
    },
    'portable-ore-compactor': {
      material: 'NETHER_STAR',
      name: '§x§3§3§d§8§f§2Portable Ore Compactor ⏹',
      lore: [
        '§8-------------------------',
        '',
        '§bRight-click§f from your',
        '§finventory to compact items',
        '§finto blocks.',
        '',
        '§8-------------------------',
        "§6MINER's Crate Item",
      ],
      enchantments: {
        final: 1,
        soulbound: 1,
      },
      triggers: [
        {
          trigger: 'right-click-inventory',
          actions: [
            {
              action: 'sudo-command',
              permissions: [
                'essentials.condense',
              ],
              command: 'condense',
            },
            {
              action: 'play-sound',
              sound: 'BLOCK_BEACON_POWER_SELECT',
              pitch: 2.0,
            },
          ],
        },
        {
          trigger: 'right-click',
          actions: [
            {
              action: 'sudo-command',
              permissions: [
                'essentials.condense',
              ],
              command: 'condense',
            },
            {
              action: 'play-sound',
              sound: 'BLOCK_BEACON_POWER_SELECT',
              pitch: 2.0,
            },
          ],
        },
      ],
    },
    lumos: {
      material: 'TORCH',
      'custom-model-data': 1,
      name: '§fLumos§e ☀',
      lore: [
        '§8-------------------------',
        '',
        '§fNever be afraid of the dark',
        '§fagain!',
        '',
        '§8-------------------------',
        '§6INFINITY Crate Item',
      ],
      enchantments: {
        soulbound: 1,
        final: 1,
        infinity: 1,
      },
      triggers: [
        {
          trigger: 'right-click-block',
          cancel: true,
          conditions: [
            {
              condition: 'item-cooldown',
            },
          ],
          actions: [
            {
              action: 'place-block',
              material: 'torch',
            },
            {
              action: 'item-cooldown',
              ticks: 5,
            },
          ],
        },
      ],
    },
    excavator: {
      material: 'IRON_SHOVEL',
      'custom-model-data': 1,
      name: '§6Excavator§c ⛏',
      lore: [
        '§8-------------------------',
        '',
        '§fBreaks blocks in a §b3x3§f area.',
        '',
        '§8-------------------------',
        "§6MINER's Crate Item",
      ],
      enchantments: {
        soulbound: 1,
        final: 1,
      },
      unbreakable: true,
      triggers: [
        {
          trigger: 'break-block',
          actions: [
            {
              action: 'hammer',
              range: 1,
            },
          ],
          cancel: false,
        },
      ],
    },
    'switch-pick': {
      material: 'NETHERITE_PICKAXE',
      name: '§x§d§7§4§4§8§1Switch Pick ⛏',
      lore: [
        '§8-------------------------',
        '',
        '§bRight-click§f to toggle',
        '§fthe §bSilk Touch§f enchantment.',
        '',
        '§8-------------------------',
        "§6MINER's Crate Item",
      ],
      enchantments: {
        silk_touch: 1,
        soulbound: 1,
      },
      triggers: [
        {
          trigger: 'right-click',
          actions: [
            {
              action: 'toggle-enchantment',
              name: 'Silk Touch',
              enchantment: 'silk_touch',
              level: 1,
            },
          ],
        },
      ],
    },
    'portable-trash-can': {
      material: 'STRIPPED_WARPED_HYPHAE',
      name: '§x§3§3§8§7§6§dPortable Trash Can ⏹',
      lore: [
        '§8-------------------------',
        '',
        '§bRight-click§f from your',
        '§finventory to open a',
        '§fportable trash can.',
        '',
        '§8-------------------------',
        "§6MINER's Crate Item",
      ],
      enchantments: {
        final: 1,
        soulbound: 1,
      },
      triggers: [
        {
          trigger: 'right-click-inventory',
          actions: [
            {
              action: 'play-sound',
              sound: 'BLOCK_SHULKER_BOX_OPEN',
              pitch: 0.8,
            },
            {
              action: 'sudo-command',
              permissions: [
                'essentials.disposal',
              ],
              command: 'disposal',
            },
          ],
        },
        {
          trigger: 'right-click',
          actions: [
            {
              action: 'play-sound',
              sound: 'BLOCK_SHULKER_BOX_OPEN',
              pitch: 0.8,
            },
            {
              action: 'sudo-command',
              permissions: [
                'essentials.disposal',
              ],
              command: 'disposal',
            },
          ],
        },
      ],
    },
    'subspace-bubble': {
      material: 'ENDER_PEARL',
      name: '§5Subspace Bubble§d ⏺',
      lore: [
        '§8-------------------------',
        '',
        '§fTeleport wherever you want!',
        '',
        '§8-------------------------',
        '§6INFINITY Crate Item',
      ],
      enchantments: {
        soulbound: 1,
        final: 1,
        infinity: 1,
      },
      triggers: [
        {
          trigger: 'right-click',
          cancel: true,
          conditions: [
            {
              condition: 'item-cooldown',
            },
          ],
          actions: [
            {
              action: 'play-sound',
              sound: 'ENTITY_ENDER_PEARL_THROW',
              pitch: 0.5,
              volume: 0.5,
            },
            {
              action: 'launch-entity',
              type: 'ENDER_PEARL',
              magnitude: 1.5,
              pitch: 0,
            },
            {
              action: 'item-cooldown',
              ticks: 20,
            },
          ],
        },
      ],
    },
    'nile-water': {
      material: 'WATER_BUCKET',
      name: '§9Nile Water§1 ≈',
      lore: [
        '§8-------------------------',
        '',
        '§f"Water from the Nile."',
        '',
        '§8-------------------------',
        '§6INFINITY Crate Item',
      ],
      enchantments: {
        soulbound: 1,
        final: 1,
        infinity: 1,
      },
      triggers: [
        {
          trigger: 'right-click',
          cancel: true,
          actions: [
            {
              action: 'water-bucket',
            },
          ],
        },
      ],
    },
    'recurve-bow': {
      material: 'BOW',
      name: '§6Recurve Bow',
      lore: [
        '§8-------------------------',
        '',
        '§fNever repair your bow again!',
        '',
        '§8-------------------------',
        '§6INFINITY Crate Item',
      ],
      enchantments: {
        infinity: 1,
        mending: 1,
        soulbound: 1,
      },
    },
    'doraemons-pocket': {
      material: 'ENDER_CHEST',
      name: "§9Doraemon's Pocket",
      lore: [
        '§8-------------------------',
        '',
        '§bRight-click§f from your',
        '§finventory to access your',
        '§fender chest.',
        '',
        '§8-------------------------',
        '§6INFINITY Crate Item',
      ],
      enchantments: {
        soulbound: 1,
      },
      triggers: [
        {
          trigger: 'right-click-block',
          cancel: true,
          actions: [],
        },
      ],
    },

    'miners-helmet': {
      material: 'GLOWSTONE_DUST',
      'custom-model-data': 1,
      name: "§x§f§f§d§7§4§7Miner's Helmet",
      lore: [
        '§7Haste II',
        '§8-------------------------',
        '',
        '§fSpelunky!',
        '',
        '§8-------------------------',
        "§6MINER's Crate Item",
      ],
      enchantments: {
        soulbound: 1,
        final: 1,
        blast_protection: 4,
        aqua_affinity: 1,
        nightvision: 1,
      },
      unbreakable: true,
      'attribute-modifiers': [
        {
          uuid: 'fb096a4d-ad65-4520-9dc0-1107e34c8124',
          name: 'Armor modifier',
          attribute: 'GENERIC_ARMOR',
          amount: 3,
          operation: 'ADD_NUMBER',
          slot: 'HEAD',
        },
        {
          uuid: '549077a2-e23e-4c6e-9235-f20b66b6a08b',
          name: 'Armor toughness',
          attribute: 'GENERIC_ARMOR_TOUGHNESS',
          amount: 3,
          operation: 'ADD_NUMBER',
          slot: 'HEAD',
        },
        {
          uuid: '00fa2498-a7a2-46ac-bf9d-527cbe1b601c',
          name: 'Knockback resistance',
          attribute: 'GENERIC_KNOCKBACK_RESISTANCE',
          amount: 0.1,
          operation: 'ADD_NUMBER',
          slot: 'HEAD',
        },
      ],
      triggers: [
        {
          trigger: 'equip-head',
          actions: [
            {
              action: 'add-permanent-potion-effects',
              key: 'miners-helmet',
              effects: {
                fast_digging: 1,
              },
            },
          ],
        },
        {
          trigger: 'unequip-armor',
          actions: [
            {
              action: 'remove-permanent-potion-effects',
              key: 'miners-helmet',
            },
          ],
        },
      ] + equipHead,
    },

    'santa-hat': {
      material: 'RED_WOOL',
      'custom-model-data': 1,
      name: "§x§a§1§2§7§2§2Santa Hat",
      triggers: equipHead,
    },
    'elf-hat': {
      material: 'GREEN_WOOL',
      'custom-model-data': 1,
      name: "§x§5§4§6§d§1§bElf Hat",
      triggers: equipHead,
    },

    'builders-crate-coupon-server': crateCoupon("BUILDER's", "builder"),
    'miners-crate-coupon-server': crateCoupon("MINER's", "miner"),
    'infinity-crate-coupon-server': crateCoupon('INFINITY', "infinity"),
    'nature-crate-coupon-server': crateCoupon('NATURE', "nature"),

    'disguise-skeleton': disguise('Skeleton', null, "BUILDER's") + { material: 'SKELETON_SKULL' },
    'disguise-chicken': disguise('Chicken', '1638469a599ceef7207537603248a9ab11ff591fd378bea4735b346a7fae893', "BUILDER's"),
    'disguise-zombie': disguise('Zombie', null, "MINER's") + { material: 'ZOMBIE_HEAD' },
    'disguise-creeper': disguise('Creeper', null, "MINER's") + { material: 'CREEPER_HEAD' },
    'disguise-enderman': disguise('Enderman', '7a59bb0a7a32965b3d90d8eafa899d1835f424509eadd4e6b709ada50b9cf', 'INFINITY'),
    'disguise-pig': disguise('Pig', '621668ef7cb79dd9c22ce3d1f3f4cb6e2559893b6df4a469514e667c16aa4', 'INFINITY'),
    'disguise-baby-sheep': disguise('Baby Sheep', 'f9719ec9ee6f53c17b1f6f747866b3121401d35f77a39859f122d472863e48e', 'NATURE', { type: 'SHEEP', baby: true }),
    'disguise-bee': disguise('Bee', 'd0299a2aae9a605b5dbd1945fc4368ccee88ae06e47dc90f953131e0d903b322', 'NATURE'),
    'disguise-tropical-fish': disguise('Tropical Fish', '12510b301b088638ec5c8747e2d754418cb747a5ce7022c9c712ecbdc5f6f065', 'NATURE'),
    'disguise-fern': disguise('Fern', null, 'NATURE', { kind: 'block', material: 'FERN' }) + { material: 'FERN' },
    'disguise-sheep': disguise('Sheep', 'f31f9ccc6b3e32ecf13b8a11ac29cd33d18c95fc73db8a66c5d657ccb8be70', '???'),

    'unidentified-biome-wand': {
      'custom-model-data': 1,
      material: 'WOODEN_HOE',
      name: '§fUnidentified Biome Wand',
      lore: [
        '§8-------------------------',
        '',
        '§bRight-click§f to turn an area',
        '§finto a §8§kunknown§f biome.',
        '',
        '§8-------------------------',
        '§6NATURE Crate Item',
      ],
      enchantments: {
        soulbound: 1,
        final: 1,
      },
      unbreakable: true,
    },
    'plains-biome-wand': biomeWand('Plains', '8DB360'),
    'forest-biome-wand': biomeWand('Forest', '056621'),
    'swamp-biome-wand': biomeWand('Swamp', '07F9B2'),
    'jungle-biome-wand': biomeWand('Jungle', '537B09'),
    'snowy-tundra-biome-wand': biomeWand('Snowy Tundra', 'FFFFFF'),
    'snowy-taiga-biome-wand': biomeWand('Snowy Taiga', '31554A'),
    'taiga-biome-wand': biomeWand('Taiga', '0B6659'),
    'desert-biome-wand': biomeWand('Desert', 'FA9418'),
    'savanna-biome-wand': biomeWand('Savanna', 'BDB25F'),
    'badlands-biome-wand': biomeWand('Badlands', 'D94515'),
    'warm-ocean-biome-wand': biomeWand('Warm Ocean', '0000AC'),
    'lukewarm-ocean-biome-wand': biomeWand('Lukewarm Ocean', '000090'),
    'cold-ocean-biome-wand': biomeWand('Cold Ocean', '202070'),
    'river-biome-wand': biomeWand('River', '0000FF'),
    'mushroom-fields-biome-wand': biomeWand('Mushroom Fields', 'FF00FF'),
    'basalt-deltas-wand': biomeWand('Basalt Deltas', '403636'),

    'boots-of-haste': {
      material: 'LEATHER_BOOTS',
      'custom-model-data': 1,
      name: "§x§f§4§c§f§8§bBoots of Haste 🪶",
      lore: [
        '§8-------------------------',
        '',
        '§fMove fast and break things.',
        '',
        '§8-------------------------',
        '§6NATURE Crate Item',
      ],
      enchantments: {
        soulbound: 1,
        final: 1,
        protection: 4,
        depth_strider: 3,
        feather_falling: 4,
        soul_speed: 3,
        stride: 5,
      },
      unbreakable: true,
      'attribute-modifiers': [
        {
          uuid: 'c5f2fe33-1f7f-41c5-872c-5868739f409a',
          name: 'Movement speed',
          attribute: 'GENERIC_MOVEMENT_SPEED',
          amount: 0.2,
          operation: 'MULTIPLY_SCALAR_1',
          slot: 'FEET',
        },
      ],
    },
    scythe: {
      material: 'SHEARS',
      'custom-model-data': 1,
      name: '§x§2§d§7§a§5§3Scythe',
      lore: [
        '§8-------------------------',
        '',
        '§fBreaks grass in a §b5x5§f area.',
        '',
        '§8-------------------------',
        "§6NATURE Crate Item",
      ],
      enchantments: {
        soulbound: 1,
        final: 1,
        efficiency: 5,
        sharpness: 5,
      },
      unbreakable: true,
      'attribute-modifiers': [
        {
          uuid: 'CB3F55D3-645C-4F38-A497-9C13A33DB5CF',
          name: 'Tool modifier',
          attribute: 'GENERIC_ATTACK_DAMAGE',
          amount: 5,
          operation: 'ADD_NUMBER',
          slot: 'HAND',
        },
        {
          uuid: 'FA233E1C-4180-4865-B01B-BCCE9785ACA3',
          name: 'Tool modifier',
          attribute: 'GENERIC_ATTACK_SPEED',
          amount: -1.5,
          operation: 'ADD_NUMBER',
          slot: 'HAND',
        },
      ],
      triggers: [
        {
          trigger: 'break-block',
          actions: [
            {
              action: 'hammer',
              range: 2,
              depth: 2,
            },
          ],
          cancel: false,
        },
      ],
    },
  },
}

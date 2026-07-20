package com.aquamancer.czlib.api.textures;

import com.aquamancer.czlib.api.abils.Actives;
import com.aquamancer.czlib.api.abils.Aspect;
import com.aquamancer.czlib.api.abils.Curse;
import com.aquamancer.czlib.api.abils.Gifts;
import com.aquamancer.czlib.api.abils.Passives;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

import java.util.HashMap;
import java.util.Optional;

import net.minecraft.item.Items;

public class ZenithTextures {
    private static final HashMap<Enum<?>, ItemStack> ICONS = new HashMap<>();

    public static Optional<ItemStack> getItem(Enum<?> ability) {
        return Optional.ofNullable(ICONS.get(ability));
    }

    private static void register(Enum<?> key, Item item, String name) {
        ItemStack stack = new ItemStack(item);

        // in bouquet_of_gold.properties in resource pack:
        // nbt.plain.display.Name=Bouquet of Gold
        // equivalent nbt:
        // plain{display{Name: "Bouquet of Gold"}}
        NbtCompound nameNbt = new NbtCompound();
        nameNbt.putString("Name", name);

        NbtCompound display = new NbtCompound();
        display.put("display", nameNbt);

        NbtCompound plain = new NbtCompound();
        plain.put("plain", display);

        stack.setNbt(plain);
        ICONS.put(key, stack);
    }

    static {
        // Actives.Combo
        register(Actives.SOOTHING, Items.HONEYCOMB, "Soothing Combos");
        register(Actives.EARTHEN, Items.WOODEN_SWORD, "Earthen Combos");
        register(Actives.VOLCANIC, Items.BLAZE_ROD, "Volcanic Combos");
        register(Actives.FRIGID, Items.BLUE_DYE, "Frigid Combos");
        register(Actives.DARK, Items.FLINT, "Dark Combos");
        register(Actives.FOCUSED, Items.SPECTRAL_ARROW, "Focused Combos");
        register(Actives.WINDSWEPT, Items.WHITE_CANDLE, "Windswept Combos");

        // Actives.Right
        register(Actives.WARD_OF_LIGHT, Items.LANTERN, "Ward of Light");
        register(Actives.BEASTS_CLAW, Items.WOODEN_HOE, "Beast's Claw");
        register(Actives.FIREBALL, Items.FIREWORK_STAR, "Fireball");
        register(Actives.ICE_LANCE, Items.SNOWBALL, "Ice Lance");
        register(Actives.ADVANCING_SHADOWS, Items.WITHER_SKELETON_SKULL, "Advancing Shadows");
        register(Actives.SIDEARM, Items.CROSSBOW, "Sidearm");
        register(Actives.WIND_WALK, Items.WHITE_DYE, "Wind Walk");
        register(Actives.SOLAR_RAY, Items.END_ROD, "Solar Ray");

        // Actives.LeftShift
        register(Actives.RADIANT_BLESSING, Items.SUNFLOWER, "Radiant Blessing");
        register(Actives.TAUNT, Items.GOLDEN_CHESTPLATE, "Taunt");
        register(Actives.IGNEOUS_RUNE, Items.BLAZE_POWDER, "Igneous Rune");
        register(Actives.SNOWSTORM, Items.ICE, "Snowstorm");
        register(Actives.CLOAK_OF_SHADOWS, Items.BLACK_CONCRETE, "Cloak of Shadows");
        register(Actives.SCRAPSHOT, Items.NETHERITE_SCRAP, "Scrapshot");
        register(Actives.GUARDING_BOLT, Items.HORN_CORAL, "Guarding Bolt");
        register(Actives.ENCORE, Items.JUKEBOX, "Encore");

        // Actives.RightShift
        register(Actives.BOTTLED_SUNLIGHT, Items.HONEY_BOTTLE, "Bottled Sunlight");
        register(Actives.IRON_GRIP, Items.IRON_ORE, "Iron Grip");
        register(Actives.FLAMESTRIKE, Items.FLINT_AND_STEEL, "Flamestrike");
        register(Actives.ICE_BARRIER, Items.PRISMARINE_WALL, "Ice Barrier");
        register(Actives.BLADE_FLURRY, Items.IRON_SWORD, "Blade Flurry");
        register(Actives.FIREWORK_BLAST, Items.FIREWORK_ROCKET, "Firework Blast");
        register(Actives.AEROBLAST, Items.PHANTOM_MEMBRANE, "Aeroblast");
        register(Actives.CHROMA_BLADE, Items.DIAMOND_SWORD, "Chroma Blade");

        // Actives.Wildcard
        register(Actives.LIGHTNING_BOTTLE, Items.BREWING_STAND, "Lightning Bottle");
        register(Actives.ENTRENCH, Items.SOUL_SAND, "Entrench");
        register(Actives.FLAME_SPIRIT, Items.SOUL_CAMPFIRE, "Flame Spirit");
        register(Actives.PERMAFROST, Items.QUARTZ, "Permafrost");
        register(Actives.PHANTOM_FORCE, Items.CHARCOAL, "Phantom Force");
        register(Actives.RAPID_FIRE, Items.REPEATER, "Rapid Fire");
        register(Actives.WHIRLWIND, Items.IRON_PICKAXE, "Whirlwind");
        register(Actives.CONVERGENCE, Items.RECOVERY_COMPASS, "Convergence");

        // Actives.Bow
        register(Actives.DIVINE_BEAM, Items.YELLOW_CANDLE, "Divine Beam");
        register(Actives.EARTHQUAKE, Items.COARSE_DIRT, "Earthquake");
        register(Actives.PYROBLAST, Items.TNT_MINECART, "Pyroblast");
        register(Actives.PIERCING_COLD, Items.PRISMARINE_SHARD, "Piercing Cold");
        register(Actives.DUMMY_DECOY, Items.ARMOR_STAND, "Dummy Decoy");
        register(Actives.VOLLEY, Items.ARROW, "Volley");
        register(Actives.SKYHOOK, Items.FISHING_ROD, "Skyhook");
        register(Actives.DISCO_BALL, Items.PEARLESCENT_FROGLIGHT, "Disco Ball");

        // Actives.Swap
        register(Actives.SPARK_OF_INSPIRATION, Items.BELL, "Spark of Inspiration");
        register(Actives.EARTHEN_WRATH, Items.TURTLE_HELMET, "Earthen Wrath");
        register(Actives.VOLCANIC_METEOR, Items.MAGMA_BLOCK, "Volcanic Meteor");
        register(Actives.AVALANCHE, Items.SNOW_BLOCK, "Avalanche");
        register(Actives.CHAOS_DAGGER, Items.ITEM_FRAME, "Chaos Dagger");
        register(Actives.GRAVITY_BOMB, Items.GRAY_GLAZED_TERRACOTTA, "Gravity Bomb");
        register(Actives.THUNDERCLOUD_FORM, Items.WHITE_GLAZED_TERRACOTTA, "Thundercloud Form");
        register(Actives.REFRACTION, Items.SPYGLASS, "Refraction");
        register(Actives.COLOR_SPLASH, Items.BEACON, "Color Splash");

        // Actives.Lifeline
        register(Actives.ETERNAL_SAVIOR, Items.YELLOW_GLAZED_TERRACOTTA, "Eternal Savior");
        register(Actives.APOCALYPSE, Items.ORANGE_DYE, "Apocalypse");
        register(Actives.CRYOBOX, Items.GHAST_TEAR, "Cryobox");
        register(Actives.ESCAPE_ARTIST, Items.ENDER_PEARL, "Escape Artist");
        register(Actives.STEEL_STALLION, Items.IRON_HORSE_ARMOR, "Steel Stallion");
        register(Actives.LAST_BREATH, Items.DRAGON_BREATH, "Last Breath");

        // Aspect
        register(Aspect.BOX, Items.BARREL, "Mystery Box");
        register(Aspect.AXE, Items.IRON_AXE, "Aspect of the Axe");
        register(Aspect.BOW, Items.BOW, "Aspect of the Bow");
        register(Aspect.SCYTHE, Items.IRON_HOE, "Aspect of the Scythe");
        register(Aspect.SWORD, Items.IRON_SWORD, "Aspect of the Sword");
        register(Aspect.WAND, Items.STICK, "Aspect of the Wand");

        // Curse
        register(Curse.ANCHORING, Items.BEDROCK, "Curse of Anchoring");
        register(Curse.ARACHNOPHOBIA, Items.SPIDER_EYE, "Curse of Arachnophobia");
        register(Curse.CHAOS, Items.MUSIC_DISC_11, "Curse of Chaos");
        register(Curse.DEATH, Items.NETHERITE_HOE, "Curse of Death");
        register(Curse.DEPENDENCY, Items.LEAD, "Curse of Dependency");
        register(Curse.ENVY, Items.APPLE, "Curse of Envy");
        register(Curse.GLUTTONY, Items.FERMENTED_SPIDER_EYE, "Curse of Gluttony");
        register(Curse.GREED, Items.BEETROOT, "Curse of Greed");
        register(Curse.IMPATIENCE, Items.CLOCK, "Curse of Impatience");
        register(Curse.LUST, Items.RED_CANDLE, "Curse of Lust");
        register(Curse.OBSCURITY, Items.ROTTEN_FLESH, "Curse of Obscurity");
        register(Curse.PESSIMISM, Items.RED_GLAZED_TERRACOTTA, "Curse of Pessimism");
        register(Curse.PRIDE, Items.PURPLE_GLAZED_TERRACOTTA, "Curse of Pride");
        register(Curse.REDUNDANCY, Items.BRICK, "Curse of Redundancy");
        register(Curse.RUIN, Items.CRACKED_NETHER_BRICKS, "Curse of Ruin");
        register(Curse.SLOTH, Items.MOSSY_COBBLESTONE, "Curse of Sloth");
        register(Curse.SOBRIETY, Items.GLASS_BOTTLE, "Curse of Sobriety");

        // Gifts
        register(Gifts.TWISTED_SCROLL, Items.PAPER, "Twisted Scroll");
        register(Gifts.FORSAKEN_GRIMOIRE, Items.BOOK, "Forsaken Grimoire");
        register(Gifts.PRISMATIC_CUBE, Items.DIAMOND_BLOCK, "Prismatic Cube");
        register(Gifts.NORTHERN_STAR, Items.END_CRYSTAL, "Northern Star");
        register(Gifts.BOTTOMLESS_BOWL, Items.BOWL, "Bottomless Bowl");
        register(Gifts.POETS_QUILL, Items.WRITABLE_BOOK, "Poet's Quill");
        register(Gifts.PURGING_STONE, Items.CLAY, "Purging Stone");
        register(Gifts.WILD_CARD, Items.FLOWER_BANNER_PATTERN, "Wild Card");
        register(Gifts.AVARICIOUS_PENDANT, Items.GOLD_NUGGET, "Avaricious Pendant");
        register(Gifts.CELESTIAL_SURPRISE, Items.CAKE, "Celestial Surprise");
        register(Gifts.COMB_OF_SELECTION, Items.HORN_CORAL_FAN, "Comb of Selection");
        register(Gifts.PILLAR_OF_LIGHT, Items.BEACON, "Pillar of Light");
        register(Gifts.BROKEN_CLOCK, Items.BLAZE_SPAWN_EGG, "Broken Clock");
        register(Gifts.TREASURE_MAP, Items.GLOW_ITEM_FRAME, "Treasure Map");
        register(Gifts.MEGA_HAMMER, Items.IRON_AXE, "Mega Hammer");
        register(Gifts.KALEIDOSCOPIC_LENS, Items.SPYGLASS, "Kaleidoscopic Lens");
        register(Gifts.CALLICARPAS_POINTED_HAT, Items.DRAGON_EGG, "Callicarpa's Pointed Hat");
        register(Gifts.VENOM_OF_THE_BROODMOTHER, Items.POTION, "Venom of the Broodmother");
        register(Gifts.BROODMOTHERS_WEBBING, Items.COBWEB, "Broodmother's Webbing");
        register(Gifts.STATUE_OF_REGRET, Items.TOTEM_OF_UNDYING, "Statue of Regret");
        register(Gifts.RAINBOW_GEODE, Items.AMETHYST_SHARD, "Rainbow Geode");
        register(Gifts.CRACKED_IDOL, Items.EMERALD, "Cracked Idol");
        register(Gifts.ORB_OF_DARKNESS, Items.ENDER_PEARL, "Orb of Darkness");

        // Passives
        register(Passives.ENLIGHTENMENT, Items.EXPERIENCE_BOTTLE, "Enlightenment");
        register(Passives.REJUVENATION, Items.NETHER_STAR, "Rejuvenation");
        register(Passives.SUNDROPS, Items.HONEYCOMB_BLOCK, "Sundrops");
        register(Passives.BRAMBLE_SHELL, Items.SWEET_BERRIES, "Bramble Shell");
        register(Passives.BULWARK, Items.NETHERITE_HELMET, "Bulwark");
        register(Passives.TOUGHNESS, Items.CRYING_OBSIDIAN, "Toughness");
        register(Passives.DETONATION, Items.TNT, "Detonation");
        register(Passives.PRIMORDIAL_MASTERY, Items.FIRE_CORAL_FAN, "Primordial Mastery");
        register(Passives.PYROMANIA, Items.CAMPFIRE, "Pyromania");
        register(Passives.FROZEN_DOMAIN, Items.IRON_BOOTS, "Frozen Domain");
        register(Passives.ICEBREAKER, Items.TUBE_CORAL_FAN, "Icebreaker");
        register(Passives.BRUTALIZE, Items.STONE_SWORD, "Brutalize");
        register(Passives.DEADLY_STRIKE, Items.BLACK_CONCRETE_POWDER, "Deadly Strike");
        register(Passives.DETHRONER, Items.DRAGON_HEAD, "Dethroner");
        register(Passives.SHADOW_SLAM, Items.ANVIL, "Shadow Slam");
        register(Passives.SHARPSHOOTER, Items.TARGET, "Sharpshooter");
        register(Passives.SPLIT_ARROW, Items.CHAIN, "Split Arrow");
        register(Passives.AEROMANCY, Items.FEATHER, "Aeromancy");
        register(Passives.DODGING, Items.COBWEB, "Dodging");
        register(Passives.ONE_WITH_THE_WIND, Items.LIGHT_GRAY_BANNER, "One with the Wind");
        register(Passives.RESTORING_DRAFT, Items.GOLDEN_BOOTS, "Restoring Draft");
        register(Passives.ABNORMALITY, Items.GLOW_INK_SAC, "Abnormality");
        register(Passives.GENEROSITY, Items.RAW_GOLD, "Generosity");
        register(Passives.CHARITY, Items.STONE_SHOVEL, "Charity");
        register(Passives.FLEXIBILITY, Items.WHITE_WOOL, "Flexibility");
        register(Passives.MULTIPLICITY, Items.AMETHYST_CLUSTER, "Multiplicity");
        register(Passives.PROSPERITY, Items.ENDER_CHEST, "Prosperity");
        register(Passives.REBIRTH, Items.CRIMSON_HYPHAE, "Rebirth");
        register(Passives.DIVERSITY, Items.CHISELED_QUARTZ_BLOCK, "Diversity");
        register(Passives.OPPORTUNITY, Items.FROGSPAWN, "Opportunity");
    }
}

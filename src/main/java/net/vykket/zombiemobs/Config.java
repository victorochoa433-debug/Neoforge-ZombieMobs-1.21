package net.vykket.zombiemobs;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {

    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    // spawn toggles
    public static final ModConfigSpec.BooleanValue SPAWN_ZOMBIE_SHEEP;
    public static final ModConfigSpec.BooleanValue SPAWN_ZOMBIE_PIG;
    public static final ModConfigSpec.BooleanValue SPAWN_ZOMBIE_RABBIT;
    public static final ModConfigSpec.BooleanValue SPAWN_GELID_RABBIT;
    public static final ModConfigSpec.BooleanValue SPAWN_ZOMBIE_CHICKEN;
    public static final ModConfigSpec.BooleanValue SPAWN_HUSK_RABBIT;
    public static final ModConfigSpec.BooleanValue SPAWN_ZOMBIE_CAT;
    public static final ModConfigSpec.BooleanValue SPAWN_ZOMBIE_COW;
    public static final ModConfigSpec.BooleanValue SPAWN_ZOMBIE_POLAR_BEAR;
    public static final ModConfigSpec.BooleanValue SPAWN_ZOMBIE_DOLPHIN;
    // behavior
    public static final ModConfigSpec.BooleanValue ZOMBIES_ATTACK_LIVING_VARIANTS;
    public static final ModConfigSpec.BooleanValue ZOMBIE_COW_BREAK_DOORS;
    public static final ModConfigSpec.BooleanValue ZOGLIN_BREAK_DOORS;
    public static final ModConfigSpec.BooleanValue CAN_BURN_IN_SUN;
    public static final ModConfigSpec.BooleanValue ZOMBIES_BURN_IN_DAY;
    // abilities
    public static final ModConfigSpec.BooleanValue ENABLE_SCREAM_ABILITY;
    public static final ModConfigSpec.IntValue SCREAM_COOLDOWN_SECONDS;
    public static final ModConfigSpec.IntValue SCREAM_RADIUS;
    public static final ModConfigSpec.BooleanValue POLAR_BEAR_SPECIAL_ATTACK;
    public static final ModConfigSpec.BooleanValue FREEZE_ON_DEATH;
    public static final ModConfigSpec.IntValue FREEZE_TICKS;
    // gameplay
    public static final ModConfigSpec.BooleanValue APOCALYPSE_MODE;
    public static final ModConfigSpec.BooleanValue ZOMBIE_SIEGE_ALERT;

    public static final ModConfigSpec SPEC;

    static {

        BUILDER.translation("config.zombiemobs.spawning").push("Spawning");

        SPAWN_ZOMBIE_SHEEP = BUILDER
                .translation("config.zombiemobs.spawn_zombie_sheep")
                .define("spawnZombieSheep", true);

        SPAWN_ZOMBIE_PIG = BUILDER
                .translation("config.zombiemobs.spawn_zombie_pig")
                .define("spawnZombiePig", true);

        SPAWN_ZOMBIE_RABBIT = BUILDER
                .translation("config.zombiemobs.spawn_zombie_rabbit")
                .define("spawnZombieRabbit", true);

        SPAWN_ZOMBIE_CAT = BUILDER
                .translation("config.zombiemobs.spawn_zombie_cat")
                .define("spawnZombieCat", true);

        SPAWN_ZOMBIE_COW = BUILDER
                .translation("config.zombiemobs.spawn_zombie_cow")
                .define("spawnZombieCow", true);

        SPAWN_ZOMBIE_CHICKEN = BUILDER
                .translation("config.zombiemobs.spawn_zombie_chicken")
                .define("spawnZombieChicken", true);

        SPAWN_ZOMBIE_DOLPHIN = BUILDER
                .translation("config.zombiemobs.spawn_zombie_dolphin")
                .define("spawnZombieDolphin", true);

        SPAWN_ZOMBIE_POLAR_BEAR = BUILDER
                .translation("config.zombiemobs.spawn_zombie_polar_bear")
                .define("spawnZombiePolarBear", true);

        SPAWN_GELID_RABBIT = BUILDER
                .translation("config.zombiemobs.spawn_gelid_rabbit")
                .define("spawnGelidRabbit", true);

        SPAWN_HUSK_RABBIT = BUILDER
                .comment("Enable Husk Rabbit spawning from Husks")
                .translation("config.zombiemobs.spawn_husk_rabbit")
                .define("spawnHuskRabbit", true);

        BUILDER.pop();

        BUILDER.translation("config.zombiemobs.mob_behavior").push("Mob Behavior");

        ZOMBIES_ATTACK_LIVING_VARIANTS = BUILDER
                .comment("Zombie animals can attack their living counterparts")
                .translation("config.zombiemobs.zombies_attack_living_variants")
                .define("zombiesAttackLivingVariants", false);

        ZOMBIE_COW_BREAK_DOORS = BUILDER
                .comment("Zombie cows can break doors")
                .translation("config.zombiemobs.zombie_cow_break_doors")
                .define("zombieCowBreakDoors", true);

        ZOGLIN_BREAK_DOORS = BUILDER
                .comment("Zoglins can break doors")
                .translation("config.zombiemobs.zoglin_break_doors")
                .define("zoglinBreakDoors", true);

        CAN_BURN_IN_SUN = BUILDER
                .comment("Zombie animals burn in sunlight")
                .translation("config.zombiemobs.zombie_animals_burn_in_sun")
                .define("zombieAnimalsBurnInSun", true);

        ZOMBIES_BURN_IN_DAY = BUILDER
                .comment("Vanilla zombies and drowneds burn in sunlight")
                .translation("config.zombiemobs.zombies_burn_in_day")
                .define("zombiesBurnInSun", true);

        BUILDER.pop();

        BUILDER.translation("config.zombiemobs.mob_abilities").push("Mob Abilities");

        BUILDER.translation("config.zombiemobs.zombie_sheep").push("Zombie Sheep");

        ENABLE_SCREAM_ABILITY = BUILDER
                .translation("config.zombiemobs.enable_scream_ability")
                .define("enableScreamAbility", true);

        SCREAM_COOLDOWN_SECONDS = BUILDER
                .translation("config.zombiemobs.scream_cooldown_seconds")
                .defineInRange("screamCooldownSeconds", 7, 1, 60);

        SCREAM_RADIUS = BUILDER
                .translation("config.zombiemobs.scream_radius")
                .defineInRange("screamRadius", 30, 5, 64);

        BUILDER.pop();


        BUILDER.translation("config.zombiemobs.gelid_polar_bear").push("Gelid Polar Bear");

        POLAR_BEAR_SPECIAL_ATTACK = BUILDER
                .comment("Enable the gelid polar bear freeze attack")
                .translation("config.zombiemobs.polar_bear_special_attack")
                .define("polarBearSpecialAttack", true);

        BUILDER.pop();


        BUILDER.translation("config.zombiemobs.gelid_rabbit").push("Gelid Rabbit");

        FREEZE_ON_DEATH = BUILDER
                .comment("Freeze area effect when the rabbit dies")
                .translation("config.zombiemobs.freeze_on_death")
                .define("freezeOnDeath", true);

        FREEZE_TICKS = BUILDER
                .comment("Ticks entities remain frozen")
                .translation("config.zombiemobs.freeze_ticks")
                .defineInRange("freezeTicks", 180, 0, 2000);

        BUILDER.pop();

        BUILDER.pop();

        BUILDER.translation("config.zombiemobs.gameplay").push("Gameplay");

        APOCALYPSE_MODE = BUILDER
                .comment("Hard mode: zombies attack animals and infections can spread")
                .translation("config.zombiemobs.apocalypse_mode")
                .define("apocalypseMode", false);

        ZOMBIE_SIEGE_ALERT = BUILDER
                .comment("Play a sound when a zombie siege begins")
                .translation("config.zombiemobs.zombie_siege_alert")
                .define("zombieSiegeAlert", true);

        BUILDER.pop();

        SPEC = BUILDER.build();
    }
}



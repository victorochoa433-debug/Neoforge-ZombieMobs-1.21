package net.vykket.zombiemobs.entity;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

import net.vykket.zombiemobs.Config;
import net.vykket.zombiemobs.registry.ModEntities;

public class ModSpawnPlacements {

    @SubscribeEvent
    public static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {

// Zombie pig
        event.register(
                ModEntities.ZOMBIE_PIG.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (type, world, reason, pos, random) -> {

                    if (!Config.SPAWN_ZOMBIE_PIG.get()) return false;

                    return Monster.checkMonsterSpawnRules(type, world, reason, pos, random);
                },
                RegisterSpawnPlacementsEvent.Operation.REPLACE
        );

// Gelid polar bear
        event.register(
                ModEntities.ZOMBIE_POLAR_BEAR.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (type, level, reason, pos, random) -> {

                    if (!Config.SPAWN_ZOMBIE_POLAR_BEAR.get()) return false;

                    if (!net.minecraft.world.entity.monster.Monster.isDarkEnoughToSpawn(level, pos, random)) return false;

                    return level.getBlockState(pos.below()).canOcclude();
                },
                RegisterSpawnPlacementsEvent.Operation.REPLACE
        );

// Zombie chicken
        event.register(
                ModEntities.ZOMBIE_CHICKEN.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (type, level, reason, pos, random) -> {

                    if (!Config.SPAWN_ZOMBIE_CHICKEN.get()) return false;

                    if (!net.minecraft.world.entity.monster.Monster.isDarkEnoughToSpawn(level, pos, random)) return false;

                    return level.getBlockState(pos.below()).isSolid();
                },
                RegisterSpawnPlacementsEvent.Operation.REPLACE
        );

// Zombie cat
        event.register(
                ModEntities.ZOMBIE_CAT.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (type, level, reason, pos, random) -> {

                    if (!Config.SPAWN_ZOMBIE_CAT.get()) return false;

                    if (!net.minecraft.world.entity.monster.Monster.isDarkEnoughToSpawn(level, pos, random)) return false;

                    return level.getBlockState(pos.below()).isSolid();
                },
                RegisterSpawnPlacementsEvent.Operation.REPLACE
        );

// Zombie rabbit
        event.register(
                ModEntities.ZOMBIE_RABBIT.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (type, level, reason, pos, random) -> {

                    if (!Config.SPAWN_ZOMBIE_RABBIT.get()) return false;

                    if (!net.minecraft.world.entity.monster.Monster.isDarkEnoughToSpawn(level, pos, random)) return false;

                    return level.getBlockState(pos.below()).isSolid();
                },
                RegisterSpawnPlacementsEvent.Operation.REPLACE
        );

// Gelid rabbit
        event.register(
                ModEntities.GELID_RABBIT.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (type, level, reason, pos, random) -> {

                    if (!Config.SPAWN_GELID_RABBIT.get()) return false;

                    if (!net.minecraft.world.entity.monster.Monster.isDarkEnoughToSpawn(level, pos, random)) return false;

                    return level.getBlockState(pos.below()).isSolid();
                },
                RegisterSpawnPlacementsEvent.Operation.REPLACE
        );

// Zombie cow
        event.register(
                ModEntities.ZOMBIE_COW.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (type, level, reason, pos, random) -> {

                    if (!Config.SPAWN_ZOMBIE_COW.get()) return false;

                    if (!net.minecraft.world.entity.monster.Monster.isDarkEnoughToSpawn(level, pos, random)) return false;

                    return level.getBlockState(pos.below()).isSolid();
                },
                RegisterSpawnPlacementsEvent.Operation.REPLACE
        );

// Zombie sheep
        event.register(
                ModEntities.ZOMBIE_SHEEP.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (type, level, reason, pos, random) -> {

                    if (!Config.SPAWN_ZOMBIE_SHEEP.get()) return false;

                    if (!net.minecraft.world.entity.monster.Monster.isDarkEnoughToSpawn(level, pos, random)) return false;

                    return level.getBlockState(pos.below()).isSolid();
                },
                RegisterSpawnPlacementsEvent.Operation.REPLACE
        );

// Zombie dolphin
        event.register(
                ModEntities.ZOMBIE_DOLPHIN.get(),
                SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (type, level, reason, pos, random) -> {

                    if (!Config.SPAWN_ZOMBIE_DOLPHIN.get()) return false;

                    if (!level.getFluidState(pos).isSource()) return false;

                    return net.minecraft.world.entity.monster.Monster.isDarkEnoughToSpawn(level, pos, random);
                },
                RegisterSpawnPlacementsEvent.Operation.REPLACE
        );

// Husk rabbit
        event.register(
                ModEntities.HUSK_RABBIT.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                net.minecraft.world.entity.monster.Monster::checkMobSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.REPLACE
        );
    }
}











package net.vykket.zombiemobs.entity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Zombie;

import net.vykket.zombiemobs.registry.ModEntities;

import java.util.HashMap;
import java.util.Map;

public class ZombieAnimalInfection {

    public static final Map<Class<? extends PathfinderMob>, EntityType<? extends PathfinderMob>> INFECTABLES = new HashMap<>();

    static {

        INFECTABLES.put(Sheep.class, ModEntities.ZOMBIE_SHEEP.get());
        INFECTABLES.put(Cow.class, ModEntities.ZOMBIE_COW.get());
        INFECTABLES.put(Chicken.class, ModEntities.ZOMBIE_CHICKEN.get());
        INFECTABLES.put(Rabbit.class, ModEntities.ZOMBIE_RABBIT.get());
        INFECTABLES.put(PolarBear.class, ModEntities.ZOMBIE_POLAR_BEAR.get());
        INFECTABLES.put(Pig.class, ModEntities.ZOMBIE_PIG.get());
        INFECTABLES.put(Dolphin.class, ModEntities.ZOMBIE_DOLPHIN.get());
        INFECTABLES.put(Cat.class, ModEntities.ZOMBIE_CAT.get());
        INFECTABLES.put(Horse.class, EntityType.ZOMBIE_HORSE);

        ResourceLocation camelHuskId = ResourceLocation.tryParse("minecraft:camel_husk");

        if (camelHuskId != null &&
                BuiltInRegistries.ENTITY_TYPE.containsKey(camelHuskId)) {

            EntityType<?> camelHuskType =
                    BuiltInRegistries.ENTITY_TYPE.get(camelHuskId);

            if (camelHuskType instanceof EntityType<?> type) {
                INFECTABLES.put(Camel.class,
                        (EntityType<? extends PathfinderMob>) type);
            }
        }
    }

    public static EntityType<? extends PathfinderMob> getZombieTypeFor(PathfinderMob mob) {
        if (mob == null) return null;

        for (Map.Entry<Class<? extends PathfinderMob>, EntityType<? extends PathfinderMob>> entry : INFECTABLES.entrySet()) {
            if (entry.getKey().isInstance(mob)) {
                return entry.getValue();
            }
        }

        return null;
    }

    public static void tryInfect(Zombie attacker, PathfinderMob target) {

        if (attacker.level().isClientSide) return;
        if (target == null || !target.isAlive()) return;
        if (target instanceof Enemy) return;

        ServerLevel level = (ServerLevel) attacker.level();
        EntityType<? extends PathfinderMob> zombieType = getZombieTypeFor(target);
        if (zombieType == null) return;

        boolean shouldInfect;
        Difficulty difficulty = level.getDifficulty();

        switch (difficulty) {
            case PEACEFUL:
            case EASY:
                shouldInfect = false;
                break;
            case NORMAL:
                shouldInfect = attacker.getRandom().nextFloat() < 0.5F;
                break;
            case HARD:
                shouldInfect = true;
                break;
            default:
                shouldInfect = false;
        }

        if (!shouldInfect) return;

        PathfinderMob zombie = zombieType.create(level);
        if (zombie == null) return;

        zombie.moveTo(
                target.getX(),
                target.getY(),
                target.getZ(),
                target.getYRot(),
                target.getXRot()
        );

        if (target.isBaby() && zombie instanceof AgeableMob ageable) {
            ageable.setBaby(true);
        }

        level.addFreshEntity(zombie);

        level.playSound(
                null,
                target.blockPosition(),
                SoundEvents.ZOMBIE_INFECT,
                SoundSource.HOSTILE,
                1.0F,
                1.0F
        );

        target.discard();
    }
}


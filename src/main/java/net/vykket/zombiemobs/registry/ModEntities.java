package net.vykket.zombiemobs.registry;

import net.vykket.zombiemobs.ZombieMobs;
import net.vykket.zombiemobs.entity.*;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(Registries.ENTITY_TYPE, ZombieMobs.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<ZombiePigEntity>> ZOMBIE_PIG =
            ENTITIES.register("zombie_pig",
                    () -> EntityType.Builder.of(ZombiePigEntity::new, MobCategory.MONSTER)
                            .sized(0.9F, 0.9F)
                            .build("zombie_pig")
            );
    public static final DeferredHolder<EntityType<?>, EntityType<ZombiePolarBearEntity>> ZOMBIE_POLAR_BEAR =
            ENTITIES.register("zombie_polar_bear",
                    () -> EntityType.Builder.of(ZombiePolarBearEntity::new, MobCategory.MONSTER)
                            .sized(1.4F, 1.4F)
                            .build("zombie_polar_bear")
            );
    public static final DeferredHolder<EntityType<?>, EntityType<ZombieRabbitEntity>> ZOMBIE_RABBIT =
            ENTITIES.register("zombie_rabbit",
                    () -> EntityType.Builder.of(ZombieRabbitEntity::new, MobCategory.MONSTER)
                            .sized(0.4F, 0.5F)
                            .build("zombie_rabbit")
            );
    public static final DeferredHolder<EntityType<?>, EntityType<HuskRabbitEntity>> HUSK_RABBIT =
            ENTITIES.register("husk_rabbit",
                    () -> EntityType.Builder.of(HuskRabbitEntity::new, MobCategory.MONSTER)
                            .sized(0.4F, 0.5F)
                            .build("husk_rabbit")
            );
    public static final DeferredHolder<EntityType<?>, EntityType<ZombieChickenEntity>> ZOMBIE_CHICKEN =
            ENTITIES.register("zombie_chicken",
                    () -> EntityType.Builder.of(ZombieChickenEntity::new, MobCategory.MONSTER)
                            .sized(0.4F, 0.7F)
                            .build("zombie_chicken")
            );
    public static final DeferredHolder<EntityType<?>, EntityType<ZombieCowEntity>> ZOMBIE_COW =
            ENTITIES.register("zombie_cow",
                    () -> EntityType.Builder.of(ZombieCowEntity::new, MobCategory.MONSTER)
                            .sized(1.0F, 1.5F)
                            .build("zombie_cow")
            );
    public static final DeferredHolder<EntityType<?>, EntityType<GelidRabbitEntity>> GELID_RABBIT =
            ENTITIES.register("gelid_rabbit",
                    () -> EntityType.Builder.of(GelidRabbitEntity::new, MobCategory.MONSTER)
                            .sized(0.4F, 0.5F)
                            .build("gelid_rabbit")
            );
    public static final DeferredHolder<EntityType<?>, EntityType<ZombieSheepEntity>> ZOMBIE_SHEEP =
            ENTITIES.register("zombie_sheep",
                    () -> EntityType.Builder.of(ZombieSheepEntity::new, MobCategory.MONSTER)
                            .sized(0.9F, 1.3F)
                            .build("zombie_sheep")
            );
    public static final DeferredHolder<EntityType<?>, EntityType<ZombieDolphinEntity>> ZOMBIE_DOLPHIN =
            ENTITIES.register("zombie_dolphin",
                    () -> EntityType.Builder.of(ZombieDolphinEntity::new, MobCategory.MONSTER)
                            .sized(0.9F, 0.6F)
                            .build("zombie_dolphin")
            );
    public static final DeferredHolder<EntityType<?>, EntityType<RiderDrownedEntity>> RIDER_DROWNED =
            ENTITIES.register("rider_drowned",
                    () -> EntityType.Builder
                            .of(RiderDrownedEntity::new, MobCategory.MONSTER)
                            .sized(0.6F, 1.95F)
                            .clientTrackingRange(8)
                            .build("rider_drowned")
            );
    public static final DeferredHolder<EntityType<?>, EntityType<ZombieCatEntity>> ZOMBIE_CAT =
            ENTITIES.register("zombie_cat",
                    () -> EntityType.Builder.of(ZombieCatEntity::new, MobCategory.MONSTER)
                            .sized(0.6F, 0.7F)
                            .build("zombie_cat")
            );
}





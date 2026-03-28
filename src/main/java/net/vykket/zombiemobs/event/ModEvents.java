package net.vykket.zombiemobs.event;

import net.vykket.zombiemobs.entity.*;
import net.vykket.zombiemobs.registry.ModEntities;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

public class ModEvents {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {

        event.put(
                ModEntities.ZOMBIE_PIG.get(),
                ZombiePigEntity.createAttributes().build()
        );

        event.put(
                ModEntities.ZOMBIE_RABBIT.get(),
                ZombieRabbitEntity.createAttributes().build()
        );

        event.put(
                ModEntities.ZOMBIE_POLAR_BEAR.get(),
                ZombiePolarBearEntity.createAttributes().build()
        );

        event.put(
                ModEntities.ZOMBIE_CHICKEN.get(),
                ZombieChickenEntity.createAttributes().build()
        );

        event.put(
                ModEntities.ZOMBIE_CAT.get(),
                ZombieCatEntity.createAttributes().build()
        );

        event.put(
                ModEntities.ZOMBIE_COW.get(),
                ZombieCowEntity.createAttributes().build()
        );

        event.put(
                ModEntities.GELID_RABBIT.get(),
                GelidRabbitEntity.createAttributes().build()
        );

        event.put(
                ModEntities.ZOMBIE_SHEEP.get(),
                ZombieSheepEntity.createAttributes().build()
        );

        event.put(
                ModEntities.RIDER_DROWNED.get(),
                RiderDrownedEntity.createAttributes().build()
        );

        event.put(
                ModEntities.ZOMBIE_DOLPHIN.get(),
                ZombieDolphinEntity.createAttributes().build()
        );

        event.put(
                ModEntities.HUSK_RABBIT.get(),
                HuskRabbitEntity.createAttributes().build()
        );
    }
}







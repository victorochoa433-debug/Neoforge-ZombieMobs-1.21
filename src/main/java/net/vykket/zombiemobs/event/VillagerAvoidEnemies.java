package net.vykket.zombiemobs.event;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.npc.Villager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

public class VillagerAvoidEnemies {

    @SubscribeEvent
    public void onEntityJoin(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();

        if (!(entity instanceof Villager villager)) return;
        if (event.getLevel().isClientSide()) return;

        villager.goalSelector.addGoal(
                1,
                new AvoidEntityGoal<>(
                        villager,
                        LivingEntity.class,
                        10.0F,
                        0.6D,
                        0.7D,
                        e -> e instanceof Enemy
                )
        );
    }
}




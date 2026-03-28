package net.vykket.zombiemobs.event;

import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Wolf;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

import net.vykket.zombiemobs.ZombieMobs;
import net.vykket.zombiemobs.entity.ZombieSheepEntity;

@EventBusSubscriber(modid = ZombieMobs.MODID)
public class MobTargetEvents {

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinLevelEvent event) {

        if (event.getEntity() instanceof Wolf wolf) {

            wolf.targetSelector.addGoal(
                    2,
                    new NearestAttackableTargetGoal<>(
                            wolf,
                            ZombieSheepEntity.class,
                            true
                    )
            );
        }
    }
}



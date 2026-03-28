package net.vykket.zombiemobs.event;

import net.minecraft.world.entity.monster.Zoglin;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

import net.vykket.zombiemobs.entity.ai.ZoglinBreakDoorGoal;

@EventBusSubscriber(modid = "zombiemobs")
public class ZoglinGoalHandler {

    @SubscribeEvent
    public static void onEntityJoin(EntityJoinLevelEvent event) {

        if (!(event.getEntity() instanceof Zoglin zoglin)) return;

        zoglin.goalSelector.addGoal(
                2,
                new ZoglinBreakDoorGoal(zoglin)
        );
    }
}

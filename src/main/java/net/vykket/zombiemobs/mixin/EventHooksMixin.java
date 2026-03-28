package net.vykket.zombiemobs.mixin;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.ServerLevelAccessor;
import net.neoforged.neoforge.event.EventHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EventHooks.class)
public class EventHooksMixin {

    @Inject(
            method = "checkSpawnPositionSpawner",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void zombiemobs$allowZombieAnimals(
            Mob mob,
            ServerLevelAccessor level,
            MobSpawnType spawnType,
            SpawnData data,
            BaseSpawner spawner,
            CallbackInfoReturnable<Boolean> cir
    ) {

        String id = mob.getType().toString();

        if (id.startsWith("zombiemobs:")) {

            if (level instanceof net.minecraft.server.level.ServerLevel serverLevel) {

                if (!net.minecraft.world.entity.monster.Monster.isDarkEnoughToSpawn(
                        serverLevel,
                        mob.blockPosition(),
                        serverLevel.random
                )) {
                    cir.setReturnValue(false);
                    return;
                }
            }

            cir.setReturnValue(true);
        }
    }
}

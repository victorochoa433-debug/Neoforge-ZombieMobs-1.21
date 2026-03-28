package net.vykket.zombiemobs.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.vykket.zombiemobs.Config;
import net.vykket.zombiemobs.registry.ModSounds;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.world.entity.ai.village.VillageSiege")
public abstract class ZombieSiegeAlertMixin {

    @Unique
    private boolean zombiemobs$wasActiveLastTick = false;

    @Inject(method = "tick", at = @At("RETURN"))
    private void zombiemobs$onSiegeReturn(
            ServerLevel level,
            boolean spawnHostiles,
            boolean spawnPassives,
            CallbackInfoReturnable<Integer> cir
    ) {

        if (!Config.ZOMBIE_SIEGE_ALERT.get())
            return;

        int result = cir.getReturnValue();

        boolean isActiveNow = (result == 1) && spawnHostiles;

        if (isActiveNow && !zombiemobs$wasActiveLastTick) {

            for (var player : level.players()) {

                if (!level.isVillage(player.blockPosition())) {
                    continue;
                }

                level.playSound(
                        null,
                        player.blockPosition(),
                        ModSounds.ZOMBIE_SIEGE_ALERT.get(),
                        SoundSource.HOSTILE,
                        2.5F,
                        0.1F
                );
            }
        }

        zombiemobs$wasActiveLastTick = isActiveNow;
    }
}

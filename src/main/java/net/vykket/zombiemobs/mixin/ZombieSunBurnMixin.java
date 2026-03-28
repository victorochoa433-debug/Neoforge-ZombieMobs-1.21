package net.vykket.zombiemobs.mixin;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Zombie;
import net.vykket.zombiemobs.Config;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public abstract class ZombieSunBurnMixin {

    @Inject(method = "isSunBurnTick", at = @At("HEAD"), cancellable = true)
    private void zombiemobs$preventZombieSunBurn(CallbackInfoReturnable<Boolean> cir) {

        if (!Config.ZOMBIES_BURN_IN_DAY.get()) {

            if ((Object)this instanceof Zombie) {
                cir.setReturnValue(false);
            }
        }
    }
}

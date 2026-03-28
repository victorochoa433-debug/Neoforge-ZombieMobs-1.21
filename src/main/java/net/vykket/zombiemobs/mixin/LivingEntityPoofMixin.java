package net.vykket.zombiemobs.mixin;

import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.vykket.zombiemobs.ClientConfig;
import net.vykket.zombiemobs.Config;
import net.vykket.zombiemobs.particles.ModParticles;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityPoofMixin {

    @Inject(method = "makePoofParticles", at = @At("HEAD"), cancellable = true)
    private void zombiemobs$replacePoof(CallbackInfo info) {
        LivingEntity entity = (LivingEntity)(Object)this;

        if (!ClientConfig.ZOMBIE_POOF_PARTICLES.get()) {
            return;
        }

        if (entity.getType().is(EntityTypeTags.ZOMBIES)
                || entity.getType() == EntityType.GIANT
                || entity.getType() == EntityType.PHANTOM) {

            info.cancel();

            for (int i = 0; i < 20; i++) {
                double xd = entity.getRandom().nextGaussian() * 0.02;
                double yd = entity.getRandom().nextGaussian() * 0.02;
                double zd = entity.getRandom().nextGaussian() * 0.02;

                entity.level().addParticle(
                        ModParticles.ZOMBIE_POOF.get(),
                        entity.getRandomX(1.0),
                        entity.getRandomY(),
                        entity.getRandomZ(1.0),
                        xd, yd, zd
                );
            }
        }
    }
}


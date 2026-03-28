package net.vykket.zombiemobs.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Zombie;

import net.vykket.zombiemobs.Config;
import net.vykket.zombiemobs.entity.ZombieAnimalInfection;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Zombie.class)
public abstract class ZombieInfectionMixin {

    @Inject(method = "doHurtTarget", at = @At("RETURN"))
    private void zombiemobs$infectAnimals(Entity target, CallbackInfoReturnable<Boolean> cir) {

        if (!cir.getReturnValue()) return;

        Zombie self = (Zombie)(Object)this;

        if (self.level().isClientSide()) return;
        if (!Config.APOCALYPSE_MODE.get()) return;

        if (!(target instanceof PathfinderMob mob)) return;
        if (mob instanceof Enemy) return;

        EntityType<? extends PathfinderMob> zombieType =
                ZombieAnimalInfection.getZombieTypeFor(mob);

        if (zombieType == null) return;

        float finalHealth = mob.getHealth()
                - (float) self.getAttributeValue(Attributes.ATTACK_DAMAGE);

        if (finalHealth > 0.0F) return;

        Difficulty difficulty = self.level().getDifficulty();

        boolean shouldInfect = switch (difficulty) {
            case EASY -> false;
            case NORMAL -> self.getRandom().nextBoolean();
            case HARD -> true;
            default -> false;
        };

        if (!shouldInfect) return;

        ServerLevel level = (ServerLevel) self.level();

        mob.setHealth(1.0F);

        PathfinderMob zombie = zombieType.create(level);
        if (zombie == null) return;

        zombie.moveTo(
                mob.getX(),
                mob.getY(),
                mob.getZ(),
                mob.getYRot(),
                mob.getXRot()
        );

        if (mob.isBaby() && zombie instanceof net.minecraft.world.entity.AgeableMob ageable) {
            ageable.setBaby(true);
        }

        level.addFreshEntity(zombie);

        level.playSound(
                null,
                mob.blockPosition(),
                SoundEvents.ZOMBIE_INFECT,
                SoundSource.HOSTILE,
                1.0F,
                1.0F
        );

        mob.discard();
    }
}

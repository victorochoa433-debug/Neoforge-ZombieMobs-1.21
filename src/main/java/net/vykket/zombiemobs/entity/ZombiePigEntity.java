package net.vykket.zombiemobs.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.vykket.zombiemobs.Config;
import net.vykket.zombiemobs.registry.ModEntities;
import org.jetbrains.annotations.Nullable;

import net.vykket.zombiemobs.registry.ModSounds;

public class ZombiePigEntity extends Zombie implements Enemy {

    public ZombiePigEntity(EntityType<? extends Zombie> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();

        this.targetSelector.addGoal(2,
                new NearestAttackableTargetGoal<>(this, Villager.class, true));
        this.targetSelector.addGoal(3,
                new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        if (Config.ZOMBIES_ATTACK_LIVING_VARIANTS.get()) {
            this.targetSelector.addGoal(2,
                    new NearestAttackableTargetGoal<>(
                            this,
                            Pig.class,
                            true,
                            target -> !(target instanceof ZombiePigEntity)
                    )
            );
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Zombie.createAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.ATTACK_DAMAGE, 2.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    @Override
    public boolean shouldDespawnInPeaceful() {
        return true;
    }

    @Override
    public SpawnGroupData finalizeSpawn(
            ServerLevelAccessor level,
            DifficultyInstance difficulty,
            MobSpawnType reason,
            @Nullable SpawnGroupData spawnData
    ) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, spawnData);

        if (this.isPassenger()) {
            this.stopRiding();
        }

        return data;
    }

    @Override
    protected boolean isSunBurnTick() {
        if (!Config.CAN_BURN_IN_SUN.get()) {
            return false;
        }

        return super.isSunBurnTick();
    }

    @Override
    protected boolean convertsInWater() {
        return false;
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        boolean hit = super.doHurtTarget(target);

        if (hit
                && !this.level().isClientSide()
                && target instanceof Pig pig
                && !(pig instanceof Enemy)) {

            if (!pig.isAlive() || pig.getHealth() <= 0.0F) {

                Difficulty difficulty = this.level().getDifficulty();
                boolean shouldInfect = false;

                switch (difficulty) {
                    case EASY -> shouldInfect = false;
                    case NORMAL -> shouldInfect = this.random.nextBoolean();
                    case HARD -> shouldInfect = true;
                }

                if (!shouldInfect) return hit;

                ServerLevel serverLevel = (ServerLevel) this.level();

                ZombiePigEntity zombie =
                        ModEntities.ZOMBIE_PIG.get().create(serverLevel);

                if (zombie != null) {

                    zombie.moveTo(
                            pig.getX(),
                            pig.getY(),
                            pig.getZ(),
                            pig.getYRot(),
                            pig.getXRot()
                    );

                    if (pig.isBaby()) {
                        zombie.setBaby(true);
                    }

                    serverLevel.addFreshEntity(zombie);

                    serverLevel.playSound(
                            null,
                            pig.blockPosition(),
                            SoundEvents.ZOMBIE_INFECT,
                            SoundSource.HOSTILE,
                            1.0F,
                            1.0F
                    );
                }

                pig.discard();
            }
        }

        return hit;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.ZOMBIE_PIG_AMBIENT.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return ModSounds.ZOMBIE_PIG_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.ZOMBIE_PIG_DEATH.get();
    }

    @Override
    protected SoundEvent getStepSound() {
        return SoundEvents.PIG_STEP;
    }
}




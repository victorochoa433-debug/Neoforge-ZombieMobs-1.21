package net.vykket.zombiemobs.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.vykket.zombiemobs.Config;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import net.vykket.zombiemobs.registry.ModSounds;
import net.vykket.zombiemobs.registry.ModEntities;

public class GelidRabbitEntity extends Rabbit implements Enemy {

    public GelidRabbitEntity(EntityType<? extends Rabbit> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.getAvailableGoals().clear();
        this.targetSelector.getAvailableGoals().clear();

        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(3, new MoveThroughVillageGoal(
                this,
                1.0D,
                false,
                10,
                () -> false
        ));

        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2D, true));

        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Villager.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        if (Config.ZOMBIES_ATTACK_LIVING_VARIANTS.get()) {
            this.targetSelector.addGoal(2,
                    new NearestAttackableTargetGoal<>(
                            this,
                            Rabbit.class,
                            true,
                            target -> !(target instanceof GelidRabbitEntity) &&
                                    !(target instanceof ZombieRabbitEntity) &&
                                    !(target instanceof HuskRabbitEntity)
                    )
            );
        }
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Zombie.createAttributes()
                .add(Attributes.MAX_HEALTH, 3.0D)
                .add(Attributes.ATTACK_DAMAGE, 1.5D)
                .add(Attributes.MOVEMENT_SPEED, 0.28D);
    }

    @Override
    public boolean doHurtTarget(Entity target) {

        boolean success = super.doHurtTarget(target);

        if (!this.level().isClientSide() && success) {

            if (target instanceof LivingEntity living && living.isAlive()) {

                int freezeTicks = Config.FREEZE_TICKS.get();

                living.setTicksFrozen(
                        living.getTicksFrozen() + freezeTicks
                );
            }

            if (target instanceof Rabbit rabbit && !rabbit.isAlive()) {

                Difficulty difficulty = this.level().getDifficulty();

                boolean shouldInfect = switch (difficulty) {
                    case EASY -> false;
                    case NORMAL -> this.random.nextBoolean();
                    case HARD -> true;
                    default -> false;
                };

                if (!shouldInfect) return success;

                if (this.level() instanceof ServerLevel serverLevel) {

                    GelidRabbitEntity zombie =
                            ModEntities.GELID_RABBIT.get().create(serverLevel);

                    if (zombie != null) {

                        zombie.moveTo(
                                rabbit.getX(),
                                rabbit.getY(),
                                rabbit.getZ(),
                                rabbit.getYRot(),
                                rabbit.getXRot()
                        );

                        if (rabbit.isBaby()) {
                            zombie.setBaby(true);
                        }

                        serverLevel.addFreshEntity(zombie);

                        serverLevel.playSound(
                                null,
                                rabbit.blockPosition(),
                                SoundEvents.ZOMBIE_INFECT,
                                SoundSource.HOSTILE,
                                1.0F,
                                1.0F
                        );

                        rabbit.discard();
                    }
                }
            }
        }

        return success;
    }

    @Override
    public void checkDespawn() {
        if (this.level().getDifficulty() == Difficulty.PEACEFUL) {
            this.discard();
            return;
        }

        Player nearestPlayer = this.level().getNearestPlayer(this, -1.0D);

        if (nearestPlayer != null) {
            double distanceSqr = nearestPlayer.distanceToSqr(this);

            if (distanceSqr > 16384.0D) {
                this.discard();
            }

            if (distanceSqr > 1024.0D && this.random.nextInt(800) == 0) {
                this.discard();
            }
        }
    }

    @Override
    public void die(DamageSource damageSource) {
        BlockPos deathPos = this.blockPosition();

        super.die(damageSource);

        if (!this.level().isClientSide && this.level() instanceof ServerLevel serverLevel) {

            if (!Config.FREEZE_ON_DEATH.get()) return;

            int freezeTicks = 240;

            serverLevel.sendParticles(
                    ParticleTypes.SNOWFLAKE,
                    deathPos.getX() + 0.5,
                    deathPos.getY() + 0.5,
                    deathPos.getZ() + 0.5,
                    10,
                    0.6, 0.6, 0.6,
                    0.05
            );

            AABB box = new AABB(deathPos).inflate(0.5);

            List<LivingEntity> entities =
                    serverLevel.getEntitiesOfClass(LivingEntity.class, box);

            for (LivingEntity entity : entities) {
                if (!entity.isAlive()) continue;

                // 2 segundos exactos
                entity.addEffect(new MobEffectInstance(
                        MobEffects.MOVEMENT_SLOWDOWN,
                        80,
                        1
                ));

                entity.setTicksFrozen(freezeTicks);
            }
        }
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
    public boolean shouldDespawnInPeaceful() {
        return true;
    }

    @Override
    public GelidRabbitEntity getBreedOffspring(ServerLevel level, AgeableMob partner) {
        return ModEntities.GELID_RABBIT.get().create(level);
    }

    @Override
    public void setAge(int age) {
        if (age >= 0) return;
        super.setAge(age);
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (!this.level().isClientSide && this.isAlive()
                && Config.CAN_BURN_IN_SUN.get()) {

            if (this.isSunBurnTick()) {
                this.igniteForSeconds(8);
            }
        }

        if (!this.level().isClientSide) {

            if (this.getNavigation().isInProgress()
                    && this.onGround()
                    && this.horizontalCollision) {

                this.setDeltaMovement(
                        this.getDeltaMovement().add(0.0D, 0.42D, 0.0D)
                );
            }
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.GELID_RABBIT_AMBIENT.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return ModSounds.GELID_RABBIT_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.GELID_RABBIT_DEATH.get();
    }

    @Override
    protected SoundEvent getJumpSound() {
        return SoundEvents.RABBIT_JUMP;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.isInWater()) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D));
            this.setOnGround(true);
            this.setNoGravity(true);
        } else {
            this.setNoGravity(false);
        }
    }
}


package net.vykket.zombiemobs.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.server.level.ServerLevel; // <-- NUEVO
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.vykket.zombiemobs.Config;
import org.jetbrains.annotations.Nullable;

import net.vykket.zombiemobs.registry.ModSounds;
import net.vykket.zombiemobs.registry.ModEntities;

public class ZombieRabbitEntity extends Rabbit implements Enemy {

    private int stuckTicks = 0;

    public ZombieRabbitEntity(EntityType<? extends Rabbit> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.getAvailableGoals().clear();
        this.targetSelector.getAvailableGoals().clear();
        this.goalSelector.addGoal(7,
                new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(8,
                new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8,
                new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(3, new MoveThroughVillageGoal(
                this,
                1.0D,
                false,
                10,
                () -> false
        ));
        this.goalSelector.addGoal(2,
                new MeleeAttackGoal(this, 1.2D, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Villager.class, true));
        this.targetSelector.addGoal(2,
                new NearestAttackableTargetGoal<>(
                        this,
                        LivingEntity.class,
                        10,
                        true,
                        false,
                        entity -> {
                            if (entity == null) return false;

                            return entity.getType()
                                    .builtInRegistryHolder()
                                    .key()
                                    .location()
                                    .toString()
                                    .equals("guardvillagers:guard");
                        }
                )
        );


        this.targetSelector.addGoal(2,
                new NearestAttackableTargetGoal<>(this, Villager.class, true));
        this.targetSelector.addGoal(2,
                new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3,
                new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        if (Config.ZOMBIES_ATTACK_LIVING_VARIANTS.get()) {
            this.targetSelector.addGoal(2,
                    new NearestAttackableTargetGoal<>(
                            this,
                            Rabbit.class,
                            true,
                            target -> !(target instanceof ZombieRabbitEntity) &&
                                    !(target instanceof GelidRabbitEntity) &&
                                    !(target instanceof HuskRabbitEntity)
                    )
            );
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Zombie.createAttributes()
                .add(Attributes.MAX_HEALTH, 3.0D)
                .add(Attributes.ATTACK_DAMAGE, 1.5D)
                .add(Attributes.MOVEMENT_SPEED, 0.30D);
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
    public boolean shouldDespawnInPeaceful() {
        return true;
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        boolean success = super.doHurtTarget(target);

        if (success
                && !this.level().isClientSide()
                && target instanceof Rabbit rabbit) {

            if (!rabbit.isAlive() || rabbit.getHealth() <= 0.0F) {

                Difficulty difficulty = this.level().getDifficulty();
                boolean shouldInfect = false;

                switch (difficulty) {
                    case EASY -> shouldInfect = false;
                    case NORMAL -> shouldInfect = this.random.nextFloat() < 0.3F;
                    case HARD -> shouldInfect = true;
                }

                if (!shouldInfect) return success;

                ServerLevel serverLevel = (ServerLevel) this.level();

                ZombieRabbitEntity zombie =
                        ModEntities.ZOMBIE_RABBIT.get().create(serverLevel);

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
                }

                rabbit.discard();
            }
        }

        return success;
    }

    @Override
    public ZombieRabbitEntity getBreedOffspring(ServerLevel level, AgeableMob partner) {
        return ModEntities.ZOMBIE_RABBIT.get().create(level);
    }

    @Override
    public void setAge(int age) {
        if (age >= 0) {
            return;
        }
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
        return ModSounds.ZOMBIE_RABBIT_AMBIENT.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return ModSounds.ZOMBIE_RABBIT_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.ZOMBIE_RABBIT_DEATH.get();
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







package net.vykket.zombiemobs.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.vykket.zombiemobs.Config;
import org.jetbrains.annotations.Nullable;

import net.vykket.zombiemobs.registry.ModSounds;
import net.vykket.zombiemobs.registry.ModEntities;

import static net.minecraft.world.Difficulty.EASY;

public class ZombieChickenEntity extends Chicken implements Enemy {

    public ZombieChickenEntity(EntityType<? extends Chicken> type, Level level) {
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

        this.targetSelector.addGoal(4,
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

        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Villager.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        if (Config.ZOMBIES_ATTACK_LIVING_VARIANTS.get()) {
            this.targetSelector.addGoal(2,
                    new NearestAttackableTargetGoal<>(
                            this,
                            Chicken.class,
                            true,
                            target -> !(target instanceof ZombieChickenEntity)
                    )
            );
        }

        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Zombie.createAttributes()
                .add(Attributes.MAX_HEALTH, 4.0D)
                .add(Attributes.ATTACK_DAMAGE, 1.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.23D);
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
        return false;
    }

    @Override
    public SpawnGroupData finalizeSpawn(
            ServerLevelAccessor level,
            DifficultyInstance difficulty,
            MobSpawnType reason,
            @Nullable SpawnGroupData spawnData
    ) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, spawnData);

        if (!level.isClientSide() && level.getRandom().nextFloat() < 0.03F) { // 3%
            Zombie zombie = EntityType.ZOMBIE.create(level.getLevel());

            if (zombie != null) {
                zombie.moveTo(
                        this.getX(),
                        this.getY(),
                        this.getZ(),
                        this.getYRot(),
                        this.getXRot()
                );

                zombie.setBaby(true);
                zombie.setCanPickUpLoot(false);

                zombie.finalizeSpawn(level, difficulty, reason, null);
                zombie.startRiding(this, true);
                level.addFreshEntity(zombie);
            }
        }

        if (this.isPassenger()) {
            this.stopRiding();
        }

        return data;
    }

    @Override
    protected boolean isFlapping() {
        return !this.onGround() && this.getDeltaMovement().y < -0.08D;
    }

    @Override
    public ZombieChickenEntity getBreedOffspring(ServerLevel level, AgeableMob partner) {
        return ModEntities.ZOMBIE_CHICKEN.get().create(level);
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

        this.eggTime = Integer.MAX_VALUE;

        if (!this.level().isClientSide
                && this.isAlive()
                && Config.CAN_BURN_IN_SUN.get()
                && this.isSunBurnTick()) {

            this.igniteForSeconds(8);
        }
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
        boolean result = super.doHurtTarget(target);

        if (result) {

            if (!this.level().isClientSide()
                    && target instanceof Chicken chicken
                    && !(chicken instanceof Enemy)) {

                float finalHealth = chicken.getHealth()
                        - (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);

                if (finalHealth <= 0.0F) {

                    Difficulty difficulty = this.level().getDifficulty();
                    boolean shouldInfect = false;

                    switch (difficulty) {
                        case EASY -> shouldInfect = false;
                        case NORMAL -> shouldInfect = this.random.nextBoolean();
                        case HARD -> shouldInfect = true;
                    }

                    if (!shouldInfect) {
                        return result;
                    }

                    ServerLevel serverLevel = (ServerLevel) this.level();

                    chicken.setHealth(1.0F);

                    ZombieChickenEntity zombie =
                            ModEntities.ZOMBIE_CHICKEN.get().create(serverLevel);

                    if (zombie != null) {

                        zombie.moveTo(
                                chicken.getX(),
                                chicken.getY(),
                                chicken.getZ(),
                                chicken.getYRot(),
                                chicken.getXRot()
                        );

                        if (chicken.isBaby()) {
                            zombie.setBaby(true);
                        }

                        serverLevel.addFreshEntity(zombie);

                        serverLevel.playSound(
                                null,
                                chicken.blockPosition(),
                                SoundEvents.ZOMBIE_INFECT,
                                SoundSource.HOSTILE,
                                1.0F,
                                1.0F
                        );
                    }

                    chicken.discard();
                }
            }

            if (this.onGround() && !this.isInWater()) {

                double forwardStrength = 0.25D;
                double upward = 0.5D;

                double x = -Math.sin(Math.toRadians(this.getYRot())) * forwardStrength;
                double z =  Math.cos(Math.toRadians(this.getYRot())) * forwardStrength;

                this.setDeltaMovement(
                        this.getDeltaMovement().add(x, upward, z)
                );

                this.hurtMarked = true;
            }
        }

        return result;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.CHICKEN_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.CHICKEN_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.CHICKEN_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.CHICKEN_STEP, 0.15F, 0.7F);
    }

    @Override
    public float getVoicePitch() {
        return 0.8F;
    }
}




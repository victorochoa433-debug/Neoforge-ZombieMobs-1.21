package net.vykket.zombiemobs.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.Dolphin;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.vykket.zombiemobs.Config;
import net.vykket.zombiemobs.registry.ModEntities;
import net.vykket.zombiemobs.registry.ModSounds;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ZombieDolphinEntity extends Dolphin implements Enemy {

    public ZombieDolphinEntity(EntityType<? extends Dolphin> type, Level level) {
        super(type, level);
    }

    private int randomTurnTime = 0;
    private int pauseTime = 0;
    private BlockPos spawnOrigin;
    private static final int MAX_RADIUS = 32;

    @Override
    protected void registerGoals() {

        super.registerGoals();

        this.goalSelector.getAvailableGoals().removeIf(goal ->
                goal.getGoal().getClass().getSimpleName().contains("PlayWithItems"));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.3D, true));
        this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.02F, 0.1F, true);
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1,
                new NearestAttackableTargetGoal<>(this, Player.class, true));

        if (Config.ZOMBIES_ATTACK_LIVING_VARIANTS.get()) {
            this.targetSelector.addGoal(2,
                    new NearestAttackableTargetGoal<>(
                            this,
                            Dolphin.class,
                            true,
                            target -> !(target instanceof ZombieDolphinEntity)
                    )
            );
        }
    }
    public static AttributeSupplier.Builder createAttributes() {
        return Dolphin.createAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.35D);
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        boolean success = super.doHurtTarget(target);

        if (success
                && !this.level().isClientSide()
                && target instanceof Dolphin dolphin) {

            if (!dolphin.isAlive() || dolphin.getHealth() <= 0.0F) {

                Difficulty difficulty = this.level().getDifficulty();
                boolean shouldInfect = false;

                switch (difficulty) {
                    case EASY -> shouldInfect = false;
                    case NORMAL -> shouldInfect = this.random.nextFloat() < 0.4F;
                    case HARD -> shouldInfect = true;
                }

                if (!shouldInfect) return success;

                ServerLevel serverLevel = (ServerLevel) this.level();

                ZombieDolphinEntity zombie =
                        ModEntities.ZOMBIE_DOLPHIN.get().create(serverLevel);

                if (zombie != null) {

                    zombie.moveTo(
                            dolphin.getX(),
                            dolphin.getY(),
                            dolphin.getZ(),
                            dolphin.getYRot(),
                            dolphin.getXRot()
                    );

                    serverLevel.addFreshEntity(zombie);

                    serverLevel.playSound(
                            null,
                            dolphin.blockPosition(),
                            SoundEvents.ZOMBIE_INFECT,
                            SoundSource.HOSTILE,
                            1.0F,
                            1.0F
                    );
                }

                dolphin.discard();
            }
        }

        return success;
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new WaterBoundPathNavigation(this, level);
    }

    @Override
    public void aiStep() {
        super.aiStep();

            if (this.isPassenger()) {
                this.setSwimming(false);
            }

        if (!this.level().isClientSide && this.isAlive()) {

            if (!this.isInWater() && Config.CAN_BURN_IN_SUN.get()) {
                if (this.level().isDay()
                        && this.level().canSeeSky(this.blockPosition())) {
                    this.igniteForSeconds(8);
                }
            }
            if (this.isInWater()) {

                if (spawnOrigin == null) {
                    spawnOrigin = this.blockPosition();
                }

                if (spawnOrigin != null) {

                    double distSqr = this.blockPosition().distSqr(spawnOrigin);

                    boolean hasTarget = this.getTarget() != null && this.getTarget().isAlive();

                    if (!hasTarget && distSqr > (MAX_RADIUS * MAX_RADIUS)) {

                        double dx = spawnOrigin.getX() - this.getX();
                        double dz = spawnOrigin.getZ() - this.getZ();

                        float targetYaw = (float)(Math.atan2(dz, dx) * (180F / Math.PI)) - 90F;

                        this.setYRot(targetYaw);
                    }
                }

                if (pauseTime > 0) {
                    pauseTime--;

                    Vec3 current = this.getDeltaMovement();
                    this.setDeltaMovement(current.scale(0.85D));
                    return;
                }

                Vec3 movement = this.getLookAngle().scale(0.18D);
                this.setDeltaMovement(movement);
                if (randomTurnTime <= 0) {

                    this.setYRot(this.getYRot() + (this.random.nextFloat() * 80F - 40F));
                    this.setXRot(this.getXRot() + (this.random.nextFloat() * 20F - 10F));

                    randomTurnTime = 40 + this.random.nextInt(60);
                } else {
                    randomTurnTime--;
                }

                Vec3 look = this.getLookAngle();
                BlockPos frontPos = BlockPos.containing(
                        this.getX() + look.x * 1.2D,
                        this.getY() + look.y * 1.2D,
                        this.getZ() + look.z * 1.2D
                );

                BlockState state = this.level().getBlockState(frontPos);

                if (state.blocksMotion() || this.horizontalCollision) {
                    this.setYRot(this.getYRot() + (this.random.nextFloat() * 140F - 70F));
                    this.setXRot(this.random.nextFloat() * 30F - 15F);
                    randomTurnTime = 40;
                }
                if (this.random.nextInt(300) == 0) {
                    pauseTime = 40 + this.random.nextInt(80);
                }
                List<Drowned> drownedList = this.level().getEntitiesOfClass(
                        Drowned.class,
                        this.getBoundingBox().inflate(8.0D)
                );
                for (Drowned drowned : drownedList) {
                    drowned.addEffect(new MobEffectInstance(
                            MobEffects.MOVEMENT_SPEED,
                            100,
                            1,
                            true,
                            false
                    ));
                }
            }
        }
    }

    @Override
    public boolean shouldRiderSit() {
        return true;
    }

    @Override
    public boolean wantsToPickUp(ItemStack stack) {
        return false;
    }

    @Override
    protected void pickUpItem(ItemEntity itemEntity) {
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
    public boolean isVisuallySwimming() {
        return !this.isPassenger() && super.isVisuallySwimming();
    }


    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide()) {
            for (Entity passenger : this.getPassengers()) {

                if (passenger instanceof Drowned drowned) {

                    drowned.setSwimming(false);
                    drowned.setSprinting(false);

                    if (drowned.isInWater()) {
                        drowned.setAirSupply(drowned.getMaxAirSupply());
                    }

                    drowned.setPose(Pose.STANDING);

                    drowned.yBodyRot = this.yBodyRot;
                    drowned.setYRot(this.getYRot());
                    drowned.setYHeadRot(this.getYRot());
                }
            }
        }
    }

    @Override
    protected void positionRider(Entity passenger, MoveFunction moveFunction) {
        super.positionRider(passenger, moveFunction);

        if (passenger instanceof RiderDrownedEntity) {

            double yOffset = 0.1D;
            moveFunction.accept(
                    passenger,
                    this.getX(),
                    this.getY() + yOffset,
                    this.getZ()
            );
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

        if (!level.isClientSide() && level.getRandom().nextFloat() < 0.10F) { // 10% jockey

            RiderDrownedEntity rider =
                    ModEntities.RIDER_DROWNED.get().create(level.getLevel());

            if (rider != null) {

                rider.moveTo(
                        this.getX(),
                        this.getY(),
                        this.getZ(),
                        this.getYRot(),
                        this.getXRot()
                );

                rider.finalizeSpawn(level, difficulty, reason, null);

                rider.setBaby(false);

                rider.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.TRIDENT));
                rider.setDropChance(EquipmentSlot.MAINHAND, 0.0F);

                rider.startRiding(this, true);

                level.addFreshEntity(rider);
            }
        }

        return data;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.DOLPHIN_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.DOLPHIN_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.DOLPHIN_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.DOLPHIN_SWIM, 0.15F, 0.7F);
    }

    @Override
    public float getVoicePitch() {
        return 0.8F;
    }
}


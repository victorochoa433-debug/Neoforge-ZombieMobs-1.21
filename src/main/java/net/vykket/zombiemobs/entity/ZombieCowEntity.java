package net.vykket.zombiemobs.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.vykket.zombiemobs.Config;
import net.vykket.zombiemobs.entity.ai.ZombieCowBreakDoorGoal;
import org.jetbrains.annotations.Nullable;

import net.vykket.zombiemobs.registry.ModSounds;
import net.vykket.zombiemobs.registry.ModEntities;

public class ZombieCowEntity extends Cow implements Enemy {

    private int headLowerTicks = 0;

    public void triggerHeadbuttAnimation() {
        this.headLowerTicks = 6;
    }

    public ZombieCowEntity(EntityType<? extends Cow> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.getAvailableGoals().clear();
        this.targetSelector.getAvailableGoals().clear();

        if (Config.ZOMBIE_COW_BREAK_DOORS.get()) {
            this.goalSelector.addGoal(1, new ZombieCowBreakDoorGoal(this));
        }
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

        this.goalSelector.addGoal(
                1,
                new BreakDoorGoal(
                        this,
                        difficulty -> difficulty == Difficulty.NORMAL || difficulty == Difficulty.HARD
                )
        );

        this.targetSelector.addGoal(2,
                new NearestAttackableTargetGoal<>(this, Villager.class, true));

        if (Config.ZOMBIES_ATTACK_LIVING_VARIANTS.get()) {
            this.targetSelector.addGoal(2,
                    new NearestAttackableTargetGoal<>(
                            this,
                            Cow.class,
                            true,
                            target -> !(target instanceof ZombieCowEntity)
                    )
            );
        }

        this.targetSelector.addGoal(1,
                new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3,
                new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Zombie.createAttributes()
                .add(Attributes.MAX_HEALTH, 15.0D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 1.5D)
                .add(Attributes.MOVEMENT_SPEED, 0.23D);
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
    public ZombieCowEntity getBreedOffspring(ServerLevel level, AgeableMob partner) {
        return ModEntities.ZOMBIE_COW.get().create(level);
    }

    @Override
    public void setAge(int age) {
        if (age >= 0) {
            return;
        }
        super.setAge(age);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        GroundPathNavigation navigation = new GroundPathNavigation(this, level);
        navigation.setCanOpenDoors(true);
        return navigation;
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

        if (headLowerTicks > 0) {
            headLowerTicks--;
            this.setXRot(122.0F);
        } else {
            this.setXRot(this.getXRot() * 0.6F);
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
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (player.getItemInHand(hand).is(Items.BUCKET)) {
            return InteractionResult.FAIL;
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        boolean hit = super.doHurtTarget(target);

        if (hit) {

            if (!this.level().isClientSide()
                    && target instanceof Cow cow
                    && !(cow instanceof Enemy)) {

                float finalHealth = cow.getHealth()
                        - (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);

                if (finalHealth <= 0.0F) {

                    Difficulty difficulty = this.level().getDifficulty();
                    boolean shouldInfect = false;

                    switch (difficulty) {
                        case EASY -> shouldInfect = false;
                        case NORMAL -> shouldInfect = this.random.nextBoolean();
                        case HARD -> shouldInfect = true;
                    }

                    if (shouldInfect) {

                        ServerLevel serverLevel = (ServerLevel) this.level();

                        cow.setHealth(1.0F);

                        ZombieCowEntity zombie =
                                ModEntities.ZOMBIE_COW.get().create(serverLevel);

                        if (zombie != null) {

                            zombie.moveTo(
                                    cow.getX(),
                                    cow.getY(),
                                    cow.getZ(),
                                    cow.getYRot(),
                                    cow.getXRot()
                            );

                            if (cow.isBaby()) {
                                zombie.setBaby(true);
                            }

                            serverLevel.addFreshEntity(zombie);

                            serverLevel.playSound(
                                    null,
                                    cow.blockPosition(),
                                    SoundEvents.ZOMBIE_INFECT,
                                    SoundSource.HOSTILE,
                                    1.0F,
                                    1.0F
                            );
                        }

                        cow.discard();
                    }
                }
            }

            Vec3 look = this.getLookAngle();

            this.setDeltaMovement(
                    this.getDeltaMovement().add(
                            look.x * 0.45D,
                            0.0D,
                            look.z * 0.45D
                    )
            );

            headLowerTicks = 6;
        }

        return hit;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.COW_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.COW_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.COW_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.COW_STEP, 0.15F, 0.7F);
    }

    @Override
    public float getVoicePitch() {
        return 0.8F;
    }
}



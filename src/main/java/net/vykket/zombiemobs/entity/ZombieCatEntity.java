package net.vykket.zombiemobs.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.OcelotAttackGoal;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.server.level.ServerLevel;
import net.vykket.zombiemobs.Config;
import org.jetbrains.annotations.Nullable;

import net.vykket.zombiemobs.registry.ModEntities;
import net.vykket.zombiemobs.registry.ModSounds;

public class ZombieCatEntity extends Cat implements Enemy {

    public ZombieCatEntity(EntityType<? extends Cat> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.getAvailableGoals().clear();
        this.targetSelector.getAvailableGoals().clear();

        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Villager.class, true));
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

        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Villager.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        this.targetSelector.addGoal(3,
                new NearestAttackableTargetGoal<>(
                        this,
                        Rabbit.class,
                        true,
                        rabbit -> !(rabbit instanceof Enemy)
                )
        );

        this.targetSelector.addGoal(2,
                new NearestAttackableTargetGoal<>(
                        this,
                        Cat.class,
                        true,
                        cat -> !(cat instanceof Enemy)
                )
        );


        this.goalSelector.addGoal(0,
                new OcelotAttackGoal(this));

        this.goalSelector.addGoal(0,
                new LeapAtTargetGoal(this, 0.4F));

        this.goalSelector.addGoal(0,
                new MeleeAttackGoal(this, 1.2D, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Zombie.createAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.ATTACK_DAMAGE, 2.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    @Override
    public ZombieCatEntity getBreedOffspring(ServerLevel level, AgeableMob partner) {
        return ModEntities.ZOMBIE_CAT.get().create(level);
    }

    @Override
    public void setAge(int age) {
        if (age >= 0) {
            return;
        }
        super.setAge(age);
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float damageMultiplier, DamageSource source) {
        return false;
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
    public boolean doHurtTarget(Entity target) {
        boolean result = super.doHurtTarget(target);

        if (result
                && !this.level().isClientSide()
                && target instanceof Cat cat
                && !(cat instanceof Enemy)) {

            float finalHealth = cat.getHealth()
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

                cat.setHealth(1.0F);

                ZombieCatEntity zombie =
                        ModEntities.ZOMBIE_CAT.get().create(serverLevel);

                if (zombie != null) {

                    zombie.moveTo(
                            cat.getX(),
                            cat.getY(),
                            cat.getZ(),
                            cat.getYRot(),
                            cat.getXRot()
                    );

                    if (cat.isBaby()) {
                        zombie.setBaby(true);
                    }

                    serverLevel.addFreshEntity(zombie);

                    serverLevel.playSound(
                            null,
                            cat.blockPosition(),
                            SoundEvents.ZOMBIE_INFECT,
                            SoundSource.HOSTILE,
                            1.0F,
                            1.0F
                    );
                }

                cat.discard();
            }
        }

        return result;
    }


    @Override
    public boolean shouldDespawnInPeaceful() {
        return true;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.CAT_HISS;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.CAT_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.CAT_DEATH;
    }

    @Override
    public float getVoicePitch() {
        return 0.7F;
    }
}


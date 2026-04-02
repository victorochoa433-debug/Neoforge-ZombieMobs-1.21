package net.vykket.zombiemobs.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
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
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.vykket.zombiemobs.Config;
import net.vykket.zombiemobs.registry.ModEntities;
import net.vykket.zombiemobs.registry.ModTags;
import org.jetbrains.annotations.Nullable;

import net.vykket.zombiemobs.registry.ModSounds;

import java.util.Comparator;
import java.util.List;

public class ZombiePigEntity extends Zombie implements Enemy {

    private int eatCooldown = 0;

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
    public boolean canPickUpLoot() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) return;

        if (!this.isAlive() || this.isDeadOrDying()) return;

        if (eatCooldown > 0) {
            eatCooldown--;
            return;
        }

        if (this.getHealth() >= this.getMaxHealth()) return;

        double radius = 7.0D;

        List<ItemEntity> items = this.level().getEntitiesOfClass(
                ItemEntity.class,
                this.getBoundingBox().inflate(radius),
                item -> item.getItem().is(ModTags.ZOMBIE_PIG_FOOD)
        );

        if (items.isEmpty()) return;

        ItemEntity targetItem = null;

        for (ItemEntity item : items) {

            if (Math.abs(item.getY() - this.getY()) > 2.0D) continue;
            if (this.distanceTo(item) > radius) continue;
            if (!this.getSensing().hasLineOfSight(item)) continue;

            targetItem = item;
            break;
        }

        if (targetItem == null) return;

        if (this.getHealth() < this.getMaxHealth()) {
            this.setTarget(null);
        }

        this.getNavigation().moveTo(
                targetItem.getX(),
                targetItem.getY(),
                targetItem.getZ(),
                1.2D
        );

        this.getLookControl().setLookAt(
                targetItem,
                30.0F,
                30.0F
        );

        if (this.distanceTo(targetItem) < 1.6D) {

            ItemStack stackCopy = targetItem.getItem().copy();

            this.heal(4.0F);

            targetItem.getItem().shrink(1);

            if (targetItem.getItem().isEmpty()) {
                targetItem.discard();
            }

            this.playSound(
                    SoundEvents.GENERIC_EAT,
                    0.8F,
                    1.0F
            );

            if (this.level() instanceof ServerLevel serverLevel && !stackCopy.isEmpty()) {
                serverLevel.sendParticles(
                        new ItemParticleOption(
                                ParticleTypes.ITEM,
                                stackCopy
                        ),
                        this.getX(),
                        this.getY() + 1.0D,
                        this.getZ(),
                        10,
                        0.2D, 0.2D, 0.2D,
                        0.05D
                );
            }

            eatCooldown = 40;
        }
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




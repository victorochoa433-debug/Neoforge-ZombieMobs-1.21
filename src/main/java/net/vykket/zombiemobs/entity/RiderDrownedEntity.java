package net.vykket.zombiemobs.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.vykket.zombiemobs.Config;
import net.vykket.zombiemobs.registry.ModSounds;
import org.jetbrains.annotations.Nullable;

public class RiderDrownedEntity extends Drowned implements Enemy {

    public RiderDrownedEntity(EntityType<? extends Drowned> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {

        super.registerGoals();

        // Targets
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
        return Drowned.createAttributes()
                .add(Attributes.MAX_HEALTH, 25.0D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.23D);
    }

    @Override
    public boolean isSwimming() {
        return !this.isPassenger() && super.isSwimming();
    }

    @Override
    public boolean isVisuallySwimming() {
        return !this.isPassenger() && super.isVisuallySwimming();
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
    protected SoundEvent getAmbientSound() {
        return SoundEvents.DROWNED_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.DROWNED_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.DROWNED_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.DROWNED_STEP, 0.15F, 1.0F);
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (this.isPassenger()) {
            this.setAirSupply(this.getMaxAirSupply());
            this.setNoGravity(true);
        } else {
            this.setNoGravity(false);
        }
    }
}

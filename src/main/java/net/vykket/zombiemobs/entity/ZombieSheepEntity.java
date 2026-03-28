package net.vykket.zombiemobs.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import net.vykket.zombiemobs.Config;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.vykket.zombiemobs.particles.ModParticles;
import org.jetbrains.annotations.Nullable;

import net.vykket.zombiemobs.registry.ModSounds;
import net.vykket.zombiemobs.registry.ModEntities;

import java.util.List;

public class ZombieSheepEntity extends Sheep implements Enemy {

    private EatBlockGoal eatBlockGoal;
    private int screamCharge = 0;
    private int screamAnimationTicks = 0;
    private static final int SCREAM_DURATION = 40;

    public ZombieSheepEntity(EntityType<? extends Sheep> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();

        this.eatBlockGoal = new EatBlockGoal(this);
        this.goalSelector.addGoal(5, this.eatBlockGoal);

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

        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.1D, true));

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
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        if (Config.ZOMBIES_ATTACK_LIVING_VARIANTS.get()) {
            this.targetSelector.addGoal(2,
                    new NearestAttackableTargetGoal<>(
                            this,
                            Sheep.class,
                            true,
                            target -> !(target instanceof ZombieSheepEntity)
                    )
            );
        }

    }
    public static AttributeSupplier.Builder createAttributes() {
        return Zombie.createAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.23D);
    }
    @Override
    public void aiStep() {
        super.aiStep();

        if (!this.level().isClientSide && this.isAlive()
                && Config.CAN_BURN_IN_SUN.get()) {

            // Quemarse al sol
            if (this.isSunBurnTick()) {
                this.igniteForSeconds(8);
            }
            if (screamAnimationTicks > 0) {

                screamAnimationTicks--;

                this.getNavigation().stop();

                this.setDeltaMovement(0, this.getDeltaMovement().y, 0);

                if (this.tickCount % 2 == 0) {

                    ((ServerLevel) this.level()).sendParticles(
                            ModParticles.SCREAM.get(),
                            this.getX(),
                            this.getY() + 1.5D,
                            this.getZ(),
                            1,
                            0.4D,
                            0.3D,
                            0.4D,
                            0.0D
                    );
                }

                return;
            }
            if (!Config.ENABLE_SCREAM_ABILITY.get()) {
                screamCharge = 0;
                return;
            }

            if (this.getTarget() != null) {

                screamCharge++;

                if (screamCharge >= Config.SCREAM_COOLDOWN_SECONDS.get() * 20) {
                    performScream();
                    screamCharge = 0;
                }

            } else {
                screamCharge = 0;
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
    public ResourceKey<LootTable> getDefaultLootTable() {
        return BuiltInLootTables.EMPTY;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level,
                                       DamageSource damageSource,
                                       boolean recentlyHit) {

        int looting = 0;

        if (damageSource.getEntity() instanceof LivingEntity living) {

            var enchantmentRegistry = level.registryAccess()
                    .registryOrThrow(Registries.ENCHANTMENT);

            var lootingHolder = enchantmentRegistry.getHolderOrThrow(Enchantments.LOOTING);

            looting = EnchantmentHelper.getItemEnchantmentLevel(
                    lootingHolder,
                    living.getMainHandItem()
            );
        }

        int rottenFleshAmount = this.random.nextInt(4 + looting);
        if (rottenFleshAmount > 0) {
            this.spawnAtLocation(new ItemStack(Items.ROTTEN_FLESH, rottenFleshAmount));
        }

        int woolAmount = this.random.nextInt(3 + looting / 2);
        if (woolAmount > 0) {
            this.spawnAtLocation(new ItemStack(Items.GREEN_WOOL, woolAmount));
        }
    }

    private void performScream() {

        screamAnimationTicks = SCREAM_DURATION;

        this.level().playSound(
                null,
                this.blockPosition(),
                ModSounds.ZOMBIE_SHEEP_SCREAM.get(),
                this.getSoundSource(),
                2.0F,
                1.0F
        );
        double radius = Config.SCREAM_RADIUS.get();

        List<Mob> mobs = this.level().getEntitiesOfClass(
                Mob.class,
                this.getBoundingBox().inflate(radius),
                mob -> mob instanceof Enemy && mob != this
                        && mob != this
                        && !(mob instanceof EnderMan)
                        && !(mob instanceof Warden)
                        && !(mob instanceof EnderDragon)
                        && !(mob instanceof ZombifiedPiglin)
                        && !(mob instanceof Creeper)
                        && !(mob instanceof WitherBoss)
                        && !(mob instanceof Ghast)
        );
        for (Mob mob : mobs) {
            if (this.getTarget() != null) {
                mob.setTarget(this.getTarget());
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
        spawnData = super.finalizeSpawn(level, difficulty, reason, spawnData);

        if (reason == MobSpawnType.NATURAL) {

            if (this.random.nextBoolean()) {
                this.setSheared(true);
            }
        }

        return spawnData;
    }


    @Override
    public boolean shouldDespawnInPeaceful() {
        return true;
    }

    @Override
    public ZombieSheepEntity getBreedOffspring(ServerLevel level, AgeableMob partner) {
        return ModEntities.ZOMBIE_SHEEP.get().create(level);
    }

    @Override
    public void shear(SoundSource source) {
        this.level().playSound(
                null,
                this,
                SoundEvents.SHEEP_SHEAR,
                source,
                1.0F,
                1.0F
        );

        this.setSheared(true);

        if (!this.level().isClientSide) {
            int count = 1 + this.random.nextInt(3);

            for (int i = 0; i < count; ++i) {

                ItemEntity itemEntity = this.spawnAtLocation(
                        new ItemStack(Items.GREEN_WOOL),
                        1.0F
                );

                if (itemEntity != null) {
                    itemEntity.setDeltaMovement(
                            itemEntity.getDeltaMovement().add(
                                    (this.random.nextFloat() - this.random.nextFloat()) * 0.1F,
                                    this.random.nextFloat() * 0.05F,
                                    (this.random.nextFloat() - this.random.nextFloat()) * 0.1F
                            )
                    );
                }
            }
        }
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        boolean success = super.doHurtTarget(target);

        if (success
                && !this.level().isClientSide()
                && target instanceof Sheep sheep
                && !(sheep instanceof ZombieSheepEntity)) {

            if (!sheep.isAlive() || sheep.getHealth() <= 0.0F) {

                Difficulty difficulty = this.level().getDifficulty();
                boolean shouldInfect = false;

                switch (difficulty) {
                    case EASY -> shouldInfect = false;
                    case NORMAL -> shouldInfect = this.random.nextFloat() < 0.3F;
                    case HARD -> shouldInfect = true;
                }

                if (!shouldInfect) return success;

                ServerLevel serverLevel = (ServerLevel) this.level();

                ZombieSheepEntity zombie =
                        ModEntities.ZOMBIE_SHEEP.get().create(serverLevel);

                if (zombie != null) {

                    zombie.moveTo(
                            sheep.getX(),
                            sheep.getY(),
                            sheep.getZ(),
                            sheep.getYRot(),
                            sheep.getXRot()
                    );

                    if (sheep.isBaby()) {
                        zombie.setBaby(true);
                    }

                    serverLevel.addFreshEntity(zombie);

                    serverLevel.playSound(
                            null,
                            sheep.blockPosition(),
                            SoundEvents.ZOMBIE_INFECT,
                            SoundSource.HOSTILE,
                            1.0F,
                            1.0F
                    );
                }

                sheep.discard();
            }
        }

        return success;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SHEEP_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.SHEEP_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SHEEP_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.SHEEP_STEP, 0.15F, 0.7F);
    }

    @Override
    public float getVoicePitch() {
        return 0.7F;
    }
}



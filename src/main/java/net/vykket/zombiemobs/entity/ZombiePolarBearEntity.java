package net.vykket.zombiemobs.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.monster.Stray;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.ModList;
import net.vykket.zombiemobs.Config;
import org.jetbrains.annotations.Nullable;

import net.vykket.zombiemobs.registry.ModSounds;
import net.vykket.zombiemobs.registry.ModEntities;
import org.joml.Vector3f;

import java.util.List;

public class ZombiePolarBearEntity extends PolarBear implements Enemy {

    public ZombiePolarBearEntity(EntityType<? extends PolarBear> type, Level level) {
        super(type, level);
    }

    @Override
    public LivingEntity getControllingPassenger() {
        LivingEntity rider = super.getControllingPassenger();

        if (rider != null) {
            ResourceLocation id = BuiltInRegistries.ENTITY_TYPE.getKey(rider.getType());

            if (id != null && (
                    (ModList.get().isLoaded("variantsandventures")
                            && id.getNamespace().equals("variantsandventures")
                            && id.getPath().equals("gelid"))
            )) {
                return null;
            }
        }

        return rider;
    }


    @Override
    protected void registerGoals() {
        super.registerGoals();

        this.goalSelector.getAvailableGoals().removeIf(goal -> goal.getGoal() instanceof PanicGoal || goal.getGoal() instanceof FleeSunGoal);
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Villager.class, true));
        this.goalSelector.addGoal(3, new MoveThroughVillageGoal(
                this,
                1.0D,
                false,
                10,
                () -> false
        ));

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
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Villager.class, true));
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, Fox.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Wolf.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        this.targetSelector.addGoal(3,
                new NearestAttackableTargetGoal<>(
                        this,
                        PolarBear.class,
                        true,
                        polarBear -> !(polarBear instanceof Enemy)
                )
        );
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Zombie.createAttributes()
                .add(Attributes.MAX_HEALTH, 50.0D)
                .add(Attributes.ATTACK_DAMAGE, 6.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.6D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.STEP_HEIGHT, 1.3D);
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

        if (result && target instanceof LivingEntity living) {

            if (!Config.POLAR_BEAR_SPECIAL_ATTACK.get()) {
                return result;
            }

            living.setTicksFrozen(living.getTicksFrozen() + 140);
            living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 1));

            if (living instanceof Player player) {
                if (player.isUsingItem() && player.getUseItem().is(Items.SHIELD)) {

                    player.disableShield();

                    player.level().playSound(
                            null,
                            player.blockPosition(),
                            ModSounds.POLAR_BEAR_SHIELD_BREAK.get(),
                            SoundSource.PLAYERS,
                            0.8F,
                            1.0F
                    );

                    double strength = 2.7D;
                    double upward = 0.3D;

                    double x = -Math.sin(Math.toRadians(this.getYRot())) * strength;
                    double z =  Math.cos(Math.toRadians(this.getYRot())) * strength;

                    player.push(x, upward, z);
                    player.hurtMarked = true;

                    if (!this.level().isClientSide() && this.level() instanceof ServerLevel serverLevel) {

                        double radius = 3.5D;
                        AABB area = this.getBoundingBox().inflate(radius);

                        List<LivingEntity> nearbyEntities =
                                serverLevel.getEntitiesOfClass(LivingEntity.class, area);

                        for (LivingEntity entity : nearbyEntities) {

                            if (entity == this) continue;
                            if (entity == player) continue;
                            if (!entity.isAlive()) continue;

                            Vec3 direction = entity.position().subtract(this.position());

                            if (direction.lengthSqr() < 0.0001D) continue;

                            direction = direction.normalize();

                            double distance = this.distanceTo(entity);
                            double power = 3.0D * (1.0D - (distance / radius));

                            entity.push(
                                    direction.x * power,
                                    0.45D,
                                    direction.z * power
                            );

                            entity.hurtMarked = true;
                        }
                    }

                    if (this.level() instanceof ServerLevel serverLevel) {

                        Vec3 center = this.position().add(0, 1.2D, 0);

                        for (int i = 0; i < 30; i++) {

                            double dx = serverLevel.random.nextDouble() * 2.0 - 1.0;
                            double dy = serverLevel.random.nextDouble() * 1.0;
                            double dz = serverLevel.random.nextDouble() * 2.0 - 1.0;

                            Vec3 dir = new Vec3(dx, dy, dz).normalize();

                            double speed = 0.5D;

                            serverLevel.sendParticles(
                                    ParticleTypes.SNOWFLAKE,
                                    center.x, center.y, center.z,
                                    0,
                                    dir.x * speed,
                                    dir.y * speed,
                                    dir.z * speed,
                                    1.0D
                            );
                        }
                    }
                }
            }

            if (!this.level().isClientSide()
                    && living instanceof PolarBear polarBear
                    && !(polarBear instanceof Enemy)) {

                float finalHealth = polarBear.getHealth() - (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);

                if (finalHealth <= 0.0F) {

                    Difficulty difficulty = this.level().getDifficulty();

                    boolean shouldInfect = false;

                    switch (difficulty) {
                        case EASY -> shouldInfect = false;
                        case NORMAL -> shouldInfect = this.random.nextBoolean();
                        case HARD -> shouldInfect = true;
                    }

                    if (!shouldInfect) {
                        return super.doHurtTarget(target);
                    }

                    ServerLevel serverLevel = (ServerLevel) this.level();

                    polarBear.setHealth(1.0F);

                    ZombiePolarBearEntity zombie =
                            ModEntities.ZOMBIE_POLAR_BEAR.get().create(serverLevel);

                    if (zombie != null) {

                        zombie.moveTo(
                                polarBear.getX(),
                                polarBear.getY(),
                                polarBear.getZ(),
                                polarBear.getYRot(),
                                polarBear.getXRot()
                        );

                        if (polarBear.isBaby()) {
                            zombie.setBaby(true);
                        }

                        serverLevel.addFreshEntity(zombie);

                        serverLevel.playSound(
                                null,
                                polarBear.blockPosition(),
                                SoundEvents.ZOMBIE_INFECT,
                                SoundSource.HOSTILE,
                                1.0F,
                                1.0F
                        );
                    }

                    polarBear.discard();
                }
            }
        }

        return result;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        DamageSource onFire = this.level().damageSources().onFire();
        DamageSource inFire = this.level().damageSources().inFire();
        DamageSource lava = this.level().damageSources().lava();

        if (source == onFire || source == inFire || source == lava) {
            amount *= 2.0F;
        }

        return super.hurt(source, amount);
    }

    protected double getAttackReachSqr(LivingEntity target) {
        return (this.getBbWidth() * 3.9F) * (this.getBbWidth() * 3.9F)
                + target.getBbWidth();
    }

    @Override
    public void aiStep() {
        if (this.isAlive() && Config.CAN_BURN_IN_SUN.get()) {
            if (this.isSunBurnTick() && !this.isOnFire()) {
                this.igniteForSeconds(8);
            }
        }

        super.aiStep();
    }

    @Override
    protected boolean isSunBurnTick() {
        if (!this.level().isDay()) return false;
        if (this.level().isClientSide()) return false;

        BlockPos pos = this.blockPosition();

        if (this.level().isRainingAt(pos)) return false;

        return this.level().canSeeSky(pos);
    }

    @Override
    public void setAge(int age) {
        if (age >= 0) {
            return;
        }
        super.setAge(age);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.POLAR_BEAR_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.POLAR_BEAR_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.POLAR_BEAR_DEATH;
    }

    @Override
    public float getVoicePitch() {
        return 0.8F;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.POLAR_BEAR_STEP, 0.15F, 0.7F);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.isInWater()) {
            this.setDeltaMovement(
                    this.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D)
            );
            this.setOnGround(true);
            this.setNoGravity(true);
        } else {
            this.setNoGravity(false);
        }

        if (!this.level().isClientSide()) {
            for (Entity passenger : this.getPassengers()) {

                if (passenger instanceof Mob mob) {

                    mob.yBodyRot = this.yBodyRot;
                    mob.setYRot(this.getYRot());

                    LivingEntity riderTarget = mob.getTarget();

                    if (riderTarget != null && riderTarget.isAlive()) {
                        mob.getLookControl().setLookAt(
                                riderTarget,
                                30.0F,
                                30.0F
                        );
                    } else {
                        mob.setYHeadRot(this.getYRot());
                        mob.setXRot(this.getXRot());
                    }
                }
            }
        }
    }

    @Override
    public ZombiePolarBearEntity getBreedOffspring(ServerLevel level, AgeableMob partner) {
        return ModEntities.ZOMBIE_POLAR_BEAR.get().create(level);
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

        if (!level.isClientSide() && level.getRandom().nextFloat() < 0.10F) {

            Entity rider = null;
            boolean isGelid = false;

            if (net.neoforged.fml.ModList.get().isLoaded("variantsandventures")
                    && level.getRandom().nextFloat() < 0.5F) {

                EntityType<?> gelidType =
                        net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE.get(
                                net.minecraft.resources.ResourceLocation
                                        .fromNamespaceAndPath("variantsandventures", "gelid")
                        );

                if (gelidType != null) {
                    rider = gelidType.create(level.getLevel());
                    isGelid = true;

                }
            }

            if (rider == null) {
                rider = EntityType.STRAY.create(level.getLevel());
            }

            if (rider instanceof Mob mobRider) {

                mobRider.moveTo(
                        this.getX(),
                        this.getY(),
                        this.getZ(),
                        this.getYRot(),
                        this.getXRot()

                );

                mobRider.finalizeSpawn(level, difficulty, reason, null);

                if (mobRider instanceof net.minecraft.world.entity.monster.Zombie zombie) {
                    zombie.setBaby(false);
                }

                if (isGelid) {

                    net.minecraft.world.item.ItemStack weaponStack;

                    if (net.neoforged.fml.ModList.get().isLoaded("barched")) {

                        var spearItem = net.minecraft.core.registries.BuiltInRegistries.ITEM.get(
                                net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(
                                        "minecraft",
                                        "iron_spear"
                                )
                        );

                        if (spearItem != null && spearItem != net.minecraft.world.item.Items.AIR) {
                            weaponStack = new net.minecraft.world.item.ItemStack(spearItem);
                        } else {
                            weaponStack = new net.minecraft.world.item.ItemStack(
                                    net.minecraft.world.item.Items.IRON_SWORD
                            );
                        }

                    } else {
                        weaponStack = new net.minecraft.world.item.ItemStack(
                                net.minecraft.world.item.Items.IRON_SWORD
                        );
                    }

                    mobRider.setItemSlot(
                            net.minecraft.world.entity.EquipmentSlot.MAINHAND,
                            weaponStack
                    );

                    mobRider.setDropChance(
                            net.minecraft.world.entity.EquipmentSlot.MAINHAND,
                            0.0F
                    );
                }

                mobRider.startRiding(this, true);

                level.addFreshEntity(mobRider);
            }
        }

        return data;
    }
}






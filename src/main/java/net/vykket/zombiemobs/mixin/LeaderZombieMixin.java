package net.vykket.zombiemobs.mixin;

import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.ServerLevelAccessor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.resources.ResourceLocation;

@Mixin(Zombie.class)
public abstract class LeaderZombieMixin {

    @Shadow
    private static ResourceLocation LEADER_ZOMBIE_BONUS_ID;

    @Inject(
            method = "finalizeSpawn",
            at = @At("TAIL")
    )
    private void zombiemobs$fixLeaderHealth(
            ServerLevelAccessor level,
            DifficultyInstance difficulty,
            MobSpawnType spawnType,
            SpawnGroupData spawnGroupData,
            CallbackInfoReturnable<SpawnGroupData> cir
    ) {

        Zombie self = (Zombie)(Object)this;

        AttributeInstance healthAttr =
                self.getAttribute(Attributes.MAX_HEALTH);

        if (healthAttr == null) return;

        AttributeModifier leaderModifier =
                healthAttr.getModifier(LEADER_ZOMBIE_BONUS_ID);

        if (leaderModifier != null) {

            if (healthAttr.getBaseValue() < 50.0D) {
                healthAttr.setBaseValue(50.0D);
                self.setHealth(self.getMaxHealth());
            }
        }
    }
}

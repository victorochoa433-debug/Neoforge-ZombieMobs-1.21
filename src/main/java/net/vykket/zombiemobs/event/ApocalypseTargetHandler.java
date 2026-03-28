package net.vykket.zombiemobs.event;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Dolphin;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Zombie;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.vykket.zombiemobs.Config;
import net.vykket.zombiemobs.entity.ZombiePigEntity;

import java.util.HashSet;
import java.util.Set;

public class ApocalypseTargetHandler {

    private static final Set<EntityType<?>> BLACKLIST = new HashSet<>();

    static {
        BLACKLIST.add(EntityType.ZOMBIE_HORSE);

        ResourceLocation camelHuskId = ResourceLocation.tryParse("minecraft:camel_husk");

        if (camelHuskId != null &&
                BuiltInRegistries.ENTITY_TYPE.containsKey(camelHuskId)) {

            BLACKLIST.add(BuiltInRegistries.ENTITY_TYPE.get(camelHuskId));
        }
    }

    @SubscribeEvent
    public static void onZombieSpawn(EntityJoinLevelEvent event) {

        if (!(event.getEntity() instanceof Zombie zombie)) return;
        if (zombie instanceof ZombiePigEntity) return;
        if (event.getLevel().isClientSide()) return;
        if (!Config.APOCALYPSE_MODE.get()) return;

        zombie.targetSelector.addGoal(2,
                new NearestAttackableTargetGoal<>(
                        zombie,
                        Animal.class,
                        true,
                        animal -> shouldAttack(animal.getType())
                )
        );

        zombie.targetSelector.addGoal(2,
                new NearestAttackableTargetGoal<>(
                        zombie,
                        Dolphin.class,
                        true,
                        dolphin -> shouldAttack(dolphin.getType())
                )
        );
    }

    private static boolean shouldAttack(EntityType<?> type) {

        if (type == null) return false;

        if (BLACKLIST.contains(type)) return false;

        ResourceLocation id = BuiltInRegistries.ENTITY_TYPE.getKey(type);
        if (id == null) return true;

        if (id.getNamespace().equals("zombiemobs")) return false;

        return true;
    }
}

package net.vykket.zombiemobs.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Husk;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

import net.vykket.zombiemobs.Config;
import net.vykket.zombiemobs.registry.ModEntities;

@EventBusSubscriber
public class HuskSpawnHandler {

    @SubscribeEvent
    public static void onEntityJoin(EntityJoinLevelEvent event) {

        if (event.getLevel().isClientSide()) return;

        if (!Config.SPAWN_HUSK_RABBIT.get()) return;

        Entity entity = event.getEntity();

        if (entity instanceof Husk husk) {

            if (husk.getSpawnType() != MobSpawnType.NATURAL &&
                    husk.getSpawnType() != MobSpawnType.CHUNK_GENERATION) {
                return;
            }

            ServerLevel level = (ServerLevel) event.getLevel();
            if (level.random.nextFloat() < 0.30F) {

                int amount = 1 + level.random.nextInt(3);

                for (int i = 0; i < amount; i++) {

                    var rabbit = ModEntities.HUSK_RABBIT.get().create(level);

                    if (rabbit != null) {

                        rabbit.getPersistentData().putBoolean("FromHusk", true);
                        rabbit.getPersistentData().putInt("LifeTicks", 0);

                        if (level.random.nextFloat() < 0.25F) {
                            rabbit.setBaby(true);
                        }

                        double offsetX = (level.random.nextDouble() - 0.5D) * 2.0D;
                        double offsetZ = (level.random.nextDouble() - 0.5D) * 2.0D;

                        rabbit.moveTo(
                                husk.getX() + offsetX,
                                husk.getY(),
                                husk.getZ() + offsetZ,
                                level.random.nextFloat() * 360F,
                                0
                        );

                        level.addFreshEntity(rabbit);
                    }
                }
            }
        }
    }
}


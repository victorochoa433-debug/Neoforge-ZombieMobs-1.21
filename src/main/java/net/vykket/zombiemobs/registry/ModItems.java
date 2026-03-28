package net.vykket.zombiemobs.registry;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredItem;

import net.vykket.zombiemobs.ZombieMobs;

public class ModItems {

    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(ZombieMobs.MODID);

    public static final DeferredItem<Item> ZOMBIE_PIG_SPAWN_EGG =
            ITEMS.register("zombie_pig_spawn_egg",
                    () -> new DeferredSpawnEggItem(
                            net.vykket.zombiemobs.registry.ModEntities.ZOMBIE_PIG,
                            0x6A7F2A,
                            0xC1E08F,
                            new Item.Properties()
                    )
            );

    public static final DeferredItem<Item> ROTTEN_FLESH_BLOCK =
            ITEMS.register("rotten_flesh_block",
                    () -> new BlockItem(
                            ModBlocks.ROTTEN_FLESH_BLOCK.get(),
                            new Item.Properties()
                    )
            );

    public static final DeferredItem<Item> FROZEN_BONE_BLOCK =
            ITEMS.register("frozen_bone_block",
                    () -> new BlockItem(
                            ModBlocks.FROZEN_BONE_BLOCK.get(),
                            new Item.Properties()
                    )
            );

    public static final DeferredItem<Item> ZOMBIE_RABBIT_SPAWN_EGG =
            ITEMS.register("zombie_rabbit_spawn_egg",
                    () -> new DeferredSpawnEggItem(
                            net.vykket.zombiemobs.registry.ModEntities.ZOMBIE_RABBIT,
                            0xBDBDBD,
                            0x2E7D32,
                            new Item.Properties()
                    )
            );

    public static final DeferredItem<Item> ZOMBIE_POLAR_BEAR_SPAWN_EGG =
            ITEMS.register("zombie_polar_bear_spawn_egg",
                    () -> new DeferredSpawnEggItem(
                            net.vykket.zombiemobs.registry.ModEntities.ZOMBIE_POLAR_BEAR,
                            0xAACCFF,
                            0xA0A0A0,
                            new Item.Properties()
                    )
            );

    public static final DeferredItem<Item> HUSK_RABBIT_SPAWN_EGG =
            ITEMS.register("husk_rabbit_spawn_egg",
                    () -> new DeferredSpawnEggItem(
                            net.vykket.zombiemobs.registry.ModEntities.HUSK_RABBIT,
                            0xC2B280,
                            0x8D6E63,
                            new Item.Properties()
                    )
            );

    public static final DeferredItem<Item> ZOMBIE_CHICKEN_SPAWN_EGG =
            ITEMS.register("zombie_chicken_spawn_egg",
                    () -> new DeferredSpawnEggItem(
                            net.vykket.zombiemobs.registry.ModEntities.ZOMBIE_CHICKEN,
                            0xF5F5F5,
                            0x6FAE4F,
                            new Item.Properties()
                    )
            );

    public static final DeferredItem<Item> ZOMBIE_COW_SPAWN_EGG =
            ITEMS.register("zombie_cow_spawn_egg",
                    () -> new DeferredSpawnEggItem(
                            net.vykket.zombiemobs.registry.ModEntities.ZOMBIE_COW,
                            0x6E4F2A,
                            0x4CAF50,
                            new Item.Properties()
                    )
            );

    public static final DeferredItem<Item> GELID_RABBIT_SPAWN_EGG =
            ITEMS.register("gelid_rabbit_spawn_egg",
                    () -> new DeferredSpawnEggItem(
                            net.vykket.zombiemobs.registry.ModEntities.GELID_RABBIT,
                            0xDCEFFF,
                            0x7FB3FF,
                            new Item.Properties()
                    )
            );

    public static final DeferredItem<Item> ZOMBIE_DOLPHIN_SPAWN_EGG =
            ITEMS.register("zombie_dolphin_spawn_egg",
                    () -> new DeferredSpawnEggItem(
                            net.vykket.zombiemobs.registry.ModEntities.ZOMBIE_DOLPHIN,
                            0x4FC3F7,
                            0x2E7D32,
                            new Item.Properties()
                    )
            );

    public static final DeferredItem<Item> ZOMBIE_SHEEP_SPAWN_EGG =
            ITEMS.register("zombie_sheep_spawn_egg",
                    () -> new DeferredSpawnEggItem(
                            net.vykket.zombiemobs.registry.ModEntities.ZOMBIE_SHEEP,
                            0xEDEDED,
                            0x3F8F3F,
                            new Item.Properties()
                    )
            );

    public static final DeferredItem<Item> RIDER_DROWNED_SPAWN_EGG =
            ITEMS.register("rider_drowned_spawn_egg",
                    () -> new DeferredSpawnEggItem(
                            net.vykket.zombiemobs.registry.ModEntities.RIDER_DROWNED,
                            0x163A59,
                            0x3F8E7E,
                            new Item.Properties()
                    )
            );

    public static final DeferredItem<Item> ZOMBIE_CAT_SPAWN_EGG =
            ITEMS.register("zombie_cat_spawn_egg",
                    () -> new DeferredSpawnEggItem(
                            net.vykket.zombiemobs.registry.ModEntities.ZOMBIE_CAT,
                            0x2B2B2B,
                            0x4CAF50,
                            new Item.Properties()
                    )
            );
}





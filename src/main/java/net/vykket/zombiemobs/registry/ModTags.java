package net.vykket.zombiemobs.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModTags {

    public static final TagKey<Item> ZOMBIE_PIG_FOOD = TagKey.create(
            Registries.ITEM,
            ResourceLocation.fromNamespaceAndPath("zombiemobs", "zombie_pig_food")
    );
}

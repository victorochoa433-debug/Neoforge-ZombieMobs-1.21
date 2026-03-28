package net.vykket.zombiemobs.event;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

import net.vykket.zombiemobs.registry.ModItems;

public class ModCreativeTabs {

    public static void addItems(BuildCreativeModeTabContentsEvent event) {

        if (event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {

            event.insertAfter(
                    Items.BLUE_ICE.getDefaultInstance(),
                    ModItems.FROZEN_BONE_BLOCK.get().getDefaultInstance(),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
            );
            event.insertAfter(
                    Items.PEARLESCENT_FROGLIGHT.getDefaultInstance(),
                    ModItems.ROTTEN_FLESH_BLOCK.get().getDefaultInstance(),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
            );
        }
    }
}





package net.vykket.zombiemobs;

import com.mojang.logging.LogUtils;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.vykket.zombiemobs.client.ClientModEvents;
import net.vykket.zombiemobs.client.ModEntityRenderers;
import net.vykket.zombiemobs.entity.ModSpawnPlacements;
import net.vykket.zombiemobs.event.ApocalypseTargetHandler;
import net.vykket.zombiemobs.event.ModCreativeTabs;
import net.vykket.zombiemobs.event.VillagerAvoidEnemies;
import net.vykket.zombiemobs.particles.ModParticles;
import net.vykket.zombiemobs.registry.*;
import org.slf4j.Logger;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod(ZombieMobs.MODID)
public class ZombieMobs {

    public static final String MODID = "zombiemobs";
    public static final Logger LOGGER = LogUtils.getLogger();

    public ZombieMobs(IEventBus modEventBus, ModContainer modContainer) {

        // register
        ModEntities.ENTITIES.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModParticles.PARTICLE_TYPES.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModSounds.SOUNDS.register(modEventBus);

        // events
        NeoForge.EVENT_BUS.register(new VillagerAvoidEnemies());
        NeoForge.EVENT_BUS.register(this);
        modEventBus.addListener(ModCreativeTabs::addItems);
        modEventBus.register(net.vykket.zombiemobs.event.ModEvents.class);
        NeoForge.EVENT_BUS.addListener(ApocalypseTargetHandler::onZombieSpawn);

        // listeners
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(ClientModEvents::registerParticles);
        modEventBus.addListener(ClientModEvents::registerLayerDefinitions);
        modEventBus.addListener(ClientModEvents::registerRenderers);
        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(ModSpawnPlacements::registerSpawnPlacements);
        modContainer.registerConfig(ModConfig.Type.SERVER, Config.SPEC);
        modContainer.registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
    }


    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("ZombieMobs common setup");
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(ModEntityRenderers::register);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {

            var vis = CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS;
            ItemStack zombieEgg = new ItemStack(Items.ZOMBIE_SPAWN_EGG);
            ItemStack huskEgg = new ItemStack(Items.HUSK_SPAWN_EGG);
            event.insertAfter(zombieEgg, ModItems.ZOMBIE_RABBIT_SPAWN_EGG.get().getDefaultInstance(), vis);

            event.insertAfter(ModItems.ZOMBIE_RABBIT_SPAWN_EGG.get().getDefaultInstance(),
                    ModItems.ZOMBIE_CHICKEN_SPAWN_EGG.get().getDefaultInstance(), vis);

            event.insertAfter(ModItems.ZOMBIE_CHICKEN_SPAWN_EGG.get().getDefaultInstance(),
                    ModItems.ZOMBIE_CAT_SPAWN_EGG.get().getDefaultInstance(), vis);

            event.insertAfter(ModItems.ZOMBIE_CAT_SPAWN_EGG.get().getDefaultInstance(),
                    ModItems.ZOMBIE_PIG_SPAWN_EGG.get().getDefaultInstance(), vis);

            event.insertAfter(ModItems.ZOMBIE_PIG_SPAWN_EGG.get().getDefaultInstance(),
                    ModItems.ZOMBIE_SHEEP_SPAWN_EGG.get().getDefaultInstance(), vis);

            event.insertAfter(ModItems.ZOMBIE_SHEEP_SPAWN_EGG.get().getDefaultInstance(),
                    ModItems.ZOMBIE_COW_SPAWN_EGG.get().getDefaultInstance(), vis);

            event.insertAfter(huskEgg, ModItems.HUSK_RABBIT_SPAWN_EGG.get().getDefaultInstance(), vis);
            event.accept(ModItems.ZOMBIE_POLAR_BEAR_SPAWN_EGG.get());
            event.accept(ModItems.GELID_RABBIT_SPAWN_EGG.get());
            event.accept(ModItems.ZOMBIE_DOLPHIN_SPAWN_EGG.get());
            event.accept(ModItems.RIDER_DROWNED_SPAWN_EGG.get());
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("ZombieMobs server starting");
    }
}




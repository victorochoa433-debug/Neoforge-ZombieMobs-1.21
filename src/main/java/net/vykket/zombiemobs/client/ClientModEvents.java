package net.vykket.zombiemobs.client;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.vykket.zombiemobs.client.renderer.ModGiantRenderer;
import net.vykket.zombiemobs.particles.ModParticles;
import net.vykket.zombiemobs.particles.ScreamParticle;
import net.vykket.zombiemobs.particles.ZombiePoofParticle;
import net.vykket.zombiemobs.registry.ModBlocks;

public class ClientModEvents {

    public static void registerParticles(RegisterParticleProvidersEvent event) {

        event.registerSpriteSet(
                ModParticles.SCREAM.get(),
                ScreamParticle.Provider::new
        );

        event.registerSpriteSet(
                ModParticles.ZOMBIE_POOF.get(),
                ZombiePoofParticle.Provider::new
        );
    }

    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(
                ModModelLayers.RIDER_DROWNED_OUTER,
                ModModelLayers::createRiderDrownedOuterLayer
        );
    }

    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(
                EntityType.GIANT,
                ModGiantRenderer::new
        );
    }
}



package net.vykket.zombiemobs.client.renderer.entity;

import net.minecraft.client.model.ChickenModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import net.vykket.zombiemobs.ZombieMobs;
import net.vykket.zombiemobs.entity.ZombieChickenEntity;

public class ZombieChickenRenderer
        extends MobRenderer<ZombieChickenEntity, ChickenModel<ZombieChickenEntity>> {

    public ZombieChickenRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new ChickenModel<>(context.bakeLayer(ModelLayers.CHICKEN)),
                0.3F
        );
    }

    @Override
    protected float getBob(ZombieChickenEntity entity, float partialTicks) {
        float g = Mth.lerp(partialTicks, entity.oFlap, entity.flap);
        float h = Mth.lerp(partialTicks, entity.oFlapSpeed, entity.flapSpeed);
        return (Mth.sin(g) + 1.0F) * h;
    }

    @Override
    public ResourceLocation getTextureLocation(ZombieChickenEntity entity) {
        return ResourceLocation.fromNamespaceAndPath(
                "zombiemobs",
                "textures/entity/zombie_chicken.png"
        );
    }
}


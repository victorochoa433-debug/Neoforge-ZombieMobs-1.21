package net.vykket.zombiemobs.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.SheepRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Sheep;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.vykket.zombiemobs.ZombieMobs;
import net.vykket.zombiemobs.client.layer.RiderDrownedOuterLayer;
import net.vykket.zombiemobs.client.layer.ZombieSheepWoolLayer;
import net.vykket.zombiemobs.client.renderer.entity.ZombieChickenRenderer;
import net.vykket.zombiemobs.registry.ModEntities;
import net.vykket.zombiemobs.entity.*;
import net.vykket.zombiemobs.registry.ModEntities;

public class ModEntityRenderers {


    public static void register() {

        EntityRenderers.register(
                ModEntities.ZOMBIE_PIG.get(),
                context -> new MobRenderer<ZombiePigEntity, PigModel<ZombiePigEntity>>(
                        context,
                        new PigModel<>(context.bakeLayer(ModelLayers.PIG)),
                        0.7F
                ) {
                    @Override
                    public ResourceLocation getTextureLocation(ZombiePigEntity entity) {
                        return ResourceLocation.fromNamespaceAndPath(
                                "zombiemobs",
                                "textures/entity/zombie_pig.png"
                        );
                    }
                }
        );

        EntityRenderers.register(
                ModEntities.ZOMBIE_CHICKEN.get(),
                context -> new MobRenderer<ZombieChickenEntity, ChickenModel<ZombieChickenEntity>>(
                        context,
                        new ChickenModel<>(context.bakeLayer(ModelLayers.CHICKEN)),
                        0.3F
                ) {
                    @Override
                    protected float getBob(ZombieChickenEntity entity, float partialTicks) {
                        float g = net.minecraft.util.Mth.lerp(partialTicks, entity.oFlap, entity.flap);
                        float h = net.minecraft.util.Mth.lerp(partialTicks, entity.oFlapSpeed, entity.flapSpeed);
                        return (net.minecraft.util.Mth.sin(g) + 1.0F) * h;
                    }

                    @Override
                    public ResourceLocation getTextureLocation(ZombieChickenEntity entity) {
                        return ResourceLocation.fromNamespaceAndPath(
                                "zombiemobs",
                                "textures/entity/zombie_chicken.png"
                        );
                    }
                }
        );

        EntityRenderers.register(
                ModEntities.RIDER_DROWNED.get(),
                context -> {

                    DrownedModel<RiderDrownedEntity> model =
                            new DrownedModel<>(context.bakeLayer(ModelLayers.DROWNED));

                    MobRenderer<RiderDrownedEntity, DrownedModel<RiderDrownedEntity>> renderer =
                            new MobRenderer<>(context, model, 0.5F) {

                                @Override
                                public ResourceLocation getTextureLocation(RiderDrownedEntity entity) {
                                    return ResourceLocation.fromNamespaceAndPath(
                                            ZombieMobs.MODID,
                                            "textures/entity/rider_drowned.png"
                                    );
                                }
                            };
                    renderer.addLayer(
                            new net.minecraft.client.renderer.entity.layers.ItemInHandLayer<>(
                                    renderer,
                                    context.getItemInHandRenderer()
                            )
                    );
                    renderer.addLayer(
                            new RiderDrownedOuterLayer(renderer, context.getModelSet())
                    );

                    return renderer;
                }
        );

        EntityRenderers.register(
                ModEntities.ZOMBIE_RABBIT.get(),
                context -> new MobRenderer<ZombieRabbitEntity, RabbitModel<ZombieRabbitEntity>>(
                        context,
                        new RabbitModel<>(context.bakeLayer(ModelLayers.RABBIT)),
                        0.3F
                ) {
                    @Override
                    public ResourceLocation getTextureLocation(ZombieRabbitEntity entity) {
                        return ResourceLocation.fromNamespaceAndPath(
                                "zombiemobs",
                                "textures/entity/zombie_rabbit.png"
                        );
                    }
                }
        );

        EntityRenderers.register(
                ModEntities.HUSK_RABBIT.get(),
                context -> new MobRenderer<HuskRabbitEntity, RabbitModel<HuskRabbitEntity>>(
                        context,
                        new RabbitModel<>(context.bakeLayer(ModelLayers.RABBIT)),
                        0.3F
                ) {
                    @Override
                    public ResourceLocation getTextureLocation(HuskRabbitEntity entity) {
                        return ResourceLocation.fromNamespaceAndPath(
                                "zombiemobs",
                                "textures/entity/husk_rabbit.png"
                        );
                    }
                }
        );

        EntityRenderers.register(
                ModEntities.ZOMBIE_POLAR_BEAR.get(),
                context -> new MobRenderer<ZombiePolarBearEntity, PolarBearModel<ZombiePolarBearEntity>>(
                        context,
                        new PolarBearModel<>(context.bakeLayer(ModelLayers.POLAR_BEAR)),
                        0.9F
                ) {
                    @Override
                    public ResourceLocation getTextureLocation(ZombiePolarBearEntity entity) {
                        return ResourceLocation.fromNamespaceAndPath(
                                "zombiemobs",
                                "textures/entity/zombie_polar_bear.png"
                        );
                    }

                    @Override
                    protected void scale(ZombiePolarBearEntity entity, PoseStack poseStack, float partialTick) {
                        float scaleFactor = 1.3F; // <-- aquí aumentas el tamaño del modelo real
                        poseStack.scale(scaleFactor, scaleFactor, scaleFactor);
                        super.scale(entity, poseStack, partialTick); // importante para mantener otras transformaciones
                    }
                }
        );

        EntityRenderers.register(
                ModEntities.ZOMBIE_COW.get(),
                context -> new MobRenderer<ZombieCowEntity, CowModel<ZombieCowEntity>>(
                        context,
                        new CowModel<>(context.bakeLayer(ModelLayers.COW)),
                        0.7F
                ) {
                    @Override
                    public ResourceLocation getTextureLocation(ZombieCowEntity entity) {
                        return ResourceLocation.fromNamespaceAndPath(
                                "zombiemobs",
                                "textures/entity/zombie_cow.png"
                        );
                    }
                }
        );

        EntityRenderers.register(
                ModEntities.GELID_RABBIT.get(),
                context -> new MobRenderer<GelidRabbitEntity, RabbitModel<GelidRabbitEntity>>(
                        context,
                        new RabbitModel<>(context.bakeLayer(ModelLayers.RABBIT)),
                        0.3F
                ) {
                    @Override
                    public ResourceLocation getTextureLocation(GelidRabbitEntity entity) {
                        return ResourceLocation.fromNamespaceAndPath(
                                "zombiemobs",
                                "textures/entity/gelid_rabbit.png"
                        );
                    }
                }
        );

        EntityRenderers.register(
                ModEntities.ZOMBIE_SHEEP.get(),
                context -> new SheepRenderer(context) {

                    {
                        this.layers.clear();
                        this.addLayer(new ZombieSheepWoolLayer(this, context.getModelSet()));
                    }

                    @Override
                    public ResourceLocation getTextureLocation(Sheep entity) {
                        return ResourceLocation.fromNamespaceAndPath(
                                "zombiemobs",
                                "textures/entity/zombie_sheep.png"
                        );
                    }
                }
        );

        EntityRenderers.register(
                ModEntities.ZOMBIE_DOLPHIN.get(),
                context -> new MobRenderer<ZombieDolphinEntity, DolphinModel<ZombieDolphinEntity>>(
                        context,
                        new DolphinModel<>(context.bakeLayer(ModelLayers.DOLPHIN)),
                        0.6F
                ) {
                    @Override
                    public ResourceLocation getTextureLocation(ZombieDolphinEntity entity) {
                        return ResourceLocation.fromNamespaceAndPath(
                                "zombiemobs",
                                "textures/entity/zombie_dolphin.png"
                        );
                    }
                }
        );

        EntityRenderers.register(
                ModEntities.ZOMBIE_CAT.get(),
                context -> new MobRenderer<ZombieCatEntity, CatModel<ZombieCatEntity>>(
                        context,
                        new CatModel<>(context.bakeLayer(ModelLayers.CAT)),
                        0.4F
                ) {
                    @Override
                    public ResourceLocation getTextureLocation(ZombieCatEntity entity) {
                        return ResourceLocation.fromNamespaceAndPath(
                                "zombiemobs",
                                "textures/entity/zombie_cat.png"
                        );
                    }

                    @Override
                    protected void scale(ZombieCatEntity entity, PoseStack poseStack, float partialTick) {
                        float scaleFactor = 0.8F;
                        poseStack.scale(scaleFactor, scaleFactor, scaleFactor);
                        super.scale(entity, poseStack, partialTick);
                    }
                }
        );
    }
}









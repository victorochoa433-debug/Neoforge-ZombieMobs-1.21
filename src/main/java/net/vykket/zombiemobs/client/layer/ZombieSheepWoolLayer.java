package net.vykket.zombiemobs.client.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.SheepFurModel;
import net.minecraft.client.model.SheepModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Sheep;

public class ZombieSheepWoolLayer extends RenderLayer<Sheep, SheepModel<Sheep>> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(
                    "zombiemobs",
                    "textures/entity/zombie_sheep_fur.png"
            );

    private final SheepFurModel<Sheep> model;

    public ZombieSheepWoolLayer(RenderLayerParent<Sheep, SheepModel<Sheep>> parent,
                                EntityModelSet modelSet) {
        super(parent);
        this.model = new SheepFurModel<>(modelSet.bakeLayer(ModelLayers.SHEEP_FUR));
    }

    @Override
    public void render(PoseStack poseStack,
                       MultiBufferSource buffer,
                       int packedLight,
                       Sheep sheep,
                       float limbSwing,
                       float limbSwingAmount,
                       float partialTick,
                       float ageInTicks,
                       float netHeadYaw,
                       float headPitch) {

        if (sheep.isSheared()) return;

        this.getParentModel().copyPropertiesTo(this.model);
        this.model.prepareMobModel(sheep, limbSwing, limbSwingAmount, partialTick);
        this.model.setupAnim(sheep, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        var vertexConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));

        int overlay = LivingEntityRenderer.getOverlayCoords(sheep, 0.0F);

        this.model.renderToBuffer(
                poseStack,
                vertexConsumer,
                packedLight,
                overlay
        );
    }
}
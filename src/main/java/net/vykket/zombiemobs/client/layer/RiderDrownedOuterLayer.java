package net.vykket.zombiemobs.client.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.DrownedModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.vykket.zombiemobs.ZombieMobs;
import net.vykket.zombiemobs.client.ModModelLayers;
import net.vykket.zombiemobs.entity.RiderDrownedEntity;

public class RiderDrownedOuterLayer extends RenderLayer<RiderDrownedEntity, DrownedModel<RiderDrownedEntity>> {

    private final DrownedModel<RiderDrownedEntity> outerModel;

    private static final ResourceLocation OUTER_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(
                    ZombieMobs.MODID,
                    "textures/entity/rider_drowned_outer.png"
            );

    public RiderDrownedOuterLayer(
            RenderLayerParent<RiderDrownedEntity, DrownedModel<RiderDrownedEntity>> parent,
            EntityModelSet modelSet
    ) {
        super(parent);

        this.outerModel = new DrownedModel<>(
                modelSet.bakeLayer(ModModelLayers.RIDER_DROWNED_OUTER)
        );
    }

    @Override
    public void render(
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            RiderDrownedEntity entity,
            float limbSwing,
            float limbSwingAmount,
            float partialTicks,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        this.getParentModel().copyPropertiesTo(this.outerModel);
        this.outerModel.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTicks);
        this.outerModel.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        var vertexConsumer = buffer.getBuffer(
                this.outerModel.renderType(OUTER_TEXTURE)
        );

        int overlay = net.minecraft.client.renderer.entity.LivingEntityRenderer
                .getOverlayCoords(entity, 0.0F);

        this.outerModel.renderToBuffer(
                poseStack,
                vertexConsumer,
                packedLight,
                overlay
        );
    }
}



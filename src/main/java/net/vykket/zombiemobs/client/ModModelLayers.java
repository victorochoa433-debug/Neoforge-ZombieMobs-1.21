package net.vykket.zombiemobs.client;

import net.minecraft.client.model.DrownedModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.resources.ResourceLocation;
import net.vykket.zombiemobs.ZombieMobs;

public class ModModelLayers {

    public static final ModelLayerLocation RIDER_DROWNED_OUTER =
            new ModelLayerLocation(
                    ResourceLocation.fromNamespaceAndPath(
                            ZombieMobs.MODID,
                            "rider_drowned_outer"
                    ),
                    "main"
            );

    public static LayerDefinition createRiderDrownedOuterLayer() {
        return DrownedModel.createBodyLayer(
                new CubeDeformation(0.25F)
        );
    }
}

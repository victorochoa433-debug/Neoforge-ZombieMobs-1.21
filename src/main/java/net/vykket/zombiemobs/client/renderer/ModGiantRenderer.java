package net.vykket.zombiemobs.client.renderer;

import net.minecraft.client.renderer.entity.GiantMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Giant;
import net.vykket.zombiemobs.ZombieMobs;

public class ModGiantRenderer extends GiantMobRenderer {

    private static final ResourceLocation GIANT_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(
                    ZombieMobs.MODID,
                    "textures/entity/giant.png"
            );

    public ModGiantRenderer(EntityRendererProvider.Context context) {
        super(context, 6.0F);
    }

    @Override
    public ResourceLocation getTextureLocation(Giant entity) {
        return GIANT_TEXTURE;
    }
}

package net.vykket.zombiemobs.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.SimpleParticleType;

public class ScreamParticle extends TextureSheetParticle {

    protected ScreamParticle(ClientLevel level,
                             double x, double y, double z,
                             double xd, double yd, double zd,
                             SpriteSet sprites) {

        super(level, x, y, z, xd, yd, zd);

        this.lifetime = 30;
        this.gravity = 0.0F;
        this.quadSize = 0.3F;

        this.setSprite(sprites.get(0, 1));

        this.setAlpha(1.0F);
    }

    @Override
    public void tick() {
        super.tick();

        this.quadSize += 0.02F;

        float progress = (float)this.age / (float)this.lifetime;
        this.setAlpha(1.0F - progress);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }
    public static class Provider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public net.minecraft.client.particle.Particle createParticle(
                SimpleParticleType type,
                ClientLevel level,
                double x, double y, double z,
                double xd, double yd, double zd) {

            return new ScreamParticle(level, x, y, z, xd, yd, zd, sprites);
        }
    }
}


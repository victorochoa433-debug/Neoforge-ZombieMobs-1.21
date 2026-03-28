package net.vykket.zombiemobs.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ExplodeParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

// Inspired by the void poof from Enderscape

public class ZombiePoofParticle extends ExplodeParticle {

    protected ZombiePoofParticle(ClientLevel level, double x, double y, double z,
                                 double xd, double yd, double zd,
                                 SpriteSet sprites) {
        super(level, x, y, z, xd, yd, zd, sprites);
        float base = Mth.nextFloat(random, 0.07F, 0.19F);

        rCol = base * 0.5F;
        gCol = base * 0.9F;
        bCol = base * 0.5F;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        @Override
        public Particle createParticle(SimpleParticleType type,
                                       ClientLevel level,
                                       double x, double y, double z,
                                       double xd, double yd, double zd) {

            return new ZombiePoofParticle(level, x, y, z, xd, yd, zd, sprites);
        }
    }
}

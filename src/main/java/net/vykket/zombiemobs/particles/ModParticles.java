package net.vykket.zombiemobs.particles;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModParticles {

    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, "zombiemobs");

    public static final Supplier<SimpleParticleType> SCREAM =
            PARTICLE_TYPES.register("scream_particles",
                    () -> new SimpleParticleType(true));

    public static final Supplier<SimpleParticleType> ZOMBIE_POOF =
            PARTICLE_TYPES.register("zombie_poof",
                    () -> new SimpleParticleType(true));

    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }
}



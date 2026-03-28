package net.vykket.zombiemobs;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ClientConfig {

    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue ZOMBIE_POOF_PARTICLES;

    public static final ModConfigSpec SPEC;

    static {

        BUILDER.push("Visual Effects");

        ZOMBIE_POOF_PARTICLES = BUILDER
                .comment("Enable zombie poof particles")
                .translation("config.zombiemobs.zombie_poof_particles")
                .define("zombiePoofParticles", true);

        BUILDER.pop();

        SPEC = BUILDER.build();
    }
}

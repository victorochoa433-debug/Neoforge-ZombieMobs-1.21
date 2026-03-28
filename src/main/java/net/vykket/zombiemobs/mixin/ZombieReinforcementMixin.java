package net.vykket.zombiemobs.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Zombie.class)
public class ZombieReinforcementMixin {

    @Redirect(
            method = "hurt",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/level/Level;)Lnet/minecraft/world/entity/monster/Zombie;"
            )
    )
    private Zombie zombiemobs$spawnSameVariant(Level level) {

        Zombie self = (Zombie)(Object)this;

        String id = self.getType().toString();

        if (id.contains("gelid") || id.contains("thicket")) {
            return new Zombie(level);
        }

        EntityType<?> type = self.getType();

        Zombie zombie = (Zombie) type.create(level);

        if (zombie == null) {
            zombie = new Zombie(level);
        }

        return zombie;
    }
}

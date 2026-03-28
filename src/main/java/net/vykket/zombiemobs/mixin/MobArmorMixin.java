package net.vykket.zombiemobs.mixin;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public abstract class MobArmorMixin {

    @Inject(
            method = "populateDefaultEquipmentSlots",
            at = @At("HEAD"),
            cancellable = true
    )
    private void zombiemobs$fixArmorOrder(
            RandomSource random,
            DifficultyInstance difficulty,
            CallbackInfo ci
    ) {

        Mob self = (Mob)(Object)this;

        ci.cancel();

        float chance = 0.15F;

        if (random.nextFloat() < chance) {

            int tier = random.nextInt(2);

            EquipmentSlot[] slots = new EquipmentSlot[] {
                    EquipmentSlot.HEAD,
                    EquipmentSlot.CHEST,
                    EquipmentSlot.LEGS,
                    EquipmentSlot.FEET
            };

            for (EquipmentSlot slot : slots) {

                ItemStack current = self.getItemBySlot(slot);

                if (!current.isEmpty()) {
                    break;
                }

                Item item = Mob.getEquipmentForSlot(slot, tier);

                if (item != null) {
                    ItemStack armor = new ItemStack(item);
                    self.setItemSlot(slot, armor);
                }

                if (random.nextFloat() < 0.25F) {
                    break;
                }
            }
        }
    }
}

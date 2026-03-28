package net.vykket.zombiemobs.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.core.registries.Registries;
import net.vykket.zombiemobs.ZombieMobs;

public class ModSounds {

    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(Registries.SOUND_EVENT, ZombieMobs.MODID);

    // zombie pig

    public static final DeferredHolder<SoundEvent, SoundEvent> ZOMBIE_PIG_AMBIENT =
            SOUNDS.register("entity.zombie_pig.ambient",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(
                                    ZombieMobs.MODID,
                                    "entity.zombie_pig.ambient"
                            )
                    ));

    public static final DeferredHolder<SoundEvent, SoundEvent> ZOMBIE_PIG_HURT =
            SOUNDS.register("entity.zombie_pig.hurt",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(
                                    ZombieMobs.MODID,
                                    "entity.zombie_pig.hurt"
                            )
                    ));

    public static final DeferredHolder<SoundEvent, SoundEvent> ZOMBIE_PIG_DEATH =
            SOUNDS.register("entity.zombie_pig.death",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(
                                    ZombieMobs.MODID,
                                    "entity.zombie_pig.death"
                            )
                    ));

    public static final DeferredHolder<SoundEvent, SoundEvent> ZOMBIE_PIG_STEP =
            SOUNDS.register("entity.zombie_pig.step",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(
                                    ZombieMobs.MODID,
                                    "entity.zombie_pig.step"
                            )
                    ));

    // rider drowned

    public static final DeferredHolder<SoundEvent, SoundEvent> RIDER_DROWNED_AMBIENT =
            SOUNDS.register("entity.rider_drowned.ambient",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(
                                    ZombieMobs.MODID,
                                    "entity.rider_drowned.ambient"
                            )
                    ));

    public static final DeferredHolder<SoundEvent, SoundEvent> RIDER_DROWNED_HURT =
            SOUNDS.register("entity.rider_drowned.hurt",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(
                                    ZombieMobs.MODID,
                                    "entity.rider_drowned.hurt"
                            )
                    ));

    public static final DeferredHolder<SoundEvent, SoundEvent> RIDER_DROWNED_DEATH =
            SOUNDS.register("entity.rider_drowned.death",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(
                                    ZombieMobs.MODID,
                                    "entity.rider_drowned.death"
                            )
                    ));

    public static final DeferredHolder<SoundEvent, SoundEvent> RIDER_DROWNED_STEP =
            SOUNDS.register("entity.rider_drowned.step",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(
                                    ZombieMobs.MODID,
                                    "entity.rider_drowned.step"
                            )
                    ));

    // zombie rabbit

    public static final DeferredHolder<SoundEvent, SoundEvent> ZOMBIE_RABBIT_AMBIENT =
            SOUNDS.register("entity.zombie_rabbit.ambient",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(
                                    ZombieMobs.MODID,
                                    "entity.zombie_rabbit.ambient"
                            )
                    ));

    public static final DeferredHolder<SoundEvent, SoundEvent> ZOMBIE_RABBIT_HURT =
            SOUNDS.register("entity.zombie_rabbit.hurt",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(
                                    ZombieMobs.MODID,
                                    "entity.zombie_rabbit.hurt"
                            )
                    ));

    public static final DeferredHolder<SoundEvent, SoundEvent> ZOMBIE_RABBIT_DEATH =
            SOUNDS.register("entity.zombie_rabbit.death",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(
                                    ZombieMobs.MODID,
                                    "entity.zombie_rabbit.death"
                            )
                    ));

    public static final DeferredHolder<SoundEvent, SoundEvent> ZOMBIE_RABBIT_STEP =
            SOUNDS.register("entity.zombie_rabbit.step",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(
                                    ZombieMobs.MODID,
                                    "entity.zombie_rabbit.step"
                            )
                    ));

    // zombie chicken

    public static final DeferredHolder<SoundEvent, SoundEvent> ZOMBIE_CHICKEN_AMBIENT =
            SOUNDS.register("entity.zombie_chicken.ambient",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(
                                    ZombieMobs.MODID,
                                    "entity.zombie_chicken.ambient"
                            )
                    ));

    public static final DeferredHolder<SoundEvent, SoundEvent> ZOMBIE_CHICKEN_HURT =
            SOUNDS.register("entity.zombie_chicken.hurt",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(
                                    ZombieMobs.MODID,
                                    "entity.zombie_chicken.hurt"
                            )
                    ));

    public static final DeferredHolder<SoundEvent, SoundEvent> ZOMBIE_CHICKEN_DEATH =
            SOUNDS.register("entity.zombie_chicken.death",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(
                                    ZombieMobs.MODID,
                                    "entity.zombie_chicken.death"
                            )
                    ));

    public static final DeferredHolder<SoundEvent, SoundEvent> ZOMBIE_CHICKEN_STEP =
            SOUNDS.register("entity.zombie_chicken.step",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(
                                    ZombieMobs.MODID,
                                    "entity.zombie_chicken.step"
                            )
                    ));

    // husk rabbit

    public static final DeferredHolder<SoundEvent, SoundEvent> HUSK_RABBIT_AMBIENT =
            SOUNDS.register("entity.husk_rabbit.ambient",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(
                                    ZombieMobs.MODID,
                                    "entity.husk_rabbit.ambient"
                            )
                    ));

    public static final DeferredHolder<SoundEvent, SoundEvent> HUSK_RABBIT_HURT =
            SOUNDS.register("entity.husk_rabbit.hurt",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(
                                    ZombieMobs.MODID,
                                    "entity.husk_rabbit.hurt"
                            )
                    ));

    public static final DeferredHolder<SoundEvent, SoundEvent> HUSK_RABBIT_DEATH =
            SOUNDS.register("entity.husk_rabbit.death",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(
                                    ZombieMobs.MODID,
                                    "entity.husk_rabbit.death"
                            )
                    ));

    public static final DeferredHolder<SoundEvent, SoundEvent> HUSK_RABBIT_STEP =
            SOUNDS.register("entity.husk_rabbit.step",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(
                                    ZombieMobs.MODID,
                                    "entity.husk_rabbit.step"
                            )
                    ));

    // zombie polar bear

    public static final DeferredHolder<SoundEvent, SoundEvent> ZOMBIE_POLAR_BEAR_AMBIENT =
            SOUNDS.register("entity.zombie_polar_bear.ambient",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(
                                    ZombieMobs.MODID,
                                    "entity.zombie_polar_bear.ambient"
                            )
                    ));

    public static final DeferredHolder<SoundEvent, SoundEvent> ZOMBIE_POLAR_BEAR_HURT =
            SOUNDS.register("entity.zombie_polar_bear.hurt",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(
                                    ZombieMobs.MODID,
                                    "entity.zombie_polar_bear.hurt"
                            )
                    ));

    public static final DeferredHolder<SoundEvent, SoundEvent> ZOMBIE_POLAR_BEAR_DEATH =
            SOUNDS.register("entity.zombie_polar_bear.death",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(
                                    ZombieMobs.MODID,
                                    "entity.zombie_polar_bear.death"
                            )
                    ));

    public static final DeferredHolder<SoundEvent, SoundEvent> POLAR_BEAR_SHIELD_BREAK =
            SOUNDS.register("entity.zombie_polar_bear.shield_break",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(
                                    ZombieMobs.MODID,
                                    "entity.zombie_polar_bear.shield_break"
                            )
                    ));

    public static final DeferredHolder<SoundEvent, SoundEvent> ZOMBIE_POLAR_BEAR_STEP =
            SOUNDS.register("entity.zombie_polar_bear.step",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(
                                    ZombieMobs.MODID,
                                    "entity.zombie_polar_bear.step"
                            )
                    ));

    // zombie dolphin

    public static final DeferredHolder<SoundEvent, SoundEvent> ZOMBIE_DOLPHIN_AMBIENT =
            SOUNDS.register("entity.zombie_dolphin.ambient",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(
                                    ZombieMobs.MODID,
                                    "entity.zombie_dolphin.ambient"
                            )
                    ));

    public static final DeferredHolder<SoundEvent, SoundEvent> ZOMBIE_DOLPHIN_HURT =
            SOUNDS.register("entity.zombie_dolphin.hurt",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(
                                    ZombieMobs.MODID,
                                    "entity.zombie_dolphin.hurt"
                            )
                    ));

    public static final DeferredHolder<SoundEvent, SoundEvent> ZOMBIE_DOLPHIN_DEATH =
            SOUNDS.register("entity.zombie_dolphin.death",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(
                                    ZombieMobs.MODID,
                                    "entity.zombie_dolphin.death"
                            )
                    ));

    public static final DeferredHolder<SoundEvent, SoundEvent> ZOMBIE_DOLPHIN_STEP =
            SOUNDS.register("entity.zombie_dolphin.step",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(
                                    ZombieMobs.MODID,
                                    "entity.zombie_dolphin.step"
                            )
                    ));

    // event

    public static final DeferredHolder<SoundEvent, SoundEvent> ZOMBIE_SIEGE_ALERT =
            SOUNDS.register("event.zombie_siege.alert",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(
                                    ZombieMobs.MODID,
                                    "event.zombie_siege.alert"
                            )
                    ));

    // zombie cow

    public static final DeferredHolder<SoundEvent, SoundEvent> ZOMBIE_COW_AMBIENT =
            SOUNDS.register("entity.zombie_cow.ambient",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(
                                    ZombieMobs.MODID,
                                    "entity.zombie_cow.ambient"
                            )
                    ));

    public static final DeferredHolder<SoundEvent, SoundEvent> ZOMBIE_COW_HURT =
            SOUNDS.register("entity.zombie_cow.hurt",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(
                                    ZombieMobs.MODID,
                                    "entity.zombie_cow.hurt"
                            )
                    ));

    public static final DeferredHolder<SoundEvent, SoundEvent> ZOMBIE_COW_DEATH =
            SOUNDS.register("entity.zombie_cow.death",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(
                                    ZombieMobs.MODID,
                                    "entity.zombie_cow.death"
                            )
                    ));

    public static final DeferredHolder<SoundEvent, SoundEvent> ZOMBIE_COW_STEP =
            SOUNDS.register("entity.zombie_cow.step",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(
                                    ZombieMobs.MODID,
                                    "entity.zombie_cow.step"
                            )
                    ));

    // gelid rabbit

    public static final DeferredHolder<SoundEvent, SoundEvent> GELID_RABBIT_AMBIENT =
            SOUNDS.register("entity.gelid_rabbit.ambient",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(
                                    ZombieMobs.MODID,
                                    "entity.gelid_rabbit.ambient"
                            )
                    ));

    public static final DeferredHolder<SoundEvent, SoundEvent> GELID_RABBIT_HURT =
            SOUNDS.register("entity.gelid_rabbit.hurt",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(
                                    ZombieMobs.MODID,
                                    "entity.gelid_rabbit.hurt"
                            )
                    ));

    public static final DeferredHolder<SoundEvent, SoundEvent> GELID_RABBIT_DEATH =
            SOUNDS.register("entity.gelid_rabbit.death",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(
                                    ZombieMobs.MODID,
                                    "entity.gelid_rabbit.death"
                            )
                    ));

    public static final DeferredHolder<SoundEvent, SoundEvent> GELID_RABBIT_STEP =
            SOUNDS.register("entity.gelid_rabbit.step",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(
                                    ZombieMobs.MODID,
                                    "entity.gelid_rabbit.step"
                            )
                    ));

    // zombie sheep

    public static final DeferredHolder<SoundEvent, SoundEvent> ZOMBIE_SHEEP_AMBIENT =
            SOUNDS.register("entity.zombie_sheep.ambient",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(
                                    ZombieMobs.MODID,
                                    "entity.zombie_sheep.ambient"
                            )
                    ));

    public static final DeferredHolder<SoundEvent, SoundEvent> ZOMBIE_SHEEP_HURT =
            SOUNDS.register("entity.zombie_sheep.hurt",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(
                                    ZombieMobs.MODID,
                                    "entity.zombie_sheep.hurt"
                            )
                    ));

    public static final DeferredHolder<SoundEvent, SoundEvent> ZOMBIE_SHEEP_DEATH =
            SOUNDS.register("entity.zombie_sheep.death",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(
                                    ZombieMobs.MODID,
                                    "entity.zombie_sheep.death"
                            )
                    ));

    public static final DeferredHolder<SoundEvent, SoundEvent> ZOMBIE_SHEEP_SCREAM =
            SOUNDS.register("entity.zombie_sheep.scream",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(
                                    ZombieMobs.MODID,
                                    "entity.zombie_sheep.scream"
                            )
                    ));

    public static final DeferredHolder<SoundEvent, SoundEvent> ZOMBIE_SHEEP_STEP =
            SOUNDS.register("entity.zombie_sheep.step",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(
                                    ZombieMobs.MODID,
                                    "entity.zombie_sheep.step"
                            )
                    ));

    // zombie cat

    public static final DeferredHolder<SoundEvent, SoundEvent> ZOMBIE_CAT_AMBIENT =
            SOUNDS.register("entity.zombie_cat.ambient",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(
                                    ZombieMobs.MODID,
                                    "entity.zombie_cat.ambient"
                            )
                    ));

    public static final DeferredHolder<SoundEvent, SoundEvent> ZOMBIE_CAT_HURT =
            SOUNDS.register("entity.zombie_cat.hurt",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(
                                    ZombieMobs.MODID,
                                    "entity.zombie_cat.hurt"
                            )
                    ));

    public static final DeferredHolder<SoundEvent, SoundEvent> ZOMBIE_CAT_DEATH =
            SOUNDS.register("entity.zombie_cat.death",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(
                                    ZombieMobs.MODID,
                                    "entity.zombie_cat.death"
                            )
                    ));

    public static final DeferredHolder<SoundEvent, SoundEvent> ZOMBIE_CAT_STEP =
            SOUNDS.register("entity.zombie_cat.step",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(
                                    ZombieMobs.MODID,
                                    "entity.zombie_cat.step"
                            )
                    ));
}





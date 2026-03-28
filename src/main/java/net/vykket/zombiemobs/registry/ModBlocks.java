package net.vykket.zombiemobs.registry;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawner;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredBlock;

import net.vykket.zombiemobs.ZombieMobs;

public class ModBlocks {

    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(ZombieMobs.MODID);

    public static final DeferredBlock<Block> ROTTEN_FLESH_BLOCK =
            BLOCKS.register("rotten_flesh_block", () ->
                    new Block(BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_BROWN)
                            .strength(0.5F, 1.0F)
                            .sound(SoundType.SLIME_BLOCK)
                    )
            );

    public static final DeferredBlock<Block> FROZEN_BONE_BLOCK =
            BLOCKS.register("frozen_bone_block", () ->
                    new RotatedPillarBlock(BlockBehaviour.Properties.of()
                            .mapColor(MapColor.SNOW)
                            .strength(2.0F, 6.0F)
                            .sound(SoundType.BONE_BLOCK)
                            .requiresCorrectToolForDrops()
                    )
            );
}



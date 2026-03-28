package net.vykket.zombiemobs.entity.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Zoglin;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.Vec3;
import net.vykket.zombiemobs.Config;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class ZoglinBreakDoorGoal extends Goal {

    private final Zoglin mob;
    private final List<BlockPos> doors = new ArrayList<>();
    private int breakTime;

    private static final int WOOD_BREAK_TIME = 60;
    private static final int IRON_BREAK_TIME = 120;

    public ZoglinBreakDoorGoal(Zoglin mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {

        if (!Config.ZOGLIN_BREAK_DOORS.get()) return false;

        if (mob.getTarget() == null) return false;

        Difficulty difficulty = mob.level().getDifficulty();
        if (difficulty != Difficulty.NORMAL && difficulty != Difficulty.HARD) return false;

        doors.clear();

        BlockPos base = mob.blockPosition();
        BlockPos forward = base.relative(mob.getDirection());

        for (int x = -1; x <= 1; x++) {
            for (int y = 0; y <= 1; y++) {

                BlockPos checkPos = forward
                        .relative(mob.getDirection().getClockWise(), x)
                        .above(y);

                checkDoor(checkPos);
            }
        }

        doors.sort((a, b) -> Double.compare(
                a.distSqr(mob.blockPosition()),
                b.distSqr(mob.blockPosition())
        ));

        return !doors.isEmpty();
    }

    private void checkDoor(BlockPos pos) {
        BlockState state = mob.level().getBlockState(pos);

        if (state.getBlock() instanceof DoorBlock && !state.getValue(DoorBlock.OPEN)) {

            BlockPos basePos;

            if (state.getValue(DoorBlock.HALF) == DoubleBlockHalf.LOWER) {
                basePos = pos;
            } else {
                basePos = pos.below();
            }

            if (!doors.contains(basePos)) {
                doors.add(basePos);
            }
        }
    }

    private int getBreakTime(BlockState state) {
        return state.is(Blocks.IRON_DOOR) ? IRON_BREAK_TIME : WOOD_BREAK_TIME;
    }

    private void breakDoor(BlockPos basePos) {
        BlockPos upper = basePos.above();

        mob.level().destroyBlock(basePos, true);
        mob.level().destroyBlock(upper, true);

        int breakerId = mob.getId() + basePos.hashCode();

        mob.level().destroyBlockProgress(breakerId, basePos, -1);
        mob.level().destroyBlockProgress(breakerId, basePos.above(), -1);
    }

    @Override
    public void start() {
        breakTime = 0;
    }

    @Override
    public void tick() {

        if (mob.getTarget() != null) {

            double distSq = mob.distanceToSqr(mob.getTarget());

            if (distSq < 3.0D) {
                stop();
                return;
            }
        }

        doors.removeIf(pos -> {
            BlockState state = mob.level().getBlockState(pos);
            return !(state.getBlock() instanceof DoorBlock);
        });

        if (doors.isEmpty()) {
            stop();
            return;
        }

        mob.getBrain().eraseMemory(net.minecraft.world.entity.ai.memory.MemoryModuleType.WALK_TARGET);
        mob.getBrain().eraseMemory(net.minecraft.world.entity.ai.memory.MemoryModuleType.PATH);
        mob.getBrain().eraseMemory(net.minecraft.world.entity.ai.memory.MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
        mob.getBrain().eraseMemory(net.minecraft.world.entity.ai.memory.MemoryModuleType.ATTACK_TARGET);

        if (doors.isEmpty()) return;

        double targetX = 0;
        double targetZ = 0;

        for (BlockPos door : doors) {
            targetX += door.getX() + 0.5;
            targetZ += door.getZ() + 0.5;
        }

        targetX /= doors.size();
        targetZ /= doors.size();

        double dx = targetX - mob.getX();
        double dz = targetZ - mob.getZ();

        float yaw = (float)(Math.atan2(dz, dx) * (180F / Math.PI)) - 90F;

        mob.setYRot(yaw);
        mob.setYBodyRot(yaw);
        mob.setYHeadRot(yaw);

        breakTime++;

        if (breakTime % 10 == 0) {
            for (BlockPos door : doors) {

                BlockState state = mob.level().getBlockState(door);

                if (state.is(Blocks.IRON_DOOR)) {
                    mob.playSound(
                            SoundEvents.ZOMBIE_ATTACK_IRON_DOOR,
                            0.8F,
                            0.8F + mob.getRandom().nextFloat() * 0.2F
                    );
                } else {
                    mob.level().levelEvent(1019, door, 0);
                }

                mob.level().broadcastEntityEvent(mob, (byte)4);
            }
        }

        int maxTime = 0;
        for (BlockPos door : doors) {
            BlockState state = mob.level().getBlockState(door);
            maxTime = Math.max(maxTime, getBreakTime(state));
        }

        for (BlockPos door : doors) {

            BlockState state = mob.level().getBlockState(door);
            int doorTime = getBreakTime(state);

            int progress = (int)((float)breakTime / doorTime * 10F);

            int breakerId = mob.getId() + door.hashCode();

            mob.level().destroyBlockProgress(breakerId, door, progress);
            mob.level().destroyBlockProgress(breakerId, door.above(), progress);
        }

        Vec3 look = mob.getLookAngle();

        mob.setDeltaMovement(
                mob.getDeltaMovement().add(
                        look.x * 0.45D,
                        0.0D,
                        look.z * 0.45D
                )
        );

        if (breakTime >= maxTime) {
            for (BlockPos door : doors) {
                breakDoor(door);
            }

            mob.getNavigation().recomputePath();
        }
    }

    @Override
    public boolean canContinueToUse() {

        if (doors.isEmpty()) return false;

        doors.removeIf(pos -> {
            BlockState state = mob.level().getBlockState(pos);
            return !(state.getBlock() instanceof DoorBlock);
        });

        return !doors.isEmpty();
    }

    @Override
    public void stop() {

        for (BlockPos door : doors) {

            int breakerId = mob.getId() + door.hashCode();

            mob.level().destroyBlockProgress(breakerId, door, -1);
            mob.level().destroyBlockProgress(breakerId, door.above(), -1);
        }

        doors.clear();
        breakTime = 0;
    }
}
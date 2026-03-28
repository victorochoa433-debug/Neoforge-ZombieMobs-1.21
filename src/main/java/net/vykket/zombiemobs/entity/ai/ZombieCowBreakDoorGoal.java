package net.vykket.zombiemobs.entity.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.Vec3;
import net.vykket.zombiemobs.entity.ZombieCowEntity;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class ZombieCowBreakDoorGoal extends Goal {

    private final ZombieCowEntity mob;
    private final List<BlockPos> doors = new ArrayList<>();
    private int breakTime;

    private static final int MAX_BREAK_TIME = 60;

    public ZombieCowBreakDoorGoal(ZombieCowEntity mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (mob.getTarget() == null) return false;

        Level level = mob.level();
        Difficulty difficulty = level.getDifficulty();

        if (difficulty != Difficulty.NORMAL && difficulty != Difficulty.HARD) {
            return false;
        }

        doors.clear();

        BlockPos base = mob.blockPosition();
        BlockPos front = base.relative(mob.getDirection());
        BlockPos frontLeft = front.relative(mob.getDirection().getCounterClockWise());
        BlockPos frontRight = front.relative(mob.getDirection().getClockWise());

        checkDoor(front);
        checkDoor(frontLeft);
        checkDoor(frontRight);

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

    @Override
    public void start() {
        breakTime = 0;
    }

    @Override
    public void tick() {

        doors.removeIf(pos -> {
            BlockState state = mob.level().getBlockState(pos);
            return !(state.getBlock() instanceof DoorBlock);
        });

        if (doors.isEmpty()) {
            stop();
            return;
        }

        breakTime++;

        if (breakTime % 10 == 0) {

            for (BlockPos door : doors) {
                mob.level().levelEvent(1019, door, 0);
            }

            mob.triggerHeadbuttAnimation();

            Vec3 look = mob.getLookAngle();
            mob.setDeltaMovement(
                    mob.getDeltaMovement().add(
                            look.x * 0.25D,
                            0.0D,
                            look.z * 0.25D
                    )
            );
        }

        for (BlockPos door : doors) {

            int breakerId = (mob.getId() * 31) ^ door.hashCode();

            int progress = (int)((float)breakTime / MAX_BREAK_TIME * 10F);

            mob.level().destroyBlockProgress(breakerId, door, progress);
            mob.level().destroyBlockProgress(breakerId, door.above(), progress);
        }

        if (breakTime >= MAX_BREAK_TIME) {

            for (BlockPos door : doors) {

                int breakerId = (mob.getId() * 31) ^ door.hashCode();

                mob.level().destroyBlock(door, true);
                mob.level().destroyBlock(door.above(), true);

                mob.level().destroyBlockProgress(breakerId, door, -1);
                mob.level().destroyBlockProgress(breakerId, door.above(), -1);
            }

            doors.clear();
            mob.getNavigation().recomputePath();
            stop();
        }
    }

    @Override
    public boolean canContinueToUse() {

        if (doors.isEmpty()) return false;

        doors.removeIf(pos -> {
            BlockState state = mob.level().getBlockState(pos);
            return !(state.getBlock() instanceof DoorBlock);
        });

        if (doors.isEmpty()) return false;

        BlockPos closestDoor = doors.get(0);

        double distSq = mob.distanceToSqr(
                closestDoor.getX() + 0.5,
                closestDoor.getY(),
                closestDoor.getZ() + 0.5
        );

        if (distSq > 6.0D) {
            return false;
        }

        return breakTime < MAX_BREAK_TIME;
    }

    @Override
    public void stop() {

        for (BlockPos door : doors) {

            int breakerId = (mob.getId() * 31) ^ door.hashCode();

            mob.level().destroyBlockProgress(breakerId, door, -1);
            mob.level().destroyBlockProgress(breakerId, door.above(), -1);
        }

        doors.clear();
        breakTime = 0;
    }
}

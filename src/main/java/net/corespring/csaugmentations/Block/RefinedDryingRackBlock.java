package net.corespring.csaugmentations.Block;

import net.corespring.csaugmentations.Block.BlockEntities.RefinedDryingRackBlockEntity;
import net.corespring.csaugmentations.Registry.CSBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class RefinedDryingRackBlock extends AbstractDryingRackBlock {
    private final Block invisibleBlock;

    public RefinedDryingRackBlock(Properties properties, Block invisibleBlock) {
        super(properties);
        this.invisibleBlock = invisibleBlock;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return box(0, 0, 0, 16, 28, 16);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing = context.getHorizontalDirection().getOpposite();
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();

        BlockPos leftPos = pos.relative(facing.getClockWise());
        BlockPos rightPos = pos.relative(facing.getCounterClockWise());

        if (level.isEmptyBlock(leftPos) && level.isEmptyBlock(rightPos)) {
            level.setBlock(leftPos, invisibleBlock.defaultBlockState(), 3);
            level.setBlock(rightPos, invisibleBlock.defaultBlockState(), 3);
            return super.getStateForPlacement(context);
        }
        return null;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            Direction facing = state.getValue(FACING);
            BlockPos leftPos = pos.relative(facing.getClockWise());
            BlockPos rightPos = pos.relative(facing.getCounterClockWise());

            if (level.getBlockState(leftPos).getBlock() == invisibleBlock) {
                level.removeBlock(leftPos, false);
            }
            if (level.getBlockState(rightPos).getBlock() == invisibleBlock) {
                level.removeBlock(rightPos, false);
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RefinedDryingRackBlockEntity(pos, state);
    }

    @Override
    protected BlockEntityType<?> getBlockEntityType() {
        return CSBlockEntities.REFINED_DRYING_RACK_BE.get();
    }
}
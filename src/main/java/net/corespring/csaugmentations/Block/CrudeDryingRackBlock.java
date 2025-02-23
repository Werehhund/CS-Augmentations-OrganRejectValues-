package net.corespring.csaugmentations.Block;

import net.corespring.csaugmentations.Block.BlockEntities.CrudeDryingRackBlockEntity;
import net.corespring.csaugmentations.Registry.CSBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class CrudeDryingRackBlock extends AbstractDryingRackBlock {
    public CrudeDryingRackBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return box(0, 0, 0, 16, 24, 16);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CrudeDryingRackBlockEntity(pos, state);
    }

    @Override
    protected BlockEntityType<?> getBlockEntityType() {
        return CSBlockEntities.CRUDE_DRYING_RACK_BE.get();
    }
}
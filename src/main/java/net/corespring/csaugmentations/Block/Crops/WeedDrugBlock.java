package net.corespring.csaugmentations.Block.Crops;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public class WeedDrugBlock extends WildDrugBlock {
    public WeedDrugBlock(Properties pProperties) {
        super(pProperties);
    }

    protected boolean mayPlaceOn(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return pState.is(BlockTags.SAND);
    }
}

package net.corespring.csaugmentations.Block.Crops;

import net.corespring.csaugmentations.Registry.CSItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public class CocaCrop extends TwoTallCropBlock {
    public CocaCrop(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter pLevel, BlockPos pPos, BlockState pState) {
        return CSItems.COCA_SEEDS.get().getDefaultInstance();
    }
}

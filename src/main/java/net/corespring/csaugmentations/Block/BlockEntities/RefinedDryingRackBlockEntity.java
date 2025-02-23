package net.corespring.csaugmentations.Block.BlockEntities;

import net.corespring.csaugmentations.Client.Menus.RefinedDryingRackMenu;
import net.corespring.csaugmentations.Registry.CSBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class RefinedDryingRackBlockEntity extends AbstractDryingRackBlockEntity {
    public RefinedDryingRackBlockEntity(BlockPos pos, BlockState state) {
        super(CSBlockEntities.REFINED_DRYING_RACK_BE.get(), pos, state, 3);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new RefinedDryingRackMenu(containerId, inventory, this);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.csaugmentations.refined_drying_rack");
    }
}
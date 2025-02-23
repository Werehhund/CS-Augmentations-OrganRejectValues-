package net.corespring.csaugmentations.Block.BlockEntities;

import net.corespring.csaugmentations.Client.Menus.CrudeDryingRackMenu;
import net.corespring.csaugmentations.Registry.CSBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CrudeDryingRackBlockEntity extends AbstractDryingRackBlockEntity {
    public CrudeDryingRackBlockEntity(BlockPos pos, BlockState state) {
        super(CSBlockEntities.CRUDE_DRYING_RACK_BE.get(), pos, state, 1);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new CrudeDryingRackMenu(containerId, inventory, this);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.csaugmentations.crude_drying_rack");
    }
}
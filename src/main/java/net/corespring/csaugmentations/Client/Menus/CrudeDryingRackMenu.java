package net.corespring.csaugmentations.Client.Menus;

import net.corespring.csaugmentations.Block.BlockEntities.CrudeDryingRackBlockEntity;
import net.corespring.csaugmentations.Registry.CSBlockEntities;
import net.corespring.csaugmentations.Registry.CSMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.SimpleContainerData;

public class CrudeDryingRackMenu extends AbstractDryingRackMenu {
    public CrudeDryingRackMenu(int id, Inventory playerInventory, CrudeDryingRackBlockEntity blockEntity) {
        super(CSMenu.CRUDE_DRYING_RACK_MENU.get(), id, playerInventory, blockEntity, 1, new SimpleContainerData(1));
        addSlots(79, 19);
        addPlayerInventory(playerInventory);
    }

    public CrudeDryingRackMenu(int id, Inventory playerInventory, FriendlyByteBuf buf) {
        super(CSMenu.CRUDE_DRYING_RACK_MENU.get(), id, playerInventory, buf, CSBlockEntities.CRUDE_DRYING_RACK_BE.get());
        addSlots(79, 19);
        addPlayerInventory(playerInventory);
    }
}
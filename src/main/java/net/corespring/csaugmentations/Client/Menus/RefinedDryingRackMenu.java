package net.corespring.csaugmentations.Client.Menus;

import net.corespring.csaugmentations.Block.BlockEntities.RefinedDryingRackBlockEntity;
import net.corespring.csaugmentations.Registry.CSBlockEntities;
import net.corespring.csaugmentations.Registry.CSMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.SimpleContainerData;

public class RefinedDryingRackMenu extends AbstractDryingRackMenu {
    public RefinedDryingRackMenu(int id, Inventory playerInventory, RefinedDryingRackBlockEntity blockEntity) {
        super(CSMenu.REFINED_DRYING_RACK_MENU.get(), id, playerInventory, blockEntity, 3, new SimpleContainerData(3));
        addSlots(49, 19, 79, 19, 111, 19);
        addPlayerInventory(playerInventory);
    }

    public RefinedDryingRackMenu(int id, Inventory playerInventory, FriendlyByteBuf buf) {
        super(CSMenu.REFINED_DRYING_RACK_MENU.get(), id, playerInventory, buf, CSBlockEntities.REFINED_DRYING_RACK_BE.get());
        addSlots(49, 19, 79, 19, 111, 19);
        addPlayerInventory(playerInventory);
    }
}
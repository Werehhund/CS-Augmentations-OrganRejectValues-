package net.corespring.csaugmentations.Client.Menus;

import net.corespring.csaugmentations.Block.BlockEntities.ExtractorBlockEntity;
import net.corespring.csaugmentations.Registry.CSBlocks;
import net.corespring.csaugmentations.Registry.CSMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ExtractorMenu extends AbstractContainerMenu {
    private final ExtractorBlockEntity blockEntity;
    private final ContainerData data;

    public ExtractorMenu(int id, Inventory playerInventory, ExtractorBlockEntity blockEntity, ContainerData data) {
        super(CSMenu.EXTRACTOR_MENU.get(), id);
        this.blockEntity = blockEntity;
        this.data = data;

        addBlockEntitySlots();
        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);

        addDataSlots(data);
    }

    public ExtractorMenu(int id, Inventory playerInventory, FriendlyByteBuf buf) {
        this(id, playerInventory, playerInventory.player.level().getBlockEntity(buf.readBlockPos()) instanceof ExtractorBlockEntity blockEntity ?
                blockEntity : null, new SimpleContainerData(2));
    }

    private void addBlockEntitySlots() {
        ItemStackHandler handler = blockEntity.getItemHandler();
        this.addSlot(new SlotItemHandler(handler, ExtractorBlockEntity.INPUT_SLOT, 81, 5));
        this.addSlot(new SlotItemHandler(handler, ExtractorBlockEntity.OUTPUT_SLOT_1, 57, 54));
        this.addSlot(new SlotItemHandler(handler, ExtractorBlockEntity.OUTPUT_SLOT_2, 81, 59));
        this.addSlot(new SlotItemHandler(handler, ExtractorBlockEntity.OUTPUT_SLOT_3, 105, 54));
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            if (index < ExtractorBlockEntity.TOTAL_SLOTS) {
                if (!this.moveItemStackTo(itemstack1, ExtractorBlockEntity.TOTAL_SLOTS, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, ExtractorBlockEntity.INPUT_SLOT, ExtractorBlockEntity.INPUT_SLOT + 1, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos()),
                player, CSBlocks.EXTRACTOR.get());
    }

    public boolean isCrafting() {
        return data.get(0) > 0;
    }

    public int getScaledProgress() {
        int progress = this.data.get(0);
        int maxProgress = this.data.get(1);
        return maxProgress != 0 ? (progress * 14) / maxProgress : 0;
    }
}
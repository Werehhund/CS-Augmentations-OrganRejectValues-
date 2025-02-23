package net.corespring.csaugmentations.Client.Menus;

import net.corespring.csaugmentations.Block.BlockEntities.DistilleryBlockEntity;
import net.corespring.csaugmentations.Block.BlockEntities.FabricatorSlots;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import net.corespring.csaugmentations.Registry.CSBlocks;
import net.corespring.csaugmentations.Registry.CSMenu;

public class DistilleryMenu extends AbstractContainerMenu {
    private final DistilleryBlockEntity blockEntity;
    private final ContainerData data;

    public DistilleryMenu(int id, Inventory playerInventory, DistilleryBlockEntity blockEntity, ContainerData data) {
        super(CSMenu.DISTILLERY_MENU.get(), id);
        this.blockEntity = blockEntity;
        this.data = data;

        addSlots(blockEntity);
        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);

        addDataSlots(data);
    }

    public DistilleryMenu(int id, Inventory playerInventory, FriendlyByteBuf buf) {
        this(id, playerInventory,
                playerInventory.player.level().getBlockEntity(buf.readBlockPos()) instanceof DistilleryBlockEntity blockEntity
                        ? blockEntity
                        : null,
                new SimpleContainerData(4));
    }

    private void addSlots(DistilleryBlockEntity blockEntity) {
        this.addSlot(new SlotItemHandler(blockEntity.getItemHandler(),
                DistilleryBlockEntity.FUEL_SLOT, 22, 54));
        this.addSlot(new SlotItemHandler(blockEntity.getItemHandler(),
                DistilleryBlockEntity.INPUT_SLOT, 22, 5));
        this.addSlot(new FabricatorSlots.FabricatorOutputSlot(blockEntity.getItemHandler(),
                DistilleryBlockEntity.OUTPUT_SLOT_1, 103, 57));
        this.addSlot(new FabricatorSlots.FabricatorOutputSlot(blockEntity.getItemHandler(),
                DistilleryBlockEntity.OUTPUT_SLOT_2, 133, 57));
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            if (index < DistilleryBlockEntity.TOTAL_SLOTS) {
                if (!this.moveItemStackTo(itemstack1, DistilleryBlockEntity.TOTAL_SLOTS,
                        this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (this.moveItemStackTo(itemstack1, 0, DistilleryBlockEntity.TOTAL_SLOTS, false)) {
                    return ItemStack.EMPTY;
                }
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
        return stillValid(ContainerLevelAccess.create(blockEntity.getLevel(),
                blockEntity.getBlockPos()), player, CSBlocks.DISTILLERY.get());
    }

    public int getScaledProgress() {
        int progress = data.get(0);
        int maxProgress = data.get(1);
        return maxProgress != 0 ? (progress * 40) / maxProgress : 0;
    }

    public int getScaledFuelProgress() {
        int fuel = data.get(2);
        int maxFuel = data.get(3);
        return maxFuel != 0 ? (fuel * 13) / maxFuel : 0;
    }
}
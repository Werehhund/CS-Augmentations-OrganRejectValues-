package net.corespring.csaugmentations.Client.Menus;

import net.corespring.csaugmentations.Block.BlockEntities.AbstractDryingRackBlockEntity;
import net.corespring.csaugmentations.Registry.CSBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nullable;

public abstract class AbstractDryingRackMenu extends AbstractContainerMenu {
    protected final AbstractDryingRackBlockEntity blockEntity;
    protected final ContainerData data;
    protected final ItemStackHandler itemHandler;
    protected final int slotCount;

    protected AbstractDryingRackMenu(@Nullable MenuType<?> menuType, int id, Inventory playerInventory, AbstractDryingRackBlockEntity blockEntity, int slotCount, ContainerData data) {
        super(menuType, id);
        this.blockEntity = blockEntity;
        this.itemHandler = blockEntity.getItemHandler();
        this.slotCount = slotCount;
        this.data = data;

        addDataSlots(data);
    }

    protected AbstractDryingRackMenu(@Nullable MenuType<?> menuType, int id, Inventory playerInventory, FriendlyByteBuf buf, BlockEntityType<? extends AbstractDryingRackBlockEntity> expectedType) {
        this(menuType, id, playerInventory,
                getBlockEntity(playerInventory, buf, expectedType),
                expectedType == CSBlockEntities.CRUDE_DRYING_RACK_BE.get() ? 1 : 3,
                new SimpleContainerData(expectedType == CSBlockEntities.CRUDE_DRYING_RACK_BE.get() ? 1 : 3));
    }

    private static AbstractDryingRackBlockEntity getBlockEntity(Inventory playerInventory, FriendlyByteBuf buf, BlockEntityType<? extends AbstractDryingRackBlockEntity> type) {
        BlockPos pos = buf.readBlockPos();
        Level level = playerInventory.player.level();
        BlockEntity entity = level.getBlockEntity(pos);
        if (entity != null && entity.getType() == type) {
            return (AbstractDryingRackBlockEntity) entity;
        }
        throw new IllegalStateException("BlockEntity at " + pos + " is not correct type");
    }

    protected void addSlots(int... positions) {
        for (int i = 0; i < slotCount; i++) {
            int x = positions[i * 2];
            int y = positions[i * 2 + 1];
            this.addSlot(new SlotItemHandler(itemHandler, i, x, y));
        }
    }

    protected void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            if (index < slotCount) {
                if (!this.moveItemStackTo(itemstack1, slotCount, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, slotCount, false)) {
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
        return true;
    }
}
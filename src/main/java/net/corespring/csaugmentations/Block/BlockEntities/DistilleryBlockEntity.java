package net.corespring.csaugmentations.Block.BlockEntities;

import net.corespring.csaugmentations.Client.Menus.DistilleryMenu;
import net.corespring.csaugmentations.Recipes.DistilleryRecipe;
import net.corespring.csaugmentations.Registry.CSBlockEntities;
import net.corespring.csaugmentations.Registry.CSRecipeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class DistilleryBlockEntity extends BlockEntity implements MenuProvider {
    public static final int FUEL_SLOT = 0;
    public static final int INPUT_SLOT = 1;
    public static final int OUTPUT_SLOT_1 = 2;
    public static final int OUTPUT_SLOT_2 = 3;
    public static final int TOTAL_SLOTS = 4;

    protected final ContainerData data;
    private final ItemStackHandler itemHandler = new ItemStackHandler(TOTAL_SLOTS) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return switch (slot) {
                case FUEL_SLOT -> ForgeHooks.getBurnTime(stack, null) > 0;
                case INPUT_SLOT -> true;
                case OUTPUT_SLOT_1, OUTPUT_SLOT_2 -> false;
                default -> super.isItemValid(slot, stack);
            };
        }
    };

    private LazyOptional<IItemHandler> inputHandler = LazyOptional.empty();
    private LazyOptional<IItemHandler> outputHandler = LazyOptional.empty();
    private int progress = 0;
    private int maxProgress = 200;
    private int burnTime = 0;
    private int fuelTime = 0;

    public DistilleryBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(CSBlockEntities.DISTILLERY_BE.get(), pPos, pBlockState);
        this.data = new ContainerData() {
            public int get(int index) {
                return switch (index) {
                    case 0 -> progress;
                    case 1 -> maxProgress;
                    case 2 -> burnTime;
                    case 3 -> fuelTime;
                    default -> 0;
                };
            }

            public void set(int index, int value) {
                switch (index) {
                    case 0 -> progress = value;
                    case 1 -> maxProgress = value;
                    case 2 -> burnTime = value;
                    case 3 -> fuelTime = value;
                }
            }

            public int getCount() {
                return 4;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.csaugmentations.distillery");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new DistilleryMenu(i, inventory, this, this.data);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        inputHandler = LazyOptional.of(() -> new InputHandler(itemHandler));
        outputHandler = LazyOptional.of(() -> new OutputHandler(itemHandler));
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        inputHandler.invalidate();
        outputHandler.invalidate();
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return side == Direction.DOWN ? outputHandler.cast() : inputHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.put("inventory", itemHandler.serializeNBT());
        tag.putInt("progress", progress);
        tag.putInt("burnTime", burnTime);
        tag.putInt("fuelTime", fuelTime);
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        itemHandler.deserializeNBT(tag.getCompound("inventory"));
        progress = tag.getInt("progress");
        burnTime = tag.getInt("burnTime");
        fuelTime = tag.getInt("fuelTime");
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide) return;

        boolean isBurning = burnTime > 0;
        boolean hasRecipe = getCurrentRecipe().isPresent();
        boolean canCraft = hasRecipe && canProcess(getCurrentRecipe().get());

        if (hasRecipe) {
            this.maxProgress = getCurrentRecipe().get().getProcessingTime();
        }

        if (!isBurning && canCraft) {
            tryConsumeFuel(level);
            isBurning = burnTime > 0;
        }

        if (isBurning && canCraft) {
            progress++;
            if (progress >= maxProgress) {
                craftItem(getCurrentRecipe().get());
                resetProgress();
            }
            setChanged();
        } else {
            resetProgress();
        }

        if (isBurning) {
            burnTime--;
            setChanged();
        }
    }

    private void tryConsumeFuel(Level level) {
        ItemStack fuelStack = itemHandler.getStackInSlot(FUEL_SLOT);
        int burnTimeValue = ForgeHooks.getBurnTime(fuelStack, null);

        if (burnTimeValue > 0) {
            burnTime = burnTimeValue;
            fuelTime = burnTimeValue;

            ItemStack extracted = itemHandler.extractItem(FUEL_SLOT, 1, false);
            if (!extracted.isEmpty()) {
                ItemStack container = extracted.getCraftingRemainingItem();
                if (!container.isEmpty()) {
                    ItemStack remaining = itemHandler.insertItem(FUEL_SLOT, container, false);
                    if (!remaining.isEmpty()) {
                        Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), remaining);
                    }
                }
            }
            setChanged();
        }
    }

    private Optional<DistilleryRecipe> getCurrentRecipe() {
        SimpleContainer inventory = new SimpleContainer(TOTAL_SLOTS);
        for (int i = 0; i < TOTAL_SLOTS; i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        return level.getRecipeManager().getRecipeFor(CSRecipeTypes.DISTILLING.get(), inventory, level);
    }

    private boolean canProcess(DistilleryRecipe recipe) {
        if (recipe == null) return false;

        ItemStack output1 = recipe.getResultItem1(level.registryAccess());
        ItemStack output2 = recipe.getResultItem2(level.registryAccess());

        boolean canAcceptOutput1 = output1.isEmpty() || canInsertItem(output1, OUTPUT_SLOT_1);
        boolean canAcceptOutput2 = output2.isEmpty() || canInsertItem(output2, OUTPUT_SLOT_2);

        return canAcceptOutput1 && canAcceptOutput2;
    }

    private boolean canInsertItem(ItemStack stack, int slot) {
        ItemStack slotStack = itemHandler.getStackInSlot(slot);
        return slotStack.isEmpty() ||
                (ItemStack.isSameItemSameTags(slotStack, stack) &&
                        slotStack.getCount() + stack.getCount() <= stack.getMaxStackSize());
    }

    private void craftItem(DistilleryRecipe recipe) {
        itemHandler.extractItem(INPUT_SLOT, 1, false);

        ItemStack output1 = recipe.getResultItem1(level.registryAccess()).copy();
        ItemStack output2 = recipe.getResultItem2(level.registryAccess()).copy();

        if (!output1.isEmpty()) {
            ItemStack existing = itemHandler.getStackInSlot(OUTPUT_SLOT_1);
            if (existing.isEmpty()) {
                itemHandler.setStackInSlot(OUTPUT_SLOT_1, output1);
            } else if (ItemStack.isSameItemSameTags(existing, output1)) {
                existing.grow(output1.getCount());
            }
        }

        if (!output2.isEmpty()) {
            ItemStack existing = itemHandler.getStackInSlot(OUTPUT_SLOT_2);
            if (existing.isEmpty()) {
                itemHandler.setStackInSlot(OUTPUT_SLOT_2, output2);
            } else if (ItemStack.isSameItemSameTags(existing, output2)) {
                existing.grow(output2.getCount());
            }
        }
    }

    private void resetProgress() {
        progress = 0;
        setChanged();
    }

    public ItemStackHandler getItemHandler() {
        return this.itemHandler;
    }

    private record InputHandler(ItemStackHandler handler) implements IItemHandler {

        @Override
        public int getSlots() {
            return handler.getSlots();
        }

        @NotNull
        @Override
        public ItemStack getStackInSlot(int slot) {
            return handler.getStackInSlot(slot);
        }

        @NotNull
        @Override
        public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            if (slot == FUEL_SLOT || slot == INPUT_SLOT) {
                return handler.insertItem(slot, stack, simulate);
            }
            return stack;
        }

        @NotNull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            return ItemStack.EMPTY;
        }

        @Override
        public int getSlotLimit(int slot) {
            return handler.getSlotLimit(slot);
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return handler.isItemValid(slot, stack);
        }
    }

    private record OutputHandler(ItemStackHandler handler) implements IItemHandler {

        @Override
        public int getSlots() {
            return handler.getSlots();
        }

        @NotNull
        @Override
        public ItemStack getStackInSlot(int slot) {
            return handler.getStackInSlot(slot);
        }

        @NotNull
        @Override
        public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            return stack;
        }

        @NotNull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (slot == OUTPUT_SLOT_1 || slot == OUTPUT_SLOT_2) {
                return handler.extractItem(slot, amount, simulate);
            }
            return ItemStack.EMPTY;
        }

        @Override
        public int getSlotLimit(int slot) {
            return handler.getSlotLimit(slot);
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return false;
        }
    }
}
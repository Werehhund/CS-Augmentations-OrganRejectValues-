package net.corespring.csaugmentations.Block.BlockEntities;

import net.corespring.csaugmentations.Client.Menus.ExtractorMenu;
import net.corespring.csaugmentations.Recipes.ExtractorRecipe;
import net.corespring.csaugmentations.Registry.CSBlockEntities;
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
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class ExtractorBlockEntity extends BlockEntity implements MenuProvider {
    public static final int INPUT_SLOT = 0;
    public static final int OUTPUT_SLOT_1 = 1;
    public static final int OUTPUT_SLOT_2 = 2;
    public static final int OUTPUT_SLOT_3 = 3;
    public static final int TOTAL_SLOTS = 4;

    protected final ContainerData data;
    private final ItemStackHandler itemHandler = new ItemStackHandler(TOTAL_SLOTS) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return slot == INPUT_SLOT;
        }
    };

    public ItemStackHandler getItemHandler() {
        return this.itemHandler;
    }

    private LazyOptional<IItemHandler> inputHandler = LazyOptional.empty();
    private LazyOptional<IItemHandler> outputHandler = LazyOptional.empty();
    private int progress = 0;
    private int maxProgress = 0;

    public ExtractorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(CSBlockEntities.EXTRACTOR_BE.get(), pPos, pBlockState);
        this.data = new ContainerData() {
            public int get(int index) {
                return switch (index) {
                    case 0 -> progress;
                    case 1 -> maxProgress;
                    default -> 0;
                };
            }

            public void set(int index, int value) {
                switch (index) {
                    case 0 -> progress = value;
                    case 1 -> maxProgress = value;
                }
            }

            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.csaugmentations.extractor");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new ExtractorMenu(i, inventory, this, this.data);
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
            return side == null ? inputHandler.cast() : outputHandler.cast();
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
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        itemHandler.deserializeNBT(tag.getCompound("inventory"));
        progress = tag.getInt("progress");
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide) return;

        Optional<ExtractorRecipe> recipeOptional = getCurrentRecipe();

        if (recipeOptional.isPresent()) {
            ExtractorRecipe recipe = recipeOptional.get();
            maxProgress = recipe.getProcessingTime();

            if (canProcess(recipe)) {
                progress++;
                if (progress >= maxProgress) {
                    craftItem(recipe);
                    resetProgress();
                }
                setChanged();
            } else {
                resetProgress();
            }
        } else {
            resetProgress();
        }
    }

    private Optional<ExtractorRecipe> getCurrentRecipe() {
        SimpleContainer inventory = new SimpleContainer(1);
        inventory.setItem(0, itemHandler.getStackInSlot(INPUT_SLOT));
        return level.getRecipeManager().getRecipeFor(ExtractorRecipe.Type.INSTANCE, inventory, level);
    }

    private boolean canProcess(ExtractorRecipe recipe) {
        if (recipe == null) return false;

        ItemStack inputStack = itemHandler.getStackInSlot(INPUT_SLOT);
        if (!recipe.getInputIngredient().ingredient().test(inputStack) || inputStack.getCount() < recipe.getInputIngredient().count()) {
            return false;
        }

        List<ItemStack> outputs = recipe.getOutputs();
        for (int i = 0; i < outputs.size(); i++) {
            ItemStack output = outputs.get(i);
            int slot = OUTPUT_SLOT_1 + i;
            if (!canInsertItem(output, slot)) {
                return false;
            }
        }
        return true;
    }

    private boolean canInsertItem(ItemStack stack, int slot) {
        ItemStack slotStack = itemHandler.getStackInSlot(slot);
        return slotStack.isEmpty() ||
                (ItemStack.isSameItemSameTags(slotStack, stack) &&
                        slotStack.getCount() + stack.getCount() <= stack.getMaxStackSize());
    }

    private void craftItem(ExtractorRecipe recipe) {
        itemHandler.extractItem(INPUT_SLOT, recipe.getInputIngredient().count(), false);

        List<ItemStack> outputs = recipe.getOutputs();
        for (int i = 0; i < outputs.size(); i++) {
            ItemStack output = outputs.get(i).copy();
            int slot = OUTPUT_SLOT_1 + i;

            ItemStack existing = itemHandler.getStackInSlot(slot);
            if (existing.isEmpty()) {
                itemHandler.setStackInSlot(slot, output);
            } else if (ItemStack.isSameItemSameTags(existing, output)) {
                existing.grow(output.getCount());
            }
        }
    }

    private void resetProgress() {
        progress = 0;
        setChanged();
    }

    private record InputHandler(ItemStackHandler handler) implements IItemHandler {
        @Override
        public int getSlots() { return handler.getSlots(); }

        @NotNull
        @Override
        public ItemStack getStackInSlot(int slot) { return handler.getStackInSlot(slot); }

        @NotNull
        @Override
        public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            return slot == INPUT_SLOT ? handler.insertItem(slot, stack, simulate) : stack;
        }

        @NotNull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            return ItemStack.EMPTY;
        }

        @Override
        public int getSlotLimit(int slot) { return handler.getSlotLimit(slot); }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return handler.isItemValid(slot, stack);
        }
    }

    private record OutputHandler(ItemStackHandler handler) implements IItemHandler {
        @Override
        public int getSlots() { return handler.getSlots(); }

        @NotNull
        @Override
        public ItemStack getStackInSlot(int slot) { return handler.getStackInSlot(slot); }

        @NotNull
        @Override
        public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            return stack;
        }

        @NotNull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (slot >= OUTPUT_SLOT_1 && slot <= OUTPUT_SLOT_3) {
                return handler.extractItem(slot, amount, simulate);
            }
            return ItemStack.EMPTY;
        }

        @Override
        public int getSlotLimit(int slot) { return handler.getSlotLimit(slot); }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return false;
        }
    }
}
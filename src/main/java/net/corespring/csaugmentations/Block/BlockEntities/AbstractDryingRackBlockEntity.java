package net.corespring.csaugmentations.Block.BlockEntities;

import net.corespring.csaugmentations.Recipes.DryingRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Optional;

public abstract class AbstractDryingRackBlockEntity extends BlockEntity implements MenuProvider {
    protected final int[] progress;
    protected final ItemStack[] lastProcessedStacks;
    private final ItemStackHandler itemHandler;
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    public AbstractDryingRackBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int slotCount) {
        super(type, pos, state);
        this.progress = new int[slotCount];
        this.lastProcessedStacks = new ItemStack[slotCount];
        Arrays.fill(lastProcessedStacks, ItemStack.EMPTY);
        this.itemHandler = new ItemStackHandler(slotCount) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
                if(!level.isClientSide()) {
                    level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
                }
            }

            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                if (level == null) return false;
                SimpleContainer inv = new SimpleContainer(stack);
                return level.getRecipeManager().getRecipeFor(DryingRecipe.Type.INSTANCE, inv, level).isPresent();
            }
        };
    }

    public static void tick(Level level, BlockPos pos, BlockState state, AbstractDryingRackBlockEntity blockEntity) {
        blockEntity.handleTick(level, pos, state);
    }

    public ItemStackHandler getItemHandler() {
        return this.itemHandler;
    }

    public void handleTick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide) return;

        for (int slot = 0; slot < this.itemHandler.getSlots(); slot++) {
            ItemStack currentStack = this.itemHandler.getStackInSlot(slot);
            ItemStack lastStack = this.lastProcessedStacks[slot];

            if (!ItemStack.isSameItemSameTags(currentStack, lastStack)) {
                this.progress[slot] = 0;
                this.lastProcessedStacks[slot] = currentStack.copy();
                this.setChanged();
            }

            if (currentStack.isEmpty()) {
                this.progress[slot] = 0;
                continue;
            }

            SimpleContainer inv = new SimpleContainer(currentStack);
            Optional<DryingRecipe> recipeOptional = level.getRecipeManager()
                    .getRecipeFor(DryingRecipe.Type.INSTANCE, inv, level);

            if (recipeOptional.isPresent()) {
                DryingRecipe recipe = recipeOptional.get();
                int requiredTicks = currentStack.getCount() * recipe.getProcessingTime();

                if (requiredTicks <= 0) {
                    this.progress[slot] = 0;
                    continue;
                }

                this.progress[slot]++;
                if (this.progress[slot] >= requiredTicks) {
                    ItemStack result = recipe.getResultItem(level.registryAccess()).copy();
                    result.setCount(currentStack.getCount());
                    this.itemHandler.setStackInSlot(slot, result);
                    this.progress[slot] = 0;
                    this.lastProcessedStacks[slot] = ItemStack.EMPTY;
                    this.setChanged();
                }
            } else {
                this.progress[slot] = 0;
            }
        }
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
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
        tag.putIntArray("progress", progress);

        CompoundTag lastProcessedTag = new CompoundTag();
        for (int i = 0; i < lastProcessedStacks.length; i++) {
            CompoundTag stackTag = new CompoundTag();
            lastProcessedStacks[i].save(stackTag);
            lastProcessedTag.put("Slot_" + i, stackTag);
        }
        tag.put("LastProcessedStacks", lastProcessedTag);
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        itemHandler.deserializeNBT(tag.getCompound("inventory"));
        int[] progressArray = tag.getIntArray("progress");
        System.arraycopy(progressArray, 0, progress, 0, Math.min(progressArray.length, progress.length));

        CompoundTag lastProcessedTag = tag.getCompound("LastProcessedStacks");
        for (int i = 0; i < lastProcessedStacks.length; i++) {
            CompoundTag stackTag = lastProcessedTag.getCompound("Slot_" + i);
            lastProcessedStacks[i] = ItemStack.of(stackTag);
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
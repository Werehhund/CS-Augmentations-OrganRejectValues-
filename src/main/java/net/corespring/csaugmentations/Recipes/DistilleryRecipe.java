package net.corespring.csaugmentations.Recipes;

import net.corespring.csaugmentations.Block.BlockEntities.DistilleryBlockEntity;
import net.corespring.csaugmentations.Registry.CSBlocks;
import net.corespring.csaugmentations.Registry.CSRecipeSerializers;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class DistilleryRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final String group;
    private final Ingredient ingredient;
    private final ItemStack result1;
    private final ItemStack result2;
    private final int processingTime;

    public DistilleryRecipe(ResourceLocation id, String group, Ingredient ingredient, ItemStack result1, ItemStack result2, int processingTime) {
        this.id = id;
        this.group = group;
        this.ingredient = ingredient;
        this.result1 = result1;
        this.result2 = result2;
        this.processingTime = processingTime;
    }

    @Override
    public boolean matches(Container container, Level level) {
        ItemStack inputStack = container.getItem(DistilleryBlockEntity.INPUT_SLOT);
        return ingredient.test(inputStack);
    }

    @Override
    public ItemStack assemble(Container container, RegistryAccess registryAccess) {
        return result1.copy();
    }

    public ItemStack getResultItem1(RegistryAccess registryAccess) {
        return result1.copy();
    }


    public ItemStack getResultItem2(RegistryAccess registryAccess) {
        return result2.copy();
    }

    public int getProcessingTime() {
        return processingTime;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return result1.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return CSRecipeSerializers.DISTILLERY_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(ingredient);
        return list;
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(CSBlocks.DISTILLERY.get());
    }

    @Override
    public String getGroup() {
        return group;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(Container container) {
        NonNullList<ItemStack> remainders = NonNullList.withSize(container.getContainerSize(), ItemStack.EMPTY);
        ItemStack input = container.getItem(DistilleryBlockEntity.INPUT_SLOT);
        if (!input.isEmpty()) {
            ItemStack containerItem = net.minecraftforge.common.ForgeHooks.getCraftingRemainingItem(input);
            remainders.set(DistilleryBlockEntity.INPUT_SLOT, containerItem);
        }
        return remainders;
    }

    public static class Type implements RecipeType<DistilleryRecipe> {
        public static final DistilleryRecipe.Type INSTANCE = new DistilleryRecipe.Type();
    }
}
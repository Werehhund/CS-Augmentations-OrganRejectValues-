package net.corespring.csaugmentations.Recipes;

import net.corespring.csaugmentations.Registry.CSBlocks;
import net.corespring.csaugmentations.Registry.CSRecipeSerializers;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.Collections;
import java.util.List;

public class ExtractorRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final String group;
    private final IngredientWithCount input;
    private final List<ItemStack> outputs;
    private final int processingTime;

    public ExtractorRecipe(ResourceLocation id, String group, IngredientWithCount input, List<ItemStack> outputs, int processingTime) {
        this.id = id;
        this.group = group;
        this.input = input;
        this.outputs = outputs;
        this.processingTime = processingTime;
    }

    public IngredientWithCount getInputIngredient() {
        return input;
    }

    public List<ItemStack> getOutputs() {
        return Collections.unmodifiableList(outputs);
    }

    public int getProcessingTime() {
        return processingTime;
    }

    @Override
    public boolean matches(Container container, Level level) {
        ItemStack inputStack = container.getItem(0);
        return input.ingredient().test(inputStack) && inputStack.getCount() >= input.count();
    }

    @Override
    public ItemStack assemble(Container container, RegistryAccess registryAccess) {
        return outputs.isEmpty() ? ItemStack.EMPTY : outputs.get(0).copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return outputs.isEmpty() ? ItemStack.EMPTY : outputs.get(0).copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return CSRecipeSerializers.EXTRACTING_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(CSBlocks.EXTRACTOR.get());
    }

    @Override
    public String getGroup() {
        return group;
    }

    public record IngredientWithCount(Ingredient ingredient, int count) {
    }

    public static class Type implements RecipeType<ExtractorRecipe> {
        public static final Type INSTANCE = new Type();
    }
}
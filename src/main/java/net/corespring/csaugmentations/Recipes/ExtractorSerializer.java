package net.corespring.csaugmentations.Recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;

import java.util.ArrayList;
import java.util.List;

public class ExtractorSerializer implements RecipeSerializer<ExtractorRecipe> {
    public static final ExtractorSerializer INSTANCE = new ExtractorSerializer();

    @Override
    public ExtractorRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        String group = GsonHelper.getAsString(json, "group", "");

        JsonObject inputObj = GsonHelper.getAsJsonObject(json, "input");
        Ingredient ingredient = Ingredient.fromJson(inputObj.get("ingredient"));
        int inputCount = GsonHelper.getAsInt(inputObj, "count", 1);
        ExtractorRecipe.IngredientWithCount input = new ExtractorRecipe.IngredientWithCount(ingredient, inputCount);

        JsonArray outputsArray = GsonHelper.getAsJsonArray(json, "outputs");
        List<ItemStack> outputs = new ArrayList<>();
        for (int i = 0; i < Math.min(outputsArray.size(), 3); i++) {
            outputs.add(ShapedRecipe.itemStackFromJson(outputsArray.get(i).getAsJsonObject()));
        }

        int processingTime = GsonHelper.getAsInt(json, "processingTime", 200);

        return new ExtractorRecipe(recipeId, group, input, outputs, processingTime);
    }

    @Override
    public ExtractorRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        String group = buffer.readUtf();
        Ingredient ingredient = Ingredient.fromNetwork(buffer);
        int inputCount = buffer.readVarInt();
        ExtractorRecipe.IngredientWithCount input = new ExtractorRecipe.IngredientWithCount(ingredient, inputCount);

        int outputCount = Math.min(buffer.readVarInt(), 3);
        List<ItemStack> outputs = new ArrayList<>();
        for (int i = 0; i < outputCount; i++) {
            outputs.add(buffer.readItem());
        }

        int processingTime = buffer.readVarInt();

        return new ExtractorRecipe(recipeId, group, input, outputs, processingTime);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, ExtractorRecipe recipe) {
        buffer.writeUtf(recipe.getGroup());
        recipe.getInputIngredient().ingredient().toNetwork(buffer);
        buffer.writeVarInt(recipe.getInputIngredient().count());

        buffer.writeVarInt(recipe.getOutputs().size());
        for (ItemStack output : recipe.getOutputs()) {
            buffer.writeItem(output);
        }

        buffer.writeVarInt(recipe.getProcessingTime());
    }
}
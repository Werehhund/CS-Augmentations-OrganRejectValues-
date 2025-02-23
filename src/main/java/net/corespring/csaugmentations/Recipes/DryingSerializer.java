package net.corespring.csaugmentations.Recipes;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;

public class DryingSerializer implements RecipeSerializer<DryingRecipe> {
    public static final DryingSerializer INSTANCE = new DryingSerializer();

    @Override
    public DryingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        Ingredient input = Ingredient.fromJson(json.getAsJsonObject("input"));
        ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));
        int processingTime = GsonHelper.getAsInt(json, "processingTime", 200);
        return new DryingRecipe(recipeId, input, output, processingTime);
    }

    @Override
    public DryingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        Ingredient input = Ingredient.fromNetwork(buffer);
        ItemStack output = buffer.readItem();
        int processingTime = buffer.readVarInt();
        return new DryingRecipe(recipeId, input, output, processingTime);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, DryingRecipe recipe) {
        recipe.getInput().toNetwork(buffer);
        buffer.writeItem(recipe.getResultItem(null));
        buffer.writeVarInt(recipe.getProcessingTime());
    }
}
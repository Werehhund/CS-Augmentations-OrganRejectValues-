package net.corespring.csaugmentations.Recipes;

import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;

public class DistillerySerializer implements RecipeSerializer<DistilleryRecipe> {
    public static final DistillerySerializer INSTANCE = new DistillerySerializer();

    @Override
    public DistilleryRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        System.out.println("Loading Distillery Recipe: " + recipeId);

        String group = GsonHelper.getAsString(json, "group", "");
        Ingredient ingredient = Ingredient.fromJson(json.get("ingredient"));

        JsonObject result1Json = GsonHelper.getAsJsonObject(json, "result1");
        ItemStack result1 = ShapedRecipe.itemStackFromJson(result1Json);

        ItemStack result2 = ItemStack.EMPTY;
        if (json.has("result2")) {
            JsonObject result2Json = GsonHelper.getAsJsonObject(json, "result2");
            result2 = ShapedRecipe.itemStackFromJson(result2Json);
        }

        int processingTime = GsonHelper.getAsInt(json, "processingTime", 200);

        return new DistilleryRecipe(recipeId, group, ingredient, result1, result2, processingTime);
    }

    @Override
    public DistilleryRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        String group = buffer.readUtf();
        Ingredient ingredient = Ingredient.fromNetwork(buffer);
        ItemStack result1 = buffer.readItem();
        ItemStack result2 = buffer.readItem();
        int processingTime = buffer.readVarInt();
        return new DistilleryRecipe(recipeId, group, ingredient, result1, result2, processingTime);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, DistilleryRecipe recipe) {
        buffer.writeUtf(recipe.getGroup());
        recipe.getIngredients().get(0).toNetwork(buffer);
        buffer.writeItem(recipe.getResultItem(RegistryAccess.EMPTY));
        buffer.writeItem(recipe.getResultItem2(RegistryAccess.EMPTY));
        buffer.writeVarInt(recipe.getProcessingTime());
    }
}
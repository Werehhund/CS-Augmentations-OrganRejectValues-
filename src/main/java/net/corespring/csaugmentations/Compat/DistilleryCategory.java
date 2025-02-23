package net.corespring.csaugmentations.Compat;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.corespring.csaugmentations.CSAugmentations;
import net.corespring.csaugmentations.Recipes.DistilleryRecipe;
import net.corespring.csaugmentations.Registry.CSBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class DistilleryCategory implements IRecipeCategory<DistilleryRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(CSAugmentations.MOD_ID, "distillery");
    public static final ResourceLocation TEXTURE = new ResourceLocation(CSAugmentations.MOD_ID, "textures/gui/distillery_gui.png");
    public static final RecipeType<DistilleryRecipe> DISTILLING = new RecipeType<>(UID, DistilleryRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public DistilleryCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 166);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(CSBlocks.DISTILLERY.get()));
    }

    @Override
    public @NotNull RecipeType<DistilleryRecipe> getRecipeType() {
        return DISTILLING;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("block.csaugmentations.distillery");
    }

    @Override
    public @NotNull IDrawable getBackground() {
        return this.background;
    }

    @Override
    public @NotNull IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, DistilleryRecipe recipe, @NotNull IFocusGroup focusGroup) {
        RegistryAccess registryAccess = Minecraft.getInstance().level.registryAccess();
        builder.addSlot(RecipeIngredientRole.INPUT, 22, 5).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 103, 57).addItemStack(recipe.getResultItem1(registryAccess));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 133, 57).addItemStack(recipe.getResultItem2(registryAccess));
    }

    @Override
    public void draw(DistilleryRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        this.drawProcessingTime(recipe, guiGraphics, 70);
    }

    private void drawProcessingTime(DistilleryRecipe recipe, GuiGraphics guiGraphics, int y) {
        int processingTime = recipe.getProcessingTime();
        if (processingTime > 0) {
            int processingSeconds = processingTime / 20;
            Component timeString = Component.translatable("gui.jei.category.smelting.time.seconds", processingSeconds);
            Font font = Minecraft.getInstance().font;
            int stringWidth = font.width(timeString);
            guiGraphics.drawString(font, timeString, background.getWidth() - stringWidth - 5, y, 0x404040, false);
        }
    }
}
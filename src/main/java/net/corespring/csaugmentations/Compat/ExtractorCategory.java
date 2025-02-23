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
import net.corespring.csaugmentations.Recipes.ExtractorRecipe;
import net.corespring.csaugmentations.Registry.CSBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class ExtractorCategory implements IRecipeCategory<ExtractorRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(CSAugmentations.MOD_ID, "extractor");
    public static final ResourceLocation TEXTURE = new ResourceLocation(CSAugmentations.MOD_ID, "textures/gui/extractor_gui.png");
    public static final RecipeType<ExtractorRecipe> EXTRACTING = new RecipeType<>(UID, ExtractorRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public ExtractorCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 83);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(CSBlocks.EXTRACTOR.get()));
    }

    @Override
    public RecipeType<ExtractorRecipe> getRecipeType() {
        return EXTRACTING;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.csaugmentations.extractor");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ExtractorRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 81, 5)
                .addIngredients(recipe.getInputIngredient().ingredient());

        int outputCount = recipe.getOutputs().size();
        switch(outputCount) {
            case 3:
                builder.addSlot(RecipeIngredientRole.OUTPUT, 105, 54)
                        .addItemStack(recipe.getOutputs().get(2));
            case 2:
                builder.addSlot(RecipeIngredientRole.OUTPUT, 81, 59)
                        .addItemStack(recipe.getOutputs().get(1));
            case 1:
                builder.addSlot(RecipeIngredientRole.OUTPUT, 57, 54)
                        .addItemStack(recipe.getOutputs().get(0));
                break;
            default:
                CSAugmentations.LOGGER.error("Invalid output count for extractor recipe: {}", outputCount);
                break;
        }
    }

    @Override
    public void draw(ExtractorRecipe recipe, IRecipeSlotsView slots, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        this.drawProcessingTime(recipe, guiGraphics, 70);
    }

    private void drawProcessingTime(ExtractorRecipe recipe, GuiGraphics guiGraphics, int y) {
        int time = recipe.getProcessingTime();
        if (time > 0) {
            int seconds = time / 20;
            Component timeString = Component.translatable("gui.jei.category.smelting.time.seconds", seconds);
            Font font = Minecraft.getInstance().font;
            int stringWidth = font.width(timeString);
            guiGraphics.drawString(font, timeString, background.getWidth() - stringWidth - 5, y, 0x404040, false);
        }
    }
}
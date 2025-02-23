package net.corespring.csaugmentations.Compat;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.corespring.csaugmentations.CSAugmentations;
import net.corespring.csaugmentations.Recipes.DryingRecipe;
import net.corespring.csaugmentations.Registry.CSBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class DryingCategory implements IRecipeCategory<DryingRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(CSAugmentations.MOD_ID, "drying");
    public static final ResourceLocation TEXTURE = new ResourceLocation(CSAugmentations.MOD_ID, "textures/gui/jei_atlas.png");
    public static final RecipeType<DryingRecipe> DRYING = new RecipeType<>(UID, DryingRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated progressArrow;

    public DryingCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 90, 84, 53);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK,
                new ItemStack(CSBlocks.CRUDE_DRYING_RACK.get()));

        IDrawableStatic progressArrowStatic = helper.createDrawable(TEXTURE, 84, 90, 24, 19);
        this.progressArrow = helper.createAnimatedDrawable(progressArrowStatic, 200, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public RecipeType<DryingRecipe> getRecipeType() {
        return DRYING;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("menu.csaugmentations.drying_rack");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void draw(DryingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics,
                     double mouseX, double mouseY) {
        this.drawProcessingTime(recipe, guiGraphics, 45);
        this.progressArrow.draw(guiGraphics, 24, 19);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, DryingRecipe recipe, IFocusGroup focusGroup) {
        RegistryAccess registryAccess = Minecraft.getInstance().level.registryAccess();

        builder.addSlot(RecipeIngredientRole.INPUT, 4, 18).addIngredients(recipe.getInput());

        builder.addSlot(RecipeIngredientRole.OUTPUT, 61, 19).addItemStack(recipe.getResultItem(registryAccess));
    }

    protected void drawProcessingTime(DryingRecipe recipe, GuiGraphics guiGraphics, int y) {
        int processingTime = recipe.getProcessingTime();
        if (processingTime > 0) {
            int seconds = processingTime / 20;
            Component timeString = Component.translatable("gui.jei.category.smelting.time.seconds", seconds);
            Font font = Minecraft.getInstance().font;
            int stringWidth = font.width(timeString);
            guiGraphics.drawString(font, timeString, this.getWidth() - stringWidth - 5, y, -8355712, false);
        }
    }
}
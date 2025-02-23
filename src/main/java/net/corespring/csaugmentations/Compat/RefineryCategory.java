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
import net.corespring.csaugmentations.Recipes.RefineryRecipe;
import net.corespring.csaugmentations.Registry.CSBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class RefineryCategory implements IRecipeCategory<RefineryRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(CSAugmentations.MOD_ID, "refining");
    public static final ResourceLocation TEXTURE = new ResourceLocation(CSAugmentations.MOD_ID, "textures/gui/jei_atlas.png");

    public static final RecipeType<RefineryRecipe> REFINING = new RecipeType<>(UID, RefineryRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated progressArrow;
    private final IDrawableAnimated fuelBar;

    public RefineryCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 82, 53);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(CSBlocks.REFINERY.get()));

        IDrawableStatic progressArrowStatic = helper.createDrawable(TEXTURE, 84, 14, 24, 19);
        IDrawableStatic fuelBarStatic = helper.createDrawable(TEXTURE, 84, 0, 14, 14);
        this.progressArrow = helper.createAnimatedDrawable(progressArrowStatic, 200, IDrawableAnimated.StartDirection.LEFT, false);
        this.fuelBar = helper.createAnimatedDrawable(fuelBarStatic, 300, IDrawableAnimated.StartDirection.TOP, true);
    }

    @Override
    public RecipeType<RefineryRecipe> getRecipeType() {
        return REFINING;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.csaugmentations.refinery");
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
    public void setRecipe(IRecipeLayoutBuilder builder, RefineryRecipe recipe, IFocusGroup focuses) {
        RegistryAccess registryAccess = Minecraft.getInstance().level.registryAccess();

        builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 62, 19).addItemStack(recipe.getResultItem(registryAccess));
    }

    @Override
    public void draw(RefineryRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        this.progressArrow.draw(guiGraphics, 24, 19);
        this.fuelBar.draw(guiGraphics, 2, 21);

        this.drawExperience(recipe, guiGraphics, 1);
        this.drawCookTime(recipe, guiGraphics, 45);
    }

    protected void drawExperience(RefineryRecipe recipe, GuiGraphics guiGraphics, int y) {
        float experience = recipe.getExperience();
        if (experience > 0.0F) {
            Component experienceString = Component.translatable("gui.jei.category.smelting.experience", experience);
            Minecraft minecraft = Minecraft.getInstance();
            Font fontRenderer = minecraft.font;
            int stringWidth = fontRenderer.width(experienceString);
            guiGraphics.drawString(fontRenderer, experienceString, this.getWidth() - stringWidth, y, -8355712, false);
        }
    }

    protected void drawCookTime(RefineryRecipe recipe, GuiGraphics guiGraphics, int y) {
        int cookTime = recipe.getCookingTime();
        if (cookTime > 0) {
            int cookTimeSeconds = cookTime / 20;
            Component timeString = Component.translatable("gui.jei.category.smelting.time.seconds", cookTimeSeconds);
            Minecraft minecraft = Minecraft.getInstance();
            Font fontRenderer = minecraft.font;
            int stringWidth = fontRenderer.width(timeString);
            guiGraphics.drawString(fontRenderer, timeString, this.getWidth() - stringWidth, y, -8355712, false);
        }
    }

    public int getWidth() {
        return this.background.getWidth();
    }
}
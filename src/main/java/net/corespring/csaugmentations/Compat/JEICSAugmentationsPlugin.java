package net.corespring.csaugmentations.Compat;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.corespring.csaugmentations.CSAugmentations;
import net.corespring.csaugmentations.Client.Screens.*;
import net.corespring.csaugmentations.Recipes.*;
import net.corespring.csaugmentations.Registry.CSRecipeTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;

@JeiPlugin
public class JEICSAugmentationsPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(CSAugmentations.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new CultivatorCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new RefineryCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new ChemistryCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new FabricatorCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new DistilleryCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new DryingCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new ExtractorCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        List<CultivatorRecipe> cultivatingRecipes = recipeManager.getAllRecipesFor(CSRecipeTypes.CULTIVATING.get());
        registration.addRecipes(CultivatorCategory.CULTIVATING, cultivatingRecipes);
        List<RefineryRecipe> refiningRecipes = recipeManager.getAllRecipesFor(CSRecipeTypes.REFINING.get());
        registration.addRecipes(RefineryCategory.REFINING, refiningRecipes);
        List<ChemistryRecipe> chemistryRecipes = recipeManager.getAllRecipesFor(CSRecipeTypes.CHEMISTRY.get());
        registration.addRecipes(ChemistryCategory.CHEMISTRY, chemistryRecipes);
        List<FabricatorRecipe> fabricatorRecipes = recipeManager.getAllRecipesFor(CSRecipeTypes.FABRICATING.get());
        registration.addRecipes(FabricatorCategory.FABRICATING, fabricatorRecipes);
        List<DistilleryRecipe> distilleryRecipes = recipeManager.getAllRecipesFor(CSRecipeTypes.DISTILLING.get());
        registration.addRecipes(DistilleryCategory.DISTILLING, distilleryRecipes);
        List<DryingRecipe> dryingRecipes = recipeManager.getAllRecipesFor(CSRecipeTypes.DRYING.get());
        registration.addRecipes(DryingCategory.DRYING, dryingRecipes);
        List<ExtractorRecipe> extractorRecipes = recipeManager.getAllRecipesFor(CSRecipeTypes.EXTRACTING.get());
        registration.addRecipes(ExtractorCategory.EXTRACTING, extractorRecipes);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(CultivatorScreen.class, 76, 30, 28, 23, CultivatorCategory.CULTIVATING);
        registration.addRecipeClickArea(RefineryScreen.class, 20, 32, 20, 20, RefineryCategory.REFINING);
        registration.addRecipeClickArea(ChemistryScreen.class, 76, 24, 34, 18, ChemistryCategory.CHEMISTRY);
        registration.addRecipeClickArea(FabricatorScreen.class, 16, 61, 34, 18, FabricatorCategory.FABRICATING);
        registration.addRecipeClickArea(DistilleryScreen.class, 153, 32, 34, 18, DistilleryCategory.DISTILLING);
        registration.addRecipeClickArea(CrudeDryingRackScreen.class, 79, 47, 34, 18, DryingCategory.DRYING);
        registration.addRecipeClickArea(RefinedDryingRackScreen.class, 79, 47, 34, 18, DryingCategory.DRYING);
        registration.addRecipeClickArea(ExtractorScreen.class, 13, 34, 34, 18, ExtractorCategory.EXTRACTING);
    }

}

package net.corespring.csaugmentations.Registry;

import net.corespring.csaugmentations.CSAugmentations;
import net.corespring.csaugmentations.Recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CSRecipeTypes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, CSAugmentations.MOD_ID);

    public static final RegistryObject<RecipeType<CultivatorRecipe>> CULTIVATING =
            RECIPE_TYPES.register("cultivating",
                    () -> new RecipeType<>() {
                        @Override
                        public String toString() {
                            return new ResourceLocation(CSAugmentations.MOD_ID, "cultivating").toString();
                        }
                    });

    public static final RegistryObject<RecipeType<RefineryRecipe>> REFINING =
            RECIPE_TYPES.register("refining",
                    () -> new RecipeType<>() {
                        @Override
                        public String toString() {
                            return new ResourceLocation(CSAugmentations.MOD_ID, "refining").toString();
                        }
                    });

    public static final RegistryObject<RecipeType<ChemistryRecipe>> CHEMISTRY =
            RECIPE_TYPES.register("chemistry", () -> ChemistryRecipe.Type.INSTANCE);

    public static final RegistryObject<RecipeType<DistilleryRecipe>> DISTILLING =
            RECIPE_TYPES.register("distilling", () -> DistilleryRecipe.Type.INSTANCE);

    public static final RegistryObject<RecipeType<FabricatorRecipe>> FABRICATING =
            RECIPE_TYPES.register("fabricating", () -> FabricatorRecipe.Type.INSTANCE);

    public static final RegistryObject<RecipeType<DryingRecipe>> DRYING =
            RECIPE_TYPES.register("drying", () -> DryingRecipe.Type.INSTANCE);

    public static final RegistryObject<RecipeType<ExtractorRecipe>> EXTRACTING =
            RECIPE_TYPES.register("extracting", () -> ExtractorRecipe.Type.INSTANCE);

    public static void register(IEventBus eventBus) {
        RECIPE_TYPES.register(eventBus);
    }
}

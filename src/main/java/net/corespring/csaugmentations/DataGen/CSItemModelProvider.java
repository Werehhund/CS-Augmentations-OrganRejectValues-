package net.corespring.csaugmentations.DataGen;


import net.corespring.csaugmentations.CSAugmentations;
import net.corespring.csaugmentations.Registry.CSBlocks;
import net.corespring.csaugmentations.Registry.CSItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class CSItemModelProvider extends ItemModelProvider {

    public CSItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, CSAugmentations.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem((RegistryObject<Item>) CSItems.NATURAL_BRAIN);
        simpleItem((RegistryObject<Item>) CSItems.NATURAL_EYES);
        simpleItem((RegistryObject<Item>) CSItems.NATURAL_HEART);
        simpleItem((RegistryObject<Item>) CSItems.NATURAL_KIDNEY);
        simpleItem((RegistryObject<Item>) CSItems.NATURAL_LIVER);
        simpleItem((RegistryObject<Item>) CSItems.NATURAL_LUNGS);
        simpleItem((RegistryObject<Item>) CSItems.NATURAL_STOMACH);

        simpleItem((RegistryObject<Item>) CSItems.PROSTHETIC_EYES);
        simpleItem((RegistryObject<Item>) CSItems.PROSTHETIC_HEART);
        simpleItem((RegistryObject<Item>) CSItems.PROSTHETIC_KIDNEY);
        simpleItem((RegistryObject<Item>) CSItems.PROSTHETIC_LIVER);
        simpleItem((RegistryObject<Item>) CSItems.PROSTHETIC_STOMACH);

        simpleItem((RegistryObject<Item>) CSItems.CYBER_BRAIN);
        simpleItem((RegistryObject<Item>) CSItems.CYBER_BRAIN_IMPLANT);
        simpleItem((RegistryObject<Item>) CSItems.CYBER_EYES);
        simpleItem((RegistryObject<Item>) CSItems.CYBER_HEART);
        simpleItem((RegistryObject<Item>) CSItems.CYBER_KIDNEY);
        simpleItem((RegistryObject<Item>) CSItems.CYBER_LIVER);
        simpleItem((RegistryObject<Item>) CSItems.CYBER_STOMACH);

        evenSimplerBlockItem(CSBlocks.CULTIVATOR);
        evenSimplerBlockItem(CSBlocks.REFINERY);
        evenSimplerBlockItem(CSBlocks.CHEMISTRY_TABLE);
        evenSimplerBlockItem(CSBlocks.FABRICATOR);
        evenSimplerBlockItem(CSBlocks.DISTILLERY);
        evenSimplerBlockItem(CSBlocks.CRUDE_DRYING_RACK);
        evenSimplerBlockItem(CSBlocks.REFINED_DRYING_RACK);
        evenSimplerBlockItem(CSBlocks.EXTRACTOR);

        simpleItem((RegistryObject<Item>) CSItems.UNFINISHED_MORTAR);
        simpleItem((RegistryObject<Item>) CSItems.UNFINISHED_PESTLE);
        simpleItem((RegistryObject<Item>) CSItems.MORTAR);
        simpleItem((RegistryObject<Item>) CSItems.PESTLE);
        simpleItem((RegistryObject<Item>) CSItems.CRUDE_PETRI_DISH);
        simpleItem((RegistryObject<Item>) CSItems.REFINED_PETRI_DISH);

        simpleItem((RegistryObject<Item>) CSItems.NEURAL_ANALYZER);
        handheldItem((RegistryObject<Item>) CSItems.SCALPEL);
        simpleItem((RegistryObject<Item>) CSItems.HEMOSTAT);
        simpleItem((RegistryObject<Item>) CSItems.RETRACTORS);
        simpleItem((RegistryObject<Item>) CSItems.SUTURE);

        simpleItem((RegistryObject<Item>) CSItems.LOADED_CRUDE_PETRI_DISH);
        simpleItem((RegistryObject<Item>) CSItems.LOADED_REFINED_PETRI_DISH);

        simpleItem((RegistryObject<Item>) CSItems.BLUEPRINT);
        simpleItem((RegistryObject<Item>) CSItems.PLANT_MATTER);

        simpleItem((RegistryObject<Item>) CSItems.CRUSHED_ACID_MUSHROOM);

        simpleItem((RegistryObject<Item>) CSItems.NEURAL_INTERFACE);
        simpleItem((RegistryObject<Item>) CSItems.HYDRAULIC);
        simpleItem((RegistryObject<Item>) CSItems.STEEL_WIRE);
        simpleItem((RegistryObject<Item>) CSItems.WARP_DRIVE);
        simpleItem((RegistryObject<Item>) CSItems.FUSE);
        simpleItem((RegistryObject<Item>) CSItems.TRANSFORMER);
        simpleItem((RegistryObject<Item>) CSItems.POWER_CONTROL_MODULE);
        simpleItem((RegistryObject<Item>) CSItems.CRUDE_LOGIC_COMPONENT);
        simpleItem((RegistryObject<Item>) CSItems.REFINED_LOGIC_COMPONENT);

        handheldItem((RegistryObject<Item>) CSItems.REINFORCED_BONE);
        simpleItem((RegistryObject<Item>) CSItems.BALL_SOCKET_JOINT);
        simpleItem((RegistryObject<Item>) CSItems.CRUDE_BLOOD_PUMP);
        simpleItem((RegistryObject<Item>) CSItems.CRUDE_BLOOD_FILTER);
        simpleItem((RegistryObject<Item>) CSItems.CRUDE_OPTICAL_SENSOR);
        simpleItem((RegistryObject<Item>) CSItems.REFINED_BLOOD_PUMP);
        simpleItem((RegistryObject<Item>) CSItems.REFINED_BLOOD_FILTER);
        simpleItem((RegistryObject<Item>) CSItems.REFINED_OPTICAL_SENSOR);
        simpleItem((RegistryObject<Item>) CSItems.ARTIFICIAL_MUSCLE);
        simpleItem((RegistryObject<Item>) CSItems.ARTIFICIAL_TISSUE);
        simpleItem((RegistryObject<Item>) CSItems.ARTIFICIAL_NERVES);
        simpleItem((RegistryObject<Item>) CSItems.BRASS_LIMB_BASE);
        simpleItem((RegistryObject<Item>) CSItems.CRUDE_AUGMENTED_MUSCLE);
        simpleItem((RegistryObject<Item>) CSItems.CRUDE_AUGMENTED_TISSUE);
        simpleItem((RegistryObject<Item>) CSItems.REFINED_AUGMENTED_MUSCLE);
        simpleItem((RegistryObject<Item>) CSItems.REFINED_AUGMENTED_TISSUE);

        simpleItem((RegistryObject<Item>) CSItems.FOSSIL);

        simpleItem((RegistryObject<Item>) CSItems.CRUDE_OIL);
        simpleItem((RegistryObject<Item>) CSItems.REFINED_OIL);
        simpleItem((RegistryObject<Item>) CSItems.CRUDE_PLASTIC);
        simpleItem((RegistryObject<Item>) CSItems.REFINED_PLASTIC);

        simpleItem((RegistryObject<Item>) CSItems.DUST_SALT);
        simpleItem((RegistryObject<Item>) CSItems.LIMESTONE_CLUMP);
        simpleItem((RegistryObject<Item>) CSItems.SULFUR_DIOXIDE);
        simpleItem((RegistryObject<Item>) CSItems.NUTRIOGLOOP);
        simpleItem((RegistryObject<Item>) CSItems.DMEM);
        simpleItem((RegistryObject<Item>) CSItems.NBS);
        simpleItem((RegistryObject<Item>) CSItems.BEAKER_DISTILLED_WATER);
        simpleItem((RegistryObject<Item>) CSItems.BEAKER_SULFURIC_CONCENTRATE);
        simpleItem((RegistryObject<Item>) CSItems.BEAKER_SULFURIC_ACID);
        simpleItem((RegistryObject<Item>) CSItems.BEAKER_CINNAMOYLCOCAINE);
        simpleItem((RegistryObject<Item>) CSItems.BEAKER_COCAINE_BASE);

        simpleItem((RegistryObject<Item>) CSItems.MEAD);

        simpleBlockItemBlockTexture((RegistryObject<Block>) CSBlocks.CYCLOFUNGI);
        simpleItem((RegistryObject<Item>) CSItems.BEAKER_CYCLOSPORINE);
        simpleItem((RegistryObject<Item>) CSItems.BEAKER_SPORINE);
        simpleItem((RegistryObject<Item>) CSItems.IMMUNOSUPPRESSANT);
        simpleItem((RegistryObject<Item>) CSItems.REFINED_IMMUNOSUPPRESSANT);

        simpleBlockItemBlockTexture((RegistryObject<Block>) CSBlocks.WILD_SOMNIFERUM);
        complexBlockwithBlockTexture((RegistryObject<Block>) CSBlocks.SOMNIFERUM_CLUSTER, "somniferum_stage3");
        simpleItem((RegistryObject<Item>) CSItems.BEAKER_SILK);
        simpleItem((RegistryObject<Item>) CSItems.SOMNIFERUM_SAP);
        simpleItem((RegistryObject<Item>) CSItems.SOMNIFERUM_SEEDPOD);

        simpleBlockItemBlockTexture((RegistryObject<Block>) CSBlocks.WILD_WEED);
        complexBlockwithBlockTexture((RegistryObject<Block>) CSBlocks.CROP_WEED, "weed_stage_7");
        simpleItem((RegistryObject<Item>) CSItems.WEED);
        simpleItem((RegistryObject<Item>) CSItems.WEED_SEEDS);
        simpleItem((RegistryObject<Item>) CSItems.DRIED_WEED);
        simpleItem((RegistryObject<Item>) CSItems.CRUSHED_WEED);
        simpleItem((RegistryObject<Item>) CSItems.JOINT);
        simpleBlockItemBlockTexture((RegistryObject<Block>) CSBlocks.WILD_COCA);
        complexBlockwithBlockTexture((RegistryObject<Block>) CSBlocks.CROP_COCA, "coca_stage_7");
        simpleItem((RegistryObject<Item>) CSItems.COCA);
        simpleItem((RegistryObject<Item>) CSItems.COCA_SEEDS);
        simpleItem((RegistryObject<Item>) CSItems.DRIED_COCA);
        simpleItem((RegistryObject<Item>) CSItems.BEAKER_COCAINE);
        simpleItem((RegistryObject<Item>) CSItems.COCAINE);
        simpleItem((RegistryObject<Item>) CSItems.CRACK);

        evenSimplerBlockItem(CSBlocks.SALT_SLAB);
        evenSimplerBlockItem(CSBlocks.SALT_STAIRS);
        evenSimplerBlockItem(CSBlocks.POLISHED_SALT_SLAB);
        evenSimplerBlockItem(CSBlocks.POLISHED_SALT_STAIRS);
        evenSimplerBlockItem(CSBlocks.LIMESTONE_SLAB);
        evenSimplerBlockItem(CSBlocks.LIMESTONE_STAIRS);
        evenSimplerBlockItem(CSBlocks.POLISHED_LIMESTONE_SLAB);
        evenSimplerBlockItem(CSBlocks.POLISHED_LIMESTONE_STAIRS);

    }

    public void trapdoorItem(Supplier<Block> block) {
        this.withExistingParent(BuiltInRegistries.BLOCK.getKey(block.get()).getPath(),
                modLoc("block/" + BuiltInRegistries.BLOCK.getKey(block.get()).getPath() + "_bottom"));
    }

    private ItemModelBuilder simpleBlockItem(RegistryObject<Block> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(CSAugmentations.MOD_ID, "item/" + item.getId().getPath()));
    }

    private ItemModelBuilder complexBlockItem(RegistryObject<Block> item, String texturename) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(CSAugmentations.MOD_ID, "item/" + texturename));
    }

    private ItemModelBuilder complexBlockwithBlockTexture(RegistryObject<Block> item, String texturename) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(CSAugmentations.MOD_ID, "block/" + texturename));
    }

    public void evenSimplerBlockItem(Supplier<Block> block) {
        this.withExistingParent(CSAugmentations.MOD_ID + ":" + BuiltInRegistries.BLOCK.getKey(block.get()).getPath(),
                modLoc("block/" + BuiltInRegistries.BLOCK.getKey(block.get()).getPath()));
    }


    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(CSAugmentations.MOD_ID, "item/" + item.getId().getPath()));
    }

    private ItemModelBuilder handheldItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/handheld")).texture("layer0",
                new ResourceLocation(CSAugmentations.MOD_ID, "item/" + item.getId().getPath()));

    }

    private ItemModelBuilder simpleBlockItemBlockTexture(RegistryObject<Block> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(CSAugmentations.MOD_ID, "block/" + item.getId().getPath()));
    }
}
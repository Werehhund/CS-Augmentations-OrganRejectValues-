package net.corespring.csaugmentations.Registry;

import net.corespring.csaugmentations.Block.*;
import net.corespring.csaugmentations.Block.Crops.CocaCrop;
import net.corespring.csaugmentations.Block.Crops.WeedCrop;
import net.corespring.csaugmentations.Block.Crops.WeedDrugBlock;
import net.corespring.csaugmentations.Block.Crops.WildDrugBlock;
import net.corespring.csaugmentations.CSAugmentations;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;
import java.util.function.ToIntFunction;

public class CSBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, CSAugmentations.MOD_ID);

    public static final Supplier<Block> INVISIBLE_CHEM = registerBlock("invisible_chem",
            () -> new InvisibleChemBlock(BlockBehaviour.Properties.of().strength(3f).sound(SoundType.COPPER).noOcclusion().noLootTable()));
    public static final Supplier<Block> INVISIBLE_FAB = registerBlock("invisible_fab",
            () -> new InvisibleFabBlock(BlockBehaviour.Properties.of().strength(4f).sound(SoundType.COPPER).noOcclusion().noLootTable().lightLevel(state -> 8)));
    public static final Supplier<Block> INVISIBLE_DISTILLERY = registerBlock("invisible_distillery",
            () -> new InvisibleDistilleryBlock(BlockBehaviour.Properties.of().strength(3f).sound(SoundType.COPPER).noOcclusion().noLootTable()));
    public static final Supplier<Block> INVISIBLE_DRYING_RACK = registerBlock("invisible_drying_rack",
            () -> new InvisibleDryingRackBlock(BlockBehaviour.Properties.of().strength(3f).sound(SoundType.COPPER).noOcclusion().noLootTable()));
    public static final Supplier<Block> INVISIBLE_EXTRACTOR = registerBlock("invisible_extractor",
            () -> new InvisibleExtractorBlock(BlockBehaviour.Properties.of().strength(3f).sound(SoundType.COPPER).noOcclusion().noLootTable()));
    public static final Supplier<Block> CULTIVATOR = registerBlock("cultivator",
            () -> new CultivatorBlock(BlockBehaviour.Properties.copy(Blocks.ANVIL)));
    public static final Supplier<Block> REFINERY = registerBlock("refinery",
            () -> new RefineryBlock(BlockBehaviour.Properties.copy(Blocks.ANVIL).lightLevel(litBlockEmission(15))));
    public static final Supplier<Block> CHEMISTRY_TABLE = registerBlock("chemistry_table",
            () -> new ChemistryBlock(BlockBehaviour.Properties.of().strength(4f).sound(SoundType.COPPER).noOcclusion().noLootTable(), INVISIBLE_CHEM.get()));
    public static final Supplier<Block> FABRICATOR = registerBlock("fabricator",
            () -> new FabricatorBlock(BlockBehaviour.Properties.of().strength(4f).sound(SoundType.COPPER).noOcclusion().lightLevel(state -> 8), INVISIBLE_FAB.get()));
    public static final Supplier<Block> DISTILLERY = registerBlock("distillery",
            () -> new DistilleryBlock(BlockBehaviour.Properties.of().strength(4f).sound(SoundType.COPPER).noOcclusion(), INVISIBLE_DISTILLERY.get()));
    public static final Supplier<Block> CRUDE_DRYING_RACK = registerBlock("crude_drying_rack",
            () -> new CrudeDryingRackBlock(BlockBehaviour.Properties.of().strength(2f).sound(SoundType.WOOD).noOcclusion()));
    public static final Supplier<Block> REFINED_DRYING_RACK = registerBlock("refined_drying_rack",
            () -> new RefinedDryingRackBlock(BlockBehaviour.Properties.of().strength(4f).sound(SoundType.COPPER).noOcclusion(), INVISIBLE_DRYING_RACK.get()));
    public static final Supplier<Block> EXTRACTOR = registerBlock("extractor",
            () -> new ExtractorBlock(BlockBehaviour.Properties.of().strength(4f).sound(SoundType.COPPER).noOcclusion(), INVISIBLE_EXTRACTOR.get()));

    public static final Supplier<Block> FOSSIL_ORE = registerBlock("fossil_ore",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f).sound(SoundType.STONE)
                    .requiresCorrectToolForDrops()));
    public static final Supplier<Block> DEEPSLATE_FOSSIL_ORE = registerBlock("deepslate_fossil_ore",
            () -> new Block(BlockBehaviour.Properties.of().strength(6f).sound(SoundType.DEEPSLATE)
                    .requiresCorrectToolForDrops()));
    public static final Supplier<Block> FOSSIL_BLOCK = registerBlock("fossil_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f).sound(SoundType.BONE_BLOCK)
                    .requiresCorrectToolForDrops()));

    public static final Supplier<Block> SALT = registerBlock("salt",
            () -> new Block(BlockBehaviour.Properties.of().strength(3f).sound(SoundType.CALCITE)
                    .requiresCorrectToolForDrops()));
    public static final Supplier<Block> SALT_SLAB = registerBlock("salt_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of()
                    .strength(2F).requiresCorrectToolForDrops().sound(SoundType.CALCITE)));
    public static final Supplier<Block> SALT_STAIRS = registerBlock("salt_stairs",
            () -> new StairBlock(SALT.get().defaultBlockState(), BlockBehaviour.Properties.of()
                    .strength(2F).requiresCorrectToolForDrops().sound(SoundType.CALCITE)));
    public static final Supplier<Block> POLISHED_SALT = registerBlock("polished_salt",
            () -> new Block(BlockBehaviour.Properties.of().strength(3f).sound(SoundType.CALCITE)
                    .requiresCorrectToolForDrops()));
    public static final Supplier<Block> POLISHED_SALT_SLAB = registerBlock("polished_salt_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of()
                    .strength(2F, 6f).requiresCorrectToolForDrops().sound(SoundType.CALCITE)));
    public static final Supplier<Block> POLISHED_SALT_STAIRS = registerBlock("polished_salt_stairs",
            () -> new StairBlock(POLISHED_SALT.get().defaultBlockState(), BlockBehaviour.Properties.of()
                    .strength(2F, 6f).requiresCorrectToolForDrops().sound(SoundType.CALCITE)));
    public static final Supplier<Block> LIMESTONE = registerBlock("limestone",
            () -> new Block(BlockBehaviour.Properties.of().strength(3f, 6f).sound(SoundType.CALCITE)
                    .requiresCorrectToolForDrops()));
    public static final Supplier<Block> LIMESTONE_SLAB = registerBlock("limestone_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of()
                    .strength(2F, 6f).requiresCorrectToolForDrops().sound(SoundType.CALCITE)));
    public static final Supplier<Block> LIMESTONE_STAIRS = registerBlock("limestone_stairs",
            () -> new StairBlock(LIMESTONE.get().defaultBlockState(), BlockBehaviour.Properties.of()
                    .strength(2F, 6f).requiresCorrectToolForDrops().sound(SoundType.CALCITE)));
    public static final Supplier<Block> POLISHED_LIMESTONE = registerBlock("polished_limestone",
            () -> new Block(BlockBehaviour.Properties.of().strength(3f, 6f).sound(SoundType.CALCITE)
                    .requiresCorrectToolForDrops()));
    public static final Supplier<Block> POLISHED_LIMESTONE_SLAB = registerBlock("polished_limestone_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of()
                    .strength(2F, 6f).requiresCorrectToolForDrops().sound(SoundType.CALCITE)));
    public static final Supplier<Block> POLISHED_LIMESTONE_STAIRS = registerBlock("polished_limestone_stairs",
            () -> new StairBlock(POLISHED_LIMESTONE.get().defaultBlockState(), BlockBehaviour.Properties.of()
                    .strength(2F, 6f).requiresCorrectToolForDrops().sound(SoundType.CALCITE)));

    public static final Supplier<Block> CYCLOFUNGI = registerBlock("cyclofungi",
            () -> new CSMushroomBlock(BlockBehaviour.Properties.copy(Blocks.RED_MUSHROOM).noOcclusion().noCollission().lightLevel((state) -> 1)));

    public static final Supplier<Block> WILD_SOMNIFERUM = registerBlock("wild_somniferum",
            () -> new WildDrugBlock(BlockBehaviour.Properties.copy(Blocks.POPPY).noOcclusion().noCollission()));
    public static final Supplier<Block> SOMNIFERUM_CLUSTER = registerBlock("somniferum_cluster",
            () -> new SomniferumCrop(BlockBehaviour.Properties.copy(Blocks.WHEAT).noOcclusion().noCollission()));
    public static final Supplier<Block> BLOCK_SOMNIFERUM_SAP = registerBlock("block_somniferum_sap",
            () -> new HoneyBlock(BlockBehaviour.Properties.of().strength(1f).sound(SoundType.HONEY_BLOCK)));

    public static final Supplier<Block> WILD_WEED = registerBlock("wild_weed",
            () -> new WeedDrugBlock(BlockBehaviour.Properties.of().strength(0.1f).sound(SoundType.GRASS).instabreak().noOcclusion().noCollission()));
    public static final Supplier<Block> CROP_WEED = registerBlock("crop_weed",
            () -> new WeedCrop(BlockBehaviour.Properties.of().strength(0.1f).sound(SoundType.GRASS).instabreak().noOcclusion().noCollission()));
    public static final Supplier<Block> BLOCK_WEED = registerBlock("block_weed",
            () -> new Block(BlockBehaviour.Properties.of().strength(0.5f).instrument(NoteBlockInstrument.BANJO).sound(SoundType.GRASS)));
    public static final Supplier<Block> BLOCK_DRIED_WEED = registerBlock("block_dried_weed",
            () -> new Block(BlockBehaviour.Properties.of().strength(0.5f).instrument(NoteBlockInstrument.BANJO).sound(SoundType.GRASS)));

    public static final Supplier<Block> WILD_COCA = registerBlock("wild_coca",
            () -> new WildDrugBlock(BlockBehaviour.Properties.of().strength(0.1f).sound(SoundType.GRASS).instabreak().noOcclusion().noCollission()));
    public static final Supplier<Block> CROP_COCA = registerBlock("crop_coca",
            () -> new CocaCrop(BlockBehaviour.Properties.of().strength(0.1f).sound(SoundType.GRASS).instabreak().noOcclusion().noCollission()));
    public static final Supplier<Block> BLOCK_COKE = registerBlock("block_coke",
            () -> new Block(BlockBehaviour.Properties.of().strength(0.5f).sound(SoundType.SAND)));
    public static final Supplier<Block> BLOCK_COCA = registerBlock("block_coca",
            () -> new Block(BlockBehaviour.Properties.of().strength(0.5f).instrument(NoteBlockInstrument.BANJO).sound(SoundType.GRASS)));
    public static final Supplier<Block> BLOCK_DRIED_COCA = registerBlock("block_dried_coca",
            () -> new Block(BlockBehaviour.Properties.of().strength(0.5f).instrument(NoteBlockInstrument.BANJO).sound(SoundType.GRASS)));


    private static ToIntFunction<BlockState> litBlockEmission(int pLightValue) {
        return (state) -> state.getValue(BlockStateProperties.LIT) ? pLightValue : 0;
    }

    private static <T extends Block> Supplier<T> registerBlock(String name, Supplier<T> block) {
        Supplier<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }
    private static <T extends Block> Supplier<Item> registerBlockItem(String name, Supplier<T> block) {
        return CSItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}

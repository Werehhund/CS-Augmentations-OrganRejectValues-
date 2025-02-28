package net.corespring.csaugmentations.DataGen.loot;

import net.corespring.csaugmentations.Block.Crops.TwoTallCropBlock;
import net.corespring.csaugmentations.Registry.CSBlocks;
import net.corespring.csaugmentations.Registry.CSItems;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.Set;

public class CSBlockLootTables extends BlockLootSubProvider {

    public CSBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.dropSelf(CSBlocks.CULTIVATOR.get());
        this.dropSelf(CSBlocks.REFINERY.get());
        this.dropSelf(CSBlocks.FABRICATOR.get());
        this.dropSelf(CSBlocks.DISTILLERY.get());
        this.dropSelf(CSBlocks.CRUDE_DRYING_RACK.get());
        this.dropSelf(CSBlocks.REFINED_DRYING_RACK.get());
        this.dropSelf(CSBlocks.EXTRACTOR.get());

        this.add(CSBlocks.FOSSIL_ORE.get(),
                block -> createOreDrop(CSBlocks.FOSSIL_ORE.get(), CSItems.FOSSIL.get()));
        this.add(CSBlocks.DEEPSLATE_FOSSIL_ORE.get(),
                block -> createOreDrop(CSBlocks.DEEPSLATE_FOSSIL_ORE.get(), CSItems.FOSSIL.get()));
        this.dropSelf(CSBlocks.FOSSIL_BLOCK.get());

        this.add(CSBlocks.SALT.get(),
                block -> createFourDrops(CSBlocks.SALT.get(), CSItems.DUST_SALT.get()));
        this.dropSelf(CSBlocks.SALT_STAIRS.get());
        this.add(CSBlocks.SALT_SLAB.get(),
                block -> createSlabItemTable(CSBlocks.SALT_SLAB.get()));
        this.dropSelf(CSBlocks.POLISHED_SALT.get());
        this.dropSelf(CSBlocks.POLISHED_SALT_STAIRS.get());
        this.add(CSBlocks.POLISHED_SALT_SLAB.get(),
                block -> createSlabItemTable(CSBlocks.POLISHED_SALT_SLAB.get()));
        this.dropSelf(CSBlocks.LIMESTONE.get());
        this.dropSelf(CSBlocks.LIMESTONE_STAIRS.get());
        this.add(CSBlocks.LIMESTONE_SLAB.get(),
                block -> createSlabItemTable(CSBlocks.SALT_SLAB.get()));
        this.dropSelf(CSBlocks.POLISHED_LIMESTONE.get());
        this.dropSelf(CSBlocks.POLISHED_LIMESTONE_STAIRS.get());
        this.add(CSBlocks.POLISHED_LIMESTONE_SLAB.get(),
                block -> createSlabItemTable(CSBlocks.POLISHED_LIMESTONE_SLAB.get()));

        this.dropSelf(CSBlocks.CYCLOFUNGI.get());

        this.dropSelf(CSBlocks.WILD_SOMNIFERUM.get());
        this.dropSelf(CSBlocks.BLOCK_SOMNIFERUM_SAP.get());
        this.add(CSBlocks.SOMNIFERUM_CLUSTER.get(),
                block -> LootTable.lootTable()
                        .withPool(createSomniferumClusterPool(0, LootItem.lootTableItem(CSItems.SOMNIFERUM_SEEDPOD.get())))
                        .withPool(createSomniferumClusterPool(1, LootItem.lootTableItem(CSItems.SOMNIFERUM_SEEDPOD.get())))
                        .withPool(createSomniferumClusterPool(2, LootItem.lootTableItem(CSItems.SOMNIFERUM_SEEDPOD.get())))
                        .withPool(createSomniferumClusterPool(3, LootItem.lootTableItem(CSItems.SOMNIFERUM_SEEDPOD.get())))
                        .withPool(createSomniferumClusterPool(4, LootItem.lootTableItem(CSItems.SOMNIFERUM_SEEDPOD.get())))
                        .withPool(createSomniferumClusterPool(5, LootItem.lootTableItem(CSBlocks.WILD_SOMNIFERUM.get()).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))))
                        .withPool(createSomniferumClusterPool(6, LootItem.lootTableItem(CSBlocks.WILD_SOMNIFERUM.get()).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))))
                        .withPool(createSomniferumClusterPool(7, LootItem.lootTableItem(CSBlocks.WILD_SOMNIFERUM.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))))
        );

        this.dropSelf(CSBlocks.BLOCK_WEED.get());
        this.dropSelf(CSBlocks.BLOCK_DRIED_WEED.get());
        this.dropSelf(CSBlocks.WILD_WEED.get());
        this.add(CSBlocks.CROP_WEED.get(), createTwoTallCropDrops(CSBlocks.CROP_WEED.get(), CSItems.WEED.get(), CSItems.WEED_SEEDS.get()));
        this.dropSelf(CSBlocks.BLOCK_COKE.get());
        this.dropSelf(CSBlocks.BLOCK_COCA.get());
        this.dropSelf(CSBlocks.BLOCK_DRIED_COCA.get());
        this.dropSelf(CSBlocks.WILD_COCA.get());
        this.add(CSBlocks.CROP_COCA.get(), createTwoTallCropDrops(CSBlocks.CROP_COCA.get(), CSItems.COCA.get(), CSItems.COCA_SEEDS.get()));
    }

    private LootTable.Builder createTwoTallCropDrops(Block block, Item crop, Item seeds) {
        LootItemCondition.Builder isAge7 = LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(TwoTallCropBlock.AGE, 7));
        LootItemCondition.Builder isAge15 = LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(TwoTallCropBlock.AGE, 15));

        return LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .add(LootItem.lootTableItem(crop)
                                .when(isAge7)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))
                                .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE)))
                        .add(LootItem.lootTableItem(crop)
                                .when(isAge15)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F)))
                                .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))))
                .withPool(LootPool.lootPool()
                        .add(LootItem.lootTableItem(seeds)
                                .when(isAge7.or(isAge15))
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))
                                .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
    }

    private LootPool.Builder createSomniferumClusterPool(int age, LootItem.Builder<?> lootItem) {
        return LootPool.lootPool()
                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(CSBlocks.SOMNIFERUM_CLUSTER.get())
                        .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(BlockStateProperties.AGE_7, age)))
                .add(lootItem);
    }

    protected LootTable.Builder createRareMetalOreDrops(Block pBlock, Item pItem) {
        return createSilkTouchDispatchTable(pBlock, this.applyExplosionDecay(pBlock, LootItem.lootTableItem(pItem)));
    }

    protected LootTable.Builder CreateCommonOreDrops(Block pBlock, Item pItem) {
        return createSilkTouchDispatchTable(pBlock, this.applyExplosionDecay(pBlock, LootItem.lootTableItem(pItem)
                .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
    }

    protected LootTable.Builder createStoneDrops(Block pBlock, Block pItem) {
        return createSilkTouchDispatchTable(pBlock, this.applyExplosionDecay(pBlock, LootItem.lootTableItem(pItem)));
    }

    protected LootTable.Builder createFourDrops(Block pBlock, Item pItem) {
        return createSilkTouchDispatchTable(pBlock, this.applyExplosionDecay(pBlock, LootItem.lootTableItem(pItem)
                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(4)))));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return CSBlocks.BLOCKS.getEntries().stream().map(entry -> (Block) entry.get())::iterator;
    }
}
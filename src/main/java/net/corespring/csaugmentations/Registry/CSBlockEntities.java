package net.corespring.csaugmentations.Registry;

import net.corespring.csaugmentations.Block.BlockEntities.*;
import net.corespring.csaugmentations.CSAugmentations;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class CSBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, CSAugmentations.MOD_ID);

    public static final Supplier<BlockEntityType<RefineryBlockEntity>> REFINERY_BE =
            BLOCK_ENTITY.register("refinery_be", () ->
                    BlockEntityType.Builder.of(RefineryBlockEntity::new,
                            CSBlocks.REFINERY.get()).build(null));

    public static final Supplier<BlockEntityType<ChemistryBlockEntity>> CHEMISTRY_TABLE_BE =
            BLOCK_ENTITY.register("chemistry_table_be", () ->
                    BlockEntityType.Builder.of(ChemistryBlockEntity::new,
                            CSBlocks.CHEMISTRY_TABLE.get()).build(null));

    public static final Supplier<BlockEntityType<FabricatorBlockEntity>> FABRICATOR_BE =
            BLOCK_ENTITY.register("fabricator_be", () ->
                    BlockEntityType.Builder.of(FabricatorBlockEntity::new,
                            CSBlocks.FABRICATOR.get()).build(null));

    public static final Supplier<BlockEntityType<DistilleryBlockEntity>> DISTILLERY_BE =
            BLOCK_ENTITY.register("distillery_be", () ->
                    BlockEntityType.Builder.of(DistilleryBlockEntity::new,
                            CSBlocks.DISTILLERY.get()).build(null));

    public static final Supplier<BlockEntityType<CrudeDryingRackBlockEntity>> CRUDE_DRYING_RACK_BE =
            BLOCK_ENTITY.register("crude_drying_rack_be", () ->
                    BlockEntityType.Builder.of(CrudeDryingRackBlockEntity::new,
                            CSBlocks.CRUDE_DRYING_RACK.get()).build(null));

    public static final Supplier<BlockEntityType<RefinedDryingRackBlockEntity>> REFINED_DRYING_RACK_BE =
            BLOCK_ENTITY.register("refined_drying_rack_be", () ->
                    BlockEntityType.Builder.of(RefinedDryingRackBlockEntity::new,
                            CSBlocks.REFINED_DRYING_RACK.get()).build(null));

    public static final Supplier<BlockEntityType<ExtractorBlockEntity>> EXTRACTOR_BE =
            BLOCK_ENTITY.register("extractor_be", () ->
                    BlockEntityType.Builder.of(ExtractorBlockEntity::new,
                            CSBlocks.EXTRACTOR.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITY.register(eventBus);
    }
}

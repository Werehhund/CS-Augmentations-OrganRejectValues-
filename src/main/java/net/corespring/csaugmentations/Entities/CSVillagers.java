package net.corespring.csaugmentations.Entities;

import com.google.common.collect.ImmutableSet;
import net.corespring.csaugmentations.CSAugmentations;
import net.corespring.csaugmentations.Registry.CSBlocks;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class CSVillagers {
    public static final DeferredRegister<PoiType> POI_TYPES =
            DeferredRegister.create(ForgeRegistries.POI_TYPES, CSAugmentations.MOD_ID);
    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS =
            DeferredRegister.create(ForgeRegistries.VILLAGER_PROFESSIONS, CSAugmentations.MOD_ID);

    public static final Supplier<PoiType> CHEM_POI = POI_TYPES.register("chem_poi",
            () -> new PoiType(ImmutableSet.copyOf(CSBlocks.CHEMISTRY_TABLE.get().getStateDefinition().getPossibleStates()),
                    1,1));

    public static final Supplier<VillagerProfession> PHARMACIST = VILLAGER_PROFESSIONS.register("pharmacist",
            () -> new VillagerProfession("pharmacist",
                    holder -> holder.get() == CHEM_POI.get(), holder -> holder.get() == CHEM_POI.get(),
                    ImmutableSet.of(), ImmutableSet.of(), SoundEvents.VILLAGER_WORK_CLERIC));

    public static void register(IEventBus eventBus) {
        POI_TYPES.register(eventBus);
        VILLAGER_PROFESSIONS.register(eventBus);
    }
}

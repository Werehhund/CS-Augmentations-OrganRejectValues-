package net.corespring.csaugmentations;

import com.mojang.logging.LogUtils;
import net.corespring.csaugmentations.Block.RefinedDryingRackBlock;
import net.corespring.csaugmentations.Client.Renderers.CrudeDryingRackRenderer;
import net.corespring.csaugmentations.Client.Renderers.RefinedDryingRackRenderer;
import net.corespring.csaugmentations.Network.CSNetwork;
import net.corespring.csaugmentations.Registry.CSRecipeSerializers;
import net.corespring.csaugmentations.Registry.CSRecipeTypes;
import net.corespring.csaugmentations.Registry.*;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(CSAugmentations.MOD_ID)
public class CSAugmentations {
    public static final String MOD_ID = "csaugmentations";
    public static final Logger LOGGER = LogUtils.getLogger();

    public CSAugmentations() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);

        CSItems.register(modEventBus);
        CSBlocks.register(modEventBus);
        CSCreativeTab.register(modEventBus);

        CSBlockEntities.register(modEventBus);
        CSEffects.register(modEventBus);
        CSMenu.register(modEventBus);
        CSRecipeSerializers.register(modEventBus);
        CSRecipeTypes.register(modEventBus);

        CSSoundEvents.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CSCommonConfigs.SPEC, "csaugmentations-common.toml");
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        CSNetwork.init();
        ComposterBlock.COMPOSTABLES.put(CSItems.COCA_SEEDS.get(), 0.3f);
        ComposterBlock.COMPOSTABLES.put(CSItems.COCA.get(), 0.65f);
        ComposterBlock.COMPOSTABLES.put(CSItems.WEED_SEEDS.get(), 0.3f);
        ComposterBlock.COMPOSTABLES.put(CSItems.WEED.get(), 0.65f);
        ComposterBlock.COMPOSTABLES.put(CSItems.SOMNIFERUM_SEEDPOD.get(), 0.35f);
        ComposterBlock.COMPOSTABLES.put(CSBlocks.WILD_SOMNIFERUM.get(), 0.65f);
        ComposterBlock.COMPOSTABLES.put(CSItems.PLANT_MATTER.get(), 0.95f);
    }

    private void clientSetup(FMLClientSetupEvent event) {
        BlockEntityRenderers.register(CSBlockEntities.CRUDE_DRYING_RACK_BE.get(), CrudeDryingRackRenderer::new);
        BlockEntityRenderers.register(CSBlockEntities.REFINED_DRYING_RACK_BE.get(), RefinedDryingRackRenderer::new);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class EventSubscriber {

        @SubscribeEvent
        public static void onCommonSetup(FMLCommonSetupEvent event) {
        }
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
        }
    }
}

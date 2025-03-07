package net.corespring.csaugmentations.Events;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.corespring.csaugmentations.CSAugmentations;
import net.corespring.csaugmentations.Entities.CSVillagers;
import net.corespring.csaugmentations.Registry.CSBlocks;
import net.corespring.csaugmentations.Registry.CSItems;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = CSAugmentations.MOD_ID)
public class VillagerTradeEvents {

    @SubscribeEvent
    public static void addPharmacistTrades(VillagerTradesEvent events) {
        if(events.getType() == CSVillagers.PHARMACIST.get()) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = events.getTrades();

            //Level 1 Trades

            trades.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(net.corespring.cslibrary.Registry.CSItems.NUGGET_SILVER.get(), 18),
                    new ItemStack(Items.EMERALD, 1),
                    4, 3, 0.01f
            ));

            trades.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.STRING, 16),
                    new ItemStack(Items.EMERALD, 1),
                    14, 2, 0.01f
            ));

            trades.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.COAL, 12),
                    new ItemStack(Items.EMERALD, 1),
                    7, 2, 0.01f
            ));

            trades.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 10 + pRandom.nextInt(12)),
                    new ItemStack(CSItems.SCALPEL.get(), 1),
                    5, 4, 0.01f
            ));

            trades.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 10 + pRandom.nextInt(12)),
                    new ItemStack(CSItems.SUTURE.get(), 16),
                    6 + pRandom.nextInt(2), 4, 0.01f
            ));

            trades.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(CSItems.BEAKER_SILK.get(), 1),
                    new ItemStack(Items.EMERALD, 7 + pRandom.nextInt(4)),
                    24, 4, 0.02f
            ));

            // Level 2 Trades

            trades.get(2).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 10 + pRandom.nextInt(8)),
                    new ItemStack(CSItems.HEMOSTAT.get(), 1),
                    4 + pRandom.nextInt(2), 6, 0.01f
            ));

            trades.get(2).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 6),
                    new ItemStack(net.corespring.cslibrary.Registry.CSItems.COTTON_BANDAGE.get(), 4),
                    4 + pRandom.nextInt(4), 7, 0.01f
            ));

            trades.get(2).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(net.corespring.cslibrary.Registry.CSItems.INGOT_STEEL.get(), 4),
                    new ItemStack(Items.EMERALD, 18 + pRandom.nextInt(10)),
                    8 + pRandom.nextInt(2), 7, 0.02f
            ));

            trades.get(2).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 10 + pRandom.nextInt(4)),
                    new ItemStack(net.corespring.cslibrary.Registry.CSItems.BEAKER.get(), 1),
                    new ItemStack(CSItems.NBS.get(), 1),
                    14 + pRandom.nextInt(10), 7, 0.02f
            ));

            trades.get(2).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 8 + pRandom.nextInt(4)),
                    new ItemStack(CSItems.NUTRIOGLOOP.get(), 1),
                    19 + pRandom.nextInt(5), 7, 0.02f
            ));

            trades.get(2).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 3 + pRandom.nextInt(6)),
                    new ItemStack(Items.DIAMOND, 1),
                    new ItemStack(net.corespring.cslibrary.Registry.CSItems.FLESH.get(), 6),
                    1 + pRandom.nextInt(2), 7, 0.03f
            ));

            // Level 3 Trades

            trades.get(3).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 15 + pRandom.nextInt(24)),
                    new ItemStack(CSItems.SOMNIFERUM_SEEDPOD.get(), 1),
                    4 + pRandom.nextInt(2), 8, 0.04f
            ));

            trades.get(3).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(CSItems.CRACK.get(), 1),
                    new ItemStack(Items.EMERALD, 9 + pRandom.nextInt(5)),
                    7 + pRandom.nextInt(2), 7, 0.04f
            ));

            trades.get(3).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(CSItems.JOINT.get(), 1),
                    new ItemStack(Items.EMERALD, 7 + pRandom.nextInt(4)),
                    7 + pRandom.nextInt(2), 8, 0.02f
            ));

            trades.get(3).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.CYAN_BED, 1),
                    new ItemStack(Items.EMERALD, 6 + pRandom.nextInt(3)),
                    4 + pRandom.nextInt(4), 7, 0.03f
            ));

            // Level 4 Trades

            trades.get(4).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(CSItems.COCAINE.get(), 1),
                    new ItemStack(Items.DIAMOND, 4 + pRandom.nextInt(7)),
                    7 + pRandom.nextInt(2), 9, 0.04f
            ));

            trades.get(4).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 8 + pRandom.nextInt(4)),
                    new ItemStack(net.corespring.cslibrary.Registry.CSItems.BEAKER.get(), 1),
                    new ItemStack(CSItems.BEAKER_SULFURIC_CONCENTRATE.get(), 1),
                    22 + pRandom.nextInt(2), 9, 0.04f
            ));

            // Level 5 Trades

            trades.get(5).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.DIAMOND, 1 + pRandom.nextInt(1)),
                    new ItemStack(CSItems.POWER_CONTROL_MODULE.get(), 1),
                    2 + pRandom.nextInt(2), 9, 0.04f
            ));

            trades.get(5).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.DIAMOND, 1 + pRandom.nextInt(1)),
                    new ItemStack(CSItems.IMMUNOSUPPRESSANT.get(), 4),
                    3 + pRandom.nextInt(2), 9, 0.05f
            ));

        }
    }
}

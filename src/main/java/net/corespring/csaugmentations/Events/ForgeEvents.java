package net.corespring.csaugmentations.Events;

import com.google.common.util.concurrent.AtomicDouble;
import com.mojang.logging.LogUtils;
import net.corespring.csaugmentations.Augmentations.Base.IMixinMobEffectInstance;
import net.corespring.csaugmentations.Augmentations.Base.IOrganTickable;
import net.corespring.csaugmentations.Augmentations.Base.SimpleOrgan;
import net.corespring.csaugmentations.CSAugmentations;
import net.corespring.csaugmentations.CSCommonConfigs;
import net.corespring.csaugmentations.Capability.Cyberpsychosis;
import net.corespring.csaugmentations.Capability.OrganCap;
import net.corespring.csaugmentations.Capability.OrganCapProvider;
import net.corespring.csaugmentations.Capability.SyringeGunCap;
import net.corespring.csaugmentations.Item.SyringeGunItem;
import net.corespring.csaugmentations.Utility.CSOrganTiers;
import net.corespring.csaugmentations.Network.CSNetwork;
import net.corespring.csaugmentations.Network.Packets.S2CSyncDataPacket;
import net.corespring.csaugmentations.Registry.CSEffects;
import net.corespring.csaugmentations.Utility.CSAugUtil;
import net.corespring.csaugmentations.Utility.IOrganTiers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ForgeEvents {
    @Mod.EventBusSubscriber(modid = CSAugmentations.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class InventoryEvents {
        private static final Logger LOGGER = LogUtils.getLogger();

        @SubscribeEvent
        public static void playerInventory(TickEvent.PlayerTickEvent event) {
            if (event.player.level().isClientSide()) return;
            if (event.phase == TickEvent.Phase.END) {
                Player player = event.player;

                player.getCapability(OrganCap.ORGAN_DATA).ifPresent(cap -> {
                    cap.organTick();
                    cap.updateOrganData();

                    player.getActiveEffects().forEach(effectInstance -> {
                        IMixinMobEffectInstance mixinEffectInstance = (IMixinMobEffectInstance) effectInstance;
                        if (mixinEffectInstance.cS_Augmentations$isEfficiencyApplied()) {
                            mixinEffectInstance.cS_Augmentations$setDuration(effectInstance.getDuration());
                        }
                    });
                });
            }
        }

        @SubscribeEvent
        public static void onLivingHurt(LivingHurtEvent event) {
            if (event.getEntity() instanceof Player player) {
                player.getCapability(OrganCap.ORGAN_DATA).ifPresent(cap -> {
                    if (!cap.isPresent(CSAugUtil.OrganSlots.RIBS)) {
                        float originalDamage = event.getAmount();
                        event.setAmount(originalDamage * 2);
                    }
                });

                if (event.getEntity().hasEffect(CSEffects.SILK.get())) {
                    int amplifier = event.getEntity().getEffect(CSEffects.SILK.get()).getAmplifier();
                    event.setAmount(event.getAmount() * (1.0F - 0.20F * (amplifier + 1)));
                }

                if (event.getSource().is(DamageTypes.FALL) && player.isCrouching()) {
                    player.getCapability(OrganCap.ORGAN_DATA).ifPresent(cap -> {
                        double fallDamageReductionFactor = 1.0;

                        int[] legSlots = new int[]{
                                CSAugUtil.OrganSlots.RIGHT_LEG,
                                CSAugUtil.OrganSlots.LEFT_LEG
                        };

                        for (int slot : legSlots) {
                            ItemStack stack = cap.getStackInSlot(slot);
                            if (!stack.isEmpty()) {
                                    IOrganTiers tier = cap.getOrganTier(stack);
                                    fallDamageReductionFactor += tier.getFallDamageReduction();
                            }
                        }

                        event.setAmount((float) (event.getAmount() / fallDamageReductionFactor));
                    });
                }
            }
        }

        @SubscribeEvent
        public static void onPlayerEat(LivingEntityUseItemEvent.Finish event) {
            if (event.getEntity() instanceof Player player) {
                player.getCapability(OrganCap.ORGAN_DATA).ifPresent(OrganCap.OrganData::applyStomachBuffs);
            }
        }


        @SubscribeEvent
        public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
            ServerPlayer player = (ServerPlayer) event.getEntity();
            player.getCapability(OrganCap.ORGAN_DATA).ifPresent(cap -> {
                cap.updateOrganData();
                if (!cap.initialized) {
                    cap.pDefaultOrgans();
                    cap.initialized = true;
                    cap.updatePersistentData();
                }
                CSNetwork.NETWORK_CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new S2CSyncDataPacket(cap, player.getId()));
            });
        }

        @SubscribeEvent
        public static void onPlayerDeath(LivingDropsEvent event) {
            if (event.getEntity() instanceof Player player) {
                    player.getCapability(OrganCap.ORGAN_DATA).ifPresent(cap -> {
                        if (CSCommonConfigs.CYBERNETICS_DROP_ON_DEATH.get()) {

                            List<ItemStack> organDrops = new ArrayList<>();

                            for (int slotIndex = 0; slotIndex < cap.getSlots(); slotIndex++) {
                                ItemStack organStack = cap.getStackInSlot(slotIndex);
                                if (CSAugUtil.shouldDropOrgan(organStack)) {
                                    organDrops.add(organStack.copy());
                                    cap.setStackInSlot(slotIndex, ItemStack.EMPTY);
                                }
                            }

                            Level level = player.level();
                            for (ItemStack drop : organDrops) {
                                ItemEntity itemEntity = new ItemEntity(level, player.getX(), player.getY(), player.getZ(), drop);
                                event.getDrops().add(itemEntity);
                            }
                        }
                    });
            }
        }


        @SubscribeEvent
        public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
            ServerPlayer player = (ServerPlayer) event.getEntity();

            if(!CSCommonConfigs.CYBERNETICS_DROP_ON_DEATH.get()) {
                player.addEffect(new MobEffectInstance(CSEffects.Immunosuppressant.get(), 6000, 0, false, false, true));
            }

            player.getCapability(OrganCap.ORGAN_DATA).ifPresent(cap -> {
                if (CSCommonConfigs.CYBERNETICS_DROP_ON_DEATH.get()) {
                    cap.pDefaultOrgans();
                }
                cap.updateOrganData();
                CSNetwork.NETWORK_CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new S2CSyncDataPacket(cap, player.getId()));
            });
        }

        @SubscribeEvent
        public static void onPlayerClone(PlayerEvent.Clone event) {
            if (!CSCommonConfigs.CYBERNETICS_DROP_ON_DEATH.get()) {
                Player oldPlayer = event.getOriginal();
                oldPlayer.reviveCaps();
                oldPlayer.getCapability(OrganCap.ORGAN_DATA).ifPresent(oldCap -> {
                    event.getEntity().getCapability(OrganCap.ORGAN_DATA).ifPresent(newCap -> {
                        newCap.copy(oldCap);
                        newCap.updateOrganData();
                    });
                });
                oldPlayer.invalidateCaps();
            }
        }

        @SubscribeEvent
        public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
            if (event.phase == TickEvent.Phase.END) {
                Player player = event.player;
                player.getCapability(OrganCap.ORGAN_DATA).ifPresent(cap -> {
                    for (int i = 0; i < cap.getSlots(); i++) {
                        ItemStack stack = cap.getStackInSlot(i);
                        if (stack.getItem() instanceof IOrganTickable organ) {
                            organ.organTick(stack, player.level(), player, i);
                        }
                    }

                    if (CSCommonConfigs.CYBERPSYCHOSIS_TOGGLE.get()) {
                        if (cap.isCyberpsycho() && player instanceof ServerPlayer serverPlayer) {
                            Cyberpsychosis cyberpsychosis = cap.getCyberpsychosis();
                            cyberpsychosis.handleCyberpsychosis(serverPlayer);
                        }
                    }
                });
            }
        }

        @Mod.EventBusSubscriber(modid = CSAugmentations.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
        public static class EffectHandler {

            @SubscribeEvent
            public static void onDimensionChange(PlayerEvent.PlayerChangedDimensionEvent event) {
                ServerPlayer player = (ServerPlayer) event.getEntity();
                player.getCapability(OrganCap.ORGAN_DATA).ifPresent(cap -> {
                    cap.updateOrganData();
                    CSNetwork.NETWORK_CHANNEL.send(
                            PacketDistributor.PLAYER.with(() -> player),
                            new S2CSyncDataPacket(cap, player.getId())
                    );
                });
            }

            @SubscribeEvent
            public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
                if (event.phase == TickEvent.Phase.START && !event.player.level().isClientSide) {
                    applyKidneyEffects(event.player);
                    applyLiverEffects(event.player);
                }
            }

            @SubscribeEvent
            public static void onLevelTick(TickEvent.LevelTickEvent event) {
                if (event.phase == TickEvent.Phase.END && !event.level.isClientSide()) {
                    event.level.players().forEach(EffectHandler::checkPendingWithdrawals);
                }
            }

            private static void applyKidneyEffects(LivingEntity entity) {
                double combinedEfficiency = calculateKidneyBuffs(entity);
                List<MobEffectInstance> effectsToAdd = new ArrayList<>();
                List<MobEffect> effectsToRemove = new ArrayList<>();

                for (MobEffectInstance oldEffect : new ArrayList<>(entity.getActiveEffects())) {
                    if (!isHarmfulEffect(oldEffect)) continue;

                    IMixinMobEffectInstance oldMixin = (IMixinMobEffectInstance) oldEffect;
                    if (oldMixin.cS_Augmentations$isEfficiencyApplied()) continue;

                    MobEffectInstance newEffect = createAdjustedEffect(oldEffect, combinedEfficiency, false);
                    effectsToRemove.add(oldEffect.getEffect());
                    effectsToAdd.add(newEffect);
                }

                if (combinedEfficiency == 0.0) {
                    effectsToAdd.add(new MobEffectInstance(
                            CSEffects.KIDNEY_FAILURE.get(), 40, 0, false, false, true
                    ));
                }

                addRemoveEffects(entity, effectsToAdd, effectsToRemove);
            }

            private static void applyLiverEffects(LivingEntity entity) {
                double pEfficiency = calculateLiverBuffs(entity);
                List<MobEffectInstance> effectsToAdd = new ArrayList<>();
                List<MobEffect> effectsToRemove = new ArrayList<>();

                for (MobEffectInstance oldEffect : new ArrayList<>(entity.getActiveEffects())) {
                    if (!isBeneficialEffect(oldEffect)) continue;

                    IMixinMobEffectInstance oldMixin = (IMixinMobEffectInstance) oldEffect;
                    if (oldMixin.cS_Augmentations$isEfficiencyApplied()) continue;

                    MobEffectInstance newEffect = createAdjustedEffect(oldEffect, pEfficiency, true);
                    effectsToRemove.add(oldEffect.getEffect());
                    effectsToAdd.add(newEffect);
                }

                if (pEfficiency == 0.0) {
                    effectsToAdd.add(new MobEffectInstance(
                            CSEffects.LIVER_FAILURE.get(), 40, 0, false, false, true
                    ));
                }

                addRemoveEffects(entity, effectsToAdd, effectsToRemove);
            }

            private static double calculateKidneyBuffs(LivingEntity entity) {
                return calculateCombinedEfficiency(entity,
                        new int[]{CSAugUtil.OrganSlots.LEFT_KIDNEY, CSAugUtil.OrganSlots.RIGHT_KIDNEY},
                        CSOrganTiers.Attribute.KIDNEY_EFFICIENCY
                );
            }

            private static double calculateLiverBuffs(LivingEntity entity) {
                return calculateCombinedEfficiency(entity,
                        new int[]{CSAugUtil.OrganSlots.LIVER},
                        CSOrganTiers.Attribute.LIVER_EFFICIENCY
                );
            }

            public static void adjustEffectDuration(MobEffectInstance effectInstance, double combinedEfficiency) {
                IMixinMobEffectInstance mixinEffectInstance = (IMixinMobEffectInstance) effectInstance;
                int duration = mixinEffectInstance.cS_Augmentations$getDuration();
                float multiplier = combinedEfficiency > 1.0 ?
                        1.0f / (float) combinedEfficiency :
                        1.0f + (1.0f - (float) combinedEfficiency);
                mixinEffectInstance.cS_Augmentations$setDuration((int) (duration * multiplier));
            }

            public static void adjustEffectDurationAndTier(MobEffectInstance effectInstance, double combinedEfficiency) {
                IMixinMobEffectInstance mixinEffectInstance = (IMixinMobEffectInstance) effectInstance;
                int duration = mixinEffectInstance.cS_Augmentations$getDuration();
                float multiplier = combinedEfficiency > 1.0 ?
                        1.0f + (float) (combinedEfficiency - 1.0) :
                        1.0f - (1.0f - (float) combinedEfficiency);
                mixinEffectInstance.cS_Augmentations$setDuration((int) (duration * multiplier));

                int additionalTiers = (int) ((combinedEfficiency - 1.0) / 0.5);
                mixinEffectInstance.cS_Augmentations$setAmplifier(
                        mixinEffectInstance.cS_Augmentations$getAmplifier() + additionalTiers
                );
            }

            private static double calculateCombinedEfficiency(LivingEntity entity, int[] slots, CSOrganTiers.Attribute attribute) {
                AtomicDouble totalEfficiency = new AtomicDouble(0.0);

                Arrays.stream(slots).forEach(slot -> {
                    entity.getCapability(OrganCap.ORGAN_DATA).ifPresent(organData -> {
                        ItemStack stack = organData.getStackInSlot(slot);
                        double efficiency = !stack.isEmpty() &&
                                stack.getItem() instanceof SimpleOrgan organ &&
                                organ.hasAttribute(attribute) ?
                                organ.getDoubleAttribute(attribute) :
                                0.0;
                        totalEfficiency.addAndGet(efficiency);
                    });
                });
                return totalEfficiency.get();
            }

            private static MobEffectInstance createAdjustedEffect(MobEffectInstance original, double efficiency, boolean isBeneficial) {
                int newDuration = calculateNewDuration(original, efficiency, isBeneficial);
                int newAmplifier = calculateNewAmplifier(original, efficiency, isBeneficial);

                MobEffectInstance newEffect = new MobEffectInstance(
                        original.getEffect(),
                        newDuration,
                        newAmplifier,
                        original.isAmbient(),
                        original.isVisible(),
                        original.showIcon()
                );

                ((IMixinMobEffectInstance) newEffect).cS_Augmentations$setEfficiencyApplied(true);
                return newEffect;
            }

            public static boolean isHarmfulEffect(MobEffectInstance effectInstance) {
                return effectInstance.getEffect().getCategory() == MobEffectCategory.HARMFUL;
            }

            public static boolean isBeneficialEffect(MobEffectInstance effectInstance) {
                return effectInstance.getEffect().getCategory() == MobEffectCategory.BENEFICIAL;
            }

            private static void addRemoveEffects(LivingEntity entity,
                                                 List<MobEffectInstance> effectsToAdd,
                                                 List<MobEffect> effectsToRemove) {
                effectsToRemove.forEach(entity::removeEffect);
                effectsToAdd.forEach(entity::addEffect);
            }

            private static int calculateNewDuration(MobEffectInstance effect, double efficiency, boolean isBeneficial) {
                float multiplier = isBeneficial ?
                        1.0f + (float) (efficiency - 1.0) :
                        1.0f / (float) efficiency;

                return (int) (effect.getDuration() * multiplier);
            }

            private static int calculateNewAmplifier(MobEffectInstance effect, double efficiency, boolean isBeneficial) {
                if (!isBeneficial) return effect.getAmplifier();
                return effect.getAmplifier() + (int) ((efficiency - 1.0) / 0.5);
            }

            private static void checkPendingWithdrawals(LivingEntity entity) {
                CompoundTag data = entity.getPersistentData();
                long currentTime = entity.getServer().getTickCount();

                if (data.contains("PendingWithdrawals", Tag.TAG_LIST)) {
                    ListTag withdrawals = data.getList("PendingWithdrawals", Tag.TAG_COMPOUND);
                    ListTag newWithdrawals = new ListTag();

                    for (Tag tag : withdrawals) {
                        CompoundTag entry = (CompoundTag) tag;
                        if (currentTime >= entry.getLong("ExpiresAt")) {
                            applyWithdrawal(entity, entry);
                        } else {
                            newWithdrawals.add(entry);
                        }
                    }

                    data.put("PendingWithdrawals", newWithdrawals);
                }
            }

            private static void applyWithdrawal(LivingEntity entity, CompoundTag entry) {
                entity.addEffect(new MobEffectInstance(
                        CSEffects.WITHDRAWAL.get(),
                        entry.getInt("Duration"),
                        entry.getInt("Strength"),
                        false, false, true
                ));
            }
        }

        @Mod.EventBusSubscriber(modid = CSAugmentations.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
        public static class CapabilityHandler {

            @SubscribeEvent
            public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
                if (event.getObject() instanceof Player) {
                    event.addCapability(new ResourceLocation(CSAugmentations.MOD_ID, "organ_data"), new OrganCapProvider((Player) event.getObject()));
                }
            }

            @SubscribeEvent
            public static void attachItemCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
                if(event.getObject().getItem() instanceof SyringeGunItem) {
                    event.addCapability(new ResourceLocation(CSAugmentations.MOD_ID, "syringe_data"), new SyringeGunCap.Provider());
                }
            }

            @SubscribeEvent
            public static void registerCapabilities(RegisterCapabilitiesEvent event) {
                event.register(OrganCap.OrganData.class);
                event.register(SyringeGunCap.SyringeData.class);
                LOGGER.info(String.valueOf(Component.literal("[CS] Augmentations Capabilities Registered")));
            }
        }
    }
}


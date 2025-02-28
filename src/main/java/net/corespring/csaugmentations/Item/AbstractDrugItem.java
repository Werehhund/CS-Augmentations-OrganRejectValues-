package net.corespring.csaugmentations.Item;

import net.corespring.csaugmentations.Registry.CSEffects;
import net.corespring.cslibrary.Registry.CSItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class AbstractDrugItem extends Item {
    public AbstractDrugItem(Properties pProperties) {
        super(pProperties);
    }

    public abstract List<MobEffectInstance> getActiveEffects();
    public abstract int getWithdrawalStrength();

    protected int getWithdrawalDuration() {
        return 2400;
    }

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);
        pPlayer.startUsingItem(pUsedHand);
        return InteractionResultHolder.consume(itemStack);
    }

    @NotNull
    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        if (!pLevel.isClientSide && pLivingEntity instanceof Player) {
            if(pLivingEntity.hasEffect(CSEffects.WITHDRAWAL.get())) {
                pLivingEntity.removeEffect(CSEffects.WITHDRAWAL.get());
                clearPendingWithdrawals(pLivingEntity);
            }
            List<MobEffectInstance> effects = getActiveEffects();
            effects.forEach(pLivingEntity::addEffect);

            int maxDuration = effects.stream()
                    .mapToInt(MobEffectInstance::getDuration)
                    .max()
                    .orElse(0);

            CompoundTag persistentData = pLivingEntity.getPersistentData();
            ListTag withdrawals = persistentData.getList("PendingWithdrawals", Tag.TAG_COMPOUND);

            CompoundTag withdrawalEntry = new CompoundTag();
            withdrawalEntry.putInt("Strength", getWithdrawalStrength());
            withdrawalEntry.putInt("Duration", getWithdrawalDuration());
            withdrawalEntry.putLong("ExpiresAt", pLevel.getServer().getTickCount() + maxDuration);

            withdrawals.add(withdrawalEntry);
            persistentData.put("PendingWithdrawals", withdrawals);
            pStack.shrink(1);
        }
        return pStack;
    }

    private void clearPendingWithdrawals(LivingEntity entity) {
        CompoundTag data = entity.getPersistentData();
        if(data.contains("PendingWithdrawals", Tag.TAG_LIST)) {
            data.remove("PendingWithdrawals");
        }
    }

    @NotNull
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.DRINK;
    }

    @NotNull
    public SoundEvent getEatingSound() {
        return SoundEvents.SNIFFER_SNIFFING;
    }

    @Override
    public SoundEvent getDrinkingSound() {
        return SoundEvents.SNIFFER_SNIFFING;
    }

    public int getUseDuration(ItemStack pStack) {
        return 10;
    }
}
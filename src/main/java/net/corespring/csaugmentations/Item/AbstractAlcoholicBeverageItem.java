package net.corespring.csaugmentations.Item;

import net.corespring.csaugmentations.Registry.CSEffects;
import net.corespring.cslibrary.Registry.CSItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractAlcoholicBeverageItem extends AbstractDrinkableItem {
    private final int tier;
    private static final int BASE_WITHDRAWAL_DURATION = 2400;

    public AbstractAlcoholicBeverageItem(int tier, Properties pProperties) {
        super(pProperties);
        this.tier = tier;
    }

    @NotNull
    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        if (!pLevel.isClientSide) {
            applyIntoxicationEffect(pLivingEntity);
            applyPositiveEffects(pLivingEntity);
            if(shouldApplyWithdrawal()) scheduleWithdrawal(pLivingEntity, pLevel);
        }

        if (pStack.isEmpty()) {
            return new ItemStack(CSItems.ALCOHOL_BOTTLE.get());
        } else {
            if (pLivingEntity instanceof Player player && !player.getAbilities().instabuild) {
                handleEmptyBottle(player);
                pStack.shrink(1);
            }
        }
        return pStack;
    }

    protected void applyIntoxicationEffect(LivingEntity entity) {
        MobEffectInstance currentEffect = entity.getEffect(CSEffects.INTOXICATED.get());
        int amplifier = getBaseAmplifierPerDrink();
        int duration = getBaseDurationPerDrink();

        if (currentEffect != null) {
            amplifier = Math.min(currentEffect.getAmplifier() + getBaseAmplifierPerDrink(), 7);
            duration = currentEffect.getDuration() + getBaseDurationPerDrink();

        }

        entity.addEffect(new MobEffectInstance(CSEffects.INTOXICATED.get(), duration, amplifier, false, false, true));
    }

    protected void applyPositiveEffects(LivingEntity entity) {
        for (MobEffectInstance effect : getPositiveEffects()) {
            entity.addEffect(effect);
        }
    }

    protected void handleEmptyBottle(Player player) {
        ItemStack bottle = new ItemStack(CSItems.ALCOHOL_BOTTLE.get());
        if (!player.getInventory().add(bottle)) {
            player.drop(bottle, false);
        }
    }

    public boolean shouldApplyWithdrawal() {
        return false;
    }

    private void scheduleWithdrawal(LivingEntity entity, Level level) {
        MobEffectInstance intoxication = entity.getEffect(CSEffects.INTOXICATED.get());
        if (intoxication != null) {
            CompoundTag data = entity.getPersistentData();
            ListTag withdrawals = data.getList("PendingWithdrawals", Tag.TAG_COMPOUND);

            CompoundTag entry = new CompoundTag();
            entry.putLong("ExpiresAt", level.getGameTime() + intoxication.getDuration());
            entry.putInt("Strength", calculateWithdrawalStrength());
            entry.putInt("Duration", calculateWithdrawalDuration());

            withdrawals.add(entry);
            data.put("PendingWithdrawals", withdrawals);
        }
    }

    protected int calculateWithdrawalStrength() {
        return Math.min(tier + 1, 3);
    }

    protected int calculateWithdrawalDuration() {
        return BASE_WITHDRAWAL_DURATION * (tier + 1);
    }

    protected abstract MobEffectInstance[] getPositiveEffects();

    protected abstract int getBaseDurationPerDrink();

    protected abstract int getBaseAmplifierPerDrink();

    public int getTier() {
        return tier;
    }
}

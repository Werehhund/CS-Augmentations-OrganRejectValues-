package net.corespring.csaugmentations.Item;

import net.corespring.csaugmentations.CSAugmentations;
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

import java.util.List;

public abstract class AbstractSyringeGunInjectable extends AbstractDrinkableItem {
    public AbstractSyringeGunInjectable(Properties pProperties) {
        super(pProperties);
    }

    public abstract List<MobEffectInstance> getEffects();

    protected int getWithdrawalStrength() {
        return 0;
    }

    protected int getWithdrawalDuration() {
        return 2400;
    }

    protected long getWithdrawalExpirationTime(Level level, List<MobEffectInstance> effects) {
        return level.getServer().getTickCount() + effects.stream()
                .mapToInt(MobEffectInstance::getDuration)
                .max()
                .orElse(0);
    }

    @NotNull
    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        if (!pLevel.isClientSide && pLivingEntity instanceof Player player) {

            List<MobEffectInstance> effects = getEffects();
            effects.forEach(player::addEffect);

                CompoundTag data = player.getPersistentData();
                ListTag withdrawals = data.getList("PendingWithdrawals", Tag.TAG_COMPOUND);

                CompoundTag entry = new CompoundTag();
                entry.putLong("ExpiresAt", getWithdrawalExpirationTime(pLevel, effects));
                entry.putInt("Strength", getWithdrawalStrength());
                entry.putInt("Duration", getWithdrawalDuration());

                withdrawals.add(entry);
                data.put("PendingWithdrawals", withdrawals);
                CSAugmentations.LOGGER.debug("Scheduled withdrawal at {}", entry.getLong("ExpiresAt"));

            handleContainerReturn(player);
            pStack.shrink(1);
        }
        return pStack;
    }

    private void handleContainerReturn(LivingEntity entity) {
        if (entity instanceof Player player && !player.getAbilities().instabuild) {
            ItemStack beaker = new ItemStack(CSItems.BEAKER.get());
            if (!player.getInventory().add(beaker)) {
                player.drop(beaker, false);
            }
        }
    }
}
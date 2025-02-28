package net.corespring.csaugmentations.Item.Pharma;

import net.corespring.csaugmentations.Item.AbstractDrinkableItem;
import net.corespring.csaugmentations.Registry.CSEffects;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class JointItem extends AbstractDrinkableItem {
    private final int tier;
    private static final int BASE_DURATION = 200;

    public JointItem(int tier, Properties pProperties) {
        super(pProperties);
        this.tier = tier;
    }

    @Override
    public @NotNull ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        if (!pLevel.isClientSide) {
            applyStonedEffect(pLivingEntity);
        }
        return pStack;
    }

    private void applyStonedEffect(LivingEntity entity) {
        MobEffectInstance current = entity.getEffect(CSEffects.STONED.get());
        int duration = BASE_DURATION * (tier + 1);
        int amplifier = Math.min(tier - 1, 3);

        if (current != null) {
            amplifier = Math.min(current.getAmplifier() + 1, 3);
            duration = current.getDuration() + (BASE_DURATION * tier);
        }

        entity.addEffect(new MobEffectInstance(CSEffects.STONED.get(), duration, amplifier, false, false, true));
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
        super.onUseTick(pLevel, pLivingEntity, pStack, pRemainingUseDuration);
        if (pLevel.isClientSide && pRemainingUseDuration % 3 == 0) {
            Vec3 look = pLivingEntity.getLookAngle();
            Vec3 pos = pLivingEntity.getEyePosition().add(look.scale(0.6));
            for (int i = 0; i < 2; i++) {
                pLevel.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                        pos.x + (Math.random() - 0.5) * 0.1,
                        pos.y + (Math.random() - 0.5) * 0.1,
                        pos.z + (Math.random() - 0.5) * 0.1,
                        0.02,
                        0.04,
                        0.02
                );
            }
        }
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 40;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.SPYGLASS;
    }

    @Override
    public @NotNull SoundEvent getDrinkingSound() {
        return SoundEvents.CANDLE_EXTINGUISH;
    }

    @Override
    public @NotNull SoundEvent getEatingSound() {
        return SoundEvents.CANDLE_EXTINGUISH;
    }
}
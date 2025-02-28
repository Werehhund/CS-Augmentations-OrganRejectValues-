package net.corespring.csaugmentations.Item.Pharma;

import net.corespring.csaugmentations.Item.AbstractSyringeGunInjectable;
import net.corespring.csaugmentations.Registry.CSEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MedicalCocaineItem extends AbstractSyringeGunInjectable {
    public MedicalCocaineItem(Properties pProperties) {
        super(pProperties);
    }

    private static final List<MobEffectInstance> EFFECTS = List.of(
            new MobEffectInstance(CSEffects.COKE_HIGH.get(), 1200, 0, false, false, true)
    );

    @Override
    public List<MobEffectInstance> getEffects() {
        return EFFECTS;
    }
}


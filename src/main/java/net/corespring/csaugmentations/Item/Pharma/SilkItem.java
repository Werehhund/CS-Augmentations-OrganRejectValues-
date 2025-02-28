package net.corespring.csaugmentations.Item.Pharma;

import net.corespring.csaugmentations.Item.AbstractSyringeGunInjectable;
import net.corespring.csaugmentations.Registry.CSEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;

import java.util.List;

public class SilkItem extends AbstractSyringeGunInjectable {
    public SilkItem(Item.Properties pProperties) {
        super(pProperties);
    }

    private static final List<MobEffectInstance> EFFECTS = List.of(
            new MobEffectInstance(CSEffects.SILK.get(), 1000, 0, false, false, true)
    );

    @Override
    public List<MobEffectInstance> getEffects() {
        return EFFECTS;
    }
}


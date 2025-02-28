package net.corespring.csaugmentations.Item.Pharma;

import net.corespring.csaugmentations.Item.AbstractDrugItem;
import net.corespring.csaugmentations.Registry.CSEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;

import java.util.List;

public class CocaineItem extends AbstractDrugItem {
    public CocaineItem(Item.Properties pProperties) {
        super(pProperties);
    }

    @Override
    public List<MobEffectInstance> getActiveEffects() {
        return List.of(
                new MobEffectInstance(CSEffects.COKE_HIGH.get(), 2400, 1, false, false, true)
        );
    }

    @Override
    public int getWithdrawalStrength() {
        return 0;
    }

    @Override
    protected int getWithdrawalDuration() {
        return 4800;
    }
}
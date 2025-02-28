package net.corespring.csaugmentations.Effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class CokeHigh extends MobEffect {
    private static final String SPEED_UUID = "B9768B9E-5F3A-4D3E-9B9A-4A2B7B8C9D0E";
    private static final String DAMAGE_UUID = "C9768B9E-5F3A-4D3E-9B9A-4A2B7B8C9D0F";

    public CokeHigh(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
        addAttributeModifier(Attributes.MOVEMENT_SPEED, SPEED_UUID, 0.015, AttributeModifier.Operation.ADDITION);
        addAttributeModifier(Attributes.ATTACK_DAMAGE, DAMAGE_UUID, 1.0, AttributeModifier.Operation.ADDITION);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}
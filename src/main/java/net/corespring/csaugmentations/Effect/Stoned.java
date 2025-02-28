package net.corespring.csaugmentations.Effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class Stoned extends MobEffect {
    private static final String SPEED_UUID = "bfe64e6f-6de7-4d97-9023-51a3b028e3c3";
    private static final String KNOCKBACK_UUID = "454698e4-de49-423a-a5ca-b22263875acf";

    public Stoned(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
        addAttributeModifier(Attributes.MOVEMENT_SPEED, SPEED_UUID, -0.015, AttributeModifier.Operation.ADDITION);
        addAttributeModifier(Attributes.KNOCKBACK_RESISTANCE, KNOCKBACK_UUID, -0.1, AttributeModifier.Operation.ADDITION);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
            entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 40, amplifier - 2, false, false, true));
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 20 == 0;
    }
}
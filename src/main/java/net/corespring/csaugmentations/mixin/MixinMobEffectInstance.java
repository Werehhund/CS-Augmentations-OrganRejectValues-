package net.corespring.csaugmentations.mixin;

import net.corespring.csaugmentations.Augmentations.Base.IMixinMobEffectInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEffectInstance.class)
public abstract class MixinMobEffectInstance implements IMixinMobEffectInstance {
    @Unique
    private boolean cS_Augmentations$efficiencyApplied = false;

    @Shadow
    private int duration;

    @Shadow
    private int amplifier;

    @Inject(method = "save", at = @At("RETURN"))
    private void cS_Augmentations$save(CompoundTag pNbt, CallbackInfoReturnable<CompoundTag> cir) {
        pNbt.putBoolean("CS_EffApplied", this.cS_Augmentations$efficiencyApplied);
    }

    @Inject(method = "load", at = @At("RETURN"))
    private static void cS_Augmentations$load(CompoundTag pNbt, CallbackInfoReturnable<MobEffectInstance> cir) {
        MobEffectInstance instance = cir.getReturnValue();
        if (instance != null) {
            ((IMixinMobEffectInstance) instance).cS_Augmentations$setEfficiencyApplied(
                    pNbt.getBoolean("CS_EffApplied")
            );
        }
    }

    @Override
    public boolean cS_Augmentations$isEfficiencyApplied() {
        return cS_Augmentations$efficiencyApplied;
    }

    @Override
    public void cS_Augmentations$setEfficiencyApplied(boolean applied) {
        this.cS_Augmentations$efficiencyApplied = applied;
    }

    @Override
    public int cS_Augmentations$getDuration() {
        return duration;
    }

    @Override
    public int cS_Augmentations$getAmplifier() {
        return amplifier;
    }

    @Override
    public void cS_Augmentations$setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public void cS_Augmentations$setAmplifier(int amplifier) {
        this.amplifier = amplifier;
    }
}

package net.corespring.csaugmentations.Augmentations.Base.Organs;

import net.corespring.csaugmentations.Augmentations.Base.SimpleOrgan;
import net.corespring.csaugmentations.Utility.IOrganTiers;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class SimpleEyes extends SimpleOrgan {
    public SimpleEyes(IOrganTiers pTier, Properties pProperties) {
        super(pTier, pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        if(Screen.hasShiftDown()) {
            pTooltipComponents.add(Component.translatable("tooltip.csaugmentations.separator").withStyle(ChatFormatting.LIGHT_PURPLE));
            pTooltipComponents.add(Component.translatable("tooltip.csaugmentations.eyes_desc").withStyle(ChatFormatting.GOLD));
        } else {
            pTooltipComponents.add(Component.translatable("tooltip.cslibrary.shift_info").withStyle(ChatFormatting.GOLD));
        }
    }
}

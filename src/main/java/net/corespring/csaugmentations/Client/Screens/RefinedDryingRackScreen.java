package net.corespring.csaugmentations.Client.Screens;

import net.corespring.csaugmentations.CSAugmentations;
import net.corespring.csaugmentations.Client.Menus.RefinedDryingRackMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class RefinedDryingRackScreen extends AbstractDryingRackScreen<RefinedDryingRackMenu> {
    private static final ResourceLocation BG_LOCATION = new ResourceLocation(CSAugmentations.MOD_ID, "textures/gui/refined_drying_rack_gui.png");

    public RefinedDryingRackScreen(RefinedDryingRackMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float partialTick, int mouseX, int mouseY) {
        pGuiGraphics.blit(BG_LOCATION, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }
}
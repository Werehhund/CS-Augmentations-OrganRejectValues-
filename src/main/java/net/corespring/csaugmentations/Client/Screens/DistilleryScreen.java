package net.corespring.csaugmentations.Client.Screens;

import net.corespring.csaugmentations.CSAugmentations;
import net.corespring.csaugmentations.Client.Menus.DistilleryMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class DistilleryScreen extends AbstractContainerScreen<DistilleryMenu> {
    private static final ResourceLocation BG_LOCATION = new ResourceLocation(CSAugmentations.MOD_ID, "textures/gui/distillery_gui.png");

    public DistilleryScreen(DistilleryMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float partialTick, int mouseX, int mouseY) {
        pGuiGraphics.blit(BG_LOCATION, leftPos, topPos, 0, 0, imageWidth, imageHeight);

        int progressHeight = menu.getScaledProgress();
        if (progressHeight > 0) {
            pGuiGraphics.blit(BG_LOCATION,
                    leftPos + 48, topPos + 28 + (40 - progressHeight),
                    176, 3 + (40 - progressHeight),
                    8, progressHeight);
        }

        int fuelHeight = menu.getScaledFuelProgress();
        if (fuelHeight > 0) {
            pGuiGraphics.blit(BG_LOCATION,
                    leftPos + 59, topPos + 44 + (13 - fuelHeight),
                    176, 43 + (13 - fuelHeight),
                    13, fuelHeight);
        }
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(pGuiGraphics);
        this.renderBg(pGuiGraphics, partialTicks, mouseX, mouseY);
        super.render(pGuiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(pGuiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int mouseX, int mouseY) {
        pGuiGraphics.drawString(this.font, this.title.getString(), 20, 6, 0x404040, false);
        pGuiGraphics.drawString(this.font, this.playerInventoryTitle.getString(), 20, this.imageHeight - 96 + 2, 0x404040, false);
    }
}

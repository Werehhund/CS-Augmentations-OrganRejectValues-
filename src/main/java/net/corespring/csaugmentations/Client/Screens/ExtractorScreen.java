package net.corespring.csaugmentations.Client.Screens;

import net.corespring.csaugmentations.CSAugmentations;
import net.corespring.csaugmentations.Client.Menus.ExtractorMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ExtractorScreen extends AbstractContainerScreen<ExtractorMenu> {
    private static final ResourceLocation BG_LOCATION =
            new ResourceLocation(CSAugmentations.MOD_ID, "textures/gui/extractor_gui.png");

    public ExtractorScreen(ExtractorMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        pGuiGraphics.blit(BG_LOCATION, x, y, 0, 0, imageWidth, imageHeight);

        if (menu.isCrafting()) {
            int progress = menu.getScaledProgress();
            pGuiGraphics.blit(BG_LOCATION,
                    x + 82,
                    y + 40 + (14 - progress),
                    176,
                    3 + (14 - progress),
                    14,
                    progress
            );
        }
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(pGuiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int mouseX, int mouseY) {
        pGuiGraphics.drawString(this.font, this.title, 8, 6, 0x404040, false);
        pGuiGraphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 0x404040, false);
    }
}
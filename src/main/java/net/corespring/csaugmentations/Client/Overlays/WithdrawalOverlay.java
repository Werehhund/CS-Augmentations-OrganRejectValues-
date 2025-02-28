package net.corespring.csaugmentations.Client.Overlays;

import com.mojang.blaze3d.systems.RenderSystem;
import net.corespring.csaugmentations.CSAugmentations;
import net.corespring.csaugmentations.Registry.CSEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class WithdrawalOverlay implements IGuiOverlay {
    private static final ResourceLocation WITHDRAWAL1 = new ResourceLocation(CSAugmentations.MOD_ID, "textures/gui/withdrawal_overlay1.png");
    private static final ResourceLocation WITHDRAWAL2 = new ResourceLocation(CSAugmentations.MOD_ID, "textures/gui/withdrawal_overlay2.png");
    private static final ResourceLocation WITHDRAWAL3 = new ResourceLocation(CSAugmentations.MOD_ID, "textures/gui/withdrawal_overlay3.png");
    protected int screenWidth;
    protected int screenHeight;

    @Override
    public void render(ForgeGui forgeGui, GuiGraphics pGuiGraphics, float partialTicks, int screenWidth, int screenHeight) {
        this.screenWidth = pGuiGraphics.guiWidth();
        this.screenHeight = pGuiGraphics.guiHeight();
        Player player = Minecraft.getInstance().player;

        if (player != null && player.hasEffect(CSEffects.WITHDRAWAL.get())) {
            switch (player.getEffect(CSEffects.WITHDRAWAL.get()).getAmplifier()) {
                case 0: renderTextureOverlay(pGuiGraphics, WITHDRAWAL1, 1.0F); break;
                case 1: renderTextureOverlay(pGuiGraphics, WITHDRAWAL2, 1.0F); break;
                default: renderTextureOverlay(pGuiGraphics, WITHDRAWAL3, 1.0F); break;
            }
        }
    }

    protected void renderTextureOverlay(GuiGraphics pGuiGraphics, ResourceLocation pShaderLocation, float pAlpha) {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, pAlpha);
        pGuiGraphics.blit(pShaderLocation, 0, 0, -90, 0.0F, 0.0F, this.screenWidth, this.screenHeight, this.screenWidth, this.screenHeight);
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
}


package net.corespring.csaugmentations.Client.Renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.corespring.csaugmentations.Block.BlockEntities.CrudeDryingRackBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;

public class CrudeDryingRackRenderer implements BlockEntityRenderer<CrudeDryingRackBlockEntity> {
    private static final float ITEM_SCALE = 0.65f;
    private static final float ITEM_SLANT = 115f;

    public CrudeDryingRackRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(@NotNull CrudeDryingRackBlockEntity blockEntity, float partialTick,
                       @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource,
                       int packedLight, int packedOverlay) {
        ItemStack itemStack = blockEntity.getItemHandler().getStackInSlot(0);
        if (!itemStack.isEmpty()) {
            poseStack.pushPose();

            Direction facing = blockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
            poseStack.translate(0.5, 0.5, 0.5);
            poseStack.mulPose(Axis.YP.rotationDegrees(-facing.toYRot()));
            poseStack.translate(-0.5, -0.5, -0.5);

            poseStack.translate(8.0f / 16.0f, 17.0f / 16.0f, 7.0f / 16.0f);

            poseStack.mulPose(Axis.XP.rotationDegrees(ITEM_SLANT));

            poseStack.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);

            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED,
                    packedLight, OverlayTexture.NO_OVERLAY,
                    poseStack, bufferSource, blockEntity.getLevel(), 0);

            poseStack.popPose();
        }
    }
}
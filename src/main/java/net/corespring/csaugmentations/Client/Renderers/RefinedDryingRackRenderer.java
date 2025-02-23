package net.corespring.csaugmentations.Client.Renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.corespring.csaugmentations.Block.BlockEntities.RefinedDryingRackBlockEntity;
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

public class RefinedDryingRackRenderer implements BlockEntityRenderer<RefinedDryingRackBlockEntity> {
    private static final float ITEM_SCALE = 0.65f;
    private static final float ITEM_SLANT = 90.0f;

    public RefinedDryingRackRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(@NotNull RefinedDryingRackBlockEntity blockEntity, float partialTick,
                       @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource,
                       int packedLight, int packedOverlay) {
        Direction facing = blockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);

        renderSlot(blockEntity, 1, 8, 16, 7, facing, poseStack, bufferSource, packedLight);
        renderSlot(blockEntity, 2, 23, 16, 7, facing, poseStack, bufferSource, packedLight);
        renderSlot(blockEntity, 0, -5, 16, 7, facing, poseStack, bufferSource, packedLight);
    }

    private void renderSlot(RefinedDryingRackBlockEntity blockEntity, int slot,
                            float x, float y, float z, Direction facing,
                            PoseStack poseStack, MultiBufferSource bufferSource,
                            int packedLight) {
        ItemStack itemStack = blockEntity.getItemHandler().getStackInSlot(slot);
        if (!itemStack.isEmpty()) {
            poseStack.pushPose();

            poseStack.translate(0.5, 0.5, 0.5);
            poseStack.mulPose(Axis.YP.rotationDegrees(-facing.toYRot()));
            poseStack.translate(-0.5, -0.5, -0.5);

            poseStack.translate(x / 16.0f, y / 16.0f, z / 16.0f);

            poseStack.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);

            poseStack.mulPose(Axis.XP.rotationDegrees(ITEM_SLANT));

            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED,
                    packedLight, OverlayTexture.NO_OVERLAY,
                    poseStack, bufferSource, blockEntity.getLevel(), 0);

            poseStack.popPose();
        }
    }
}
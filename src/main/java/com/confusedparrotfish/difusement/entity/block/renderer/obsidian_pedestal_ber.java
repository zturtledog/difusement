package com.confusedparrotfish.difusement.entity.block.renderer;

import com.confusedparrotfish.difusement.entity.block.obsidian_pedestal_be;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class obsidian_pedestal_ber implements BlockEntityRenderer<obsidian_pedestal_be> {
   private final ItemRenderer itemRenderer;

    public obsidian_pedestal_ber(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    public void render(obsidian_pedestal_be entity, float delta, PoseStack pose, MultiBufferSource bufffer,
            int light, int overlay) {
                int i = (int)entity.getBlockPos().asLong();
                // this.itemRenderer.render(entity.item, TransformType.GROUND, false, pose, bufffer, light, overlay, null);
                //.renderItem(entity.item, ModelTransformationMode.GROUND, light, overlay, matrices, vertexConsumers, entity.getWorld(), 0);

                pose.pushPose();
                pose.translate(0.5, 1.5, 0.5);
                this.itemRenderer.renderStatic(entity.item, ItemTransforms.TransformType.FIXED, light, overlay, pose, bufffer, i);
                pose.popPose();
            }

            /*
             * 
             * 
    public void render(CampfireBlockEntity p_112344_, float p_112345_, PoseStack p_112346_, MultiBufferSource p_112347_, int p_112348_, int p_112349_) {
      Direction direction = p_112344_.getBlockState().getValue(CampfireBlock.FACING);
      
             */
}

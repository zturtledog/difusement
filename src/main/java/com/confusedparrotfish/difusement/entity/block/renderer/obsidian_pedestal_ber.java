package com.confusedparrotfish.difusement.entity.block.renderer;

import com.confusedparrotfish.difusement.entity.block.obsidian_pedestal_be;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction.Axis;
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
                float bob = (ease(Math.abs((entity.time % 20)/20.0f-0.5f)*2f)-0.5f)/5f;
                pose.translate(0.5, 1.5+bob, 0.5);
                pose.mulPose(Quaternion.fromXYZDegrees(new Vector3f(bob/10f, entity.time, bob/10f)));
                this.itemRenderer.renderStatic(entity.item, ItemTransforms.TransformType.FIXED, light, overlay, pose, bufffer, i);
                pose.popPose();
            }

    public float ease(float n) {
        // \left(2x-1\right)^{6}
        // return (float) Math.pow(n,6f);

        //13.6x^{2}\left(x-1\right)^{2}
        return (float)(13.6*Math.pow(n, 2)*Math.pow(n-1, 2));
    }
}

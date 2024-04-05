package com.confusedparrotfish.difusement.entity.block.renderer;

import com.confusedparrotfish.difusement.entity.block.difusement_altar_be;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;

import net.minecraft.client.model.BookModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class difusement_altar_ber implements BlockEntityRenderer<difusement_altar_be> {
    private final BookModel bookModel;
    private final ItemRenderer itemRenderer;

    public difusement_altar_ber(BlockEntityRendererProvider.Context context) {
        this.bookModel = new BookModel(context.bakeLayer(ModelLayers.BOOK));
        this.itemRenderer = context.getItemRenderer();
    }

    public void render(difusement_altar_be entity, float delta, PoseStack pose, MultiBufferSource bufffer,
            int light, int overlay) {
        entity.book.render(delta, pose, bufffer, light, overlay, bookModel);
        entity.cloud.render(delta, pose, bufffer, light, overlay, itemRenderer, entity, entity.getBlockPos());

        int i = (int) entity.getBlockPos().asLong();
        // this.itemRenderer.render(entity.item, TransformType.GROUND, false, pose,
        // bufffer, light, overlay, null);
        // .renderItem(entity.item, ModelTransformationMode.GROUND, light, overlay,
        // matrices, vertexConsumers, entity.getWorld(), 0);

        pose.pushPose();
        float bob = (ease(Math.abs(((entity.time+entity.timeoff) % 100)/100.0f-0.5f)*2f)-0.5f)/3f;
        pose.translate(0.5, 2+bob, 0.5);
        pose.mulPose(Quaternion.fromXYZDegrees(new Vector3f(bob/10f,
        (entity.time+entity.timeoff), bob/10f)));

        if (!entity.item.getItem().getClass().isAssignableFrom(BlockItem.class)) {
            pose.scale(0.75f, 0.75f, 0.75f);
        }

        this.itemRenderer.renderStatic(entity.item, ItemTransforms.TransformType.FIXED, light, overlay, pose, bufffer,
                i);
        pose.popPose();
    }

    public float ease(float n) {
        return 1f - (float) (13.6 * Math.pow(n, 2) * Math.pow(n - 1, 2));
    }
}

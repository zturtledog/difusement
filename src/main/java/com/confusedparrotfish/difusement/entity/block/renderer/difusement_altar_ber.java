package com.confusedparrotfish.difusement.entity.block.renderer;

import com.confusedparrotfish.difusement.entity.block.difusement_altar_be;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.BookModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class difusement_altar_ber implements BlockEntityRenderer<difusement_altar_be> {
    private final BookModel bookModel;

    public difusement_altar_ber(BlockEntityRendererProvider.Context context) {
        this.bookModel = new BookModel(context.bakeLayer(ModelLayers.BOOK));
    }

    public void render(difusement_altar_be entity, float p_112419_, PoseStack pose, MultiBufferSource bufffer,
            int p_112422_, int p_112423_) {
        entity.book.render(p_112419_, pose, bufffer, p_112422_, p_112423_, bookModel);
        
        // MinecraftClient.getInstance().getItemRenderer().renderItem(entity.item, ModelTransformationMode.GROUND, light, overlay, matrices, vertexConsumers, entity.getWorld(), 0);
    }
}

package com.confusedparrotfish.difusement.util.ber_components;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;

import net.minecraft.client.model.BookModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.EnchantTableRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

//most code taken directly from net.minecraft.world.level.block.entity.EnchantmentTableBlockEntity and net.minecraft.world.level.block.entity.EnchantmentTableBlockEntityRenderer
public class enchantingbook {
    public int time;
    public float flip;
    public float oFlip;
    public float flipT;
    public float flipA;
    public float open;
    public float oOpen;
    public float rot;
    public float oRot;
    public float tRot;
    private static final RandomSource RANDOM = RandomSource.create();
    public void update(Level lvl, BlockPos pos, BlockState state) {
        this.oOpen = this.open;
        this.oRot = this.rot;
        Player player = lvl.getNearestPlayer((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D,
                (double) pos.getZ() + 0.5D, 3.0D, false);
        if (player != null) {
            double d0 = player.getX() - ((double) pos.getX() + 0.5D);
            double d1 = player.getZ() - ((double) pos.getZ() + 0.5D);
            this.tRot = (float) Mth.atan2(d1, d0);
            this.open += 0.1F;
            if (this.open < 0.5F || RANDOM.nextInt(40) == 0) {
                float f1 = this.flipT;

                do {
                    this.flipT += (float) (RANDOM.nextInt(4) - RANDOM.nextInt(4));
                } while (f1 == this.flipT);
            }
        } else {
            this.tRot += 0.02F;
            this.open -= 0.1F;
        }

        while (this.rot >= (float) Math.PI) {
            this.rot -= ((float) Math.PI * 2F);
        }

        while (this.rot < -(float) Math.PI) {
            this.rot += ((float) Math.PI * 2F);
        }

        while (this.tRot >= (float) Math.PI) {
            this.tRot -= ((float) Math.PI * 2F);
        }

        while (this.tRot < -(float) Math.PI) {
            this.tRot += ((float) Math.PI * 2F);
        }

        float f2;
        for (f2 = this.tRot - this.rot; f2 >= (float) Math.PI; f2 -= ((float) Math.PI * 2F)) {
        }

        while (f2 < -(float) Math.PI) {
            f2 += ((float) Math.PI * 2F);
        }

        this.rot += f2 * 0.4F;
        this.open = Mth.clamp(this.open, 0.0F, 1.0F);
        ++this.time;
        this.oFlip = this.flip;
        float f = (this.flipT - this.flip) * 0.4F;
        f = Mth.clamp(f, -0.2F, 0.2F);
        this.flipA += (f - this.flipA) * 0.9F;
        this.flip += this.flipA;
    }

    public void render(float p_112419_, PoseStack pose, MultiBufferSource bufffer, int p_112422_, int p_112423_, BookModel bookModel) {
        pose.pushPose();
          pose.translate(0.5D, 0.75D, 0.5D);
          float f = (float)this.time + p_112419_;
          pose.translate(0.0D, (double)(0.1F + Mth.sin(f * 0.1F) * 0.01F), 0.0D);
    
          float f1;
          for(f1 = this.rot - this.oRot; f1 >= (float)Math.PI; f1 -= ((float)Math.PI * 2F)) {
          }
    
          while(f1 < -(float)Math.PI) {
             f1 += ((float)Math.PI * 2F);
          }
    
          float f2 = this.oRot + f1 * p_112419_;
          pose.mulPose(Vector3f.YP.rotation(-f2));
          pose.mulPose(Vector3f.ZP.rotationDegrees(80.0F));
          float f3 = Mth.lerp(p_112419_, this.oFlip, this.flip);
          float f4 = Mth.frac(f3 + 0.25F) * 1.6F - 0.3F;
          float f5 = Mth.frac(f3 + 0.75F) * 1.6F - 0.3F;
          float f6 = Mth.lerp(p_112419_, this.oOpen, this.open);
          bookModel.setupAnim(f, Mth.clamp(f4, 0.0F, 1.0F), Mth.clamp(f5, 0.0F, 1.0F), f6);
          VertexConsumer vertexconsumer = EnchantTableRenderer.BOOK_LOCATION.buffer(bufffer, RenderType::entitySolid);
          bookModel.render(pose, vertexconsumer, p_112422_, p_112423_, 1.0F, 1.0F, 1.0F, 1.0F);
          pose.popPose();
    }
}

package com.confusedparrotfish.difusement.network.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import com.confusedparrotfish.difusement.util.ber_components.itemcloud;

public class ItemCloudSyncS2CPacket {
    private final itemcloud cloud;
    private final BlockPos pos;

    public ItemCloudSyncS2CPacket(itemcloud cloud, BlockPos pos) {
        this.cloud = cloud;
        this.pos = pos;
    }

    public ItemCloudSyncS2CPacket(FriendlyByteBuf buf) {
        cloud = itemcloud.readpacket(buf);
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        cloud.writepacket(buf);
        buf.writeBlockPos(pos);
    }

    @SuppressWarnings("resource") //this should be fine?
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            if(Minecraft.getInstance().level.getBlockEntity(pos) instanceof cloudImplementor ent) {
                ent.cloud_update(cloud);
            }
        });
        return true;
    }

    public static interface cloudImplementor {
        public void cloud_update(itemcloud cloud);
    }
}

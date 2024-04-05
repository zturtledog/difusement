package com.confusedparrotfish.difusement.network.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import com.confusedparrotfish.difusement.entity.block.difusement_altar_be;

public class DifusementAltarSyncS2CPacket {
    private final difusement_altar_packet pack;
    private final BlockPos pos;

    public DifusementAltarSyncS2CPacket(difusement_altar_packet pack, BlockPos pos) {
        this.pack = pack;
        this.pos = pos;
    }

    public DifusementAltarSyncS2CPacket(FriendlyByteBuf buf) {
        pack = difusement_altar_packet.readpacket(buf);
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        pack.writepacket(buf);
        buf.writeBlockPos(pos);
    }

    @SuppressWarnings("resource") //this should be fine?
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            if(Minecraft.getInstance().level.getBlockEntity(pos) instanceof difusement_altar_be ent) {
                ent.recv_packet(pack);
            }
        });
        return true;
    }

    public static class difusement_altar_packet {
        public int[] progress = new int[]{};

        public difusement_altar_packet(int[] progress) {
            this.progress=progress;
        }

        public void writepacket(FriendlyByteBuf buf) {
            buf.writeInt(progress.length);
            for (int i : progress) {
                buf.writeInt(i);
            }
        }

        public static difusement_altar_packet readpacket(FriendlyByteBuf buf) {
            int[] temp = new int[buf.readInt()];
            for (int i = 0; i < temp.length; i++) {
                temp[i] = buf.readInt();
            }
            return new difusement_altar_packet(temp);
        }
    }
}

package com.confusedparrotfish.difusement.network.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import com.confusedparrotfish.difusement.util.SingleItemInventoryBlockEntity;

public class ItemStackSyncS2CPacket {
    private final ItemStack stack;
    private final BlockPos pos;

    public ItemStackSyncS2CPacket(ItemStack stack, BlockPos pos) {
        this.stack = stack;
        this.pos = pos;
    }

    public ItemStackSyncS2CPacket(FriendlyByteBuf buf) {
        stack = buf.readItem();
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeItem(stack);
        buf.writeBlockPos(pos);
    }

    @SuppressWarnings("resource") //this should be fine?
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            if(Minecraft.getInstance().level.getBlockEntity(pos) instanceof SingleItemInventoryBlockEntity blockEntity) {
                blockEntity.item = stack;
            }
        });
        return true;
    }
}
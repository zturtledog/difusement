package com.confusedparrotfish.difusement.network;

import com.confusedparrotfish.difusement.Difusement;
import com.confusedparrotfish.difusement.network.packet.DifusementAltarSyncS2CPacket;
import com.confusedparrotfish.difusement.network.packet.ItemCloudSyncS2CPacket;
import com.confusedparrotfish.difusement.network.packet.ItemStackSyncS2CPacket;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class message {
    private static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(Difusement.MODID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(ItemStackSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ItemStackSyncS2CPacket::new)
                .encoder(ItemStackSyncS2CPacket::toBytes)
                .consumerMainThread(ItemStackSyncS2CPacket::handle)
                .add();
        
        net.messageBuilder(ItemCloudSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ItemCloudSyncS2CPacket::new)
                .encoder(ItemCloudSyncS2CPacket::toBytes)
                .consumerMainThread(ItemCloudSyncS2CPacket::handle)
                .add();

        net.messageBuilder(DifusementAltarSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(DifusementAltarSyncS2CPacket::new)
                .encoder(DifusementAltarSyncS2CPacket::toBytes)
                .consumerMainThread(DifusementAltarSyncS2CPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
         INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static <MSG> void sendToClients(MSG message) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }
}
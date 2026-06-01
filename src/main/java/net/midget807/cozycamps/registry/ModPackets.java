package net.midget807.cozycamps.registry;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.midget807.cozycamps.CozyCampsMain;
import net.midget807.cozycamps.network.C2S.packet.OpenSackScreenPacket;
import net.midget807.cozycamps.network.C2S.payload.OpenSackScreenPayload;
import net.minecraft.util.Identifier;

public class ModPackets {
    /**C2S Packets*/
    public static final Identifier OPEN_SACK_SCREEN = registerC2SId("open_sack_screen");

    /**S2C Packets*/


    public static void registerAllC2S() {
        registerC2SPayloads();
        registerC2SPackets();
    }
    public static void registerC2SPayloads() {
        PayloadTypeRegistry.playC2S().register(OpenSackScreenPayload.PAYLOAD_ID, OpenSackScreenPayload.CODEC);
    }
    public static void registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(OpenSackScreenPayload.PAYLOAD_ID, OpenSackScreenPacket::receive);
    }

    public static void registerAllS2C() {
        registerS2CPayloads();
        registerS2CPackets();
    }
    public static void registerS2CPayloads() {

    }
    public static void registerS2CPackets() {

    }

    private static Identifier registerC2SId(String name) {
        return CozyCampsMain.id(name + "_c2s_packet");
    }
    private static Identifier registerS2CId(String name) {
        return CozyCampsMain.id(name + "_s2c_packet");
    }

    public static void registerModPackets() {
        CozyCampsMain.loggerRegistry("Packets");
    }
}

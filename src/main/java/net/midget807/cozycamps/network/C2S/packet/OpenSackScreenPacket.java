package net.midget807.cozycamps.network.C2S.packet;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.midget807.cozycamps.network.C2S.payload.OpenSackScreenPayload;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

public class OpenSackScreenPacket {
    public static void receive(OpenSackScreenPayload payload, ServerPlayNetworking.Context context) {
        context.server().execute(() -> {
            ServerPlayerEntity player = context.player();
            ItemStack stack = payload.stack();
        });
    }
}

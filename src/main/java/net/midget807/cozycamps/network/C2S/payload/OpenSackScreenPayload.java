package net.midget807.cozycamps.network.C2S.payload;

import net.midget807.cozycamps.registry.ModPackets;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Hand;

public record OpenSackScreenPayload(ItemStack stack) implements CustomPayload {
    public static final CustomPayload.Id<OpenSackScreenPayload> PAYLOAD_ID = new CustomPayload.Id<>(ModPackets.OPEN_SACK_SCREEN);

    public static final PacketCodec<RegistryByteBuf, OpenSackScreenPayload> CODEC = PacketCodec.tuple(
            ItemStack.PACKET_CODEC, OpenSackScreenPayload::stack,
            OpenSackScreenPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return PAYLOAD_ID;
    }
}

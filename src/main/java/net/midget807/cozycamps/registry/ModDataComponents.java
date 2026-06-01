package net.midget807.cozycamps.registry;

import net.midget807.cozycamps.CozyCampsMain;
import net.midget807.cozycamps.item.component.SackContentsComponent;
import net.midget807.cozycamps.item.component.SackInventoryComponent;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import java.util.function.UnaryOperator;

public class ModDataComponents {
    public static final ComponentType<SackContentsComponent> SACK_CONTENTS = register(
            "sack_contents", builder -> builder.codec(SackContentsComponent.CODEC).packetCodec(SackContentsComponent.PACKET_CODEC).cache()
    );
    public static final ComponentType<SackInventoryComponent> SACK_INVENTORY = register(
            "sack_contents", builder -> builder.codec(SackInventoryComponent.CODEC).packetCodec(SackInventoryComponent.PACKET_CODEC).cache()
    );

    private static <T> ComponentType<T> register(String id, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, id, ((ComponentType.Builder)builderOperator.apply(ComponentType.builder())).build());
    }

    public static void registerModDataComponents() {
        CozyCampsMain.loggerRegistry("Data Components");
    }
}

package net.midget807.cozycamps.registry;

import net.midget807.cozycamps.CozyCampsMain;
import net.midget807.cozycamps.item.component.SackInventoryComponent;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import java.util.function.UnaryOperator;

public class ModDataComponents {

    public static final ComponentType<SackInventoryComponent> SACK_INVENTORY = register("sack_inventory", builder -> builder.codec(SackInventoryComponent.CODEC).packetCodec(SackInventoryComponent.PACKET_CODEC));

    private static <T> ComponentType<T> register(String name, UnaryOperator<ComponentType.Builder<T>> builder) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, CozyCampsMain.id(name), builder.apply(ComponentType.builder()).build());
    }

    public static void registerModDataComponents() {
        CozyCampsMain.loggerRegistry("Data Components");
    }
}

package net.midget807.cozycamps.registry;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.midget807.cozycamps.CozyCampsMain;
import net.midget807.cozycamps.screen.SackScreenHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;

public class ModScreenHandlers {
    public static final ScreenHandlerType<SackScreenHandler> SACK =
            Registry.register(Registries.SCREEN_HANDLER, CozyCampsMain.id("sack"),
                    new ExtendedScreenHandlerType<>(SackScreenHandler::new, ItemStack.PACKET_CODEC)
            );

    public static void registerModScreenHandlers() {
        CozyCampsMain.loggerRegistry("Screen Handlers");
    }
}

package net.midget807.cozycamps;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.midget807.cozycamps.entity.client.SitEntityRenderer;
import net.midget807.cozycamps.registry.ModEntities;
import net.midget807.cozycamps.registry.ModPackets;
import net.midget807.cozycamps.registry.ModScreenHandlers;
import net.midget807.cozycamps.screen.SackScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class CozyCampClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModPackets.registerAllS2C();

        EntityRendererRegistry.register(ModEntities.SIT, SitEntityRenderer::new);
        HandledScreens.register(ModScreenHandlers.SACK, SackScreen::new);
    }
}

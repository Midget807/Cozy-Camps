package net.midget807.cozycamps;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.midget807.cozycamps.entity.client.SitEntityRenderer;
import net.midget807.cozycamps.registry.ModBlocks;
import net.midget807.cozycamps.registry.ModEntities;
import net.midget807.cozycamps.registry.ModPackets;
import net.midget807.cozycamps.registry.ModScreenHandlers;
import net.midget807.cozycamps.screen.SackScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;

public class CozyCampClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModPackets.registerAllS2C();

        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(),
                ModBlocks.STAKE
        );

        EntityRendererRegistry.register(ModEntities.SIT, SitEntityRenderer::new);
        HandledScreens.register(ModScreenHandlers.SACK, SackScreen::new);
    }
}

package net.midget807.cozycamps;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.midget807.cozycamps.entity.client.SitEntityRenderer;
import net.midget807.cozycamps.registry.ModEntities;

public class CozyCampClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(ModEntities.SIT, SitEntityRenderer::new);
    }
}

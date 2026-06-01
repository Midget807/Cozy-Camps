package net.midget807.cozycamps.screen;

import net.midget807.cozycamps.CozyCampsMain;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class SackScreen extends HandledScreen<SackScreenHandler> {
    public static final Identifier TEXTURE = CozyCampsMain.id("textures/gui/container/sack.png");
    public static final int BACKGROUND_WIDTH = 212;
    public static final int BACKGROUND_HEIGHT = 222;

    public SackScreen(SackScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = BACKGROUND_WIDTH;
        this.backgroundHeight = BACKGROUND_HEIGHT;
        this.playerInventoryTitleY = this.backgroundHeight - 94;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int x = (width - BACKGROUND_WIDTH) / 2;
        int y = (height - BACKGROUND_HEIGHT) / 2;

        context.drawTexture(TEXTURE, x, y, 0, 0, 0, BACKGROUND_WIDTH, BACKGROUND_HEIGHT, 256, 256);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context,mouseX,mouseY);
    }
}

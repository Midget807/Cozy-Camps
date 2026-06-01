package net.midget807.cozycamps.item;

import net.midget807.cozycamps.item.component.SackContentsComponent;
import net.minecraft.item.tooltip.TooltipData;

public record SackTooltipData(SackContentsComponent contents) implements TooltipData {
    public SackTooltipData(SackContentsComponent contents) {
        this.contents = contents;
    }

    public SackContentsComponent getContents() {
        return contents;
    }
}

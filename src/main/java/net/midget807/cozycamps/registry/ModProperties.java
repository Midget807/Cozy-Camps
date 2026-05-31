package net.midget807.cozycamps.registry;

import net.midget807.cozycamps.block.StumpType;
import net.minecraft.state.property.EnumProperty;

public class ModProperties {
    /**A property that specifies the width of a stump.*/
    public static final EnumProperty<StumpType.Size> STUMP_SIZE = EnumProperty.of("stump_size", StumpType.Size.class);
    /**A property that specifies the height of a stump.*/
    public static final EnumProperty<StumpType.Height> STUMP_HEIGHT = EnumProperty.of("stump_height", StumpType.Height.class);
}

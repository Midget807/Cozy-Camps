package net.midget807.cozycamps.registry;

import net.midget807.cozycamps.block.LogBenchType;
import net.midget807.cozycamps.block.StakeType;
import net.midget807.cozycamps.block.StumpType;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;

public class ModProperties {
    /**A property that specifies the width of a stump.*/
    public static final EnumProperty<StumpType.Size> STUMP_SIZE = EnumProperty.of("stump_size", StumpType.Size.class);
    /**A property that specifies the height of a stump.*/
    public static final EnumProperty<StumpType.Height> STUMP_HEIGHT = EnumProperty.of("stump_height", StumpType.Height.class);
    /**A property that specifies the position-type of a log bench.*/
    public static final EnumProperty<LogBenchType.Offset> LOG_BENCH_OFFSET = EnumProperty.of("log_bench_offset", LogBenchType.Offset.class);
    /**A property that specifies the part of a stake.*/
    public static final EnumProperty<StakeType.Part> STAKE_PART = EnumProperty.of("part", StakeType.Part.class);
}

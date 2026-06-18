package net.midget807.cozycamps.registry;

import net.midget807.cozycamps.block.LogBenchType;
import net.midget807.cozycamps.block.StakeType;
import net.midget807.cozycamps.block.StumpType;
import net.minecraft.block.SkullBlock;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Property;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ModProperties {
    /**A property that specifies the width of a stump.*/
    public static final EnumProperty<StumpType.Size> STUMP_SIZE = EnumProperty.of("stump_size", StumpType.Size.class);
    /**A property that specifies the height of a stump.*/
    public static final EnumProperty<StumpType.Height> STUMP_HEIGHT = EnumProperty.of("stump_height", StumpType.Height.class);
    /**A property that specifies the position-type of a log bench.*/
    public static final EnumProperty<LogBenchType.Offset> LOG_BENCH_OFFSET = EnumProperty.of("log_bench_offset", LogBenchType.Offset.class);
    /**A property that specifies the part of a stake.*/
    public static final EnumProperty<StakeType.Part> STAKE_PART = EnumProperty.of("part", StakeType.Part.class);
    /**A property that specifies the type of skull that is on a stake.
     * Differs from {@link net.minecraft.block.SkullBlock.SkullType} as it contains {@link net.midget807.cozycamps.block.StakeType.SkullType#RANDOM} which serves as a null substitute.*/
    public static final EnumProperty<StakeType.SkullType> STAKE_SKULL_TYPE = EnumProperty.of("stake_skull_type", StakeType.SkullType.class);
    /**A property that specifies the type of skull that is on a stake.*/
    public static final EnumProperty<SkullBlock.Type> SKULL_TYPE = EnumProperty.of("stake_skull_type", SkullBlock.Type.class);
}

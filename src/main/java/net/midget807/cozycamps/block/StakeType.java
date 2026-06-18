package net.midget807.cozycamps.block;

import net.minecraft.block.SkullBlock;
import net.minecraft.util.StringIdentifiable;

public class StakeType {
    public static enum Part implements StringIdentifiable {
        BASE("base"),
        TOP("top"),
        POINT("point"),
        HEAD("head"),
        COAL("coal");

        private final String name;

        private Part(final String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }

        @Override
        public String asString() {
            return name;
        }
    }
    public static enum SkullType implements SkullBlock.SkullType {
        RANDOM("random"),
        SKELETON("skeleton"),
        WITHER_SKELETON("wither_skeleton"),
        PLAYER("player"),
        ZOMBIE("zombie"),
        CREEPER("creeper"),
        PIGLIN("piglin"),
        DRAGON("dragon");

        public final String name;

        private SkullType(final String name) {
            this.name = name;
            TYPES.put(name, this);
        }

        @Override
        public String asString() {
            return this.name;
        }
    }
}

package net.midget807.cozycamps.block;

import net.minecraft.util.StringIdentifiable;

public class StakeType {
    public static enum Part implements StringIdentifiable {
        BASE("base"),
        TOP("top"),
        POINT("point"),
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
}

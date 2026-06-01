package net.midget807.cozycamps.block;

import net.minecraft.util.StringIdentifiable;

public class LogBenchType {
    public static enum Offset implements StringIdentifiable {
        CENTER("center"),
        EDGE("edge"),;

        private final String name;

        Offset(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }

        @Override
        public String asString() {
            return this.name;
        }
    }
}

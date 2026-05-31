package net.midget807.cozycamps.block;

import net.minecraft.util.StringIdentifiable;

public class StumpType {
    public enum Size implements StringIdentifiable {
        SMALL("small"),
        MEDIUM("medium"),
        LARGE("large");

        private final String name;

        private Size(String name) {
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
    public enum Height implements StringIdentifiable {
        SHORT("short"),
        TALL("tall");

        private final String name;

        private Height(final String name) {
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

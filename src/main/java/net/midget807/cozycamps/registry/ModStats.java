package net.midget807.cozycamps.registry;

import net.midget807.cozycamps.CozyCampsMain;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;

public class ModStats {
    public static final Identifier OPEN_SACK = register("open_sack", StatFormatter.DEFAULT);

    private static Identifier register(String name, StatFormatter formatter) {
        Identifier identifier = CozyCampsMain.id(name);
        Registry.register(Registries.CUSTOM_STAT, name, identifier);
        Stats.CUSTOM.getOrCreateStat(identifier, formatter);
        return identifier;
    }

    public static void registerModStats() {
        CozyCampsMain.loggerRegistry("Stats");
    }
}

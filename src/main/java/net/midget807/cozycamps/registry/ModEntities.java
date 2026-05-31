package net.midget807.cozycamps.registry;

import net.midget807.cozycamps.CozyCampsMain;
import net.midget807.cozycamps.entity.SitEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModEntities {
    public static final EntityType<SitEntity> SIT = register("sit", EntityType.Builder.<SitEntity>create(SitEntity::new, SpawnGroup.MISC).dimensions(0.0001f, 0.0001f));

    private static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> builder) {
        return Registry.register(Registries.ENTITY_TYPE, CozyCampsMain.id(name), builder.build());
    }

    public static void registerModEntities() {
        CozyCampsMain.loggerRegistry("Entities");
    }
}

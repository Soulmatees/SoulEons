package net.soulmate.rpg_soul.util;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

import java.util.Map;
import java.util.Set;

public interface BiomeSourceAccessor {
    Map<ResourceKey<Biome>, Holder<Biome>> getResourceKeyMap();
    void setResourceKeyMap(Map<ResourceKey<Biome>, Holder<Biome>> map);
    void expandBiomesWith(Set<Holder<Biome>> newGenBiomes);
}
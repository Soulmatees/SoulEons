package net.soulmate.rpg_soul.util;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public interface MultiNoiseBiomeSourceAccessor {
    void setLastSampledSeed(long seed);
    void setLastSampledDimension(ResourceKey<Level> dimension);
}
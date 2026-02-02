package net.soulmate.rpg_soul.mixin;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.soulmate.rpg_soul.util.BiomeSourceAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Mixin(BiomeSource.class)
public abstract class BiomeSourceMixin implements BiomeSourceAccessor {
    @Shadow public abstract Set<Holder<Biome>> possibleBiomes();

    @Unique
    private Map<ResourceKey<Biome>, Holder<Biome>> rpg_soul$cache;

    @Override
    public Map<ResourceKey<Biome>, Holder<Biome>> getResourceKeyMap() {
        if (rpg_soul$cache == null) {
            rpg_soul$cache = new HashMap<>();
            try {
                for (Holder<Biome> holder : this.possibleBiomes()) {
                    holder.unwrapKey().ifPresent(key -> rpg_soul$cache.put(key, holder));
                }
            } catch (Exception e) {
                return new HashMap<>();
            }
        }
        return rpg_soul$cache;
    }

    @Override
    public void setResourceKeyMap(Map<ResourceKey<Biome>, Holder<Biome>> map) {
        if (rpg_soul$cache == null) rpg_soul$cache = new HashMap<>();
        rpg_soul$cache.putAll(map);
    }
}
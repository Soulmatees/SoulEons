package net.soulmate.rpg_soul.worldgen.biome;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.soulmate.rpg_soul.worldgen.noise.VoronoiGenerator;

import java.util.HashMap;
import java.util.Map;

public class BiomeGenerationConfig {
    public static final Map<ResourceKey<Biome>, BiomeGenerationNoiseCondition> BIOMES = new HashMap<>();

    static {
        BIOMES.put(ModBiomes.RINGING_DEPTHS, new BiomeGenerationNoiseCondition(0,
                (x, y, z, depth, sampler, dimension, voronoi) -> {
                    // Можно добавить доп. условия, например только в Overworld
                    return dimension == Level.OVERWORLD && y < 0;
                }
        ));
    }
    public static class BiomeGenerationNoiseCondition {
        private final int rarityOffset;
        private final Condition condition;

        public BiomeGenerationNoiseCondition(int rarityOffset, Condition condition) {
            this.rarityOffset = rarityOffset;
            this.condition = condition;
        }

        public int getRarityOffset() { return rarityOffset; }

        public boolean test(int x, int y, int z, float depth, Climate.Sampler sampler, ResourceKey<Level> dim, VoronoiGenerator.VoronoiInfo info) {
            return condition.test(x, y, z, depth, sampler, dim, info);
        }
    }

    @FunctionalInterface
    public interface Condition {
        boolean test(int x, int y, int z, float depth, Climate.Sampler sampler, ResourceKey<Level> dim, VoronoiGenerator.VoronoiInfo info);
    }
}
package net.soulmate.rpg_soul.mixin;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.soulmate.rpg_soul.worldgen.biome.SEBiomeRarity;
import net.soulmate.rpg_soul.util.BiomeSourceAccessor;
import net.soulmate.rpg_soul.util.MultiNoiseBiomeSourceAccessor;
import net.soulmate.rpg_soul.worldgen.biome.BiomeGenerationConfig;
import net.soulmate.rpg_soul.worldgen.noise.VoronoiGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(value = MultiNoiseBiomeSource.class, priority = -69420)
public abstract class MultiNoiseBiomeSourceMixin implements MultiNoiseBiomeSourceAccessor {

    @Unique
    private long rpg_soul$lastSampledWorldSeed;
    @Unique
    private ResourceKey<Level> rpg_soul$lastSampledDimension;

    @Inject(at = @At("HEAD"),
            method = "getNoiseBiome(IIILnet/minecraft/world/level/biome/Climate$Sampler;)Lnet/minecraft/core/Holder;",
            cancellable = true,
            remap = true)
    private void ac_getNoiseBiomeCoords(int x, int y, int z, Climate.Sampler sampler, CallbackInfoReturnable<Holder<Biome>> cir) {
        if (rpg_soul$lastSampledDimension == null) return;

        VoronoiGenerator.VoronoiInfo voronoiInfo = SEBiomeRarity.getRareBiomeInfoForQuad(rpg_soul$lastSampledWorldSeed, x, z);

        if (voronoiInfo != null) {
            float unquantizedDepth = Climate.unquantizeCoord(sampler.sample(x, y, z).depth());
            int foundRarityOffset = SEBiomeRarity.getRareBiomeOffsetId(voronoiInfo);

            for (Map.Entry<ResourceKey<Biome>, BiomeGenerationConfig.BiomeGenerationNoiseCondition> entry : BiomeGenerationConfig.BIOMES.entrySet()) {
                if (foundRarityOffset == entry.getValue().getRarityOffset() &&
                        entry.getValue().test(x, y, z, unquantizedDepth, sampler, rpg_soul$lastSampledDimension, voronoiInfo)) {

                    var map = ((BiomeSourceAccessor)this).getResourceKeyMap();
                    if (map != null && map.containsKey(entry.getKey())) {
                        cir.setReturnValue(map.get(entry.getKey()));
                    }
                }
            }
        }
    }

    @Override
    public void setLastSampledSeed(long seed) {
        this.rpg_soul$lastSampledWorldSeed = seed;
    }

    @Override
    public void setLastSampledDimension(ResourceKey<Level> dimension) {
        this.rpg_soul$lastSampledDimension = dimension;
    }
}
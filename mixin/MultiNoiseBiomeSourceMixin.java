package net.soulmate.rpg_soul.mixin;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.soulmate.rpg_soul.worldgen.biome.ModBiomes;
import net.soulmate.rpg_soul.worldgen.biome.ModBiomeRarity;
import net.soulmate.rpg_soul.util.BiomeSourceAccessor;
import net.soulmate.rpg_soul.util.MultiNoiseBiomeSourceAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = MultiNoiseBiomeSource.class, priority = -69420)
public class MultiNoiseBiomeSourceMixin implements MultiNoiseBiomeSourceAccessor {
    private long lastSampledWorldSeed;
    private ResourceKey<Level> lastSampledDimension;
    @Inject(at = @At("HEAD"), method = "getNoiseBiome", cancellable = true)
    private void rpg_soul$getNoiseBiomeCoords(int x, int y, int z, Climate.Sampler sampler, CallbackInfoReturnable<Holder<Biome>> cir) {
        // Проверяем, что мы в Обычном мире (Overworld)
        if (lastSampledDimension == Level.OVERWORLD) {
            // Используем Voronoi из Citadel
            if (ModBiomeRarity.isRingingDepthsRegion(lastSampledWorldSeed, x, z)) {

                // Проверяем глубину. В Minecraft 1.20+ "depth" (Continentalness)
                // глубоко под землей уходит в высокие значения.
                float depth = Climate.unquantizeCoord(sampler.sample(x, y, z).depth());

                // 0.45F - порог вхождения. Чем выше число, тем глубже и меньше пещера.
                if (depth > 0.45F) {
                    Holder<Biome> holder = ((BiomeSourceAccessor)this).getResourceKeyMap().get(ModBiomes.RINGING_DEPTHS);
                    if (holder != null) {
                        cir.setReturnValue(holder);
                    }
                }
            }
        }
    }

    @Override
    public void setLastSampledSeed(long seed) { this.lastSampledWorldSeed = seed; }

    public void setLastSampledDimension(ResourceKey<Level> dimension) { this.lastSampledDimension = dimension; }
}
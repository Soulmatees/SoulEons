package net.soulmate.rpg_soul.mixin;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.soulmate.rpg_soul.worldgen.biome.ModBiomes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(ChunkGenerator.class)
public abstract class ChunkGeneratorMixin {
    @Shadow public abstract BiomeSource getBiomeSource();

    @Inject(method = "createBiomes", at = @At("HEAD"))
    private void rpg_soul$initializeBiomeSource(Executor pExecutor, RandomState pRandomState, Blender pBlender, StructureManager pStructureManager, ChunkAccess pChunk, CallbackInfoReturnable<CompletableFuture<ChunkAccess>> cir) {
        BiomeSource source = this.getBiomeSource();

        if (source instanceof net.soulmate.rpg_soul.util.BiomeSourceAccessor sa) {
            var registry = pStructureManager.registryAccess().registryOrThrow(Registries.BIOME);
            var holder = registry.getHolder(ModBiomes.RINGING_DEPTHS);

            holder.ifPresent(biomeHolder -> {
                sa.expandBiomesWith(Set.of(biomeHolder));
                sa.getResourceKeyMap();
            });
        }

        if (source instanceof net.soulmate.rpg_soul.util.MultiNoiseBiomeSourceAccessor accessor) {
            accessor.setLastSampledSeed(pRandomState.sampler().hashCode());
            accessor.setLastSampledDimension(Level.OVERWORLD);
        }
    }
}
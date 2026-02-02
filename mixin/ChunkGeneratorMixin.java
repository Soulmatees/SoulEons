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
import net.soulmate.rpg_soul.RPG_Soul;
import net.soulmate.rpg_soul.util.MultiNoiseBiomeSourceAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(ChunkGenerator.class)
public abstract class ChunkGeneratorMixin {
    @Shadow public abstract BiomeSource getBiomeSource();

    @Inject(method = "createBiomes", at = @At("HEAD"))
    private void rpg_soul$initializeBiomeSource(Executor pExecutor, RandomState pRandomState, Blender pBlender, StructureManager pStructureManager, ChunkAccess pChunk, CallbackInfoReturnable<CompletableFuture<ChunkAccess>> cir) {
        BiomeSource source = this.getBiomeSource();

        if (source instanceof MultiNoiseBiomeSourceAccessor accessor) {
            long worldSeed = pRandomState.sampler().hashCode();
            accessor.setLastSampledSeed(worldSeed);
            accessor.setLastSampledDimension(Level.OVERWORLD);
        }

        if (source instanceof net.soulmate.rpg_soul.util.BiomeSourceAccessor sa) {
            sa.getResourceKeyMap();
        }
    }
}
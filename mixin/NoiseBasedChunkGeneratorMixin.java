package net.soulmate.rpg_soul.mixin;

import net.minecraft.core.Holder;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.soulmate.rpg_soul.util.MultiNoiseBiomeSourceAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(NoiseBasedChunkGenerator.class)
public abstract class NoiseBasedChunkGeneratorMixin extends ChunkGenerator {

    public NoiseBasedChunkGeneratorMixin(net.minecraft.world.level.biome.BiomeSource p_223032_) {
        super(p_223032_);
    }

    @Inject(method = "createBiomes", at = @At("HEAD"))
    private void rpg_soul$passDataToBiomeSource(Executor executor, RandomState randomState, Blender blender, StructureManager structureManager, ChunkAccess chunkAccess, CallbackInfoReturnable<CompletableFuture<ChunkAccess>> cir) {
        if (this.biomeSource instanceof MultiNoiseBiomeSourceAccessor accessor) {
            accessor.setLastSampledSeed(0L);
        }
    }
}
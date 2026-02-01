package net.soulmate.rpg_soul.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.soulmate.rpg_soul.util.MultiNoiseBiomeSourceAccessor;
import net.soulmate.rpg_soul.worldgen.biome.ModBiomeRarity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin {
    @Shadow
    public abstract long getSeed();

    @Inject(method = "<init>", at = @At("TAIL"))
    private void rpg_soul$initEverything(CallbackInfo ci) {
        ServerLevel level = (ServerLevel) (Object) this;
        ModBiomeRarity.init(level.getSeed());
        ChunkGenerator generator = level.getChunkSource().getGenerator();
        if (generator.getBiomeSource() instanceof MultiNoiseBiomeSourceAccessor accessor) {
            accessor.setLastSampledDimension(level.dimension());
            accessor.setLastSampledSeed(level.getSeed());
        }
    }
}
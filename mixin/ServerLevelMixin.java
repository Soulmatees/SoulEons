package net.soulmate.rpg_soul.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.soulmate.rpg_soul.util.MultiNoiseBiomeSourceAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {
    @Inject(method = "<init>", at = @At("TAIL"))
    private void rpg_soul$passDimensionToBiomeSource(CallbackInfo ci) {
        ServerLevel level = (ServerLevel) (Object) this;
        ChunkGenerator generator = level.getChunkSource().getGenerator();
        if (generator.getBiomeSource() instanceof MultiNoiseBiomeSourceAccessor accessor) {
            accessor.setLastSampledDimension(level.dimension());
            accessor.setLastSampledSeed(level.getSeed());
        }
    }
}
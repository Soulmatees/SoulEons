package net.soulmate.rpg_soul.mixin;

import net.minecraft.core.HolderGetter;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.NoiseRouter;
import net.minecraft.world.level.levelgen.NoiseRouterData;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.soulmate.rpg_soul.worldgen.ModDensityFunctions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NoiseRouterData.class)
public class NoiseRouterDataMixin {
    @Inject(method = "overworld", at = @At("RETURN"), cancellable = true)
    private static void rpg_soul$injectCustomCaves(HolderGetter<DensityFunction> df, HolderGetter<NormalNoise.NoiseParameters> np, boolean p_256193_, boolean p_256561_, CallbackInfoReturnable<NoiseRouter> cir) {
        NoiseRouter original = cir.getReturnValue();

        // ОШИБКА БЫЛА ТУТ: df.getOrThrow возвращает Holder.Reference
        // Нам нужно превратить Holder в DensityFunction через DensityFunctions.holder()
        DensityFunction customCave = new DensityFunctions.HolderHolder(df.getOrThrow(ModDensityFunctions.RINGING_CAVE_SHAPE));

        // Теперь каст будет успешным, так как DensityFunctions.holder() реализует интерфейс DensityFunction
        DensityFunction modifiedFinal = DensityFunctions.min(original.finalDensity(), customCave);

        NoiseRouter newRouter = new NoiseRouter(
                original.barrierNoise(), original.fluidLevelFloodednessNoise(),
                original.fluidLevelSpreadNoise(), original.lavaNoise(),
                original.veinToggle(), original.veinRidged(), original.veinGap(),
                original.erosion(), original.depth(), original.ridges(),
                original.initialDensityWithoutJaggedness(),
                modifiedFinal,
                original.temperature(), original.continents(), original.vegetation()
        );

        cir.setReturnValue(newRouter);
    }
}
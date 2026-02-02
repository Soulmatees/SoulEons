package net.soulmate.rpg_soul.util;

import net.minecraft.core.HolderGetter;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.NoiseRouter;
import net.minecraft.world.level.levelgen.NoiseRouterData;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

public class NoiseRouterAccess extends NoiseRouterData {
    public static NoiseRouter getOverworld(HolderGetter<DensityFunction> df, HolderGetter<NormalNoise.NoiseParameters> np) {
        return NoiseRouterData.overworld(df, np, false, false);
    }
}
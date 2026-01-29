package net.soulmate.rpg_soul.worldgen;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.soulmate.rpg_soul.RPG_Soul;

public class ModDensityFunctions {

    public static final ResourceKey<DensityFunction> RINGING =
            ResourceKey.create(Registries.DENSITY_FUNCTION,
                    ResourceLocation.fromNamespaceAndPath(RPG_Soul.MOD_ID, "ringing_density"));

    public static void bootstrap(BootstapContext<DensityFunction> ctx) {
        var noiseGetter = ctx.lookup(Registries.NOISE);
        var noiseHolder = noiseGetter.getOrThrow(ModNoises.RINGING_CAVES);
        DensityFunction noise =
                DensityFunctions.noise(noiseHolder, 1.3, 1.0);
        DensityFunction caves =
                DensityFunctions.add(noise, DensityFunctions.constant(-0.4));
        ctx.register(RINGING, caves);
    }
}

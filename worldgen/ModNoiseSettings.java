package net.soulmate.rpg_soul.worldgen;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.OverworldBiomeBuilder;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.soulmate.rpg_soul.RPG_Soul;
import net.soulmate.rpg_soul.util.NoiseRouterAccess; // Не забудь импорт!
import net.soulmate.rpg_soul.worldgen.noise.ModNoises;

public class ModNoiseSettings {
    public static final ResourceKey<NoiseGeneratorSettings> RINGING_DEPTHS =
            ResourceKey.create(Registries.NOISE_SETTINGS, ResourceLocation.fromNamespaceAndPath(RPG_Soul.MOD_ID, "ringing_depths"));

    public static void bootstrap(BootstapContext<NoiseGeneratorSettings> context) {
        HolderGetter<DensityFunction> df = context.lookup(Registries.DENSITY_FUNCTION);
        HolderGetter<NormalNoise.NoiseParameters> np = context.lookup(Registries.NOISE);
        NoiseRouter vanilla = NoiseRouterAccess.getOverworld(df, np);


        DensityFunction localNoise = DensityFunctions.noise(np.getOrThrow(ModNoises.RINGING_CAVES), 0.002, 0.002);
        DensityFunction localGradient = DensityFunctions.yClampedGradient(-20, 80, 1.0, 0.0);


        DensityFunction myExtremeCave = DensityFunctions.mul(
                DensityFunctions.mul(localNoise, localGradient),
                DensityFunctions.constant(-150.0)
        );

        DensityFunction customFinal = DensityFunctions.min(myExtremeCave, vanilla.finalDensity());

        NoiseRouter customRouter = new NoiseRouter(
                vanilla.barrierNoise(),
                vanilla.fluidLevelFloodednessNoise(),
                vanilla.fluidLevelSpreadNoise(),
                vanilla.lavaNoise(),
                vanilla.veinToggle(),
                vanilla.veinRidged(),
                vanilla.veinGap(),
                vanilla.erosion(),
                vanilla.depth(),
                vanilla.ridges(),
                vanilla.initialDensityWithoutJaggedness(),
                customFinal, // Наша подмена
                vanilla.temperature(),
                vanilla.continents(),
                vanilla.vegetation()
        );


        context.register(RINGING_DEPTHS, new NoiseGeneratorSettings(
                NoiseSettings.create(-64, 384, 1, 2),
                Blocks.STONE.defaultBlockState(),
                Blocks.WATER.defaultBlockState(),
                customRouter,
                ModSurfaceRules.makeRules(),
                new OverworldBiomeBuilder().spawnTarget(),
                63,
                false,
                true,
                true,
                false
        ));
    }
}
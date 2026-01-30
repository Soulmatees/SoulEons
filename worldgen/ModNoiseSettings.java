package net.soulmate.rpg_soul.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.*;
import net.soulmate.rpg_soul.RPG_Soul;

public class ModNoiseSettings {

    public static final ResourceKey<NoiseGeneratorSettings> RPG_OVERWORLD =
            ResourceKey.create(
                    Registries.NOISE_SETTINGS,
                    ResourceLocation.fromNamespaceAndPath(RPG_Soul.MOD_ID, "rpg_overworld")
            );

    public static void bootstrap(BootstapContext<NoiseGeneratorSettings> ctx) {

        var densityFunctions = ctx.lookup(Registries.DENSITY_FUNCTION);
        NoiseGeneratorSettings baseSettings = NoiseGeneratorSettings.overworld(ctx, false, false);

        var ringingFunction = densityFunctions.getOrThrow(ModDensityFunctions.RINGING);

        NoiseRouter vanillaRouter = baseSettings.noiseRouter();

        NoiseRouter rpgRouter = new NoiseRouter(
                vanillaRouter.barrierNoise(),
                vanillaRouter.fluidLevelFloodednessNoise(),
                vanillaRouter.fluidLevelSpreadNoise(),
                vanillaRouter.lavaNoise(),
                vanillaRouter.temperature(),
                vanillaRouter.vegetation(),
                vanillaRouter.continents(),
                vanillaRouter.erosion(),
                vanillaRouter.depth(),
                vanillaRouter.ridges(),
                vanillaRouter.initialDensityWithoutJaggedness(),
                DensityFunctions.add(vanillaRouter.finalDensity(), new DensityFunctions.HolderHolder(ringingFunction)),
                vanillaRouter.veinToggle(),
                vanillaRouter.veinRidged(),
                vanillaRouter.veinGap()
        );

        NoiseGeneratorSettings rpgSettings = new NoiseGeneratorSettings(
                baseSettings.noiseSettings(),
                baseSettings.defaultBlock(),
                baseSettings.defaultFluid(),
                rpgRouter,
                ModSurfaceRules.makeRules(),
                baseSettings.spawnTarget(),
                baseSettings.seaLevel(),
                baseSettings.disableMobGeneration(),
                baseSettings.aquifersEnabled(),
                baseSettings.oreVeinsEnabled(),
                baseSettings.useLegacyRandomSource()
        );

        ctx.register(RPG_OVERWORLD, rpgSettings);
    }
}

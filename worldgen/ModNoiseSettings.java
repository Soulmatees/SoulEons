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
        NoiseGeneratorSettings baseSettings = NoiseGeneratorSettings.overworld(ctx, false, false);

        // 2. Создаем новые настройки на основе ванильных, но вставляем свои SurfaceRules
        NoiseGeneratorSettings rpgSettings = new NoiseGeneratorSettings(
                baseSettings.noiseSettings(),
                baseSettings.defaultBlock(),
                baseSettings.defaultFluid(),
                baseSettings.noiseRouter(),
                ModSurfaceRules.makeRules(), // Твои правила с Resolith и Sparkling Sand
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

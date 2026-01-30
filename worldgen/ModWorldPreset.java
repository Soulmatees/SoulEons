package net.soulmate.rpg_soul.worldgen;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import net.soulmate.rpg_soul.RPG_Soul;
import net.soulmate.rpg_soul.worldgen.biome.ModBiomes;

import java.util.List;
import java.util.Map;

public class ModWorldPreset {
    public static final ResourceKey<WorldPreset> RPG = ResourceKey.create(Registries.WORLD_PRESET,
            ResourceLocation.fromNamespaceAndPath(RPG_Soul.MOD_ID, "rpg"));

    public static void bootstrap(BootstapContext<WorldPreset> ctx) {
        var biomes = ctx.lookup(Registries.BIOME);
        var noiseSettings = ctx.lookup(Registries.NOISE_SETTINGS);
        var dimTypes = ctx.lookup(Registries.DIMENSION_TYPE);

        // ВРУЧНУЮ создаем список биомов для нашего мира
        // Здесь ты можешь добавить сколько угодно биомов через List.of
        Climate.ParameterList<Holder<Biome>> parameterList = new Climate.ParameterList<>(List.of(
                Pair.of(Climate.parameters(
                                Climate.Parameter.span(-1.0F, 1.0F), // Любая температура
                                Climate.Parameter.span(-1.0F, 1.0F), // Любая влажность
                                Climate.Parameter.span(-1.0F, 1.0F), // Любая континентальность
                                Climate.Parameter.span(-1.0F, 1.0F), // Любая эрозия
                                Climate.Parameter.span(-1.0F, 1.0F), // Любая глубина
                                Climate.Parameter.span(-1.0F, 1.0F), // Любая странность
                                0.0F),
                        biomes.getOrThrow(ModBiomes.RINGING_DEPTHS))
        ));

        // Создаем генератор напрямую из списка
        NoiseBasedChunkGenerator rpgGenerator = new NoiseBasedChunkGenerator(
                MultiNoiseBiomeSource.createFromList(parameterList),
                noiseSettings.getOrThrow(ModNoiseSettings.RPG_OVERWORLD)
        );

        ctx.register(RPG, new WorldPreset(Map.of(
                LevelStem.OVERWORLD, new LevelStem(dimTypes.getOrThrow(BuiltinDimensionTypes.OVERWORLD), rpgGenerator)
        )));
    }
}
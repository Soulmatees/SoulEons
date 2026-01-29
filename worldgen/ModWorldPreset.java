package net.soulmate.rpg_soul.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterLists;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import net.soulmate.rpg_soul.RPG_Soul;

import java.util.Map;

public class ModWorldPreset {

    public static final ResourceKey<WorldPreset> RPG =
            ResourceKey.create(
                    Registries.WORLD_PRESET,
                    ResourceLocation.fromNamespaceAndPath(RPG_Soul.MOD_ID, "rpg")
            );

    public static void bootstrap(BootstapContext<WorldPreset> ctx) {

        ctx.register(RPG,
                new WorldPreset(
                        Map.of(
                                LevelStem.OVERWORLD,
                                new LevelStem(
                                        ctx.lookup(Registries.DIMENSION_TYPE)
                                                .getOrThrow(BuiltinDimensionTypes.OVERWORLD),

                                        new NoiseBasedChunkGenerator(
                                                MultiNoiseBiomeSource.createFromPreset(
                                                        ctx.lookup(Registries.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST)
                                                                .getOrThrow(MultiNoiseBiomeSourceParameterLists.OVERWORLD)
                                                ),
                                                ctx.lookup(Registries.NOISE_SETTINGS)
                                                        .getOrThrow(ModNoiseSettings.RPG_OVERWORLD)
                                        )
                                )
                        )
                )
        );
    }
}

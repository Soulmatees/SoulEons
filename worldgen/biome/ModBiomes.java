package net.soulmate.rpg_soul.worldgen.biome;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.soulmate.rpg_soul.RPG_Soul;

public class ModBiomes {
    public static final ResourceKey<Biome> RINGING_DEPTHS =
            ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(RPG_Soul.MOD_ID, "ringing_depths"));

    public static void bootstrap(BootstapContext<Biome> context) {
        HolderGetter<PlacedFeature> features = context.lookup(Registries.PLACED_FEATURE);
        BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(features,
                context.lookup(Registries.CONFIGURED_CARVER));
        context.register(RINGING_DEPTHS, new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .temperature(0.5f)
                .downfall(0f)
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .fogColor(0x0c0c84)
                        .waterColor(0x3f76e4)
                        .waterFogColor(0x082533)
                        .skyColor(0)
                        .ambientParticle(new AmbientParticleSettings(
                                net.minecraft.core.particles.ParticleTypes.UNDERWATER, 0.025f))
                        .build())
                .mobSpawnSettings(new MobSpawnSettings.Builder().build())
                .generationSettings(generation.build())
                .build());
    }
}
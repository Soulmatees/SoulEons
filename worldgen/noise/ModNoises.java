package net.soulmate.rpg_soul.worldgen.noise;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.soulmate.rpg_soul.RPG_Soul;

public class ModNoises {
    public static final ResourceKey<NormalNoise.NoiseParameters> SPARKLING_PATCHES =
            ResourceKey.create(Registries.NOISE, ResourceLocation.fromNamespaceAndPath(RPG_Soul.MOD_ID, "sparkling_patches"));
    public static final ResourceKey<NormalNoise.NoiseParameters> HARDENED_SAND =
            ResourceKey.create(Registries.NOISE, ResourceLocation.fromNamespaceAndPath(RPG_Soul.MOD_ID, "hardened_sand"));
    public static final ResourceKey<NormalNoise.NoiseParameters> ECHO_VEINS =
            ResourceKey.create(Registries.NOISE, ResourceLocation.fromNamespaceAndPath(RPG_Soul.MOD_ID, "echo_veins"));
    public static final ResourceKey<NormalNoise.NoiseParameters> RINGING_CAVES =
            ResourceKey.create(Registries.NOISE, ResourceLocation.fromNamespaceAndPath(RPG_Soul.MOD_ID, "ringing_caves"));
    public static void bootstrap(BootstapContext<NormalNoise.NoiseParameters> ctx) {
        ctx.register(SPARKLING_PATCHES, new NormalNoise.NoiseParameters(-4, 1.0));
        ctx.register(HARDENED_SAND, new NormalNoise.NoiseParameters(-3, 1.0));
        ctx.register(ECHO_VEINS, new NormalNoise.NoiseParameters(-5, 1.0));
        ctx.register(RINGING_CAVES, new NormalNoise.NoiseParameters(-8, 1.0, 1.0));
    }
}
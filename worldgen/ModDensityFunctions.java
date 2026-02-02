package net.soulmate.rpg_soul.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.soulmate.rpg_soul.RPG_Soul;
import net.soulmate.rpg_soul.worldgen.noise.ModNoises;

public class ModDensityFunctions {
    public static final ResourceKey<DensityFunction> RINGING_CAVE_SHAPE =
            ResourceKey.create(Registries.DENSITY_FUNCTION,
                    ResourceLocation.fromNamespaceAndPath(RPG_Soul.MOD_ID, "ringing_cave_shape"));

    public static void bootstrap(BootstapContext<DensityFunction> context) {
        DensityFunction noise = DensityFunctions.noise(
                context.lookup(Registries.NOISE).getOrThrow(ModNoises.RINGING_CAVES),
                0.002D, 0.002D
        );

        DensityFunction verticalGradient = DensityFunctions.yClampedGradient(-20, 80, 1.0, 0.0);

        DensityFunction caverns = DensityFunctions.mul(
                DensityFunctions.mul(noise, verticalGradient),
                DensityFunctions.constant(-20.0D)
        );

        context.register(RINGING_CAVE_SHAPE, caverns);
    }
}

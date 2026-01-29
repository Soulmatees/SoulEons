package net.soulmate.rpg_soul.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext; // Проверь импорт контекста
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.soulmate.rpg_soul.RPG_Soul;

public class ModNoises {
    // Используем NormalNoise.NoiseParameters вместо Noise
    public static final ResourceKey<NormalNoise.NoiseParameters> RINGING_CAVES =
            ResourceKey.create(Registries.NOISE,
                    ResourceLocation.fromNamespaceAndPath(RPG_Soul.MOD_ID, "ringing_caves"));

    public static void bootstrap(BootstapContext<NormalNoise.NoiseParameters> ctx) {
        // Регистрация параметров шума: (первое число - начальная октава, далее - амплитуды)
        ctx.register(RINGING_CAVES,
                new NormalNoise.NoiseParameters(0, 1.0));
    }
}
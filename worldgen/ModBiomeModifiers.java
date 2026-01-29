package net.soulmate.rpg_soul.worldgen;

import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;
import net.soulmate.rpg_soul.RPG_Soul;
import net.soulmate.rpg_soul.worldgen.biome.ModBiomes;

public class ModBiomeModifiers {
    public static final ResourceKey<BiomeModifier> ADD_RINGING_DEPTHS = ResourceKey.create(
            ForgeRegistries.Keys.BIOME_MODIFIERS,
            ResourceLocation.fromNamespaceAndPath(RPG_Soul.MOD_ID, "add_ringing_depths")
    );

    public static void bootstrap(BootstapContext<BiomeModifier> context) {
        var biomes = context.lookup(Registries.BIOME);

        context.register(ADD_RINGING_DEPTHS, new AddBiomesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(ModBiomes.RINGING_DEPTHS)),
                new Climate.ParameterPoint(
                        Climate.Parameter.span(-0.5f, 0.5f), // temp
                        Climate.Parameter.span(-1.0f, -0.5f), // hum
                        Climate.Parameter.span(0.2f, 1.0f),  // continentalness
                        Climate.Parameter.span(-0.1f, 0.1f), // erosion
                        Climate.Parameter.span(0.5f, 1.0f),  // depth (ДЕЛАЕТ ПЕЩЕРНЫМ)
                        Climate.Parameter.span(0.0f, 0.0f),  // weirdness
                        0L                                   // offset
                )
        ));
    }
}
package net.soulmate.rpg_soul.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.carver.WorldCarver; // Важно: WorldCarver вместо Carver
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
import net.soulmate.rpg_soul.RPG_Soul;

public class ModCarvers {

    public static final ResourceKey<ConfiguredWorldCarver<?>> RINGING_CAVES =
            ResourceKey.create(Registries.CONFIGURED_CARVER,
                    ResourceLocation.fromNamespaceAndPath(RPG_Soul.MOD_ID, "ringing_caves"));

    public static void bootstrap(BootstapContext<ConfiguredWorldCarver<?>> ctx) {
        ctx.register(RINGING_CAVES, new ConfiguredWorldCarver<>(
                WorldCarver.CAVE,
                new CaveCarverConfiguration(
                        0.8f,                                              // probability
                        UniformHeight.of(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(0)), // y
                        UniformFloat.of(0.1f, 0.9f),                       // yScale (вертикальный масштаб)
                        VerticalAnchor.aboveBottom(10),                    // lavaLevel
                        // Набор заменяемых блоков (тег)
                        ctx.lookup(Registries.BLOCK).getOrThrow(net.minecraft.tags.BlockTags.OVERWORLD_CARVER_REPLACEABLES),
                        UniformFloat.of(0.7f, 1.4f),                       // horizontalRadiusModifier
                        UniformFloat.of(0.8f, 1.3f),                       // verticalRadiusModifier
                        UniformFloat.of(-1.0f, 0.0f)                       // floorLevel
                )
        ));
    }
}
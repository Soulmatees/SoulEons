package net.soulmate.rpg_soul.worldgen;

import com.github.alexthe666.citadel.server.generation.SurfaceRulesManager;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.soulmate.rpg_soul.RPG_Soul;
import net.soulmate.rpg_soul.block.ModBlocks;
import net.soulmate.rpg_soul.worldgen.biome.ModBiomes;

public class ModSurfaceRules {

    private static final SurfaceRules.ConditionSource IS_RINGING_DEPTHS =
            SurfaceRules.isBiome(ModBiomes.RINGING_DEPTHS);

    public static void setup() {
        SurfaceRulesManager.registerOverworldSurfaceRule(
                IS_RINGING_DEPTHS,
                makeRules()
        );
    }

    public static SurfaceRules.RuleSource makeRules() {
        SurfaceRules.RuleSource resolith =
                SurfaceRules.state(ModBlocks.RESOLITH.get().defaultBlockState());
        SurfaceRules.RuleSource sparklingSand =
                SurfaceRules.state(ModBlocks.SPARKLING_SAND.get().defaultBlockState());
        SurfaceRules.RuleSource hardenedSand =
                SurfaceRules.state(ModBlocks.HARDENED_SPARKLING_SAND.get().defaultBlockState());
        SurfaceRules.RuleSource echoCluster =
                SurfaceRules.state(ModBlocks.ECHO_CLUSTER.get().defaultBlockState());

        SurfaceRules.RuleSource complexSurface = SurfaceRules.sequence(
                SurfaceRules.ifTrue(
                        SurfaceRules.noiseCondition(
                                ResourceKey.create(Registries.NOISE, ResourceLocation.fromNamespaceAndPath(RPG_Soul.MOD_ID, "sparkling_patches")),
                                0.6, 1.0), sparklingSand),
                SurfaceRules.ifTrue(
                        SurfaceRules.noiseCondition(
                                ResourceKey.create(Registries.NOISE, ResourceLocation.fromNamespaceAndPath(RPG_Soul.MOD_ID, "hardened_sand")),
                                0.4, 0.6), hardenedSand),
                SurfaceRules.ifTrue(
                        SurfaceRules.noiseCondition(
                                ResourceKey.create(Registries.NOISE, ResourceLocation.fromNamespaceAndPath(RPG_Soul.MOD_ID, "echo_veins")),
                                0.75, 1.0), echoCluster), resolith);
        return SurfaceRules.sequence(
                SurfaceRules.ifTrue(IS_RINGING_DEPTHS,
                        SurfaceRules.sequence(
                                SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, complexSurface),
                                SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, resolith),
                                resolith
                        )
                )
        );
    }
}
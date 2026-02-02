package net.soulmate.rpg_soul.worldgen.biome;

import net.soulmate.rpg_soul.worldgen.noise.VoronoiGenerator;

public class SEBiomeRarity {
    public static VoronoiGenerator.VoronoiInfo getRareBiomeInfoForQuad(long seed, int x, int z) {
        VoronoiGenerator.VoronoiInfo info = VoronoiGenerator.getRareBiomeInfoForQuad(seed, x, z);
        if (info.distanceSq() < 0.5) {
            return info;
        }
        return null;
    }

    public static int getRareBiomeOffsetId(VoronoiGenerator.VoronoiInfo info) {
        return Math.abs((info.cellX() * 31 + info.cellZ()) % 100);
    }
}
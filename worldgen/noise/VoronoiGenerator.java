package net.soulmate.rpg_soul.worldgen.noise;

import net.minecraft.util.Mth;

public class VoronoiGenerator {

    private static double getOffset(long seed, int x, int z, int offset) {
        long l = Mth.getSeed(x, offset, z) ^ seed;
        return ((double)((l >> 24) & 1023L) / 1024.0D - 0.5D) * 0.75D;
    }

    public static VoronoiInfo getRareBiomeInfoForQuad(long seed, int x, int z) {
        int cellSize = 1000;
        int cellX = Math.floorDiv(x, cellSize);
        int cellZ = Math.floorDiv(z, cellSize);

        double minDist = 1e10;
        int targetX = 0;
        int targetZ = 0;
        for(int i = -1; i <= 1; i++) {
            for(int j = -1; j <= 1; j++) {
                int curX = cellX + i;
                int curZ = cellZ + j;
                double centerX = curX + 0.5 + getOffset(seed, curX, curZ, 3110);
                double centerZ = curZ + 0.5 + getOffset(seed, curX, curZ, 420);

                double dx = centerX - (double)x / cellSize;
                double dz = centerZ - (double)z / cellSize;
                double dist = dx * dx + dz * dz;

                if(dist < minDist) {
                    minDist = dist;
                    targetX = curX;
                    targetZ = curZ;
                }
            }
        }
        return new VoronoiInfo(targetX, targetZ, minDist);
    }

    public record VoronoiInfo(int cellX, int cellZ, double distanceSq) {}
}
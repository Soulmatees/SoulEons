package net.soulmate.rpg_soul.worldgen.biome;

import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.XoroshiroRandomSource;

public class ModBiomeRarity {

    public static boolean isRingingDepthsRegion(long seed, int x, int z) {

        int regionSize = 500;
        int rx = Math.floorDiv(x, regionSize);
        int rz = Math.floorDiv(z, regionSize);
        long regionSeed = Mth.getSeed(rx, 0, rz) ^ seed;
        WorldgenRandom random = new WorldgenRandom(new XoroshiroRandomSource(regionSeed));
        return random.nextFloat() < 0.20F;
    }
}

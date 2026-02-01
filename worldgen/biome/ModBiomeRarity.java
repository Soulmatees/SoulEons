package net.soulmate.rpg_soul.worldgen.biome;

import net.minecraft.world.level.levelgen.XoroshiroRandomSource;
import net.minecraft.world.level.levelgen.synth.PerlinSimplexNoise;
import net.soulmate.rpg_soul.worldgen.noise.VoronoiGenerator;

import java.util.List;

/**
 * Определяет, находится ли (x,z) внутри региона Ringing Depths
 * Использует Voronoi + Noise (стиль Alex's Caves)
 */
public class ModBiomeRarity {

    /* =========================
       НАСТРОЙКИ (ТРОГАЙ ТОЛЬКО ИХ)
       ========================= */

    /** Средний радиус биома (чем больше — тем жирнее зоны) */
    private static final double BIOME_SIZE = 0.28D;

    /** Расстояние между центрами биомов */
    private static final double SEPARATION_DISTANCE = 1.0D;

    /** Насколько шум "ломает" границу */
    private static final double WIDTH_RANDOMNESS = 0.35D;

    /** Масштаб в блоках (1024 = как большие cave biomes) */
    private static final double BLOCK_SCALE = 1024.0D;

    /* ========================= */

    private static final VoronoiGenerator VORONOI = new VoronoiGenerator(42L);
    private static final PerlinSimplexNoise NOISE_X =
            new PerlinSimplexNoise(new XoroshiroRandomSource(1234L), List.of(0));
    private static final PerlinSimplexNoise NOISE_Z =
            new PerlinSimplexNoise(new XoroshiroRandomSource(4321L), List.of(0));
    /**
     * ГЛАВНЫЙ МЕТОД
     * Вызывается из MultiNoiseBiomeSourceMixin
     */
    public static boolean isRingingDepthsRegion(long worldSeed, int x, int z) {
        // Voronoi должен быть привязан к seed мира
        VORONOI.setSeed(worldSeed);
        // Перевод координат блоков → "квадраты биома"
        double sampleX = x / (BLOCK_SCALE * SEPARATION_DISTANCE);
        double sampleZ = z / (BLOCK_SCALE * SEPARATION_DISTANCE);
        // Noise для искажения формы
        double offsetX = WIDTH_RANDOMNESS * NOISE_X.getValue(sampleX, sampleZ, false);
        double offsetZ = WIDTH_RANDOMNESS * NOISE_Z.getValue(sampleX, sampleZ, false);
        // Получаем Voronoi-инфу
        VoronoiGenerator.VoronoiInfo info =
                VORONOI.get2(sampleX + offsetX, sampleZ + offsetZ);
        // Проверяем: точка внутри радиуса биома
        return info.distance() < BIOME_SIZE;
    }
    /**
     * (ОПЦИОНАЛЬНО)
     * Центр текущего биома — может пригодиться позже
     */
    public static double[] getBiomeCenter(long worldSeed, int x, int z) {
        VORONOI.setSeed(worldSeed);
        double sx = x / (BLOCK_SCALE * SEPARATION_DISTANCE);
        double sz = z / (BLOCK_SCALE * SEPARATION_DISTANCE);
        VoronoiGenerator.VoronoiInfo info = VORONOI.get2(sx, sz);
        return new double[]{
                info.cellPos().x() * BLOCK_SCALE,
                info.cellPos().y() * BLOCK_SCALE
        };
    }


    public static void init(long worldSeed) {
        VORONOI.setSeed(worldSeed);
        // Насколько "кривые" границы
        VORONOI.setOffsetAmount(0.35D);
        // Тип расстояния (EUCLIDEAN = круглые биомы)
        VORONOI.setDistanceType(VoronoiGenerator.DistanceType.euclidean);
    }
}
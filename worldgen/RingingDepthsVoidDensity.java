package net.soulmate.rpg_soul.worldgen;

import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

public class RingingDepthsVoidDensity implements DensityFunction {

    @Override
    public double compute(FunctionContext ctx) {
        int y = ctx.blockY();

        if (y > -36 && y < 36) {
            return -1.0; // воздух
        }
        return 1.0; // камень
    }

    @Override
    public void fillArray(double[] pArray, ContextProvider pContextProvider) {

    }
    @Override
    public DensityFunction mapAll(Visitor pVisitor) {
        return null;
    }
    @Override
    public double minValue() { return -1.0; }

    @Override
    public double maxValue() { return 1.0; }
    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return null;
    }
}

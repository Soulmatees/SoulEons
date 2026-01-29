package net.soulmate.rpg_soul.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import net.soulmate.rpg_soul.RPG_Soul;

// Этот класс говорит игре: "Возьми этот биом и эту точку климата"
public record AddBiomesBiomeModifier(HolderSet<Biome> biomes, Climate.ParameterPoint target) implements BiomeModifier {

    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        // Здесь мы ничего не меняем в существующих биомах
    }

    @Override
    public Codec<? extends BiomeModifier> codec() {
        return RPG_Soul.ADD_BIOME_CODEC.get();
    }
}
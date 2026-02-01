package net.soulmate.rpg_soul.datagen;

import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.soulmate.rpg_soul.RPG_Soul;
import net.soulmate.rpg_soul.worldgen.biome.ModBiomes;
import net.soulmate.rpg_soul.worldgen.noise.ModNoises;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModWorldGenProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.NOISE, ModNoises::bootstrap)
            .add(Registries.BIOME, ModBiomes::bootstrap);


    public ModWorldGenProvider(
            PackOutput output,
            CompletableFuture<net.minecraft.core.HolderLookup.Provider> registries
    ) {
        super(output, registries, BUILDER, Set.of(RPG_Soul.MOD_ID, "minecraft"));
    }
}

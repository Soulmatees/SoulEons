package net.soulmate.rpg_soul.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.soulmate.rpg_soul.RPG_Soul;
import net.soulmate.rpg_soul.worldgen.biome.ModBiomes;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public class ModCitadelData implements DataProvider {
    private final PackOutput output;

    public ModCitadelData(PackOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        JsonObject json = new JsonObject();
        JsonArray biomes = new JsonArray();
        JsonObject biomeEntry = new JsonObject();

        biomeEntry.addProperty("biome", ModBiomes.RINGING_DEPTHS.location().toString());

        JsonObject params = new JsonObject();
        params.add("temperature", range(-1.0, 1.0));
        params.add("humidity", range(-1.0, 1.0));
        params.add("continentalness", range(-1.0, 1.0));
        params.add("erosion", range(-1.0, 1.0));
        params.add("weirdness", range(-1.0, 1.0));
        params.add("depth", range(0.6, 1.0)); // ПЕЩЕРЫ
        params.addProperty("offset", 0.0);

        biomeEntry.add("parameters", params);
        biomes.add(biomeEntry);
        json.add("biomes", biomes);

        Path path = output.getOutputFolder().resolve("data/citadel/citadel_expanded_biomes.json");
        return DataProvider.saveStable(cache, json, path);
    }

    private JsonArray range(double min, double max) {
        JsonArray arr = new JsonArray();
        arr.add(min);
        arr.add(max);
        return arr;
    }

    @Override
    public String getName() {
        return "Citadel Biome Data for " + RPG_Soul.MOD_ID;
    }
}
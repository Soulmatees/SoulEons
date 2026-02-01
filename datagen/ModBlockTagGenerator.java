package net.soulmate.rpg_soul.datagen;

import io.netty.util.internal.shaded.org.jctools.queues.MpscArrayQueue;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.common.Mod;
import net.soulmate.rpg_soul.RPG_Soul;
import net.soulmate.rpg_soul.block.ModBlocks;
import net.soulmate.rpg_soul.util.ModTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagGenerator extends BlockTagsProvider {
    public ModBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, RPG_Soul.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        // --- 1. ИНСТРУМЕНТЫ (Mineable) ---

        // Кирка
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.RESONITE_BLOCK.get())
                .add(ModBlocks.RESONITE_INGOT_BLOCK.get())
                .add(ModBlocks.RESOLITH.get())
                .add(ModBlocks.HARDENED_QUARTZITE.get())
                .add(ModBlocks.HARDENED_SPARKLING_SAND.get());

        // Топор (Добавляем твои блоки дерева, чтобы не было пустого массива)
        this.tag(BlockTags.MINEABLE_WITH_AXE);

        // --- 2. УРОВНИ (Tool Tiers) ---
        this.tag(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.RESONITE_INGOT_BLOCK.get());

        this.tag(BlockTags.NEEDS_STONE_TOOL)
                .add(ModBlocks.RESOLITH.get())
                .add(ModBlocks.HARDENED_QUARTZITE.get())
                .add(ModBlocks.HARDENED_SPARKLING_SAND.get())
                .add(ModBlocks.RESONITE_BLOCK.get());

        this.tag(BlockTags.WALLS)
                .add(ModBlocks.RESOLITH_WALL.get())
                .add(ModBlocks.POLISHED_RESOLITH_WALL.get());

        // Машины обычно требуют хотя бы каменную кирку
    }
}

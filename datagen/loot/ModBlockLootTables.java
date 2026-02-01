package net.soulmate.rpg_soul.datagen.loot;

import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;
import net.soulmate.rpg_soul.block.ModBlocks;
import net.soulmate.rpg_soul.item.ModItems;

import java.util.Set;
import java.util.stream.Collectors;

public class ModBlockLootTables extends BlockLootSubProvider {

    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        // --- 1. ОБЫЧНЫЕ БЛОКИ (Drop Self) ---
        this.dropSelf(ModBlocks.RESONITE_BLOCK.get());
        this.dropSelf(ModBlocks.RESONITE_INGOT_BLOCK.get());
        this.dropSelf(ModBlocks.SPARKLING_SAND.get());
        this.dropSelf(ModBlocks.HARDENED_SPARKLING_SAND.get());
        this.dropSelf(ModBlocks.RESOLITH.get());
        this.dropSelf(ModBlocks.HARDENED_QUARTZITE.get());
        this.dropSelf(ModBlocks.BELFRY.get());
        this.dropSelf(ModBlocks.CRYSTAL_CORAL.get());
        this.dropSelf(ModBlocks.CRYSTAL_GRASS.get());

        this.getKnownBlocks().forEach(block -> {
            if (!this.map.containsKey(block.getLootTable())) {
                this.dropSelf(block);
            }
        });
    }

    // Метод для руды (выпадает 2-4 единицы кварца, если нет шелкового касания)
    protected LootTable.Builder createCopperLikeOreDrops(Block pBlock, Item item) {
        return createSilkTouchDispatchTable(pBlock,
                this.applyExplosionDecay(pBlock,
                        LootItem.lootTableItem(item)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F)))
                                .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream()
                .map(RegistryObject::get)
                .collect(Collectors.toList());
    }
}
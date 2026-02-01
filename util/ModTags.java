package net.soulmate.rpg_soul.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.soulmate.rpg_soul.RPG_Soul;

public class ModTags {

    public static class Blocks{

        private static TagKey<Block> tag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(RPG_Soul.MOD_ID, name));

            
        }
    }
    public static class Items{

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(RPG_Soul.MOD_ID, name));

        }
    }
}
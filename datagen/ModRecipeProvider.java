package net.soulmate.rpg_soul.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.registries.RegistryObject;
import net.soulmate.rpg_soul.RPG_Soul;
import net.soulmate.rpg_soul.block.ModBlocks;
import net.soulmate.rpg_soul.item.ModItems;

import java.util.List;
import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
        // --- 1. ПЕСЧАНИК (Sandstone Set) ---
        // Теперь сигнатура метода принимает любые подклассы Block, ошибки исчезнут
        fullBlockSet(pWriter, ModBlocks.RESONITE_BLOCK,
                null, null, null,
                null, null, null,
                null, null, null);

        // --- 2. ДЕРЕВО (Tree Drought Set) ---


        // --- Еда, печь, коптильня, костер

        // --- 3. ПЕРЕПЛАВКА ---
        // Рецепт нагрева железа
        // --- 4. ХРАНЕНИЕ ---
        nineBlockStorageRecipes(pWriter, RecipeCategory.MISC, ModItems.RESONITE.get(), RecipeCategory.BUILDING_BLOCKS, ModBlocks.RESONITE_BLOCK.get());
        nineBlockStorageRecipes(pWriter, RecipeCategory.MISC, ModItems.RESONITE_INGOT.get(), RecipeCategory.BUILDING_BLOCKS, ModBlocks.RESONITE_INGOT_BLOCK.get());

        // --- 5. ИНСТРУМЕНТЫ И БРОНЯ ---


        // --- 6. КАСТОМНЫЕ МЕХАНИКИ ---


        // --- 7. РЕЦЕПТЫ FUSER (DataGen) ---

        // --- 8. ОБЫЧНЫЙ ВЕРСТАК (Shaped Crafting) ---
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.RESONITE_INGOT.get())
                .pattern("AAA") // Верхний ряд: Кварц, Кварц, Кварц
                .pattern("AIA") // Средний ряд: Кварц, Железо, Кварц
                .pattern("AAA") // Нижний ряд: Кварц, Кварц, Кварц
                .define('A', ModItems.RESONITE.get()) // Обозначаем 'A' как Древний Кварц
                .define('I', Items.IRON_INGOT)              // Обозначаем 'I' как Слиток железа
                .unlockedBy("has_resonite", has(ModItems.RESONITE.get())) // Условие открытия в книге рецептов
                .save(pWriter, modId("resonite_ingot_from_crafting"));
    }





    // =========================================================================================
    // === ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ (ИСПРАВЛЕННЫЕ) ===
    // =========================================================================================

    // Использование <? extends Block> позволяет передавать StairBlock, SlabBlock и т.д. без ошибок

    protected static void cookFood(Consumer<FinishedRecipe> pWriter, String name, ItemLike raw, ItemLike cooked, float xp) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(raw), RecipeCategory.FOOD, cooked, xp, 200)
                .unlockedBy(getHasName(raw), has(raw)).save(pWriter, modId(name + "_smelting"));
        SimpleCookingRecipeBuilder.smoking(Ingredient.of(raw), RecipeCategory.FOOD, cooked, xp, 100)
                .unlockedBy(getHasName(raw), has(raw)).save(pWriter, modId(name + "_smoking"));
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(raw), RecipeCategory.FOOD, cooked, xp, 600)
                .unlockedBy(getHasName(raw), has(raw)).save(pWriter, modId(name + "_campfire"));
    }

    protected static void fullBlockSet(Consumer<FinishedRecipe> pWriter, RegistryObject<? extends Block> base,
                                       RegistryObject<? extends Block> stairs, RegistryObject<? extends Block> slab,
                                       RegistryObject<? extends Block> fence, RegistryObject<? extends Block> gate,
                                       RegistryObject<? extends Block> wall, RegistryObject<? extends Block> door,
                                       RegistryObject<? extends Block> trapdoor, RegistryObject<? extends Block> button,
                                       RegistryObject<? extends Block> pressurePlate) {
        Block b = base.get();
        if (stairs != null) stairBuilder(stairs.get(), Ingredient.of(b)).unlockedBy(getHasName(b), has(b)).save(pWriter);
        if (slab != null) slabBuilder(RecipeCategory.BUILDING_BLOCKS, slab.get(), Ingredient.of(b)).unlockedBy(getHasName(b), has(b)).save(pWriter);
        if (fence != null) fenceBuilder(fence.get(), Ingredient.of(b)).unlockedBy(getHasName(b), has(b)).save(pWriter);
        if (gate != null) fenceGateBuilder(gate.get(), Ingredient.of(b)).unlockedBy(getHasName(b), has(b)).save(pWriter);
        if (wall != null) wallBuilder(RecipeCategory.BUILDING_BLOCKS, wall.get(), Ingredient.of(b)).unlockedBy(getHasName(b), has(b)).save(pWriter);
        if (door != null) doorBuilder(door.get(), Ingredient.of(b)).unlockedBy(getHasName(b), has(b)).save(pWriter);
        if (trapdoor != null) trapdoorBuilder(trapdoor.get(), Ingredient.of(b)).unlockedBy(getHasName(b), has(b)).save(pWriter);
        if (button != null) buttonBuilder(button.get(), Ingredient.of(b)).unlockedBy(getHasName(b), has(b)).save(pWriter);
        if (pressurePlate != null) pressurePlateBuilder(RecipeCategory.REDSTONE, pressurePlate.get(), Ingredient.of(b)).unlockedBy(getHasName(b), has(b)).save(pWriter);
    }

    protected static void planksFromLog(Consumer<FinishedRecipe> pWriter, RegistryObject<? extends Block> planks, RegistryObject<? extends Block> log) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, planks.get(), 4)
                .requires(log.get())
                .unlockedBy(getHasName(log.get()), has(log.get()))
                .save(pWriter, modId(getItemName(planks.get()) + "_from_" + getItemName(log.get())));
    }

    protected static void toolRecipe(Consumer<FinishedRecipe> pWriter, RegistryObject<? extends ItemLike> ingot,
                                     RegistryObject<? extends ItemLike> pick, RegistryObject<? extends ItemLike> sword,
                                     RegistryObject<? extends ItemLike> axe, RegistryObject<? extends ItemLike> shovel,
                                     RegistryObject<? extends ItemLike> hoe) {
        ItemLike s = ingot.get();
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, pick.get()).pattern("SSS").pattern(" # ").pattern(" # ").define('S', s).define('#', Items.STICK).unlockedBy(getHasName(s), has(s)).save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, sword.get()).pattern(" S ").pattern(" S ").pattern(" # ").define('S', s).define('#', Items.STICK).unlockedBy(getHasName(s), has(s)).save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, axe.get()).pattern("SS ").pattern("S# ").pattern(" # ").define('S', s).define('#', Items.STICK).unlockedBy(getHasName(s), has(s)).save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, shovel.get()).pattern(" S ").pattern(" # ").pattern(" # ").define('S', s).define('#', Items.STICK).unlockedBy(getHasName(s), has(s)).save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, hoe.get()).pattern("SS ").pattern(" # ").pattern(" # ").define('S', s).define('#', Items.STICK).unlockedBy(getHasName(s), has(s)).save(pWriter);
    }

    protected static void armorRecipe(Consumer<FinishedRecipe> pWriter, RegistryObject<? extends ItemLike> ingot,
                                      RegistryObject<? extends ItemLike> helmet, RegistryObject<? extends ItemLike> chest,
                                      RegistryObject<? extends ItemLike> leggings, RegistryObject<? extends ItemLike> boots) {
        ItemLike s = ingot.get();
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, helmet.get()).pattern("SSS").pattern("S S").define('S', s).unlockedBy(getHasName(s), has(s)).save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, chest.get()).pattern("S S").pattern("SSS").pattern("SSS").define('S', s).unlockedBy(getHasName(s), has(s)).save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, leggings.get()).pattern("SSS").pattern("S S").pattern("S S").define('S', s).unlockedBy(getHasName(s), has(s)).save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, boots.get()).pattern("S S").pattern("S S").define('S', s).unlockedBy(getHasName(s), has(s)).save(pWriter);
    }

    private static ResourceLocation modId(String path) {
        return ResourceLocation.fromNamespaceAndPath(RPG_Soul.MOD_ID, path);
    }
}
package net.soulmate.rpg_soul.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimMaterials;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.soulmate.rpg_soul.RPG_Soul;
import net.soulmate.rpg_soul.block.ModBlocks;
import net.soulmate.rpg_soul.item.ModItems;

import java.util.LinkedHashMap;

public class ModItemModelProvider extends ItemModelProvider {
    //region TRIM MATERIALS SETUP
    private static LinkedHashMap<ResourceKey<TrimMaterial>, Float> trinMaterisals = new LinkedHashMap<>();
    static  {
        trinMaterisals.put(TrimMaterials.QUARTZ, 0.1f);
        trinMaterisals.put(TrimMaterials.IRON, 0.2f);
        trinMaterisals.put(TrimMaterials.NETHERITE, 0.3f);
        trinMaterisals.put(TrimMaterials.REDSTONE, 0.4f);
        trinMaterisals.put(TrimMaterials.COPPER, 0.5f);
        trinMaterisals.put(TrimMaterials.GOLD, 0.6f);
        trinMaterisals.put(TrimMaterials.EMERALD, 0.7f);
        trinMaterisals.put(TrimMaterials.DIAMOND, 0.8f);
        trinMaterisals.put(TrimMaterials.LAPIS, 0.9f);
        trinMaterisals.put(TrimMaterials.AMETHYST, 1.0f);

    }
    //endregion

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, RPG_Soul.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        //region SIMPLE ITEMS
        simpleItem(ModItems.RESONITE);
        simpleItem(ModItems.RESONITE_INGOT);
        saplingItem(ModBlocks.CRYSTAL_CORAL);
        saplingItem(ModBlocks.CRYSTAL_GRASS);
        saplingItem(ModBlocks.BELFRY);

        evenSimplerBlockItem(ModBlocks.HARDENED_QUARTZITE);
        //endregion

        //endregion

        //region TOOLS & WEAPONS

        //endregion

        //region ARMOR

        //endregion
    }

    //region CUSTOM MODEL METHODS (Armor, Handheld, Blocks)
    private void trimmedArmorItem(RegistryObject<Item> itemRegistryObject) {
        final String MOD_ID = RPG_Soul.MOD_ID; // Твой ID мода

        if (itemRegistryObject.get() instanceof ArmorItem armorItem) {
            // Добавляем базовую текстуру для инвентаря здесь
            this.withExistingParent(itemRegistryObject.getId().getPath(), mcLoc("item/generated"))
                    .texture("layer0", ResourceLocation.fromNamespaceAndPath(MOD_ID, "item/" + itemRegistryObject.getId().getPath()));

            trinMaterisals.entrySet().forEach(entry -> {

                ResourceKey<TrimMaterial> trimMaterial = entry.getKey();
                float trimValue = entry.getValue();

                String armorType = switch (armorItem.getEquipmentSlot()) {
                    case HEAD -> "helmet";
                    case CHEST -> "chestplate";
                    case LEGS -> "leggings";
                    case FEET -> "boots";
                    default -> "";
                };

                String armorItemPath = "item/" + itemRegistryObject.getId().getPath();
                String trimPath = "trims/items/" + armorType + "_trim_" + trimMaterial.location().getPath();
                String currentTrimName = armorItemPath + "_" + trimMaterial.location().getPath() + "_trim";

                // Используем современный способ создания ResourceLocation
                ResourceLocation armorItemResLoc = ResourceLocation.fromNamespaceAndPath(MOD_ID, armorItemPath);
                // Здесь мы берем текстуру узора из самого Майнкрафта, поэтому пространство имен "minecraft" (по умолчанию)
                ResourceLocation trimResLoc = ResourceLocation.withDefaultNamespace(trimPath);
                // Модель для брони с узором тоже в нашем MOD_ID
                ResourceLocation trimNameResLoc = ResourceLocation.fromNamespaceAndPath(MOD_ID, currentTrimName);

                // Сообщаем Forge, что эти текстуры существуют, чтобы не было ошибок при генерации
                existingFileHelper.trackGenerated(trimResLoc, PackType.CLIENT_RESOURCES, ".png", "textures");

                // Создаем саму модель с наложенным узором
                getBuilder(currentTrimName)
                        .parent(new ModelFile.UncheckedModelFile("item/generated"))
                        .texture("layer0", armorItemResLoc)
                        .texture("layer1", trimResLoc);

                // Настраиваем основную модель предмета, чтобы она переключалась на "узорчатую", если применен шаблон
                this.withExistingParent(itemRegistryObject.getId().getPath(),
                                mcLoc("item/generated"))
                        .override()
                        .model(new ModelFile.UncheckedModelFile(trimNameResLoc))
                        .predicate(mcLoc("trim_type"), trimValue).end();
            });
        }
    }


    private ItemModelBuilder saplingItem(RegistryObject<Block> item) {
        return withExistingParent(item.getId().getPath(),
                ResourceLocation.fromNamespaceAndPath("minecraft", "item/generated")).texture("layer0",
                ResourceLocation.fromNamespaceAndPath(RPG_Soul.MOD_ID, "block/" + item.getId().getPath()));
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                ResourceLocation.tryParse("minecraft:item/generated")).texture("layer0",
                ResourceLocation.tryParse(RPG_Soul.MOD_ID + ":item/" + item.getId().getPath()));
    }

    public void evenSimplerBlockItem(RegistryObject<Block> block) {
        this.withExistingParent((RPG_Soul.MOD_ID + ":" + ForgeRegistries.BLOCKS.getKey(block.get()).getPath()),
                modLoc("block/" + ForgeRegistries.BLOCKS.getKey(block.get()).getPath()));
    }

    public void fenceItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), mcLoc("block/fence_inventory")).texture("texture",
                ResourceLocation.tryParse(RPG_Soul.MOD_ID + ":block/" + ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
    }

    public void trapdoorItem(RegistryObject<Block> block) {
        this.withExistingParent(block.getId().getPath(),
                modLoc("block/" + block.getId().getPath() + "_bottom"));
    }

    public void buttonItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), mcLoc("block/button_inventory")).texture("texture",
                ResourceLocation.tryParse(RPG_Soul.MOD_ID + ":block/" + ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
    }


    public void wallItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), mcLoc("block/wall_inventory")).texture("wall",
                ResourceLocation.tryParse(RPG_Soul.MOD_ID + ":block/" + ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
    }

    private ItemModelBuilder simpleBlockItem(RegistryObject<Block> item) {
        return withExistingParent(item.getId().getPath(),
                ResourceLocation.tryParse("minecraft:item/generated")).texture("layer0",
                ResourceLocation.tryParse(RPG_Soul.MOD_ID + ":item/" + item.getId().getPath()));
    }

    private ItemModelBuilder simpleBlockItemBlockTexture (RegistryObject<Block> item) {
        return withExistingParent(item.getId().getPath(),
                ResourceLocation.tryParse("minecraft:item/generated")).texture("layer0",
                ResourceLocation.tryParse(RPG_Soul.MOD_ID + ":block/" + item.getId().getPath()));
    }

    private ItemModelBuilder handhelditem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                ResourceLocation.tryParse("item/handheld")).texture("layer0",
                ResourceLocation.tryParse(RPG_Soul.MOD_ID + ":item/" + item.getId().getPath()));
    }
    //endregion
}
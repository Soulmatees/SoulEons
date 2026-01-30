package net.soulmate.rpg_soul.datagen;

import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.soulmate.rpg_soul.RPG_Soul;
import net.soulmate.rpg_soul.block.ModBlocks;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, RPG_Soul.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        // --- 1. Базовые блоки (Кубы) ---
        blockWithItem(ModBlocks.RESONITE_BLOCK);
        blockWithItem(ModBlocks.RESONITE_INGOT_BLOCK);
        blockWithItem(ModBlocks.SPARKLING_SAND);
        blockWithItem(ModBlocks.HARDENED_SPARKLING_SAND);
        blockWithItem(ModBlocks.RESOLITH);
        blockWithItem(ModBlocks.POLISHED_RESOLITH);
        blockWithItem(ModBlocks.ECHO_CLUSTER);
        simpleBlock(ModBlocks.HARDENED_QUARTZITE.get(),
                models().cubeAll("hardened_quartzite", blockTexture(ModBlocks.HARDENED_QUARTZITE.get()))
                        .renderType("minecraft:translucent"));
        // Обычная растительность
        makeFlower(ModBlocks.CRYSTAL_GRASS.get());
        makeFlower(ModBlocks.BELFRY.get());
        // Коралл со светящимся слоем
        makeEmissiveCross(ModBlocks.CRYSTAL_CORAL, "crystal_coral", "crystal_coral_e");







        //region --- 6. Песчаник (Sandstone Set) ---
        registerBlockSet(
                ModBlocks.RESOLITH,
                ModBlocks.RESOLITH_STAIRS,
                ModBlocks.RESOLITH_SLAB,
                null, // Fence (обычно для дерева)
                null, // Gate (обычно для дерева)
                ModBlocks.RESOLITH_WALL,
                null, // Door (нужна отдельная текстура)
                null, // Trapdoor (нужна отдельная текстура)
                ModBlocks.RESOLITH_BUTTON,
                ModBlocks.RESOLITH_PRESSURE_PLATE
        );
        registerBlockSet(
                ModBlocks.POLISHED_RESOLITH,
                ModBlocks.POLISHED_RESOLITH_STAIRS,
                ModBlocks.POLISHED_RESOLITH_SLAB,
                null, // Fence (обычно для дерева)
                null, // Gate (обычно для дерева)
                ModBlocks.POLISHED_RESOLITH_WALL,
                null, // Door (нужна отдельная текстура)
                null, // Trapdoor (нужна отдельная текстура)
                ModBlocks.POLISHED_RESOLITH_BUTTON,
                ModBlocks.POLISHED_RESOLITH_PRESSURE_PLATE
        );
        //endregion
    }


    // =========================================================================================
    // === ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ===
    // =========================================================================================


    private void makeEmissiveCross(RegistryObject<Block> block, String textureName, String glowTextureName) {
        BlockModelBuilder model = models().withExistingParent(block.getId().getPath(), "block/block")
                .renderType("cutout")
                .texture("particle", modLoc("block/" + textureName))
                .texture("base", modLoc("block/" + textureName))
                .texture("glow", modLoc("block/" + glowTextureName));

        float[] angles = {45f, -45f};

        for (float angle : angles) {
            // ОСНОВНОЙ СЛОЙ
            model.element()
                    .from(0.8f, 0, 8).to(15.2f, 16, 8)
                    .rotation().angle(angle).axis(Direction.Axis.Y).origin(8, 8, 8).end()
                    .face(Direction.NORTH).texture("#base").end()
                    .face(Direction.SOUTH).texture("#base").end()
                    .shade(false) // ОТКЛЮЧАЕМ ЗАТЕНЕНИЕ ТУТ
                    .end();

            // СЛОЙ СВЕЧЕНИЯ
            model.element()
                    .from(0.8f, 0, 8.02f).to(15.2f, 16, 8.02f)
                    .rotation().angle(angle).axis(Direction.Axis.Y).origin(8, 8, 8).end()
                    .face(Direction.NORTH).texture("#glow").emissivity(15, 15).end()
                    .face(Direction.SOUTH).texture("#glow").emissivity(15, 15).end()
                    .shade(false) // И ТУТ
                    .end();
        }

        simpleBlock(block.get(), model);
    }

    private void registerBlockSet(RegistryObject<Block> baseBlock, RegistryObject<Block> stairs, RegistryObject<Block> slab,
                                  RegistryObject<Block> fence, RegistryObject<Block> gate, RegistryObject<Block> wall,
                                  RegistryObject<Block> door, RegistryObject<Block> trapdoor, RegistryObject<Block> button,
                                  RegistryObject<Block> pressurePlate) {

        ResourceLocation texture = blockTexture(baseBlock.get());

        if (stairs != null) {
            stairsBlock((StairBlock) stairs.get(), texture);
            simpleBlockItem(stairs.get(), models().stairs(name(stairs.get()), texture, texture, texture));
        }
        if (slab != null) {
            slabBlock((SlabBlock) slab.get(), texture, texture);
            simpleBlockItem(slab.get(), models().slab(name(slab.get()), texture, texture, texture));
        }
        if (fence != null) {
            fenceBlock((FenceBlock) fence.get(), texture);
            simpleBlockItem(fence.get(), models().fenceInventory(name(fence.get()) + "_inventory", texture));
        }
        if (gate != null) {
            fenceGateBlock((FenceGateBlock) gate.get(), texture);
            simpleBlockItem(gate.get(), models().fenceGate(name(gate.get()), texture));
        }
        if (wall != null) {
            wallBlock((WallBlock) wall.get(), texture);
            simpleBlockItem(wall.get(), models().wallInventory(name(wall.get()) + "_inventory", texture));
        }
        if (door != null) {
            doorBlockWithRenderType((DoorBlock) door.get(), modLoc("block/" + name(door.get()) + "_bottom"), modLoc("block/" + name(door.get()) + "_top"), "cutout");
            itemModels().basicItem(door.get().asItem());
        }
        if (trapdoor != null) {
            trapdoorBlockWithRenderType((TrapDoorBlock) trapdoor.get(), modLoc("block/" + name(trapdoor.get())), true, "cutout");
            simpleBlockItem(trapdoor.get(), models().trapdoorOrientableBottom(name(trapdoor.get()), modLoc("block/" + name(trapdoor.get()))));
        }
        if (button != null) {
            buttonBlock((ButtonBlock) button.get(), texture);
            simpleBlockItem(button.get(), models().buttonInventory(name(button.get()) + "_inventory", texture));
        }
        if (pressurePlate != null) {
            pressurePlateBlock((PressurePlateBlock) pressurePlate.get(), texture);
            simpleBlockItem(pressurePlate.get(), models().withExistingParent(name(pressurePlate.get()), "minecraft:block/pressure_plate_up").texture("texture", texture));
        }
    }

    private void saplingBlock(RegistryObject<Block> blockRegistryObject) {
        simpleBlock(blockRegistryObject.get(), models().cross(name(blockRegistryObject.get()), blockTexture(blockRegistryObject.get())).renderType("cutout"));
    }

    private void makeFlower(Block block) {
        simpleBlock(block, models().cross(ForgeRegistries.BLOCKS.getKey(block).getPath(),
                blockTexture(block)).renderType("cutout"));
    }

    public void hangingSignBlock(Block signBlock, Block wallSignBlock, ResourceLocation texture) {
        ModelFile sign = models().sign(name(signBlock), texture);
        simpleBlock(signBlock, sign);
        simpleBlock(wallSignBlock, sign);
    }

    private String name(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block).getPath();
    }

    private void blockItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockItem(blockRegistryObject.get(), new ModelFile.UncheckedModelFile(RPG_Soul.MOD_ID + ":block/" + name(blockRegistryObject.get())));
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }

}
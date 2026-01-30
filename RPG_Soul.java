package net.soulmate.rpg_soul;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.soulmate.rpg_soul.block.ModBlocks;
import net.soulmate.rpg_soul.block.entity.ModBlockEntities;
import net.soulmate.rpg_soul.effect.ModEffects;
import net.soulmate.rpg_soul.entity.ModEntities;
import net.soulmate.rpg_soul.item.ModCreativeMod;
import net.soulmate.rpg_soul.item.ModItems;
import net.soulmate.rpg_soul.loot.ModLootModifiers;
import net.soulmate.rpg_soul.recipe.ModRecipes;
import net.soulmate.rpg_soul.screen.ModMenuTypes;
import net.soulmate.rpg_soul.sound.ModSounds;
import net.soulmate.rpg_soul.worldgen.AddBiomesBiomeModifier;
import net.soulmate.rpg_soul.worldgen.ModSurfaceRules;
import net.soulmate.rpg_soul.worldgen.biome.ModBiomes;
import org.slf4j.Logger;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(RPG_Soul.MOD_ID)
public class RPG_Soul {
    public static final String MOD_ID = "rpg_soul";
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, RPG_Soul.MOD_ID);

    public static final RegistryObject<Codec<AddBiomesBiomeModifier>> ADD_BIOME_CODEC =
            BIOME_MODIFIER_SERIALIZERS.register("add_biome", () ->
                    RecordCodecBuilder.create(builder -> builder.group(
                            Biome.LIST_CODEC.fieldOf("biomes").forGetter(AddBiomesBiomeModifier::biomes),
                            Climate.ParameterPoint.CODEC.fieldOf("target").forGetter(AddBiomesBiomeModifier::target)
                    ).apply(builder, AddBiomesBiomeModifier::new)));

    public RPG_Soul(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();
        BIOME_MODIFIER_SERIALIZERS.register(modEventBus);
        

        ModCreativeMod.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModLootModifiers.register(modEventBus);
        ModEntities.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModRecipes.register(modEventBus);
        ModEffects.register(modEventBus);
        modEventBus.addListener(this::commonSetup);


        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);

        ModSounds.register(modEventBus);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if(event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ModItems.RESONITE);
            event.accept(ModItems.RESONITE_INGOT);
        }
    }
    private void commonSetup(final FMLCommonSetupEvent event) {

        event.enqueueWork(() -> {
            ModSurfaceRules.setup();
            com.github.alexthe666.citadel.server.world.ExpandedBiomes.addExpandedBiome(
                    ModBiomes.RINGING_DEPTHS,
                    net.minecraft.world.level.dimension.LevelStem.OVERWORLD
            );
        });
    }


    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
        }
    }


}

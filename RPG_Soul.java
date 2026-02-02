package net.soulmate.rpg_soul;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
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
import net.soulmate.rpg_soul.util.BiomeSourceAccessor;
import net.soulmate.rpg_soul.worldgen.ModSurfaceRules;
import net.soulmate.rpg_soul.worldgen.biome.ModBiomes;
import org.slf4j.Logger;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Mod(RPG_Soul.MOD_ID)
public class RPG_Soul {
    public static final String MOD_ID = "rpg_soul";
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, RPG_Soul.MOD_ID);



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

    public static void initializeBiomeMap(BiomeSource source, Registry<Biome> registry) {
        if (source instanceof BiomeSourceAccessor accessor) {
            Map<ResourceKey<Biome>, Holder<Biome>> map = new HashMap<>();
            map.put(ModBiomes.RINGING_DEPTHS, registry.getHolderOrThrow(ModBiomes.RINGING_DEPTHS));

            accessor.setResourceKeyMap(map);
            accessor.expandBiomesWith(Set.of(registry.getHolderOrThrow(ModBiomes.RINGING_DEPTHS)));
        }
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


    @SubscribeEvent
    public void onLevelLoad(LevelEvent.Load event) {
        if (event.getLevel() instanceof ServerLevel serverLevel) {
            BiomeSource source = serverLevel.getChunkSource().getGenerator().getBiomeSource();
            Registry<Biome> registry = serverLevel.registryAccess().registryOrThrow(Registries.BIOME);
            initializeBiomeMap(source, registry);
        }
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        // Вызываем регистрацию нашей команды телепортации
    }


}

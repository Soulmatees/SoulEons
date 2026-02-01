package net.soulmate.rpg_soul.mixin;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.NoiseRouterData;
import net.soulmate.rpg_soul.worldgen.RingingDepthsVoidDensity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NoiseRouterData.class)
public class NoiseRouterDataMixin {
    @Inject(method = "createKey", at = @At("HEAD"))
    private static void rpg_soul$ensureDensityRegistered(String name, CallbackInfoReturnable<ResourceKey<DensityFunction>> cir) {
    }
    @Inject(method = "bootstrap", at = @At("TAIL"))
    private static void rpg_soul$injectCustomRouter(BootstapContext<DensityFunction> pContext, CallbackInfoReturnable<Holder<? extends DensityFunction>> cir) {


    }
    private static void rpg_soul$injectCustomDensity(
            BootstapContext<DensityFunction> context,
            CallbackInfo ci
    ) {
        context.register(
                ResourceKey.create(
                        Registries.DENSITY_FUNCTION,
                        ResourceLocation.fromNamespaceAndPath("rpg_soul", "ringing_depths_void")
                ),
                new RingingDepthsVoidDensity()
        );
    }
}
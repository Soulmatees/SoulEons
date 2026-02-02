package net.soulmate.rpg_soul.mixin;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.soulmate.rpg_soul.util.BiomeSourceAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

@Mixin(BiomeSource.class)
public abstract class BiomeSourceMixin implements BiomeSourceAccessor {
    @Shadow public abstract Set<Holder<Biome>> possibleBiomes();

    @Unique
    private Map<ResourceKey<Biome>, Holder<Biome>> rpg_soul$cache;
    @Unique
    private final Set<Holder<Biome>> rpg_soul$extraBiomes = new HashSet<>();

    @Inject(method = "possibleBiomes", at = @At("RETURN"), cancellable = true)
    private void rpg_soul$addExtraBiomes(CallbackInfoReturnable<Set<Holder<Biome>>> cir) {
        if (!rpg_soul$extraBiomes.isEmpty()) {
            Set<Holder<Biome>> original = cir.getReturnValue();
            Set<Holder<Biome>> combined = new HashSet<>(original);
            combined.addAll(rpg_soul$extraBiomes);
            cir.setReturnValue(Collections.unmodifiableSet(combined));
        }
    }

    @Override
    public Map<ResourceKey<Biome>, Holder<Biome>> getResourceKeyMap() {
        if (rpg_soul$cache == null || rpg_soul$cache.isEmpty()) {
            rpg_soul$cache = new HashMap<>();
            for (Holder<Biome> holder : this.possibleBiomes()) {
                holder.unwrapKey().ifPresent(key -> rpg_soul$cache.put(key, holder));
            }
        }
        return rpg_soul$cache;
    }

    @Override
    public void expandBiomesWith(Set<Holder<Biome>> biomes) {
        if (this.rpg_soul$extraBiomes.addAll(biomes)) {
            this.rpg_soul$cache = null;
        }
    }
}
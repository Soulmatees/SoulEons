package net.soulmate.rpg_soul.util;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biome;
import net.soulmate.rpg_soul.worldgen.biome.ModBiomes;

public class ModTeleportCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("tpbiome").requires((source) -> source.hasPermission(2))
                .executes((context) -> {
                    ServerLevel level = context.getSource().getLevel();
                    BlockPos sourcePos = BlockPos.containing(context.getSource().getPosition());

                    // Ищем биом в радиусе 10000 блоков
                    BlockPos targetPos = level.findClosestBiome3d(
                            holder -> holder.is(ModBiomes.RINGING_DEPTHS),
                            sourcePos, 10000, 32, 64).getFirst();

                    if (targetPos != null) {
                        context.getSource().getPlayerOrException().teleportTo(targetPos.getX(), targetPos.getY(), targetPos.getZ());
                        context.getSource().sendSuccess(() -> Component.literal("Биом найден! Телепортация..."), true);
                    } else {
                        context.getSource().sendFailure(Component.literal("Биом НЕ найден в генерации мира. Проблема в Citadel/DataGen."));
                    }
                    return 1;
                })
        );
    }
}
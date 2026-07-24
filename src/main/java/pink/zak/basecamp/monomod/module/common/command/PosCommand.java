package pink.zak.basecamp.monomod.module.common.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import static net.minecraft.commands.Commands.literal;

public class PosCommand {

    public PosCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                literal("pos")
                        .executes(ctx -> {
                            ServerPlayer player = ctx.getSource().getPlayerOrException();
                            BlockPos pos = player.blockPosition();
                            String dimensionId = ctx.getSource().getLevel().dimension().identifier().getPath();

                            String dimensionName = switch (dimensionId) {
                                case "overworld" -> "the overworld";
                                case "the_nether" -> "the nether";
                                case "the_end" -> "the end";
                                default -> dimensionId;
                            };

                            Component msg = Component.literal(
                                    "<%s> %d, %d, %d (%s)".formatted(
                                            player.getName().getString(),
                                            pos.getX(), pos.getY(), pos.getZ(), dimensionName
                                    )
                            );

                            ctx.getSource().getServer().getPlayerList().broadcastSystemMessage(msg, false);
                            return 1;
                        })
        );
    }
}

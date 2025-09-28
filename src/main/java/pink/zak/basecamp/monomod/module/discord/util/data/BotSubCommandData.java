package pink.zak.basecamp.monomod.module.discord.util.data;

import org.jetbrains.annotations.NotNull;
import pink.zak.basecamp.monomod.module.discord.util.command.BotSubCommand;

public record BotSubCommandData(@NotNull BotSubCommand command, @NotNull String subCommandId,
                                @NotNull String subCommandGroup) {
}
package pink.zak.basecamp.monomod.module.discord.util.data.stored;

import org.jetbrains.annotations.NotNull;

public interface SlashCommandInfo {

    long getId();

    @NotNull String getName();
}
package pink.zak.basecamp.monomod.module.discord.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pink.zak.basecamp.monomod.module.discord.util.data.stored.SlashCommandInfo;

import java.util.Set;

public interface SlashCommandDetailsProvider {

    @Nullable Set<? extends SlashCommandInfo> loadCommands();

    void saveCommands(@NotNull Set<SlashCommandInfo> slashCommandInfos);
}
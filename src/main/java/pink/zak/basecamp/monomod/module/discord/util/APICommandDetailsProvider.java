package pink.zak.basecamp.monomod.module.discord.util;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.Command;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pink.zak.basecamp.monomod.module.discord.util.data.stored.SlashCommandInfo;
import pink.zak.basecamp.monomod.module.discord.util.data.stored.SlashCommandInfoImpl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class APICommandDetailsProvider implements SlashCommandDetailsProvider {
    private final @NotNull JDA jda;
    private final @Nullable Guild guild;

    public APICommandDetailsProvider(@NotNull JDA jda, @Nullable Guild guild) {
        this.jda = jda;
        this.guild = guild;
    }

    @Override
    public @Nullable Set<? extends SlashCommandInfo> loadCommands() {
        List<Command> apiCommands;
        if (this.guild == null) {
            apiCommands = this.jda.retrieveCommands().complete();
        } else {
            apiCommands = this.jda.getGuildById(this.guild.getId()).retrieveCommands().complete();
        }

        return apiCommands.stream()
                .map(command -> new SlashCommandInfoImpl(command.getName(), command.getIdLong()))
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public void saveCommands(@NotNull Set<SlashCommandInfo> slashCommandInfos) {
        // no explicit saving needed as they are already pushed to the API
    }
}

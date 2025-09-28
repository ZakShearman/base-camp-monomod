package pink.zak.basecamp.monomod.module.discord.util.data.stored;

import org.jetbrains.annotations.NotNull;

public record SlashCommandInfoImpl(@NotNull String name, long id) implements SlashCommandInfo {

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public @NotNull String getName() {
        return this.name;
    }
}
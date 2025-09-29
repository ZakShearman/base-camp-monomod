package pink.zak.basecamp.monomod.module.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import pink.zak.basecamp.monomod.module.discord.commands.LinkCommand;
import pink.zak.basecamp.monomod.module.discord.util.APICommandDetailsProvider;
import pink.zak.basecamp.monomod.module.discord.util.DiscordCommandBackend;
import pink.zak.basecamp.monomod.module.discord.util.command.BotCommand;

import java.util.Set;

public class DiscordModule {
    private static final String BOT_TOKEN = System.getenv("BOT_TOKEN");
    private static final long GUILD_ID = 1421797476188291086L;

    private final @NotNull JDA jda;
    private final @NotNull Guild guild;

    public DiscordModule(@NotNull DSLContext repo) {
        if (BOT_TOKEN == null || BOT_TOKEN.isEmpty()) {
            throw new NullPointerException("Bot token is null or empty");
        }

        try {
            this.jda = JDABuilder.createDefault(BOT_TOKEN).build().awaitReady();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        this.guild = this.jda.getGuildById(GUILD_ID);

        Set<BotCommand> commands = Set.of(
                new LinkCommand(repo, this.guild)
        );

        APICommandDetailsProvider commandDetailsProvider = new APICommandDetailsProvider(this.jda, this.guild);
        DiscordCommandBackend commandBackend = new DiscordCommandBackend(this.jda, commandDetailsProvider,
                commands, Set.of(), this.guild);

        this.jda.addEventListener(commandBackend);
    }

    public void shutdown() {
        this.jda.shutdown();
    }
}

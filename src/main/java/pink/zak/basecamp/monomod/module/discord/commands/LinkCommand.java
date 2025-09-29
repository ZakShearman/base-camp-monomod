package pink.zak.basecamp.monomod.module.discord.commands;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.Result;
import pink.zak.basecamp.monomod.model.db.tables.LinkRequest;
import pink.zak.basecamp.monomod.model.db.tables.Player;
import pink.zak.basecamp.monomod.model.db.tables.records.LinkRequestRecord;
import pink.zak.basecamp.monomod.model.db.tables.records.PlayerRecord;
import pink.zak.basecamp.monomod.module.discord.util.annotations.BotCommandComponent;
import pink.zak.basecamp.monomod.module.discord.util.command.BotCommand;

import java.util.Objects;

@BotCommandComponent(name = "link")
public class LinkCommand implements BotCommand {
    private static final long LINKED_ROLE_ID = 1421961548586422382L;

    private final @NotNull DSLContext repo;
    private final @NotNull Role linkedRole;

    public LinkCommand(@NotNull DSLContext repo, @NotNull Guild guild) {
        this.repo = repo;
        this.linkedRole = Objects.requireNonNull(guild.getRoleById(LINKED_ROLE_ID));
    }

    @Override
    public void onExecute(@NotNull Member sender, @NotNull SlashCommandInteractionEvent event) {
        System.out.println("Test executed /link");
        String code = event.getOption("code").getAsString();

        Result<PlayerRecord> playerResult = this.repo.fetch(
                Player.PLAYER.where(Player.PLAYER.DISCORD_ID.eq(sender.getIdLong()))
        );
        if (!playerResult.isEmpty()) {
            event.reply(":x: Your account is already linked to the Minecraft account **%s**"
                    .formatted(playerResult.getFirst().getMinecraftUsername())
            ).setEphemeral(true).queue();
            return;
        }

        Result<LinkRequestRecord> linkRequestResult = this.repo.fetch(
                LinkRequest.LINK_REQUEST.where(LinkRequest.LINK_REQUEST.ID.eq(code))
        );
        if (linkRequestResult.isEmpty()) {
            event.reply(":x: Could not find a link code with ID " + code).setEphemeral(true).queue();
            return;
        }

        // Everything correct, link the player
        LinkRequestRecord linkRequest = linkRequestResult.getFirst();

        PlayerRecord playerRecord = this.repo.newRecord(Player.PLAYER);
        playerRecord.setMinecraftId(linkRequest.getMinecraftId());
        playerRecord.setMinecraftUsername(linkRequest.getMinecraftUsername());
        playerRecord.setDiscordId(sender.getIdLong());
        playerRecord.setLastLogin(linkRequest.getCreatedAt());
        playerRecord.store();

        // Delete the link request
        this.repo.delete(LinkRequest.LINK_REQUEST)
                .where(LinkRequest.LINK_REQUEST.ID.eq(linkRequest.getId()))
                .execute();

        sender.modifyNickname("%s (%s)".formatted(sender.getUser().getEffectiveName(), linkRequest.getMinecraftUsername()))
                .queue();

        event.getGuild().addRoleToMember(sender, this.linkedRole).queue();

        event.reply("Your account has been linked to the Minecraft account **%s**\n".formatted(linkRequest.getMinecraftUsername()) +
                "You will now be able to join the Minecraft server at `mc.zak.pink`").setEphemeral(true).queue();
    }

    @Override
    public @NotNull CommandData createCommandData() {
        return Commands.slash("link", "link your Minecraft and Discord accounts with a link code")
                .addOptions(
                        new OptionData(OptionType.STRING, "code", "Link code", true)
                                .setRequiredLength(6, 6)
                );
    }
}

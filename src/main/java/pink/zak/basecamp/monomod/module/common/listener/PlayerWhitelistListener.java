package pink.zak.basecamp.monomod.module.common.listener;

import net.fabricmc.fabric.api.networking.v1.ServerConfigurationConnectionEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.Result;
import pink.zak.basecamp.monomod.model.db.tables.LinkRequest;
import pink.zak.basecamp.monomod.model.db.tables.Player;
import pink.zak.basecamp.monomod.model.db.tables.records.LinkRequestRecord;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class PlayerWhitelistListener {
    private static final char[] LINK_REQ_ID_CHARS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    private static final int LINK_REQ_ID_LEN = 6;

    private static final Random RANDOM = new Random();

    public PlayerWhitelistListener(@NotNull DSLContext context) {
        ServerConfigurationConnectionEvents.BEFORE_CONFIGURE.register((handler, server) -> {
            UUID playerId = handler.getDebugProfile().getId();

            boolean exists = context.fetchExists(
                    context.selectFrom(Player.PLAYER).where(Player.PLAYER.MINECRAFT_ID.eq(playerId))
            );

            if (exists) return; // allow the player to log in if they have a record

            // If player does not already exist, create a linking code and kick them with it
            Result<LinkRequestRecord> existingReqResult = context.fetch(
                    context.selectFrom(LinkRequest.LINK_REQUEST).where(LinkRequest.LINK_REQUEST.MINECRAFT_ID.eq(playerId))
            );

            LinkRequestRecord linkRequest;
            if (!existingReqResult.isEmpty()) {
                linkRequest = existingReqResult.getFirst();
            } else {
                linkRequest = context.newRecord(LinkRequest.LINK_REQUEST);
                linkRequest.setId(this.genId());
                linkRequest.setMinecraftId(playerId);
                linkRequest.setMinecraftUsername(handler.getDebugProfile().getName());
                linkRequest.setCreatedAt(LocalDateTime.now());
                linkRequest.store();
            }

            handler.disconnect(
                    Text.literal("You are not whitelisted!\n\n").formatted(Formatting.RED)
                            .append(Text.literal("Please use ").formatted(Formatting.RED))
                            .append(Text.literal("/link " + linkRequest.getId()).formatted(Formatting.RED, Formatting.UNDERLINE))
                            .append(Text.literal(" on Discord to whitelist yourself").formatted(Formatting.RED))
            );
        });
    }

    private String genId() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < LINK_REQ_ID_LEN; i++) {
            sb.append(LINK_REQ_ID_CHARS[RANDOM.nextInt(LINK_REQ_ID_CHARS.length)]);
        }

        return sb.toString();
    }
}

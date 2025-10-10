package pink.zak.basecamp.monomod.module.discord.mclistener;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;

public class PlayerConnectionListener {
    // we count it ourselves as there are many tick-timing quirks otherwise
    private static final AtomicInteger PLAYER_COUNT = new AtomicInteger(0);

    private final @NotNull JDA jda;
    private MinecraftServer server;

    public PlayerConnectionListener(@NotNull JDA jda) {
        this.jda = jda;

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            this.server = server;
            this.updateStatusMessage();
        });
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            PLAYER_COUNT.getAndIncrement();
            this.updateStatusMessage();
        });
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            PLAYER_COUNT.getAndDecrement();
            this.updateStatusMessage();
        });
    }

    private void updateStatusMessage() {
        int playerCount = PLAYER_COUNT.get();
        String text = "over " + playerCount + " player";
        if (playerCount == 0 || playerCount > 1) {
            text += "s";
        }

        this.jda.getPresence().setActivity(Activity.watching(text));
    }
}

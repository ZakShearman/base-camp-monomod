package pink.zak.basecamp.monomod.module.common;

import net.fabricmc.fabric.api.networking.v1.ServerConfigurationConnectionEvents;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import pink.zak.basecamp.monomod.module.common.listener.PlayerWhitelistListener;

import java.util.UUID;

public class CommonModule {

    public CommonModule(@NotNull DSLContext context) {
        new PlayerWhitelistListener(context);
    }
}

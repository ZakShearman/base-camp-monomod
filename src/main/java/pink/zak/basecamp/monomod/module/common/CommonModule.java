package pink.zak.basecamp.monomod.module.common;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import pink.zak.basecamp.monomod.module.common.command.PosCommand;
import pink.zak.basecamp.monomod.module.common.listener.PlayerWhitelistListener;

public class CommonModule {

    public CommonModule(@NotNull DSLContext context) {
        new PlayerWhitelistListener(context);

        CommandRegistrationCallback.EVENT.register(
                (commandDispatcher, commandBuildContext, commandSelection) ->
                        new PosCommand(commandDispatcher)
        );
    }
}

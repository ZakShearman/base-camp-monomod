package pink.zak.basecamp.monomod.module.common;

import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import pink.zak.basecamp.monomod.module.common.listener.PlayerWhitelistListener;

public class CommonModule {

    public CommonModule(@NotNull DSLContext context) {
        new PlayerWhitelistListener(context);
    }
}

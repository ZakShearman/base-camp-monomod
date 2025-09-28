package pink.zak.basecamp.monomod;

import net.fabricmc.api.ModInitializer;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pink.zak.basecamp.monomod.module.common.CommonModule;
import pink.zak.basecamp.monomod.module.discord.DiscordModule;
import pink.zak.basecamp.monomod.repository.JooqRepository;

public class BaseCampMonoMod implements ModInitializer {
    public static final String MOD_ID = "basecamp";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        DSLContext context = JooqRepository.init();

        DiscordModule discordModule = new DiscordModule(context);

        new CommonModule(context);
    }
}

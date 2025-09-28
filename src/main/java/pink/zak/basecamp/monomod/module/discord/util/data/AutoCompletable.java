package pink.zak.basecamp.monomod.module.discord.util.data;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;

public interface AutoCompletable {

    void onAutoComplete(CommandAutoCompleteInteractionEvent event);
}
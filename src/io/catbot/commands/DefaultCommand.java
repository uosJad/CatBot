package src.io.catbot.commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import src.io.catbot.listeners.CommandListener;

/**
 * Created by jason on 8/4/17.
 */
public class DefaultCommand extends CommandListener {

    public DefaultCommand(){
        super();
        setAlias("ping");
        setDescription("Used to test if CatBot is responding properly");
    }

    public void doCommand(MessageReceivedEvent event, String[] args) {
        event.getTextChannel().sendMessage("pong").queue();
    }
}

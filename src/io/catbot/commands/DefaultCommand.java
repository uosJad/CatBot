package src.io.catbot.commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import src.io.catbot.listeners.CommandListener;

import java.util.List;

/**
 * Created by jason on 8/4/17.
 */
public class DefaultCommand extends CommandListener {

    public DefaultCommand(){
        super();
        //setArgOptional(false);
        //setArgs(new String[]{});
        addAlias("ping");
        addAdminCommand("default");
        setDescription("Responds with pong");
    }

    @Override
    public void doCommand(MessageReceivedEvent event, List<String> args) {
        event.getTextChannel().sendMessage("pong").queue();
    }
}

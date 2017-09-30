package src.io.catbot.commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import src.io.catbot.listeners.CommandListener;

import java.util.List;

/**
 * Created by jason on 9/17/17.
 */
public class GreetCommand extends CommandListener {

    public GreetCommand(){
        super();
        setArgOptional(true);
        setArgs(new String[]{"greeting"});
        addAlias("greet");
        addAlias("g");
        addAdminCommand("default");
        setDescription("Set a greeting message when a user joins");
    }

    public void doCommand(MessageReceivedEvent event, List<String> args) {

    }

}

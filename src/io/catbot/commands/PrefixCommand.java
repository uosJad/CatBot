package src.io.catbot.commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import src.io.catbot.listeners.CommandListener;

import java.util.List;

/**
 * Created by jason on 10/17/17.
 */
public class PrefixCommand extends CommandListener {

    public PrefixCommand(){
        super();
        setArgOptional(true);
        setArgs("default", new String[]{});
        setArgs("set", new String[]{"prefix"});
        addAlias("prefix");
        setDescription("Display commands");
    }


    public void doCommand(MessageReceivedEvent event, List<String> args) {
        String mod = args.get(1);

        if (args.size() == 1){

        }

    }

    private void displayPrefix(MessageReceivedEvent event){

    }
}

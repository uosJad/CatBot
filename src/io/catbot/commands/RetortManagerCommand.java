package src.io.catbot.commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import src.io.catbot.listeners.CommandListener;

/**
 * Created by jason on 8/18/17.
 */
public class RetortManagerCommand extends CommandListener{

    public RetortManagerCommand(){
        super();
        setArgOptional(true);
        setArgs("list", new String[]{});
        setArgs("toggle", new String[]{});
        setArgs("add", new String[]{"trigger", "retort"});
        addAlias("retort");
        addAlias("r");
        setDescription("Set a custom retort");
    }

    public void doCommand(MessageReceivedEvent event, String[] args) {
        if (args.length == 1){
            listRetorts(event);
        }
        else if (args[1].equals("add")){
            addRetorts(event);
        }
        else if(args[1].equals("list")){
            listRetorts(event);
        }
        else if(args[1].equals("toggle")){
            toggleRetorts(event);
        }
    }

    private void listRetorts(MessageReceivedEvent event){
        event.getTextChannel().sendMessage("list").queue();
    }

    private void addRetorts(MessageReceivedEvent event){
        event.getTextChannel().sendMessage("add").queue();
    }

    private void toggleRetorts(MessageReceivedEvent event){
        event.getTextChannel().sendMessage("toggle").queue();
    }

}

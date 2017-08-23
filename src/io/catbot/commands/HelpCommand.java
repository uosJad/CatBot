package src.io.catbot.commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import src.io.catbot.listeners.CommandListener;

import java.util.Iterator;
import java.util.Set;

/**
 * Created by jason on 8/4/17.
 */
public class HelpCommand extends CommandListener{

    private String helpString;

    public HelpCommand(){
        super();
        setArgOptional(true);
        setArgs(new String[]{"command"});
        addAlias("help");
        addAlias("h");
        setDescription("Display commands");
    }

    public void doCommand(MessageReceivedEvent event, String[] args) {
        if (helpString == null){
            generateHelpString();
        }

        //TODO send as pm
        if (args.length == 1){
            event.getTextChannel().sendMessage(helpString).queue();
        }
        else if(args.length == 2){

            CommandListener temp = CommandManager.getInstance().getCommandListener(args[1]);
            CommandListener temp2 = CommandManager.getInstance().getCommandListener("+"+args[1]);
            if (temp != null){
                CommandManager.getInstance().sendCommandInfo(event, temp);
            }
            else if(temp2 != null){
                CommandManager.getInstance().sendCommandInfo(event, temp2);
            }
        }
    }

    public void generateHelpString(){
        Set<CommandListener> commands = CommandManager.getInstance().getCommands();
        Iterator<CommandListener> it = commands.iterator();

        helpString = "```css\nTODO formatting\n\n";

        while(it.hasNext()){
            helpString = helpString +"  "+ CommandManager.getInstance().getShortCommandInfo(it.next());
        }

        helpString = helpString + "```";
    }


}

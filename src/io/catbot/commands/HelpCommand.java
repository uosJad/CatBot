package src.io.catbot.commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import src.io.catbot.listeners.CommandListener;

import java.util.Iterator;
import java.util.List;
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

    @Override
    public void doCommand(MessageReceivedEvent event, List<String> args) {
        if (helpString == null){
            generateHelpString(false);
        }

        //TODO send as pm
        if (args.size() == 1){
            event.getTextChannel().sendMessage(helpString).queue();
        }
        else if(args.size() == 2){

            CommandListener temp = CommandManager.getInstance().getCommandListener(args.get(1));
            CommandListener temp2 = CommandManager.getInstance().getCommandListener("+"+args.get(1));
            if (temp != null){
                CommandManager.getInstance().sendCommandInfo(event, temp, false);
            }
            else if(temp2 != null){
                CommandManager.getInstance().sendCommandInfo(event, temp2, false);
            }
        }
    }

    public void generateHelpString(boolean showAdmin){
        Set<CommandListener> commands = CommandManager.getInstance().getCommands();
        Iterator<CommandListener> it = commands.iterator();

        helpString = "```css\nTODO formatting\n\n";

        while(it.hasNext()){
            //TODO ADMIN COMMAND
            CommandListener command = it.next();

            //if not showing admin, dont show if default is hidden
            if (showAdmin || !command.getAdminCommands().contains("default")){
                helpString = helpString +"  "+ CommandManager.getInstance().getShortCommandInfo(command);
            }
        }

        helpString = helpString + "```";
    }


}

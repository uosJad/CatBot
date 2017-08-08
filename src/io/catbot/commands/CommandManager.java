package src.io.catbot.commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import src.io.catbot.listeners.CommandListener;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by jason on 8/4/17.
 */
public class CommandManager {

    private static CommandManager instance;

    Set<CommandListener> commands;

    private CommandManager(){
        commands = new HashSet();
    }

    public void addCommand(CommandListener commandListener){
        commands.add(commandListener);
    }

    public Set<CommandListener> getCommands() {
        return commands;
    }

    public static CommandManager getInstance() {
        if (instance == null){
            instance = new CommandManager();
        }
        return instance;
    }

    public CommandListener getCommandListener(String s){
        Iterator<CommandListener> it = commands.iterator();
        while(it.hasNext()){
            CommandListener temp = it.next();
            if (temp.getAliases().contains(s)){
                return temp;
            }
        }

        return null;
    }

    public void sendCommandInfo(MessageReceivedEvent event, CommandListener com){
        event.getTextChannel().sendMessage("```css\n" + getCommandInfo(com) + "```").queue();
    }


    public String getCommandInfo(CommandListener com){
        String info = getAliasString(com);
        info = info + getArgsString(com) + " | ";
        info = info + com.getDescription() + "\n";

        return info;

    }

    protected String getAliasString(CommandListener com){
        Set<String> aliases = com.getAliases();
        Iterator<String> it = aliases.iterator();
        String aliasString = "";

        while(it.hasNext()){
            aliasString = aliasString + it.next();
            if (it.hasNext()){
                aliasString += ", ";
            }
        }

        return aliasString;
    }

    protected String getArgsString(CommandListener com){
        String[] args = com.getArgs();
        String argString = " ";

        for(int i = 0; i < args.length; i++){
            argString = argString + "<" + args[i] + ">";
        }

        /*
        if (com.isArgOptional()){
            argString = argString + "(optional)";
        }
        */

        if (argString.equals(" ") || argString.equals(" (optional)")){
            argString = "";
        }
        return argString;
    }
}

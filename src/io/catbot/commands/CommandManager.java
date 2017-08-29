package src.io.catbot.commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import src.io.catbot.listeners.CommandListener;

import java.util.*;

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

    public void sendCommandInfo(MessageReceivedEvent event, CommandListener com, boolean showAdmin){
        event.getTextChannel().sendMessage("```css\n" + getCommandInfo(com, showAdmin) + "```").queue();
    }


    public String getShortCommandInfo(CommandListener com){
        String info = getAliasString(com) + " ";
        info = info + "[" + com.getDescription() + "]\n";

        return info;
    }

    public String getCommandInfo(CommandListener com, boolean showAdmin){

        String info = getShortCommandInfo(com);
        info = info + getArgsString(com, showAdmin);

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

    protected String getArgsString(CommandListener com, boolean showAdmin){
        Map<String, String[]> args = com.getArgs();
        String argString = "\n";
        Iterator<String> it = args.keySet().iterator();


        while (it.hasNext()){

            String mod = it.next();

            if (showAdmin || !com.getAdminCommands().contains(mod)) {
                String[] para = args.get(mod);

                argString = argString + "\t" + com.getAliases().iterator().next() + " ";

                if (mod != "default") {
                    argString = argString + mod + " ";
                }

                for (int i = 0; i < para.length; i++) {
                    argString = argString + "[" + para[i] + "] ";
                }
                argString = argString + "\n";
            }

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

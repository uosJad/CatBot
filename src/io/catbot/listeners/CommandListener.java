package src.io.catbot.listeners;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import src.io.catbot.commands.CommandManager;
import src.io.catbot.con.JsonHandler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by jason on 8/4/17.
 */
public abstract class CommandListener extends ListenerAdapter {

    private Set<String> aliases;
    private String desc;
    private Map<String, String[]> args;

    private JsonHandler configJsh;
    private boolean isArgOptional;
    protected final String prefix;

    public abstract void doCommand(MessageReceivedEvent event, String[] args);

    public CommandListener(){
        CommandManager.getInstance().addCommand(this);
        aliases = new HashSet<String>();
        args = new HashMap<String, String[]>(); // command modifier, args
        configJsh = new JsonHandler("data/config.json");
        prefix = (String)configJsh.getValueFromKey("CommandPrefix");
        isArgOptional = true;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] argsEvent = getStringsFromEvent(event);

        //if is an arguement
        if (isAnAlias(argsEvent[0])){
            if (argsEvent[0].equals("+help")){
                doCommand(event, argsEvent);
            }
            else if (isArgOptional){
                if (hasArgs(argsEvent) && args.containsKey(argsEvent[1]) && !argsEvent[1].equals("default")){
                    doCommand(event, argsEvent);
                }
                else if (!hasArgs(argsEvent)){
                    doCommand(event, argsEvent);
                }
                else{
                    sendInfo(event);
                }
            }
            else if (!isArgOptional && hasArgs(argsEvent) && args.containsKey(argsEvent[1])){
                doCommand(event, argsEvent);
            }

            else{
                sendInfo(event);
            }
        }
    }

    private void sendInfo(MessageReceivedEvent event){
        event.getTextChannel().sendMessage("```css\n" +
                CommandManager.getInstance().getCommandInfo(this) + "```").queue();
    }

    //TODO more verbose checking
    private boolean hasArgs(String[] strings){
        if (strings.length < 2){
            return false;
        }
        return true;
    }

    public void sendInvalidCommandMessage(MessageReceivedEvent event){
        event.getTextChannel().sendMessage("```css\n" +
                CommandManager.getInstance().getCommandInfo(this) + "```").queue();
    }

    public String[] getStringsFromEvent(MessageReceivedEvent event){
        return event.getMessage().getContent().toLowerCase().split(" ");
    }

    public boolean isAnAlias(String s){
        return aliases.contains(s);
    }

    public Set<String> getAliases(){
        return aliases;
    }

    public void addAlias(String s){
        aliases.add(prefix + s);
    }

    public String getDescription() {
        return desc;
    }

    public void setDescription(String desc) {
        this.desc = desc;
    }

    public Map<String, String[]> getArgs() {
        return args;
    }

    public void setArgs(String[] args){ this.setArgs("default", args); }

    public void setArgs(String mod, String[] args) {
        this.args.put(mod, args);
    }

    public boolean isArgOptional() {
        return isArgOptional;
    }

    public void setArgOptional(boolean argOptional) {
        isArgOptional = argOptional;
    }
}

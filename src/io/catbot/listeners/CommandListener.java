package src.io.catbot.listeners;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import src.io.catbot.commands.CommandManager;
import src.io.catbot.con.JsonHandler;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by jason on 8/4/17.
 */
public abstract class CommandListener extends ListenerAdapter {

    private Set<String> aliases;
    private String desc;
    private String[] args;
    private JsonHandler configJsh;
    private boolean isArgOptional;
    protected final String prefix;

    public abstract void doCommand(MessageReceivedEvent event, String[] args);

    public CommandListener(){
        CommandManager.getInstance().addCommand(this);
        aliases = new HashSet<String>();
        configJsh = new JsonHandler("data/config.json");
        prefix = (String)configJsh.getValueFromField("CommandPrefix");
        isArgOptional = true;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] argsEvent = getStringsFromEvent(event);
        if (isAnAlias(argsEvent[0])){
            if (isArgOptional){
                doCommand(event, argsEvent);
            }
            else if (!isArgOptional && argsEvent.length-1 == args.length){
                doCommand(event, argsEvent);
            }
            else{
                event.getTextChannel().sendMessage("```css\n" +
                        CommandManager.getInstance().getCommandInfo(this) + "```").queue();
            }
        }
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

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

    public boolean isArgOptional() {
        return isArgOptional;
    }

    public void setArgOptional(boolean argOptional) {
        isArgOptional = argOptional;
    }
}

package src.io.catbot.listeners;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import src.io.catbot.con.JsonHandler;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by jason on 8/4/17.
 */
public abstract class CommandListener extends ListenerAdapter {

    private Set<String> aliases;
    private String desc;
    private JsonHandler configJsh;
    private final String prefix;

    public abstract void doCommand(MessageReceivedEvent event, String[] args);

    public CommandListener(){
        aliases = new HashSet<String>();
        configJsh = new JsonHandler("data/config.json");
        prefix = (String)configJsh.getValueFromField("CommandPrefix");
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] args = getStringsFromEvent(event);
        if (isCommand(args[0])){
            doCommand(event, args);
        }
    }

    public String[] getStringsFromEvent(MessageReceivedEvent event){
        return event.getMessage().getContent().split(" ");
    }

    public boolean isCommand(String s){
        return aliases.contains(s);
    }

    public Set<String> getAliases(){
        return aliases;
    }

    public void setAlias(String s){
        aliases.add(prefix + s);
    }

    public String getDescription() {
        return desc;
    }

    public void setDescription(String desc) {
        this.desc = desc;
    }
}

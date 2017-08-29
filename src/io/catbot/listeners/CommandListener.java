package src.io.catbot.listeners;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.utils.PermissionUtil;
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
    private Set<String> adminCommands;

    private JsonHandler configJsh;
    private boolean isArgOptional;
    protected final String prefix;

    public abstract void doCommand(MessageReceivedEvent event, String[] args);

    public CommandListener(){
        CommandManager.getInstance().addCommand(this);
        aliases = new HashSet<String>();
        adminCommands = new HashSet<String>();
        args = new HashMap<String, String[]>(); // command modifier, args
        configJsh = new JsonHandler("data/config.json");
        prefix = (String)configJsh.getValueFromKey("CommandPrefix");
        isArgOptional = true;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] argsEvent = getStringsFromEvent(event);

        //TODO check permission

        //if is an arguement
        if (isAnAlias(argsEvent[0])){

            if (checkValidPermissions(event, argsEvent)) {
                //if help
                if (argsEvent[0].equals("+help")) {
                    doCommand(event, argsEvent);
                } else if (argsEvent[0].equals("+admin") && checkValidPermissions(event, argsEvent)) {
                    doCommand(event, argsEvent);
                }

                //if optional args
                else if (isArgOptional) {

                    //if > 2 args and is a command
                    if (hasArgs(argsEvent) && checkValidArgs(argsEvent)) {
                        doCommand(event, argsEvent);
                    } else if (!hasArgs(argsEvent)) {
                        doCommand(event, argsEvent);
                    } else {
                        sendInfo(event);
                    }
                } else if (!isArgOptional && hasArgs(argsEvent) && checkValidArgs(argsEvent) && checkValidPermissions(event, argsEvent)) {
                    doCommand(event, argsEvent);
                } else {
                    sendInfo(event);
                }
            }
        }
    }

    private void sendInfo(MessageReceivedEvent event){
        event.getTextChannel().sendMessage("```css\n" +
                CommandManager.getInstance().getCommandInfo(this, false) + "```").queue();
    }

    //if it is an adminCommand, check if user is an admin
    private boolean checkValidPermissions(MessageReceivedEvent event, String[] argsEvent){

        //if default is an admin command
        if (argsEvent.length == 1) {
            if (!adminCommands.contains("default")){
                return true;
            }
        }
        //if modifier is an admin command
        else if (!adminCommands.contains(argsEvent[1])){
            return true;
        }

        System.out.println("Is an admin command");

        //if has admin permissions
        if (PermissionUtil.checkPermission(event.getMember(), (Permission.ADMINISTRATOR))){
            return true;
        }

        event.getTextChannel().sendMessage("Insufficient permissions").queue();
        return false;

    }

    //TODO multiple args numbers per modifier
    //checks to see if there is the correct number of args for the modifier
    //assumed that args[1] is already known to be a valid modifier
    private boolean checkValidArgs(String[] argsEvent){
        if (!args.containsKey(argsEvent[1])){
            return false;
        }

        String commandKey = argsEvent[1];
        if (!(args.get(commandKey).length == argsEvent.length - 2)){
            return false;
        }

        return true;
    }


    //checks if there is 2 or more args
    private boolean hasArgs(String[] strings){
        if (strings.length < 2){
            return false;
        }
        return true;
    }

    public String[] getStringsFromEvent(MessageReceivedEvent event){
        return event.getMessage().getContent().toLowerCase().split(" ");
    }

    public Set<String> getAdminCommands() {
        return adminCommands;
    }

    public boolean isAnAlias(String s){
        return aliases.contains(s);
    }

    public Set<String> getAliases(){
        return aliases;
    }

    public void addAdminCommand(String s) { adminCommands.add(s); }

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

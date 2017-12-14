package src.io.catbot.listeners;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.utils.PermissionUtil;
import src.io.catbot.commands.CommandManager;
import src.io.catbot.conn.JsonHandler;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jason on 8/4/17.
 */
public abstract class CommandListener extends ListenerWrapper {

    private Set<String> aliases;
    private String desc;
    private Map<String, String[]> args;
    private Set<String> adminCommands;

    private JsonHandler configJsh;
    private boolean isArgOptional;
    protected final String prefix;

    public abstract void doCommand(MessageReceivedEvent event, List<String> args);

    public CommandListener(){
        CommandManager.getInstance().addCommand(this);
        aliases = new HashSet<String>();
        adminCommands = new HashSet<String>();
        args = new HashMap<String, String[]>(); // command modifier, args
        configJsh = new JsonHandler("data/config.json");
        prefix = (String)configJsh.getValueFromKey("CommandPrefix");
        isArgOptional = true;
    }

    /**
     * Checks to see if the command is in the command list and if the
     * arguments are of the correct format for each modifier
     * If not, prints the command's info
     * @param event
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        List<String> argsEvent = getStringsFromEvent(event);

        //if is an arguement
        if (isAnAlias(argsEvent.get(0))){

            System.out.println(argsEvent.get(0));

            //if has permissions to do command
            if (checkValidPermissions(event, argsEvent)) {

                System.out.println(argsEvent.get(0));

                //TODO prefix based on servers

                //if help, do it regardless of modifiers
                if (argsEvent.get(0).equals("+help")) {
                    doCommand(event, argsEvent);
                }

                //if admin, do admin if permissions
                else if (argsEvent.get(0).equals("+admin") && checkValidPermissions(event, argsEvent)) {
                    doCommand(event, argsEvent);
                }

                //if optional args
                else if (isArgOptional) {

                    //if > 2 args and is a command
                    if (checkValidArgs(argsEvent)) {
                        doCommand(event, argsEvent);
                    } else if (!hasArgs(argsEvent)) {
                        doCommand(event, argsEvent);
                    } else {
                        sendInfo(event);
                    }
                } else if (!isArgOptional && checkValidArgs(argsEvent) && checkValidPermissions(event, argsEvent)) {
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

    /**
     * if command is an admin Command, check if user is an admin
     */
    private boolean checkValidPermissions(MessageReceivedEvent event, List<String> argsEvent){

        //if default is an admin command
        if (argsEvent.size() == 1 || argsEvent.get(0).equals("+admin")) {
            if (!adminCommands.contains("default")){
                return true;
            }
        }
        //if modifier is an admin command
        else if (!adminCommands.contains(argsEvent.get(1))){
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

    //checks to see if there is the correct number of args for the modifier
    private boolean checkValidArgs(List<String> argsEvent){

        //if does not have multiple args
        if (!hasArgs(argsEvent)){
            return false;
        }

        //if is not a valid modifier
        if (!args.containsKey(argsEvent.get(1))){
            return false;
        }

        //if the length of args does not equal the modifier args
        String commandKey = argsEvent.get(1);
        if (!(args.get(commandKey).length == argsEvent.size() - 2)){
            return false;
        }

        return true;
    }


    //checks if there is 2 or more args
    private boolean hasArgs(List<String> strings){
        if (strings.size() < 2){
            return false;
        }
        return true;
    }


    //TODO make more efficient

    /**
     * Reads strings in "" to be one input
     * @param event
     * @return
     */
    public List<String> getStringsFromEvent(MessageReceivedEvent event){
        //only spaces
        //event.getMessage().getContent().toLowerCase().split(" ");

        List<String> list = new ArrayList<String>();
        Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(event.getMessage().getContent().toLowerCase());

        while (m.find()) {
            //System.out.println(m.group(1).replace("\"", ""));
            list.add(m.group(1).replace("\"", ""));
        }

        return list;

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

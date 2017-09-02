package src.io.catbot.commands;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.utils.PermissionUtil;
import src.io.catbot.con.CatBot;
import src.io.catbot.db.DBConnector;
import src.io.catbot.listeners.CommandListener;

import java.sql.ResultSet;
import java.util.List;

/**
 * Created by jason on 8/18/17.
 */
public class RetortManagerCommand extends CommandListener{

    private static final int MAX_RETORTS_PER_SERVER = 50;

    public RetortManagerCommand(){
        super();
        setArgOptional(true);
        setArgs("list", new String[]{});
        setArgs("toggle", new String[]{});
        setArgs("add", new String[]{"trigger", "retort"});
        setArgs("remove", new String[]{"trigger"});
        addAdminCommand("add");
        addAdminCommand("toggle");
        addAdminCommand("remove");


        addAlias("retort");
        addAlias("r");
        setDescription("Set a custom retort");
    }

    @Override
    public void doCommand(MessageReceivedEvent event, List<String> argsEvent) {
        if (argsEvent.size() == 1){
            listRetorts(event);
        }
        else if (argsEvent.get(1).equals("add")){
            addRetorts(event, argsEvent);
        }
        else if(argsEvent.get(1).equals("list")){
            listRetorts(event);
        }
        else if(argsEvent.get(1).equals("toggle")){
            toggleRetorts(event);
        }
    }

    private void listRetorts(MessageReceivedEvent event){
        String id = event.getGuild().getId();
        String sqlString =
                "select TriggerString, Message " +
                "from Retorts " +
                "where ServerID ='" + id + "';";
        ResultSet rs = CatBot.getInstance().sendSQLStatement(sqlString);
        printList(rs, event);

    }

    private void addRetorts(MessageReceivedEvent event, List<String> argsEvent){
        int count = checkRetortCount(event);
        if (count <= MAX_RETORTS_PER_SERVER){
            String insertString = "insert into Retorts (ServerID, TriggerString, Message) "+
                    "values ('" + event.getGuild().getId() + "', '" +
                    argsEvent.get(2) + "', '" +
                    argsEvent.get(3) + "');";
            try{
                CatBot.getInstance().sendSQLUpdate(insertString);
                event.getTextChannel().sendMessage(
                        "\"" + argsEvent.get(2) + "\" with response \"" + argsEvent.get(3) + "\" added").queue();

            }
            catch (Exception e){
                e.printStackTrace();
            }

        }


    }

    private void toggleRetorts(MessageReceivedEvent event){
        event.getTextChannel().sendMessage("toggle").queue();
    }

    private int checkRetortCount(MessageReceivedEvent event){
        String id = event.getGuild().getId();
        String sqlString =
                "select count(TriggerString) " +
                        "from Retorts " +
                        "where ServerID ='" + id + "';";
        ResultSet rs = CatBot.getInstance().sendSQLStatement(sqlString);

        try {
            rs.next();

            //System.out.println(rs.getString(1));

            return rs.getInt(1);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return -1;
    }


    private void printList(ResultSet rs, MessageReceivedEvent event){
        try {
            String s = "```\nResponse List for " + event.getGuild().getName() + ":\n\n";
            while (rs.next()){
                s = s + rs.getString("TriggerString") + ": " + rs.getString("Message") + "\n";
            }
            s = s + "```";

            event.getTextChannel().sendMessage(s).queue();

            rs.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

}

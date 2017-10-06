package src.io.catbot.commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import src.io.catbot.con.CatBot;
import src.io.catbot.listeners.CommandListener;

import java.sql.PreparedStatement;
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
        setArgs("exact", new String[]{"trigger"});
        addAdminCommand("add");
        addAdminCommand("toggle");
        addAdminCommand("exact");
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
        else if (argsEvent.get(1).equals("remove")){
            removeRetorts(event, argsEvent);
        }
        else if(argsEvent.get(1).equals("list")){
            listRetorts(event);
        }
        else if(argsEvent.get(1).equals("toggle")){
            toggleRetorts(event);
        }
        else if(argsEvent.get(1).equals("exact")){
            toggleExact(event, argsEvent);
        }
    }


    private void toggleExact(MessageReceivedEvent event, List<String> argsEvent){

        PreparedStatement sqlString = createStatement("select IsExact " +
                "from Retorts "+
                "where ServerID=? and " +
                "TriggerString=?;");

        try{
            sqlString.setString(1, event.getGuild().getId());
            sqlString.setString(2, argsEvent.get(2));

            ResultSet rs = CatBot.getInstance().sendSQLStatement(sqlString);

            if (rs.next()) {
                PreparedStatement updateString;

                if (rs.getBoolean("IsExact")){
                    updateString = createStatement("update Retorts " +
                            "set IsExact=0 "+
                            "where ServerID=? and " +
                            "TriggerString=?;");

                    updateString.setString(1, event.getGuild().getId());
                    updateString.setString(2, argsEvent.get(2));

                    CatBot.getInstance().sendSQLUpdate(updateString);
                    event.getTextChannel().sendMessage(
                            "\"" + argsEvent.get(2) + "\" trigger will now execute anytime it is typed").queue();
                }
                else{
                    updateString = createStatement("update Retorts " +
                            "set IsExact=1 "+
                            "where ServerID=? and " +
                            "TriggerString=?;");

                    updateString.setString(1, event.getGuild().getId());
                    updateString.setString(2, argsEvent.get(2));

                    CatBot.getInstance().sendSQLUpdate(updateString);
                    event.getTextChannel().sendMessage(
                            "\"" + argsEvent.get(2) + "\" trigger will now execute if it is typed exactly").queue();
                }


            }
            else {
                event.getTextChannel().sendMessage(
                        "\"" + argsEvent.get(2) + "\" doesn't exist").queue();
            }


        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    private void listRetorts(MessageReceivedEvent event){
        String id = event.getGuild().getId();
        PreparedStatement sqlString = createStatement(
                "select TriggerString, Message, IsExact " +
                "from Retorts " +
                "where ServerID =?;");

        try{
            sqlString.setString(1, id);
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        ResultSet rs = CatBot.getInstance().sendSQLStatement(sqlString);
        printList(rs, event);

    }

    private void removeRetorts(MessageReceivedEvent event, List<String> argsEvent){
        PreparedStatement insertString = createStatement("delete from Retorts "+
                "where  ServerID=? and " +
                "TriggerString=?;");



        PreparedStatement sqlString = createStatement(
                "select TriggerString, ServerID " +
                        "from Retorts " +
                        "where TriggerString =?;");
        try{
            insertString.setString(1, event.getGuild().getId());
            insertString.setString(2, argsEvent.get(2));
            sqlString.setString(1, argsEvent.get(2));

            ResultSet rs = CatBot.getInstance().sendSQLStatement(sqlString);
            if (rs.next()){
                CatBot.getInstance().sendSQLUpdate(insertString);
                event.getTextChannel().sendMessage(
                        "\"" + argsEvent.get(2) + "\" retort removed").queue();
            }
            else {
                event.getTextChannel().sendMessage(
                        "\"" + argsEvent.get(2) + "\" doesn't exist").queue();
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void addRetorts(MessageReceivedEvent event, List<String> argsEvent){
        int count = getRetortCount(event);
        if (count < getMaxRetorts(event)){
            PreparedStatement insertString = createStatement("insert into Retorts (ServerID, TriggerString, Message) "+
                    "values (?, ?, ?);");
            try{
                insertString.setString(1, event.getGuild().getId());
                insertString.setString(2, argsEvent.get(2).toLowerCase());
                insertString.setString(3, argsEvent.get(3));

                CatBot.getInstance().sendSQLUpdate(insertString);
                event.getTextChannel().sendMessage(
                        "\"" + argsEvent.get(2) + "\" with response \"" + argsEvent.get(3) + "\" added").queue();

            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        else {
            event.getTextChannel().sendMessage("Max number of retorts reached! Please remove some before adding more.").queue();
        }
    }

    private void toggleRetorts(MessageReceivedEvent event){
        PreparedStatement sqlString = createStatement("select IsCommandOn " +
                "from Servers "+
                "where ServerID=?;");

        try{
            sqlString.setString(1, event.getGuild().getId());

            ResultSet rs = CatBot.getInstance().sendSQLStatement(sqlString);
            if (rs.next()) {
                PreparedStatement updateString;

                if (rs.getBoolean("IsCommandOn")){
                    updateString = createStatement("update Servers " +
                            "set IsCommandOn=0 "+
                            "where ServerID=?;");

                    updateString.setString(1, event.getGuild().getId());

                    CatBot.getInstance().sendSQLUpdate(updateString);
                    event.getTextChannel().sendMessage("Retorts for " + event.getGuild().getName() + " turned off").queue();
                }
                else {
                    updateString = createStatement("update Servers " +
                            "set IsCommandOn=1 "+
                            "where ServerID=?;");

                    updateString.setString(1, event.getGuild().getId());

                    CatBot.getInstance().sendSQLUpdate(updateString);
                    event.getTextChannel().sendMessage("Retorts for " + event.getGuild().getName() + " turned on").queue();
                }
            }
            else {
                throw new Exception("Server: " + event.getGuild().getId() + " doesn't exist in util");
            }


        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private int getMaxRetorts(MessageReceivedEvent event){
        PreparedStatement sqlString = createStatement("select MaxRetorts " +
                        "from Servers " +
                        "where ServerID=?;");

        try{
            sqlString.setString(1, event.getGuild().getId());

            ResultSet rs = CatBot.getInstance().sendSQLStatement(sqlString);
            rs.next();
            return rs.getInt("MaxRetorts");
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return -1;

    }

    private int getRetortCount(MessageReceivedEvent event){
        String id = event.getGuild().getId();
        PreparedStatement sqlString = createStatement(
                "select count(TriggerString) " +
                        "from Retorts " +
                        "where ServerID=?;");

        try {
            sqlString.setString(1, id);

            ResultSet rs = CatBot.getInstance().sendSQLStatement(sqlString);
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
            String s = "```\nResponse List for " + event.getGuild().getName() +
                    " (" + getRetortCount(event) + "/" + getMaxRetorts(event) + ")" +
                    ":\n\n";
            while (rs.next()){
                s = s + rs.getString("TriggerString") + ": " + rs.getString("Message");
                if (!rs.getBoolean("IsExact")){
                    s = s + " (Not Exact)";
                }

                s = s + "\n";
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

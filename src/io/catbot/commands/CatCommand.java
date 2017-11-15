package src.io.catbot.commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import src.io.catbot.con.CatBot;
import src.io.catbot.listeners.CommandListener;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

/**
 * Created by jason on 11/14/17.
 */
public class CatCommand extends CommandListener{

    public CatCommand(){
        super();
        setArgOptional(true);
        setArgs("new", new String[]{});
        setArgs("status",new String[]{});
        setArgs("feed", new String[]{});
        addAlias("cat");
        setDescription("Pet commands for the server");
    }


    //TODO set name, hunger, happiness, extras

    public void doCommand(MessageReceivedEvent event, List<String> argsEvent) {
        if (argsEvent.size() == 1){
            showStatus(event);
        }
        else if (argsEvent.get(1).equals("status")){
            showStatus(event);
        }
        else if (argsEvent.get(1).equals("new")){
            makeNewCat(event, argsEvent);
        }
        else if (argsEvent.get(1).equals("feed")){
            feedCat(event, argsEvent);
        }
    }


    private void showStatus(MessageReceivedEvent event){
        String id = event.getGuild().getId();

        PreparedStatement sqlString = createStatement(
                "select * " +
                "from Pets "+
                "where ServerID=?");

        try{
            sqlString.setString(1, id);

            ResultSet rs = CatBot.getInstance().sendSQLStatement(sqlString);

            //TODO prefix
            if(!rs.next()) {
                event.getTextChannel().sendMessage("None exist for " + event.getGuild().getName() +
                        ", do command " +"+cat new").queue();
            }



        }
        catch (Exception e){
            e.printStackTrace();
        }


    }


    private void makeNewCat(MessageReceivedEvent event, List<String> argsEvent){

    }

    private void feedCat(MessageReceivedEvent event, List<String> argsEvent) {

    }


}

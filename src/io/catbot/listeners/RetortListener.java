package src.io.catbot.listeners;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import src.io.catbot.con.CatBot;
import src.io.catbot.con.JsonHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by jason on 8/4/17.
 */
public class RetortListener extends ListenerWrapper {

    public RetortListener(){}

    /**
     * Checks if message contains a retort trigger and if it does, send retort for each trigger
     * @param event
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        //TODO make more efficient

        //if self
        if (event.getAuthor().getAvatarId().equals("bc3d22581e06d67c618670e28da48d01")){
            return;
        }

        String msg = event.getMessage().getContent().toLowerCase();
        ResultSet rs = getRetortSet(event);
        doRetort(event, msg, rs);

    }

    public void doRetort(MessageReceivedEvent event, String msg, ResultSet rs){

        try {
            //loops through and checks triggers
            while (rs.next()){

                //if exact and msg equals string
                if (rs.getBoolean("IsExact")){
                    if (msg.equals(rs.getString("TriggerString"))){
                        event.getTextChannel().sendMessage(rs.getString("Message")).queue();
                    }
                }
                else if (!rs.getBoolean("IsExact")){
                    if (msg.contains(rs.getString("TriggerString"))){
                        event.getTextChannel().sendMessage(rs.getString("Message")).queue();
                    }
                }
            }

            rs.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    public ResultSet getRetortSet(MessageReceivedEvent event){
        String id = event.getGuild().getId();
        PreparedStatement ps = createStatement("select TriggerString, Message, IsExact " +
                        "from Retorts " +
                        "where ServerID=?;");
        try{
            ps.setString(1, id);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return sendSQLStatement(ps);
    }




}

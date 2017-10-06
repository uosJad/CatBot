package src.io.catbot.listeners;

import net.dv8tion.jda.core.hooks.ListenerAdapter;
import src.io.catbot.con.CatBot;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by jason on 10/3/17.
 */
public class ListenerWrapper extends ListenerAdapter{
    public PreparedStatement createStatement(String s){
        return CatBot.getInstance().createStatement(s);
    }

    public ResultSet sendSQLStatement(PreparedStatement ps){
        return CatBot.getInstance().sendSQLStatement(ps);
    }

    public void sendSQLUpdate(PreparedStatement ps){
        CatBot.getInstance().sendSQLUpdate(ps);
    }

}

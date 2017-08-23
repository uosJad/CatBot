package src.io.catbot.con;

import net.dv8tion.jda.core.entities.Guild;
import org.json.JSONObject;
import src.io.catbot.db.DBConnector;
import src.io.catbot.db.SQLReader;

import java.util.Iterator;

/**
 * Created by jason on 8/1/17.
 */
public class Driver {

    public static void main(String[] args){

        JsonHandler jsh = new JsonHandler("catbotcredentials.json"); // or to credentials.json
        CatBot.getInstance().setToken((String)jsh.getValueFromKey("Token"));
        CatBot.getInstance().setJDA();
        CatBot.getInstance().setDBConnection();
        //CatBot.getInstance().sendSQLStatement("select * from Retorts");

        SQLReader sqlr = new SQLReader("sql/test.sql");
        String s = sqlr.getFileString();
    }

}

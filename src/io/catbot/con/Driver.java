package src.io.catbot.con;

/**
 * Created by jason on 8/1/17.
 */
public class Driver {

    public static void main(String[] args){

        JsonHandler jsh = new JsonHandler("catbotcredentials.json"); // or to credentials.json
        CatBot.getInstance().setToken((String)jsh.getValueFromField("Token"));
        CatBot.getInstance().setJDA();
    }
}

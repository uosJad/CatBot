package src.io.catbot;

import src.io.catbot.con.CatBotSingleton;
import src.io.catbot.con.JSonHandler;

/**
 * Created by jason on 8/1/17.
 */
public class Driver {

    public static void main(String[] args){

        JSonHandler jsh = new JSonHandler("catbotcredentials.json");
        CatBotSingleton.getInstance().setToken((String)jsh.get("Token"));
        CatBotSingleton.getInstance().setJDA();
    }
}

package src.io.catbot;

import src.io.catbot.con.CatBotSingleton;

/**
 * Created by jason on 8/1/17.
 */
public class Driver {

    public static void main(String[] args){
        CatBotSingleton.getInstance().setToken("");
        CatBotSingleton.getInstance().setJDA();
    }
}

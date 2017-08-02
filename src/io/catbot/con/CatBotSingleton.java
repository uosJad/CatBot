package src.io.catbot.con;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import src.io.catbot.lis.ReadyListener;

/**
 * Created by jason on 8/2/17.
 */
public class CatBotSingleton {
    private static CatBotSingleton instance;

    private JDA catBot;
    private String token;

    private CatBotSingleton() {}

    public JDA getCatBot() {
        return catBot;
    }

    public void setToken(String s){
        token = s;
    }

    public static CatBotSingleton getInstance() {
        if (instance == null){
            instance = new CatBotSingleton();
        }
        return instance;
    }

    public void setJDA(){
        catBot = generateJDA();
    }

    private JDA generateJDA(){

        try{
            // Note: It is important to register your ReadyListener before building
            JDA jda = new JDABuilder(AccountType.BOT)
                    .setToken(token)
                    .addEventListener(new ReadyListener())
                    .buildBlocking();
            return jda;
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return null;

    }

}

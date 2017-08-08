package src.io.catbot.con;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import src.io.catbot.commands.AboutCommand;
import src.io.catbot.commands.DefaultCommand;
import src.io.catbot.commands.HelpCommand;
import src.io.catbot.listeners.ReadyListener;
import src.io.catbot.listeners.MessageListener;
import src.io.catbot.listeners.RetortListener;

/**
 * Created by jason on 8/2/17.
 */
public class CatBot {
    private static CatBot instance;

    private JDA catBot;
    private String token;

    private CatBot() {}

    public JDA getCatBot() {
        return catBot;
    }

    public void setToken(String s){
        token = s;
    }

    public static CatBot getInstance() {
        if (instance == null){
            instance = new CatBot();
        }
        return instance;
    }

    public void setJDA(){
        catBot = generateJDA();
    }

    private JDA generateJDA(){

        try{
            // Note: It is important to register your ReadyListener before building
            JDABuilder jdaBuilder = new JDABuilder(AccountType.BOT)
                    .setToken(token)
                    .addEventListener(new ReadyListener())
                    .addEventListener(new MessageListener());

            jdaBuilder = addCommands(jdaBuilder);

            return jdaBuilder.buildBlocking();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    private JDABuilder addCommands(JDABuilder jdaBuilder){
        jdaBuilder
                .addEventListener(new RetortListener())
                .addEventListener(new DefaultCommand())
                .addEventListener(new AboutCommand())
                .addEventListener(new HelpCommand());
        return jdaBuilder;
    }

}

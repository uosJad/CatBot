package src.io.catbot.con;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Guild;
import src.io.catbot.commands.*;
import src.io.catbot.db.DBConnector;
import src.io.catbot.listeners.JoinedGuildListener;
import src.io.catbot.listeners.ReadyListener;
import src.io.catbot.listeners.MessageListener;
import src.io.catbot.listeners.RetortListener;

import java.sql.ResultSet;
import java.util.Iterator;
import java.util.List;

/**
 * Created by jason on 8/2/17.
 */
public class CatBot {
    private static CatBot instance;

    private DBConnector connection;
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
        checkGuilds();
    }

    //TODO
    private void checkGuilds(){
        List<Guild> guildList = catBot.getGuilds();
        Iterator<Guild> it = guildList.iterator();

        while(it.hasNext()){
            Guild guild = it.next();
            checkGuildInDB(guild);
        }
    }

    public void checkGuildInDB(Guild guild){
        String sqlString = "select ServerID from Servers where ServerID='" + guild.getId() + "';";
        ResultSet rs = sendSQLStatement(sqlString);
        boolean hasServerID = false;

        try {
            while (rs.next()) {
                if (rs.getString("ServerID").equals(guild.getId())) {
                    hasServerID = true;
                    break;
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

        // if server does not exist in database
        if (!hasServerID){
            insertNewServer(guild);
        }
        else {
            System.out.println(guild.getId() + " in database");
        }
    }

    private void insertNewServer(Guild guild){
        String updateString = "insert into Servers (ServerID, IsCommandOn)" +
                "values (" + guild.getId() + ", true);";
        System.out.println(guild.getId() + " doesn't exist in database. Adding...");
        sendSQLUpdate(updateString);
    }


    public void setDBConnection(){
        connection = new DBConnector();
        JsonHandler jsh = new JsonHandler("data/catbotdb.json"); // or to data/db.json
        connection.setConnString(jsh.getValueFromKey("Host"),
                jsh.getValueFromKey("Database"),
                jsh.getValueFromKey("User"),
                jsh.getValueFromKey("Pass"));
        //connection.sendStatement("Select * from test;");
    }

    public ResultSet sendSQLStatement(String s){
        return connection.sendStatement(s);
    }

    public void sendSQLUpdate(String s){
        connection.sendUpdate(s);
    }

    private JDA generateJDA(){

        try{
            // Note: It is important to register your ReadyListener before building
            JDABuilder jdaBuilder = new JDABuilder(AccountType.BOT)
                    .setToken(token)
                    .addEventListener(new ReadyListener())
                    .addEventListener(new MessageListener())
                    .addEventListener(new JoinedGuildListener());

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
                .addEventListener(new HelpCommand())
                .addEventListener(new AdminHelpCommand())
                .addEventListener(new RetortManagerCommand());
        return jdaBuilder;
    }

}

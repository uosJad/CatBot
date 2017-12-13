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
public class PetCommand extends CommandListener{

    public PetCommand(){
        super();
        setArgOptional(true);
        setArgs("new", new String[]{"name"});
        setArgs("release", new String[]{"name"});
        setArgs("status",new String[]{});
        setArgs("feed", new String[]{});
        addAdminCommand("new");
        addAdminCommand("release");
        addAlias("cat");
        addAlias("pet");
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
        else if (argsEvent.get(1).equals("release")){
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

        try {
            sqlString.setString(1, id);

            ResultSet rs = CatBot.getInstance().sendSQLStatement(sqlString);

            //TODO prefix

            //if no pets
            if (!rs.next()) {
                event.getTextChannel().sendMessage("None exist for " + event.getGuild().getName() +
                        ", do command +cat new").queue();
                return;
            }

            String statusString = "```css\n";

            do{

                String petName = rs.getString("PetName");
                int happiness = rs.getInt("Happiness");
                long hunger = calculateHunger(event, petName, rs.getLong("TimeLastFeed"), System.currentTimeMillis());

                statusString = statusString + petName + ":" +
                        "\n\t- Happiness: " + happiness +
                        "\n\t- Fullness: " + hunger + "/100" +
                        "\n";


            } while (rs.next());

            statusString = statusString + "```";


            event.getTextChannel().sendMessage(statusString).queue();

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Creates a pet
     * @param event
     * @param argsEvent
     */
    private void makeNewCat(MessageReceivedEvent event, List<String> argsEvent){
        int count = getPetCount(event);
        if (count < getMaxPets(event)){
            PreparedStatement insertString = createStatement("insert into Pets (ServerID, PetName) "+
                    "values (?, ?);");
            try{
                insertString.setString(1, event.getGuild().getId());
                insertString.setString(2, argsEvent.get(2).toLowerCase());

                CatBot.getInstance().sendSQLUpdate(insertString);

                event.getTextChannel().sendMessage(
                        "New pet " + argsEvent.get(2) + " added for" + event.getGuild().getName() + "!").queue();

            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        else {
            event.getTextChannel().sendMessage("Max number of Pets reached! Please release some before adding more.").queue();
        }
    }

    private int getPetCount(MessageReceivedEvent event){
        String id = event.getGuild().getId();
        PreparedStatement sqlString = createStatement(
                "select count(PetName) " +
                "from Pets " +
                "where ServerID=?");

        try {
            sqlString.setString(1, id);

            ResultSet rs = CatBot.getInstance().sendSQLStatement(sqlString);
            rs.next();

            return rs.getInt(1);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return -1;

    }

    private int getMaxPets(MessageReceivedEvent event){
        String id = event.getGuild().getId();
        PreparedStatement sqlString = createStatement(
                "select MaxPets " +
                        "from Servers " +
                        "where ServerID=?");

        try {
            sqlString.setString(1, id);

            ResultSet rs = CatBot.getInstance().sendSQLStatement(sqlString);
            rs.next();

            return rs.getInt("MaxPets");
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return -1;

    }

    private void feedCat(MessageReceivedEvent event, List<String> argsEvent) {

    }



    //TODO TEST THIS
    /**
     * returns and updates the hunger of the specified pet
     * @param event
     * @param petName
     * @param lastTime
     * @param currTime
     * @return
     */
    private long calculateHunger(MessageReceivedEvent event, String petName, long lastTime, long currTime){

        String id = event.getGuild().getId();

        long seconds = (currTime - lastTime)/1000;

        PreparedStatement sqlString = createStatement(
                "select Hunger " +
                        "from Pets " +
                        "where ServerID=? " +
                        "and PetName=?;");

        PreparedStatement updateString = createStatement(
                "update Pets " +
                        "set Hunger=? " +
                        "where ServerID=? " +
                        "and PetName=?;");

        try {
            sqlString.setString(1, id);
            sqlString.setString(2, petName);

            ResultSet rs = CatBot.getInstance().sendSQLStatement(sqlString);

            //TODO externalize offset
            rs.next();
            long hunger = rs.getLong("Hunger") - seconds/300;

            //if hunger less than 0, set to 0
            if (lastTime < 0 || hunger < 0){

                updateString.setLong(1, 0);
                updateString.setString(2, id);
                updateString.setString(3, petName);

                CatBot.getInstance().sendSQLUpdate(updateString);
                return 0;
            }
            else {
                updateString.setLong(1, hunger);
                updateString.setString(2, id);
                updateString.setString(3, petName);

                CatBot.getInstance().sendSQLUpdate(updateString);
                return hunger;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return -1;
    }

}

package src.io.catbot.listeners;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import src.io.catbot.con.JsonHandler;

import java.util.Iterator;
import java.util.Set;

/**
 * Created by jason on 8/4/17.
 */
public class RetortListener extends ListenerAdapter {

    private JsonHandler retorts;

    public RetortListener(){
        retorts = new JsonHandler("data/retorts.json");
    }

    /**
     * Checks if message contains a retort trigger and if it does, send retort for each trigger
     * @param event
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        //TODO make more efficient

        if (event.getAuthor().getAvatarId().equals("bc3d22581e06d67c618670e28da48d01")){
            return;
        }

        String msg = event.getMessage().getContent().toLowerCase();

        Set<String> keys = retorts.getKeys();
        Iterator<String> it = keys.iterator();

        while(it.hasNext()){
            String temp = it.next();
            if (msg.contains(temp)){
                doRetort(event, temp);
            }
        }

    }

    public void doRetort(MessageReceivedEvent event, String key){
        //System.out.print("testset");
        event.getTextChannel().sendMessage(retorts.getValueFromField(key)).queue();
    }

}

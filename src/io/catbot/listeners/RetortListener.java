package src.io.catbot.listeners;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import src.io.catbot.con.JsonHandler;

/**
 * Created by jason on 8/4/17.
 */
public class RetortListener extends ListenerAdapter {

    private JsonHandler retorts;

    public RetortListener(){
        retorts = new JsonHandler("data/retorts.json");
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        if (actionExists(event)) {
            doRetort(event);
        }
    }

    public void doRetort(MessageReceivedEvent event){
        String msg = event.getMessage().getContent();
        event.getTextChannel().sendMessage(retorts.getValueFromField(msg)).queue();

    }

    public boolean actionExists(MessageReceivedEvent event) {
        return retorts.containsField(event.getMessage().getContent());

    }




}

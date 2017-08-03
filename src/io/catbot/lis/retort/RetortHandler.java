package src.io.catbot.lis.retort;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import src.io.catbot.con.JSonHandler;

/**
 * Created by jason on 8/2/17.
 */
public class RetortHandler {

    private MessageReceivedEvent event;
    private JSonHandler jsh;


    public RetortHandler(MessageReceivedEvent e){
        event = e;
        jsh = new JSonHandler("data/retorts.json");
    }

    public void relay(){
        String msg = event.getMessage().getContent();

        if (jsh.containsField(msg)){
            //TODO post retort
            System.out.println(jsh.get(msg));
        }
    }



}

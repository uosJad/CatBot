package src.io.catbot.commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import src.io.catbot.listeners.CommandListener;

/**
 * Created by jason on 8/7/17.
 */
public class AboutCommand extends CommandListener {

    public AboutCommand(){
        super();
        setArgs(new String[]{});
        addAlias("about");
        setDescription("Info about me");
    }


    public void doCommand(MessageReceivedEvent event, String[] args) {
        event.getTextChannel().sendMessage(getInfoString()).queue();
    }

    private String getInfoString(){
        String str =
                "**Developed by uosJad (aka Person):**\n"+
                "It's a cat or something. Made with 10% fun and 90% sass\n"+
                "Built in Java using JDA libraries\n\n" +
                "**Github:**\nhttps://github.com/uosJad/CatBot\n\n" +
                "**JDA:**\nhttps://github.com/DV8FromTheWorld/JDA\n\n" +
                "**Special Thanks to:**\n" +
                "TODO";

        return str;
    }


}

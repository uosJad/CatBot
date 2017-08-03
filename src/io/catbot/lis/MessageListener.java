package src.io.catbot.lis;

import net.dv8tion.jda.client.entities.Group;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import src.io.catbot.con.JSonHandler;
import src.io.catbot.lis.retort.RetortHandler;

/**
 * Created by jason on 8/2/17.
 */
public class MessageListener extends ListenerAdapter {
    protected String prefix;


    public MessageListener(){
        JSonHandler jsh = new JSonHandler("config.json");
        prefix = (String)jsh.get("CommandPrefix");
    }





    @Override
    public void onMessageReceived(MessageReceivedEvent event) {



        JDA jda = event.getJDA();                       //JDA, the core of the api.
        long responseNumber = event.getResponseNumber();//The amount of discord events that JDA has received since the last reconnect.
        User author = event.getAuthor();
        Message message = event.getMessage();
        MessageChannel channel = event.getChannel();
        String msg = message.getContent();
        boolean bot = author.isBot();

        RetortHandler retortHandler = new RetortHandler(event);
        retortHandler.relay();


        if (isCommand(event.getMessage().getContent())){
            System.out.println("is command");
        }



        if (event.isFromType(ChannelType.TEXT)){

            Guild guild = event.getGuild();
            TextChannel textChannel = event.getTextChannel();
            Member member = event.getMember();

            String name;
            if (message.isWebhookMessage()) {
                name = author.getName();                //If this is a Webhook message, then there is no Member associated
            }                                           // with the User, thus we default to the author for name.
            else {
                name = member.getEffectiveName();
            }

            System.out.printf("(%s)[%s]<%s>: %s\n", guild.getName(), textChannel.getName(), name, msg);
        }
        else if (event.isFromType(ChannelType.PRIVATE)) {

            PrivateChannel privateChannel = event.getPrivateChannel();

            System.out.printf("[PRIV]<%s>: %s\n", author.getName(), msg);
        }

        else if (event.isFromType(ChannelType.GROUP)){

                Group group = event.getGroup();
                String groupName = group.getName() != null ? group.getName() : "";

                System.out.printf("[GRP: %s]<%s>: %s\n", groupName, author.getName(), msg);
            }

    }

    protected boolean isCommand(String s){
        if (Character.toString(s.charAt(0)).equals(prefix)){
            return true;
        }
        return false;
    }


}

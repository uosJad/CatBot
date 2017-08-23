package src.io.catbot.listeners;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.ReconnectedEvent;
import net.dv8tion.jda.core.events.ResumedEvent;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.Iterator;
import java.util.List;

/**
 * Created by jason on 8/20/17.
 */
public class JoinedGuildListener extends ListenerAdapter{

    public JoinedGuildListener(){}

    public void onGuildJoin(GuildJoinEvent event){
        Guild guild = event.getGuild();
        System.out.println(guild.getId());
    }



}

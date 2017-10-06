package src.io.catbot.listeners;

import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

//TODO
public class UserJoinListener extends ListenerAdapter{
    public UserJoinListener(){}

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        String s = getJoinedMessage(event);
    }

    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
        String s = getLeaveMessage(event);
    }

    public String getJoinedMessage(GuildMemberJoinEvent event){
        return null;
    }

    public String getLeaveMessage(GuildMemberLeaveEvent event){
        return null;
    }
}

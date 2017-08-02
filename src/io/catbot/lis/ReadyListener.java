package src.io.catbot.lis;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.hooks.EventListener;

import javax.security.auth.login.LoginException;

/**
 * Created by jason on 8/1/17.
 */
public class ReadyListener implements EventListener {

    public void onEvent(Event event) {
        if (event instanceof ReadyEvent)
            System.out.println("API is ready!");
    }
}
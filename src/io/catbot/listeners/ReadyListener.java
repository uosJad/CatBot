package src.io.catbot.listeners;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.EventListener;

import java.util.Iterator;
import java.util.List;

/**
 * Created by jason on 8/1/17.
 */
public class ReadyListener implements EventListener {

    public void onEvent(Event event) {
        if (event instanceof ReadyEvent) {
            System.out.println("API is ready!");
        }
    }
}
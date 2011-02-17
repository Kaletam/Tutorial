package org.bukkit.awesomepants.Tutorial;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Handle events for all Player related events
 * @author awesomepants
 */
public class TutorialPlayerListener extends PlayerListener {
    private final Tutorial plugin;

    public TutorialPlayerListener(Tutorial instance) {
        plugin = instance;
    }

    //Insert Player related code here
}


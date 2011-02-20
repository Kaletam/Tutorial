package com.chrissquared.awesomepants.tutorial;

import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.entity.*;

/**
 *
 * Handle events for all Entity related events
 * @author awesomepants
 *
 */
public class TutorialEntityListener extends EntityListener
{
    private final Tutorial plugin;

    public TutorialEntityListener(Tutorial instance)
    {
	plugin = instance;
    }
}

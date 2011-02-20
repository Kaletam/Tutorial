package com.chrissquared.awesomepants.tutorial;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.Material;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPhysicsEvent;

/**
 * Tutorial block listener
 * @author awesomepants
 */
public class TutorialBlockListener extends BlockListener
{
    private final Tutorial plugin;

    public TutorialBlockListener(final Tutorial plugin)
    {
	this.plugin = plugin;
    }
    //put all Block related code here
}

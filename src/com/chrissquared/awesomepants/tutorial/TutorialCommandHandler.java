package com.chrissquared.awesomepants.tutorial;

// Java imports
import java.util.List;
import java.util.logging.Level;

// Bukkit imports
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Server;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.World;
import org.bukkit.Material;

/**
 *
 * @author Kaletam
 * 
 */
public class TutorialCommandHandler
{
    CommandSender sender;
    Command command;
    String[] args;
    JavaPlugin plugin;

    public TutorialCommandHandler(CommandSender sender, Command command, String[] args, JavaPlugin plugin)
    {
	this.sender = sender;
	this.command = command;
	this.args = args;
	this.plugin = plugin;
    }

    // We'll eventually rework this to output some of its own usage details, instead of defaulting to plugin.yml's data.
    public boolean Execute()
    {
	// This method for getting subcommands stolen from HotSwap.
	SubCommands sub = null;

	try
	{
	    sub = SubCommands.valueOf(args[0].toUpperCase());
	}
	catch (Exception ex) // Don't actually do anything, just return false (triggering display of usage as per plugin.yml).
	{
	    return false;
	}

	Tutorial.log.log(Level.INFO, String.format("SubCommand: %s", sub));

	if ((sender instanceof Player)) // If executed by the player.
	{
	    switch (sub)
	    {
		case PLAYER:
		    return ExecutePlayerCommand();
		case WORLD:
		    return ExecuteWorldCommand();
	    }
	}
	else
	{
	    // Do nothing right now. Eventually implement console commands... maybe. Since this is just a tutorial, we might just demonstrate how the console differs from a Player's Command.
	}

	return false;
    }

    private boolean ExecutePlayerCommand()
    {
	Player p = (Player) sender;

	SubCommands.PlayerSubCommands subsub = null;

	String format = "";

	try
	{
	    subsub = SubCommands.PlayerSubCommands.valueOf(args[1].toUpperCase());
	}
	catch (Exception ex) // Don't actually do anything, just return false (triggering display of usage as per plugin.yml).
	{
	    return false;
	}

	String targetName = "";

	if (args.length > 2)
	{
	    targetName = args[2];
	}

	switch (subsub)
	{
	    case HEAL:
		p.setHealth(20);

		return true;
	    case GET:
		if (targetName == "")
		{
		    return false;
		}

		if (plugin.getServer().getPlayer(targetName) != null)
		{
		    format = String.format("%s is a Player.", targetName);
		}
		else
		{
		    format = String.format("%s is not a Player.", targetName);
		}

		Tutorial.log.log(Level.INFO, format);

		return true;
	    case GETBLANK:
		// This either gets the current player or the first player. Must test further.
		if (plugin.getServer().getPlayer("") != null)
		{
		    p = plugin.getServer().getPlayer("");

		    format = String.format("Name: %s; IP: %s", p.getName(), p.getAddress());
		}
		else
		{
		    format = String.format("%s is not a Player.", targetName);
		}

		Tutorial.log.log(Level.INFO, format);

		return true;
	    case LIST:
		Player[] pg = plugin.getServer().getOnlinePlayers();

		p.sendMessage("The following players are online:");

		for (Player pm : pg)
		{
		    p.sendMessage(pm.getName());
		}

		return true;
	    case MATCH:
		if (targetName == "")
		{
		    return false;
		}

		p.sendMessage(String.format("The following players match '%s':", targetName));

		List<Player> players = plugin.getServer().matchPlayer(targetName);

		for (Player pm : players)
		{
		    p.sendMessage(pm.getName());
		}

		return true;
	}

	return false;
    }

    private boolean ExecuteEntityCommand()
    {
	SubCommands.PlayerSubCommands subsub = null;

	try
	{
	    subsub = SubCommands.PlayerSubCommands.valueOf(args[1].toUpperCase());
	}
	catch (Exception ex) // Don't actually do anything, just return false (triggering display of usage as per plugin.yml).
	{
	    return false;
	}

	switch (subsub)
	{
	}

	return false;
    }

    private boolean ExecuteLivingEntityCommand()
    {
	SubCommands.PlayerSubCommands subsub = null;

	try
	{
	    subsub = SubCommands.PlayerSubCommands.valueOf(args[1].toUpperCase());
	}
	catch (Exception ex) // Don't actually do anything, just return false (triggering display of usage as per plugin.yml).
	{
	    return false;
	}

	switch (subsub)
	{
	}

	return false;
    }

    private boolean ExecuteServerCommand()
    {
	SubCommands.PlayerSubCommands subsub = null;

	try
	{
	    subsub = SubCommands.PlayerSubCommands.valueOf(args[1].toUpperCase());
	}
	catch (Exception ex) // Don't actually do anything, just return false (triggering display of usage as per plugin.yml).
	{
	    return false;
	}

	switch (subsub)
	{
	}

	return false;
    }

    private boolean ExecuteWorldCommand()
    {
	SubCommands.WorldSubCommands subsub = null;

	try
	{
	    subsub = SubCommands.WorldSubCommands.valueOf(args[1].toUpperCase());
	}
	catch (Exception ex) // Don't actually do anything, just return false (triggering display of usage as per plugin.yml).
	{
	    return false;
	}

	Player p = (Player) sender;
	Location l = p.getLocation();

	Tutorial.log.log(Level.INFO, String.format("SubSubCommand: %s", subsub));

	int value = -1;

	if (args.length > 2)
	{
	    value = Integer.parseInt(args[2]);
	}

	List<LivingEntity> les;
	World w = p.getWorld();
	int minX, maxX, minZ, maxZ;

	switch (subsub)
	{
	    case SETTIME:
		if (value != -1)
		{
		    p.getWorld().setTime(value);
		}

		return true;
	    // Sigh. This is *supposed* to set all living things around the player on fire. It appears to. But then the fire goes out instantly, one tick. Whether it registers damage at all, dunno.
	    case INFERNO:
		les = w.getLivingEntities();

		for (LivingEntity le : les)
		{
		    l = le.getLocation();

		    double dist = Math.sqrt(Math.pow(l.getX() - p.getLocation().getX(), 2) + Math.pow(l.getZ() - p.getLocation().getZ(), 2));

		    if (dist <= 40)
		    {
			if (le.getEntityId() != p.getEntityId())
			{
			    Block b = w.getBlockAt(l);
			    b.setType(Material.FIRE);

   			    Tutorial.log.log(Level.INFO, String.format("Entity #%s (%s) has been set ablaze!", le.getEntityId(), le.getClass()));

			    //le.setFireTicks(10);
			    //Tutorial.log.log(Level.INFO, String.format("Entity #%s has been set on fire! Ticks: %s; Max: %s", le.getEntityId(), le.getFireTicks(), le.getMaxFireTicks()));
			}
		    }
		}

		return true;
	    case WRATH:
		les = p.getWorld().getLivingEntities();

		for (LivingEntity le : les)
		{
		    l = le.getLocation();

		    double dist = Math.sqrt(Math.pow(l.getX() - p.getLocation().getX(), 2) + Math.pow(l.getZ() - p.getLocation().getZ(), 2));

		    if (dist <= 40)
		    {
			if (le.getEntityId() != p.getEntityId())
			{
			    le.setHealth(0);
			    Tutorial.log.log(Level.INFO, String.format("Entity #%s (%s) has been destroyed!!", le.getEntityId(), le.getClass()));
			}
		    }
		}

		return true;
	    case SMITE:
		les = p.getWorld().getLivingEntities();

		for (LivingEntity le : les)
		{
		    l = le.getLocation();

		    double dist = Math.sqrt(Math.pow(l.getX() - p.getLocation().getX(), 2) + Math.pow(l.getZ() - p.getLocation().getZ(), 2));

		    if (dist <= 40)
		    {
			if (le.getEntityId() != p.getEntityId())
			{
			    if (le instanceof Zombie || le instanceof Creeper || le instanceof Skeleton || le instanceof Spider)
			    {
				le.setHealth(0);
				Tutorial.log.log(Level.INFO, String.format("Entity #%s (%s) has been smitten!", le.getEntityId(), le.getClass()));
			    }
			}
		    }
		}

		return true;
	    // Teleport all nearby entities to the sky's limit... so that they plummet.
	    case PLUMMET:
		w = p.getWorld();
		les = w.getLivingEntities();

		for (LivingEntity le : les)
		{
		    l = le.getLocation();

		    double dist = Math.sqrt(Math.pow(l.getX() - p.getLocation().getX(), 2) + Math.pow(l.getZ() - p.getLocation().getZ(), 2));

		    int x, y, z;
		    x = le.getLocation().getBlockX();
		    z = le.getLocation().getBlockZ();

		    if (dist <= 40)
		    {
			if (le.getEntityId() != p.getEntityId())
			{
			    le.teleportTo(new Location(w, x, 128, z));
			    Tutorial.log.log(Level.INFO, String.format("Entity #%s (%s) has been dropped!", le.getEntityId(), le.getClass()));
			}
		    }
		}

		return true;
	    case BATHTIME:
		w = p.getWorld();
		les = w.getLivingEntities();

		double dist;

		minX = l.getBlockX() - 20;
		maxX = l.getBlockX() + 20;
		minZ = l.getBlockZ() - 20;
		maxZ = l.getBlockZ() + 20;

		int closestX = -1000;
		int closestZ = -1000;
		boolean gotLava = false;

		for (int x2 = minX; x2 < maxX; x2++)
		{
		    for (int z2 = minZ; z2 < maxZ; z2++)
		    {
			int y = w.getHighestBlockYAt(x2, z2);

			Block b = w.getBlockAt(x2, y - 1, z2);

			//Tutorial.log.log(Level.INFO, String.format("Highest Block - x: %s; y: %s; z: %s; Material: %s", x2, y, z2, b.getType()));
			//Tutorial.log.log(Level.INFO, String.format("Next Block - x: %s; y: %s; z: %s; Material: %s", x2, y - 1, z2, b2.getType()));

			// For now, just break at the first lava block. Eventually get the closest one, as implied.
			if (b.getType() == Material.STATIONARY_LAVA)
			{
			    closestX = x2;
			    closestZ = z2;
			    gotLava = true;
			}
		    }
		}

		if (!gotLava)
		{
		    Tutorial.log.log(Level.INFO, "Couldn't find lava for a bath, aborting.");
		    return false;
		}

		Location lavaBath = new Location(w, closestX, w.getHighestBlockYAt(closestX, closestZ), closestZ);

		for (LivingEntity le : les)
		{
		    Location l2 = le.getLocation();

		    dist = getDistance(l2.getX(), l.getX(), l2.getZ(), l.getZ());

		    if (dist <= 40)
		    {
			if (le.getEntityId() != p.getEntityId())
			{
			    le.teleportTo(lavaBath);
			    Tutorial.log.log(Level.INFO, String.format("Entity #%s, class %s, has been given a bath. Ticks: %s; Max: %s", le.getEntityId(), le.getClass(), le.getFireTicks(), le.getMaxFireTicks()));
			}
		    }
		}

		return true;
	    case FLOOD:
		w = p.getWorld();

		if (value == -1)
		{
		    value = 2;
		}

		minX = l.getBlockX() - value;
		maxX = l.getBlockX() + value;
		minZ = l.getBlockZ() - value;
		maxZ = l.getBlockZ() + value;

		for (int x2 = minX; x2 < maxX; x2++)
		{
		    for (int z2 = minZ; z2 < maxZ; z2++)
		    {
			int y = w.getHighestBlockYAt(x2, z2);

			Block b = w.getBlockAt(x2, y, z2);

			if (b.getType() == Material.AIR || b.getType() == Material.SNOW)
			{
			    b.setType(Material.STATIONARY_WATER);
			}
		    }
		}

		Tutorial.log.log(Level.INFO, "Flooding a small section of the world, nothing to see here.");

		return true;
	    case FLATTEN:
		w = p.getWorld();

		if (value == -1)
		{
		    value = 10;
		}

		String blockType = "DIRT";

		if (args.length > 3)
		{
		    blockType = args[3];
		}

		Tutorial.log.log(Level.INFO, String.format("BlockType: %s", blockType.toUpperCase()));

		Material m = Material.valueOf(blockType.toUpperCase());

		Tutorial.log.log(Level.INFO, String.format("Material: %s", m));

		minX = l.getBlockX() - value;
		maxX = l.getBlockX() + value;
		minZ = l.getBlockZ() - value;
		maxZ = l.getBlockZ() + value;

		for (int x2 = minX; x2 < maxX; x2++)
		{
		    for (int z2 = minZ; z2 < maxZ; z2++)
		    {
			int y = w.getHighestBlockYAt(x2, z2);

			if (y < l.getBlockY())
			{
			    Block b = w.getBlockAt(x2, y, z2);

			    if (b.getType() == Material.AIR || b.getType() == Material.SNOW)
			    {
				b.setType(m);
			    }
			}
			else if (y > l.getBlockY())
			{
			    Block b = w.getBlockAt(x2, y, z2);

			    if (b.getType() == Material.AIR)
			    {
				b = w.getBlockAt(x2, y - 1, z2);
			    }

			    b.setType(Material.AIR);
			}
			else if (y == l.getBlockY())
			{
			    Block b = w.getBlockAt(x2, y, z2);

			    if (b.getType() == Material.SNOW)
			    {
				b.setType(Material.AIR);
			    }
			}
		    }
		}

		Tutorial.log.log(Level.INFO, "Leveling the world.");

		return true;
	}

	return false;
    }

    private double getDistance(double x1, double y1, double x2, double y2)
    {
	// c^2 = a^2 + b^2
	return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    private boolean ExecuteBlockCommand()
    {
	SubCommands.PlayerSubCommands subsub = null;

	try
	{
	    subsub = SubCommands.PlayerSubCommands.valueOf(args[1].toUpperCase());
	}
	catch (Exception ex) // Don't actually do anything, just return false (triggering display of usage as per plugin.yml).
	{
	    return false;
	}

	switch (subsub)
	{
	}

	return false;
    }

    // This method for getting subcommands stolen from HotSwap.
    private enum SubCommands
    {
	PLAYER, ENTITY, LIVINGENTITY, SERVER, WORLD, BLOCK;

	// Other possible commands: ONLINESTATUS, PERFORMCOMMAND, SETCOMPASS
	// Commands that could be run on a player but inherit from HumanEntity: GETITEM, SETITEM, GETINVENTORY (so would GETNAME)
	// Commands that could be run on a player but'll be implemented - probably - under LivingEntity: GETAIR, SETAIR, GETHEALTH, SETHEALTH, SHOOT/THROW
	// Commands that could be run on a player but'll be implemented - probably - under Entity: GETID, GETLOCATION, TELEPORTTO
	public static enum PlayerSubCommands
	{
	    GET, MATCH, LIST, GETBLANK, CHAT, GETIP, HEAL
	}

	// GET will be by ID - getEntities, and then loop for an Entity with the same ID... I guess.
	public static enum EntitySubCommands
	{
	    GET, LIST
	}

	public static enum WorldSubCommands
	{
	    SETTIME, INFERNO, BBQ, WRATH, PLUMMET, BATHTIME, SMITE, FLOOD, FLATTEN
	}
    }
}

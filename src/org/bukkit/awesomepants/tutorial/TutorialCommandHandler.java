package org.bukkit.awesomepants.tutorial;

// Java imports
import java.util.List;
import java.util.logging.Level;

// Bukkit imports
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

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

	if ((sender instanceof Player)) // If executed by the player.
	{
	    switch (sub)
	    {
		case PLAYER:
		    return ExecutePlayerCommand();
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
	    case GETALL:
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

    // This method for getting subcommands stolen from HotSwap.
    private enum SubCommands
    {
	PLAYER;

	public static enum PlayerSubCommands
	{
	    GET, MATCH, GETALL, GETBLANK
	}
    }
}

package org.bukkit.awesomepants.tutorial;

// Java imports
import java.io.File;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

// Bukkit imports
import org.bukkit.entity.Player;
import org.bukkit.Server;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * Tutorial for Bukkit
 *
 * @author Kaletam
 *
 */
public class Tutorial extends JavaPlugin
{
    private final TutorialPlayerListener playerListener = new TutorialPlayerListener(this);
    private final TutorialBlockListener blockListener = new TutorialBlockListener(this);
    private final HashMap<Player, Boolean> debugees = new HashMap<Player, Boolean>();
    public static final Logger log = Logger.getLogger("Minecraft"); // Get the Minecraft logger for, er, logging purposes.

    public Tutorial(PluginLoader pluginLoader, Server instance, PluginDescriptionFile desc, File folder, File plugin, ClassLoader cLoader)
    {
	super(pluginLoader, instance, desc, folder, plugin, cLoader);
    }

    public void onEnable()
    {
	// Register our events
	PluginManager pm = getServer().getPluginManager();

	PluginDescriptionFile pdfFile = this.getDescription();
	log.log(Level.INFO, String.format("[%s] version [%s] enabled.", pdfFile.getName(), pdfFile.getVersion()));
    }

    public void onDisable()
    {
	PluginDescriptionFile pdfFile = this.getDescription();
	log.log(Level.INFO, String.format("[%s] version [%s] signing off!", pdfFile.getName(), pdfFile.getVersion()));
    }

    public boolean isDebugging(final Player player)
    {
	if (debugees.containsKey(player))
	{
	    return debugees.get(player);
	}
	else
	{
	    return false;
	}
    }

    public void setDebugging(final Player player, final boolean value)
    {
	debugees.put(player, value);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args)
    {
	Player p = (Player) sender;

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

	String targetName = "";

	if (args.length > 1)
	{
	    targetName = args[1];
	}

	if ((sender instanceof Player)) // If executed by the player.
	{
	    String format = "";

	    switch (sub)
	    {
		case GETPLAYER:
		    if (targetName == "")
		    {
			return false;
		    }

		    if (getServer().getPlayer(targetName) != null)
		    {
			format = String.format("%s is a Player.", targetName);
		    }
		    else
		    {
			format = String.format("%s is not a Player.", targetName);
		    }

		    log.log(Level.INFO, format);

		    return true;
		case GETPLAYERBLANK:
		    // This either gets the current player or the first player. Must test further.
		    if (getServer().getPlayer("") != null)
		    {
			p = getServer().getPlayer("");

			format = String.format("Name: %s; IP: %s", p.getName(), p.getAddress());

			log.log(Level.INFO, format);

			format = String.format("%s is a Player.", targetName);
		    }
		    else
		    {
			format = String.format("%s is not a Player.", targetName);
		    }

		    log.log(Level.INFO, format);

		    return true;
		case GETPLAYERS:
		    return true;
		case MATCHPLAYER:
		    return true;
	    }


	    return true;
	}

	return false;
    }

    // This method for getting subcommands stolen from HotSwap.
    private enum SubCommands
    {
	GETPLAYER, MATCHPLAYER, GETPLAYERS, GETPLAYERBLANK
    }
}

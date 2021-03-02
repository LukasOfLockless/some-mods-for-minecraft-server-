package lockless.playerList;


import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin implements Listener
{	
	
	//TODO make a defaultconfigfile for these headers and footers
	//TODO test chatcolor on the header footer
	@Override
	public void onEnable()
	{
		this.saveDefaultConfig();
		System.out.println("[serverlist] enabled");
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	@Override
	public void onDisable()
	{
		System.out.println("[serverlist] enabled");
	}

	
	//this is going to be a bit more difficult that default.
	@EventHandler
	private void onLogin(PlayerJoinEvent event) 
	{
		//System.out.println("set foot and head");
		event.getPlayer().setPlayerListHeader(ChatColor.BOLD +""+ChatColor.ITALIC+this.getConfig().getString("header"));
		event.getPlayer().setPlayerListFooter(ChatColor.LIGHT_PURPLE+""+ChatColor.UNDERLINE+ this.getConfig().getString("footer"));
	}
	
}
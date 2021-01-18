package lockless.inspectforslimes;

import org.bukkit.ChatColor;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{

	@Override
	public void onEnable() 
	{
		System.out.println("inspect for slimes enabled");
		// TODO Auto-generated method stub
		super.onEnable();
	}
	@Override
	public void onDisable() {
		System.out.println("inspector gadget disabled");
		// TODO Auto-generated method stub
		super.onDisable();
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// TODO Auto-generated method stub
		if(label.equalsIgnoreCase("inspectforslimes")) 
		{
			if(sender instanceof Player) 
			{
				Player p = (Player) sender;
				Block loc = p.getLocation().getBlock();
				if(loc.getBiome()== Biome.SWAMP) 
				{
					p.sendMessage(ChatColor.GREEN+"could be slimes here");
					return true;
				}
				if(loc.getChunk().isSlimeChunk()) 
				{
					p.sendMessage(ChatColor.GREEN+"could be slimes here");
					return true;
				}
				p.sendMessage(ChatColor.DARK_GREEN+"nothing");
				return true;
			}
			return true;
		}
		return super.onCommand(sender, command, label, args);
	}
}

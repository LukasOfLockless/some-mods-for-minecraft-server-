package lockless.xp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin
{
	@Override
	public void onEnable() {
		System.out.println("xp enabled");
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(label.equalsIgnoreCase("xp")) 
		{
			if(args.length > 0) 
			{
				System.out.print("poo xp poo");
				return false;
			}
			
			if(sender instanceof Player) 
			{
				Player p = (Player) sender;
				p.sendMessage(ChatColor.GOLD + "Total gained : " + p.getTotalExperience());
				p.sendMessage(ChatColor.GOLD+ ""+p.getExpToLevel() + " xp to Level " +  (p.getLevel()+1));
				//p.sendMessage(ChatColor.GOLD + "Levels : " + p.getLevel());
				//p.sendMessage(ChatColor.GOLD + "Exp : " + p.getExp());
			}
			else 
			{
				//for console use
				//find highest current xp
				if(getServer().getOnlinePlayers().size()==0) 
				{
					System.out.println("/xp not work when server DEAD, advertise, kidnap and sacrife goats to the old gods to get atleast 1 player online");
				}
				else 
				{
					int highest=0;
					final Collection<? extends Player> collection = getServer().getOnlinePlayers();
					ArrayList<Player> readable = new ArrayList<Player>();
					int rng = (int)(Math.random()*collection.size());
					Player best;
					for(Player p : collection) 
					{
						readable.add(p);
					}
					best = readable.get(rng);
					for(Player p: collection) 
					{
						if(p.getTotalExperience() > highest) 
						{
							highest = p.getTotalExperience();
							best = p;
						}
					}
					System.out.println("\n\n");
					System.out.println("EXP LIST");
					System.out.println("BEST is " + best.getName() + " with " + highest + "EXP");
					for(Player p: collection) 
					{
						if ((p.getName() == best.getName())==false) 
						{
							System.out.println(p.getName()+" \t:"+p.getTotalExperience());
						}
					}
					System.out.println("\n\n");
				}
			}
			return true;
		}
		return false;
	}
	
}

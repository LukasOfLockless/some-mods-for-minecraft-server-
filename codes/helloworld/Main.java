package lockless.helloworld;

import java.util.Collection;
import java.util.Iterator;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin
{   
	public void onEnable()
	{
		System.out.println("helloWorld enabled, autobuilt");
	}
	
	public void onDisable() 
	{
		System.out.println("helloWorld disabled");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) 
	{
		
		// TODO cooldown
		if(label.equalsIgnoreCase("helloworld")) 
		{
			Collection<? extends Player> allPlayers =  getServer().getOnlinePlayers();
			//console i guess does this
			if(!(sender instanceof Player)) 
			{
				System.out.println("hello");
				System.out.println("players around " + allPlayers.size());
				String mashedPlayerNames = "total: "+allPlayers.size()+" ";
				 // while loop
				Iterator<? extends Player> i = allPlayers.iterator();
				while (i.hasNext()) 
				{
					Player thatOtherPlayer = i.next();
					mashedPlayerNames+= thatOtherPlayer.getName()+" ";
					thatOtherPlayer.sendMessage(ChatColor.MAGIC+"o "+ChatColor.WHITE + "hello" +ChatColor.MAGIC+"o ");
				}
		        System.out.println(mashedPlayerNames);
				return true;
			}
			
			
			Player playerOne = (Player) sender;
			
			
			if(sender.hasPermission("helloworld.use")) 
			{
				sender.sendMessage(ChatColor.RED+"hi");
				return true;
			}
			
			
			//now the improv part
			
			ChatColor color=ChatColor.WHITE;
			int rng = (int)(Math.random() * 16);
			switch (rng) {
			case 0:
				color = ChatColor.AQUA;
				break;
			case 1:
				color = ChatColor.BLACK;
				break;
			case 2:
				color = ChatColor.BLUE;
				break;
			case 3:
				color = ChatColor.DARK_AQUA;
				break;
			case 4:
				color = ChatColor.DARK_BLUE;
				break;
			case 5:
				color = ChatColor.DARK_GRAY;
				break;
			case 6:
				color = ChatColor.DARK_GREEN;
				break;
			case 7:
				color = ChatColor.DARK_PURPLE;
				break;
			case 8:
				color = ChatColor.DARK_RED;
				break;
			case 9:
				color = ChatColor.GOLD;
				break;
			case 10:
				color = ChatColor.GRAY;
				break;
			case 11:
				color = ChatColor.GREEN;
				break;
			case 12:
				color = ChatColor.LIGHT_PURPLE;
				break;
			case 13:
				color = ChatColor.RED;
				break;
			case 14:
				color = ChatColor.WHITE;
				break;
			case 15:
				color = ChatColor.YELLOW;
				break;
			default:
				break;
			}
			
			playerOne.sendMessage(ChatColor.BOLD+""+color+ "hi "+playerOne.getName());
			
			System.out.println(playerOne.getName()+" said hi to "+allPlayers.size()+" players");
			
			if(allPlayers.size()!=0) 
			{
				playerOne.sendMessage(color +"there are "+ (allPlayers.size()-1) +" others");
			}
			
			//loop
			Iterator<? extends Player> i = allPlayers.iterator();
			
			 // while loop
			String playerOneWorldName = playerOne.getWorld().getName();
	        while (i.hasNext()) {
	        	Player thatOtherPlayer = i.next();
	        	if (thatOtherPlayer!=playerOne) 
	        	{
	        		if (thatOtherPlayer.isOnline()) 
	        		{
	        			//check if same world.
	        			if(playerOneWorldName == thatOtherPlayer.getWorld().getName()) 
	        			{
	        				thatOtherPlayer.sendMessage(color+"World says hello");	        				
	        			}
	        		}
	        		else 
	        		{
	        			System.out.println(thatOtherPlayer.getName()+" was helloworlded and isnt online");
	        		}
	        	}
	        }
			
			
			return true;
		}
		return false;
	}
	

}

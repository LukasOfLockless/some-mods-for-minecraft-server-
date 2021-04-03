package lockless.kickonTHE;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener
{	
	
	private boolean debugprintStuff=true;
	@Override
	public void onEnable() {
		System.out.println("[kickonTHE] enabled");
		getServer().getPluginManager().registerEvents(this, this);
		super.onEnable();
	}
	@Override
	public void onDisable() {
		System.out.println("[kickonTHE] disabled");
		super.onDisable();
	}

	
	
	//events
	@EventHandler (priority = EventPriority.NORMAL)
	private void chat(AsyncPlayerChatEvent event)
	{
		if(event == null) 
		{
			if(debugprintStuff)
			{
				System.out.println("[proximity chat] ");				
			}
			
			return;
		}
		
		//boolean quick = event.isAsynchronous();
		
		
		String recipientsReallyClose="";
		String recipientsNormalProximity="";
		String recipientsMuffledOnly="";
		
		String messageMuffled="";
		
		
		boolean needToMuzzle=false;
		for (Player p : event.getRecipients()) {
			
			if(distance(event.getPlayer(),p,DistanceNormal)) 
			{
				recipientsReallyClose+= " "+p.getName();
			}
			
			if(distance(event.getPlayer(),p,DistanceNormal)) 
			{
				recipientsNormalProximity += " "+p.getName();
			}else if(distance(event.getPlayer(),p,DistanceMuffled)) 
			{
				needToMuzzle=true;
				recipientsMuffledOnly +=" "+p.getName();
			}
		}
		if (debugprintStuff) 
		{
			System.out.println("[proximity chat] chatEvent recipients");
			//dump all the shit
			System.out.println("close");
			System.out.println(recipientsReallyClose);

			System.out.println("normal");
			System.out.println(recipientsNormalProximity);
			
			System.out.println("muffled");
			System.out.println(recipientsMuffledOnly);

		}
		
		
		if(needToMuzzle) 
		{
			messageMuffled = event.getMessage();
			char[] cars2 = messageMuffled.toLowerCase().toCharArray();
			if (debugprintStuff ) 
			{
				System.out.println();
			}
			
			//muffling algorythm
			for(int i=0; i<cars2.length; i++) 
			{
				if (cars2[i] == 's' ||cars2[i] == 'z' || cars2[i] == 't'|| cars2[i] == 'd' || cars2[i] == 'k'|| cars2[i] == 'g'|| cars2[i] == 'q'|| cars2[i] == 'p'|| cars2[i] == 'b'|| cars2[i] == 'c'|| cars2[i] == 'x') 
				{
					if (debugprintStuff ) 
					{
						System.out.print(cars2[i]+"");
					}					
					cars2[i]=' ';
				}
			}
			messageMuffled = cars2.toString();
			
			if (debugprintStuff ) 
			{

				if(needToMuzzle) 
				{
					System.out.println("[proximity chat] chatEvent mesage: "+event.getMessage());
					System.out.println("[proximity chat] chatEvent me a e: "+messageMuffled);
				}
				else 
				{
					System.out.println("[proximity chat] chatEvent mesage: "+event.getMessage());
				}

			
			}
		}
		
		
	
		ArrayList<Player> recipientsThatInNormalDistance=new ArrayList<Player>();
		
		ArrayList<Player> recipientsThatInMuffledDistance=new ArrayList<Player>();
		
		Player sender=event.getPlayer();
		
			
					
		
		
		Iterator<Player> iterator = recipientsThatInMuffledDistance.iterator();
		
		while(iterator.hasNext()) 
		{
			Player p =iterator.next();
			if(distance(sender,p,DistanceNormal))
			{
				recipientsThatInNormalDistance.add(p);
				
			}
			else if (distance(sender,p,DistanceMuffled))
			{

				recipientsThatInMuffledDistance.add(p);
			}
		}
		
		if (debugprintStuff ) 
		{
			System.out.println("normal msg size()" + recipientsThatInNormalDistance.size());

			System.out.println("muffled size()" + recipientsThatInMuffledDistance.size());
		}
		

		for(Player got: recipientsThatInNormalDistance) 
		{
			got.sendMessage(messageMuffled);
			got.sendMessage(playerGetColor(event.getPlayer())+"<"+event.getPlayer().getDisplayName()+"> "+ChatColor.RESET+ event.getMessage());
		}
		for(Player got: recipientsThatInMuffledDistance) 
		{
			got.sendMessage(messageMuffled);
			got.sendMessage(playerGetColor(event.getPlayer())+"<"+event.getPlayer().getDisplayName()+"> "+ChatColor.RESET+ messageMuffled);
			
		}
		
		event.setCancelled(true);
		
	}
	
	private String playerGetColor(Player p) 
	{
		String[] colors = new String[]{""+ChatColor.WHITE , ""+ChatColor.BLUE ,   ""+ChatColor.GRAY, ""+ChatColor.GREEN, ""+ChatColor.RED, ""+ChatColor.AQUA ,  ""+ChatColor.YELLOW};
		
		int colorCodeOfTheDudeWhoAsk =0;
		
		
		for(playerAssignedColors somde :playerColorList) 
		{
			if(somde.match(p)) 
			{
				colorCodeOfTheDudeWhoAsk= somde.color;
				break;
			}
		}
		
		return colors[colorCodeOfTheDudeWhoAsk];
	}
	
	private void playerSwitchChatColor(Player p) 
	{
		for(playerAssignedColors somde :playerColorList) 
		{
			if(somde.match(p)) 
			{
				
				somde.color++;
				somde.color = somde.color%7;
				break;
			}
		}
	}
}
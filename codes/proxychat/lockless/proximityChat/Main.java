package lockless.proximityChat;

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
	
	private double DistanceNormal=800;

	private double DistanceMuffled=3200;
	
	private boolean debugprintStuff=true;
	private int lastColorUsed =0;
	private ArrayList<playerAssignedColors> playerColorList;
	
	private class playerAssignedColors
	{
		private UUID id ;
		public Player playerOne;
		public int color;
		public playerAssignedColors(Player p , int i) 
		{
			playerOne = p;
			id = p.getUniqueId();
			color  = i;
		}
		public boolean match(Player p) 
		{
			return p.getUniqueId() == id;
		}
	}
	
	
	
	@Override
	public void onEnable() {
		playerColorList = new ArrayList<playerAssignedColors>();
		System.out.println("[proximity chat] enabled");
		getServer().getPluginManager().registerEvents(this, this);
		super.onEnable();
	}
	@Override
	public void onDisable() {
		System.out.println("[proximity chat] disabled");
		super.onDisable();
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (label.equalsIgnoreCase("chatcolor")) 
		{
			if(sender instanceof Player) 
			{
				playerSwitchChatColor((Player) sender);
				sender.sendMessage(playerGetColor((Player) sender)+"your nick in chat now like this");
			}else 
			{
				System.out.println("[proximity chat] colors");
				for (playerAssignedColors p: playerColorList) 
				{
					System.out.println(p.color +" "+p.playerOne.getDisplayName() );
				}
			}
			return true;
		}
		
		return false;
	}
	@EventHandler
	private void onJingieag(PlayerJoinEvent event) 
	{
		lastColorUsed++;
		lastColorUsed = lastColorUsed%7;
		
		playerColorList.add(new playerAssignedColors(event.getPlayer(), lastColorUsed));
		
	}
	//77777777777777777777777777777
	@EventHandler
	private void onLeas(PlayerQuitEvent event)
	{
		lastColorUsed--;
		lastColorUsed = lastColorUsed%7;
		
		playerAssignedColors rm= null;
		
		for(playerAssignedColors p : playerColorList) 
		{
			if(p.match(event.getPlayer())) 
			{
				rm = p;
			}
		}
		
		try 
		{
			
		playerColorList.remove(rm);
		}catch(Exception e) 
		{
			System.out.println("color code and shit 7- 0-6 index go clicky claky\n"+e.getMessage());
		}
	}
	
	
	//logic
	
	//notice this only uses x  and z
	private boolean distance(Player p1, Player p2,double distance) 
	{
		double sqrDistance=0;//this is squared
		double x=p1.getLocation().getX() - p2.getLocation().getX();
		double z=p1.getLocation().getZ() - p2.getLocation().getZ();
		sqrDistance = x*x+z*z;
		/*
		if((sqrDistance <= (distance*distance))==false)
		{
			System.out.println("distance too far "+Math.sqrt(sqrDistance));
		}
		*/
		return(sqrDistance <= (distance*distance));
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
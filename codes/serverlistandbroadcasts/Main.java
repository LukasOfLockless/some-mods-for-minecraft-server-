package lockless.serverlistandbroadcasts;



import java.util.Iterator;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;




public class Main extends JavaPlugin implements Listener
{	
	private double OKDistance=80;
	private String header="Pidgeon Server";
	private String footer="try killing a 1024 hostile mobs on spawn island";
	
	@Override
	public void onEnable()
	{
		System.out.println("broadcaster");
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	@Override
	public void onDisable()
	{
		System.out.println("bye bye");
		
	}

	//distance Check
	@EventHandler 
	private void chat(AsyncPlayerChatEvent event)
	{
		boolean quick = event.isAsynchronous();
		String sendersName= event.getPlayer().getName();
		String whatsend = event.getMessage();
		String recipients = "";
		String recipientsProximity="";
		
		
		// :O
		for (Player p : event.getRecipients()) {
			recipients += p.getName()+" ";
			if (distance(event.getPlayer(),p)) 
			{
				//proximity.add(p);
				recipientsProximity +=" "+p.getName();
			}
		}
		boolean sameThing=true;
		
		Set<Player> newRecipients=event.getRecipients();
		Player sender=event.getPlayer();
		Iterator<Player> iterator = newRecipients.iterator();
		
		while(iterator.hasNext()) 
		{
			Player p =iterator.next();
			if(distance(sender,p)==false)
			{
				sameThing=false;
				System.out.println("trying to remove "+p.getName()+" cuzthey far");
				newRecipients.remove(p);
			}
		}
		if(sameThing==false) 
		{
				
			event.getRecipients().clear();
			iterator = newRecipients.iterator();
			while(iterator.hasNext()) 
			{
				event.getRecipients().add(iterator.next());
			}
		}
		//if(distance(event.getPlayer(),newRecipients.))
		
		System.out.println("proximity set is wonky , size is"+newRecipients.size());
		System.out.println("quick "+quick+"sent by"+ sendersName+" "+whatsend+" to "+recipients+" but " + recipientsProximity);
		
	}
	
	//notice this only uses x  and z
	private boolean distance(Player p1, Player p2) 
	{
		double sqrDistance=0;//this is squared
		double x=p1.getLocation().getX() - p2.getLocation().getX();
		double z=p1.getLocation().getZ() - p2.getLocation().getZ();
		sqrDistance = x*x+z*z;
		if((sqrDistance <= (OKDistance*OKDistance))==false)
		{
			System.out.println("distance too far "+Math.sqrt(sqrDistance));
		}
		return(sqrDistance <= (OKDistance*OKDistance));
	}

	//TODO serverlist proximity?
	//this is going to be a bit more difficult that default.
	@EventHandler
	private void onLogin(PlayerJoinEvent event) 
	{
		System.out.println("set foot and head");
		event.getPlayer().setPlayerListFooter(footer);
		event.getPlayer().setPlayerListHeader(header);
	}
	
}

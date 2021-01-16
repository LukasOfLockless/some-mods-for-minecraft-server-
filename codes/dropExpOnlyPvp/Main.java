package lockless.dropExpOnlyPvp;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class Main extends JavaPlugin implements Listener
{

	@Override
	public void onEnable() 
	{
		System.out.println("dropExpOnlyPvp");
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	
	@EventHandler
	private void HandleExpDrops(PlayerDeathEvent onDeath) 
	{
		String expModOutput="\n\n PVP EXP MOD\n";
		Player p = onDeath.getEntity().getPlayer();
		
		if (p.getKiller()!=null) 
		{
			expModOutput+="died has killer\n";
			
			if(p.getKiller() instanceof Player) 
			{
				Player killer =(Player)p.getKiller();
				killer.giveExp(ConvertPlayersLifePvPExp(p));
				killer.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,0,0);
				onDeath.setDroppedExp(0);
				expModOutput+=" PVP to Death by "+ killer.getName()+"/n";
			}
			else 
			{
				expModOutput+=" killed by smt else "+onDeath.getDeathMessage()+"/n";
				onDeath.setDroppedExp(0);
			}
		}
		else 
		{
			expModOutput+=" died has no killer no EXP\n";
			onDeath.setDroppedExp(0);
		}
		expModOutput+="\n";
		System.out.println(expModOutput);
	}
	
	private int ConvertPlayersLifePvPExp(Player Deaddy) 
	{
		double PartDrop = 0.5f;
		double actualExp=Deaddy.getLevel()*17;
		return (int)Math.floor((double)actualExp * PartDrop);
	}
	
	
}

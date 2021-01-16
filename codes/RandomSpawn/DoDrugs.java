package lockless.training2;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class DoDrugs implements Listener
{

	
	private Main ref;
	@EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerRespawn(PlayerRespawnEvent e)
    {
		ref = Main.getMain();
		if(ref.DRUGS) 
		{
			System.out.println("called that drug event");
	        Bukkit.getScheduler().runTaskLater(ref, task -> {
	            Player p = e.getPlayer();
	            //System.out.println("drug schedule");
	            if(p.getHealth()>10) 
				{
	            	//p.sendMessage("you had a hella party. head is gon explode");
					potionEffects(p);
				}
				else 
				{
					//if(p.getHealth()>1 && p.getHealth()<9) 
					{
						p.sendMessage(ChatColor.GOLD+""+ChatColor.ITALIC+"that was a poopookaka rngspawn, wasn't it?");
					}
				}
	        }, 15L);
		}
    }
	
	public void potionEffects(Player player)
	{
		//potions maybe getting triggerd
		//System.out.println("this is later. potion task");
		//player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, true, false));
		int rng1 = (int)(Math.random()*120*2+20);
		player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS , rng1 , 0 , true, false));
		int rng2 = (int)(Math.random()*120*4+20);
		player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS , rng2 , 0 , true, false));
		int rng3 = (int)(Math.random()*120*3+20);
		player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW , rng3 , 0 , true, false));
		int rng4 = (int)(Math.random()*120+20);
		player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION , rng4 , 0 , true, false));
	}
}
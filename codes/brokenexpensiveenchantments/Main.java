package lockless.fuckingexpensiveenchantments;

import org.bukkit.plugin.java.JavaPlugin;


import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.*;


import org.bukkit.Particle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.entity.Player;
import java.util.Map;

public class Main extends JavaPlugin implements Listener
{

	public double costMultiplier=10;
	@Override
	public void onEnable() 
	{
		// TODO Auto-generated method stub
		System.out.println(" fuckign expensive enchanments enabled ");
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	@EventHandler(priority = EventPriority.LOW)
	private void enchantation( EnchantItemEvent event) 
	{
		String edebug ="enchantment dbg\n";
		
		Map<Enchantment,Integer> map =event.getEnchantsToAdd();

	    // classic way, loop a Map
	    for (Map.Entry<Enchantment, Integer> entry : map.entrySet()) {
	        System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
	    }
		System.out.println("is it "+edebug );
		//var thatMap = event.getEnchantsToAdd().toString();
		//System.out.println( thatMap+" Lv "+ event.getExpLevelCost() + " on item " +event.getItem().getType().toString());
		Player p = event.getEnchanter();
		p.spawnParticle(Particle.SPELL,p.getLocation().add(0, 2, 0) , 2);
		event.getItem().addEnchantment(savedEnchants[0], 2);
	}
	
	Enchantment[] savedEnchants;
	@EventHandler 
	private void preparation( PrepareItemEnchantEvent event) 
	{
		
		//enchant.getView().//is an inventory, whould be cool to muffle or spaghetti
		System.out.println("this is enchantment prep" + event.getItem().getType());
		System.out.println("enchanter prepper " + event.getEnchanter().getName() );
		System.out.println(" on item " +event.getItem().getType().toString());
		EnchantmentOffer[] offers = event.getOffers() ;
		savedEnchants = new Enchantment[3];
		int counter=0;
		for(EnchantmentOffer offer:offers) 
		{	
			if(offer != null) 			
			{
				
				offer.setCost(offer.getCost()*10);
				System.out.println(offer.getEnchantment().getKey().toString()+" is fucking expensive now:"+offer.getCost());
				savedEnchants[counter]=offer.getEnchantment();
				counter++;
			}
		}
		
	}
}

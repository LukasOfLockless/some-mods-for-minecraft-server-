package lockless.sleep;


import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.world.TimeSkipEvent;
import org.bukkit.event.Listener;

public class Main extends JavaPlugin implements Listener{


	@Override
	public void onEnable() {
		System.out.println("enabling  different sleep event outcomes");
		//suggest tests to look at
		System.out.println("suggested tests");
		System.out.println("playerenterbed during night in a safe location, nightskipevent is cancellabe, which is nice. player gets REGENERATION applied");
		System.out.println("playerenterbed during day normally cancel, but the during that event, the player can sleep with player.sleep(bed,true), just to be immidiatelly kicked out");
		System.out.println("playerenterbed during night when its not safe, just like villager NPCs do. NOT_SAFE result is given when there are hostiles in a box(8,5,8) around the player. player gets NIGHTVISION applied ");
		System.out.println("potioneffect BLINDNESS in either case");
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if(label.equalsIgnoreCase("gut"))
		{
			if(sender instanceof Player) 
			{
				Player p = (Player) sender;
				p.sendMessage(ChatColor.YELLOW+"Food level:"+p.getFoodLevel());
				String saturationword = String.format("%.1f", p.getSaturation());//cleaner this way
				p.sendMessage(ChatColor.YELLOW+"Food saturation:"+ saturationword);
				if(p.getWorld().getName().equalsIgnoreCase("world") == false || checkNearbyHostiles(p)>0) 
				{
					//p.sendMessage(ChatColor.YELLOW+p.getWorld().getName());
					p.sendMessage(ChatColor.YELLOW+"Bad feeling in your gut");
				}
				
				if(p.getStatistic(Statistic.TIME_SINCE_REST)>12000) 
				{
					p.sendMessage(ChatColor.YELLOW+""+ChatColor.UNDERLINE+"sleepy");
				}
				
			}

		
			return true;
		}
		
		return false;
	}
	private int checkNearbyHostiles(Player p)
	{
		// monsters nearby mean 
		/// blocksIf a "monster" is within 8 blocks of the bed horizontally (in the X- and Z-axis), and 5 blocks vertically (in the Y-axis),
		 // the message "You may not rest now, there are monsters nearby" appears 
		 //  and the player is prevented from sleeping until the monsters leave or are killed.
		
		List<Entity> entitty=p.getNearbyEntities(8, 5, 8);
		int overworldishEvilCount=0;
		for(Entity titty:entitty) 
		{
			if(titty.getType()==EntityType.ZOMBIE) 
			{
				overworldishEvilCount++;
			}else if(titty.getType()==EntityType.SKELETON) 
			{
				overworldishEvilCount++;
			}else if(titty.getType()==EntityType.CREEPER) 
			{
				overworldishEvilCount++;
			}else if(titty.getType()==EntityType.SPIDER) 
			{
				overworldishEvilCount++;
			}else if(titty.getType()==EntityType.ZOMBIE_VILLAGER) 
			{
				overworldishEvilCount++;
			}else if(titty.getType()==EntityType.ZOMBIE_HORSE) 
			{
				overworldishEvilCount++;
			}else if(titty.getType()==EntityType.ZOMBIFIED_PIGLIN) 
			{
				overworldishEvilCount++;
			}else if(titty.getType()==EntityType.CAVE_SPIDER) 
			{
				overworldishEvilCount++;
			}else if(titty.getType()==EntityType.ENDER_DRAGON) 
			{
				overworldishEvilCount++;
			}else if(titty.getType()==EntityType.SKELETON_HORSE) 
			{
				overworldishEvilCount++;
			}else if(titty.getType()==EntityType.DROWNED) 
			{
				overworldishEvilCount++;
			}else if(titty.getType()==EntityType.WITHER) 
			{
				overworldishEvilCount++;
			}else if(titty.getType()==EntityType.ENDERMAN) 
			{

				overworldishEvilCount++;
			}
		}
		
		return overworldishEvilCount;
	}
	
	//event
	
	@EventHandler
	private void OnSleep(PlayerBedEnterEvent event) 
	{
		Player p =event.getPlayer();
		System.out.println("sleep stuff "+event.getBedEnterResult());
		if(event.getBedEnterResult().equals(PlayerBedEnterEvent.BedEnterResult.OK)) 
		{
			p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 600 , 0, true));			
			p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 600 , 0, true));
			if(p.hasPotionEffect(PotionEffectType.ABSORPTION)) 
			{
				p.sendMessage(ChatColor.MAGIC+"Do androids dream of electric sheep?");
			}	
		}
		
		else if(event.isCancelled())
		{			
			System.out.println("now safe handling is it cancelled? "+ event.getBedEnterResult());//true
			if(event.getBedEnterResult().equals(PlayerBedEnterEvent.BedEnterResult.NOT_POSSIBLE_NOW)||event.getBedEnterResult().equals(PlayerBedEnterEvent.BedEnterResult.NOT_SAFE) ) 
			{

				p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 600 , 0, true));		
				p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 600 , 0, true));
				Block b = event.getBed();
				boolean forceNap = true;
				System.out.println("forcing player sleep"+p.getName()+ " even if event.reasult is "+event.getBedEnterResult()); // is this a good way to do this? it puts them to bed but....
				p.sleep(b.getLocation(), forceNap);
			}
			
		}
		
		
	}
	
	@EventHandler
	private void OnWakeUp(PlayerBedLeaveEvent event) 
	{
		System.out.println(event.getEventName() + " called by " +event.getPlayer().getName());
		if(event.getPlayer().hasPotionEffect(PotionEffectType.REGENERATION)) 
		{
			event.getPlayer().removePotionEffect(PotionEffectType.BLINDNESS);			
			event.getPlayer().removePotionEffect(PotionEffectType.REGENERATION);
			System.out.println("potion effects stack indicates player was sleeping in a safe location during the night");
		}
		if(event.getPlayer().hasPotionEffect(PotionEffectType.NIGHT_VISION)) 
		{

			System.out.println("potion effects stack indicates player was sleeping in a NOT safe location or NOT during the night");
			event.getPlayer().removePotionEffect(PotionEffectType.BLINDNESS);
			event.getPlayer().removePotionEffect(PotionEffectType.NIGHT_VISION);
		}
		
		
	}
	
	
	
	
	
	
	
	
	@EventHandler 
	private void OnNightSkip(TimeSkipEvent event)
	{
		//nightskip cancel work great this way
		//players leave bed during the day. its 
		if( event.getSkipReason().equals(TimeSkipEvent.SkipReason.NIGHT_SKIP)) 
		{
			//System.out.println("Cancelling nightskip");// spams every tick!!!!!//can't wait for 1.17 where server.properties or smt seem to be able to adjust nightskip
			event.setCancelled(true);
		}
		else
		{
			System.out.println("this is the reason why tiemskipevent was called "+event.getSkipReason().toString());
		}
	}
	


	

}

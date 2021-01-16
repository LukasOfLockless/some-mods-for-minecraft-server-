package lockless.heal;

import org.bukkit.Location;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;

public class Main extends JavaPlugin{

	
	
	public void onEnable()
	{
		System.out.println("heal enabled");
	
	}
	
	public void onDisable() 
	{
		System.out.println("heal disabled");
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) 
	{
		if(label.equalsIgnoreCase("healself")) 
		{
			if(!(sender instanceof Player)) 
			{
				sender.sendMessage("need to be player to use heal");
				return true;
			}
			if (sender.hasPermission("heal.self")) 
			{
				sender.sendMessage(ChatColor.RED+"perms limited");
				return true;
			}
			Player p = (Player)sender;
			if(p.getStatistic(Statistic.BREWINGSTAND_INTERACTION)<10) 
			{
				sender.sendMessage(ChatColor.RED+"You have no idea about this");
			}
			
			commandSelf(p);
		}
		
		if(label.equalsIgnoreCase("heal")) 
		{
			if(!(sender instanceof Player)) 
			{
				sender.sendMessage("need to be player to use heal");
				return true;
			}
			if (sender.hasPermission("heal.fren")) 
			{
				sender.sendMessage("perms limited");
				return true;
			}

			Player p = (Player)sender;
			if(p.getStatistic(Statistic.BREWINGSTAND_INTERACTION)<50) 
			{
				sender.sendMessage(ChatColor.RED+"You have no idea about this");
			}
			commandFren(p);
		}
		
		return false;
	}
	
	private boolean commandSelf(Player playerOne) 
	{

		if (playerOne.isOnline() == false) 
		{
			System.out.println("catching bugs = player offline now, what?");
			return true;
		}
		
		if(playerOne.getFoodLevel()<10) 
		{
			playerOne.sendMessage(ChatColor.RED+"you are too hungry to heal");
			return true;
		}
		
		
		double rng = Math.random();
		if(rng < 0.69) 
		{
			//success
			playerOne.setFoodLevel(Math.max(playerOne.getFoodLevel()-2, 0));
			playerOne.sendMessage(ChatColor.GREEN+"you patched yourself up");
			heal(playerOne);			
		}
		else 
		{
			playerOne.setFoodLevel(Math.max(playerOne.getFoodLevel()-3, 0));
			playerOne.sendMessage(ChatColor.RED+"You just got tired.");
		}
		return true;
	}
	
	
	private boolean commandFren(Player playerOne) 
	{
		
		if(playerOne.getFoodLevel()<10) 
		{
			playerOne.sendMessage(ChatColor.RED+"you are too hungry to heal");
			return true;
		}
				
		double rng = Math.random();
		
		if(rng < 0.69) 
		{
			//success
			Player target=getTarget(playerOne);
			Player insideBox=getPlayersInBoundingBox(playerOne);
			if(target == null) 
			{
				if (insideBox == null) 
				{
					playerOne.sendMessage(" nobody close");
				}
				else 
				{
					//to do bonds friendships relationships
					if(insideBox.getHealth()<insideBox.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) 
					{
						playerOne.setFoodLevel(Math.max(playerOne.getFoodLevel()-2, 0));
						playerOne.sendMessage(ChatColor.LIGHT_PURPLE+"you healed up "+insideBox.getName()+",chosen because close and a bit of rng meybi idk");
						insideBox.sendMessage(ChatColor.GREEN+playerOne.getName()+"healed you");
						heal(insideBox);
					}
					else 
					{
						playerOne.sendMessage(ChatColor.GREEN+"they are in finest shape");
					}
				}
			}
			else
			{	
				if(target.getHealth()<target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) 
				{
					//to do bonds friendships relationships
					playerOne.setFoodLevel(Math.max(playerOne.getFoodLevel()-1, 0));
					playerOne.sendMessage(ChatColor.LIGHT_PURPLE+"you healed"+target.getName()+",chosen because you are looking at them");
					target.sendMessage(ChatColor.GREEN + playerOne.getName() +" healed you");
					heal(target);
				}
				else 
				{
					playerOne.sendMessage(ChatColor.GREEN+"they are in finest shape");
				}
			}
		}
		else 
		{
			playerOne.setFoodLevel(Math.max(playerOne.getFoodLevel()-3, 0));
			playerOne.sendMessage(ChatColor.RED+"You just got tired.");
		}
		return true;
	}
	
	private void heal(Player target) 
	{
		double hp = Math.min(target.getHealth()+1 , target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue() );
		target.setHealth(hp);
	}
	
	
	private Player getPlayersInBoundingBox(Player from) 
	{
		double boxsize=5;
		List<Entity> listClose= from.getNearbyEntities(boxsize, boxsize, boxsize);
		List<Entity> players =  new ArrayList<Entity>(); 
		for (Entity etity:listClose) 
		{
			if (etity instanceof Player) 
			{
				if (etity.getName() != from.getName()) 
				{
					players.add(etity);					
				}
			}
		}
		if(players.size() == 0) 
		{
			System.out.println(" failed to find players in a box sized "+boxsize);
			return null;
		}
		int rng=(int)(Math.random() * players.size());
		
		
		return (Player)players.get(rng);
	}
	
	
	//thanks https://bukkit.org/members/blablubbabc.64583/ but shit always returns null in my case. idk
	private Player getTarget(Player from) {
        assert from != null;
        // SOME FIXED VALUES (maybe define them globally somewhere):
        // the radius^2:
        double radius2 = 10.0D * 10.0D;
        // the min. dot product (defines the min. angle to the target player)
        // higher value means lower angle means that the player is looking "more directly" at the target):
        // do some experiments, which angle / dotProduct value fits best for your case
        double minDot = 0.69D;
     
        String fromName = from.getName();
        Location fromLocation = from.getEyeLocation();
        String fromWorldName = fromLocation.getWorld().getName();
        Vector fromDirection = fromLocation.getDirection().normalize();
        Vector fromVectorPos = fromLocation.toVector();
 
        Player target = null;
        double minDistance2 = Double.MAX_VALUE;
        for (Player somePlayer : Bukkit.getServer().getOnlinePlayers()) {
            if (somePlayer.getName().equals(fromName)) continue;
            Location newTargetLocation = somePlayer.getEyeLocation();
            // check the world:
            if (!newTargetLocation.getWorld().getName().equals(fromWorldName)) continue;
            // check distance:
            double newTargetDistance2 = newTargetLocation.distanceSquared(fromLocation);
            if (newTargetDistance2 > radius2) continue;
            // check angle to target:
            Vector toTarget = newTargetLocation.toVector().subtract(fromVectorPos).normalize();
            // check the dotProduct instead of the angle, because it's faster:
            double dotProduct = toTarget.dot(fromDirection);
            if (dotProduct > minDot && from.hasLineOfSight(somePlayer) && (target == null || newTargetDistance2 < minDistance2)) {
                target = somePlayer;
                minDistance2 = newTargetDistance2;
            }
        }
        // can return null, if no player was found, which meets the conditions:
        return target;
    }
	

	
	
	
}

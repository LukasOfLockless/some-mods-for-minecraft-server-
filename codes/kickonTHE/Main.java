package lockless.kickonTHE;


import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.block.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener
{	
	
	@Override
	public void onEnable() {
		System.out.println("[kickonTHE] enabled");
		getServer().getPluginManager().registerEvents(this, this);// this line kinda puts this plugins ear to the servers chest to listen to the heartbeat in form of specified events
		super.onEnable();//super is like a wonky thing that says "i do this, afterwards do default route, which is handled by the server.
	}
	@Override
	public void onDisable() {
		System.out.println("[kickonTHE] disabled");
		super.onDisable();
	}

	
	
	//events , HIGH is slowest. it ought to not be cool with everything else going on.
	@EventHandler (priority = EventPriority.HIGH)
	private void chat(AsyncPlayerChatEvent event)
	{
		String msg = event.getMessage();
		TWwordSearchIn(msg,event.getPlayer());
		//event.setCancelled(true); // message still gets sent with this line commented out
	}
	
	@EventHandler (priority = EventPriority.HIGH)
	private void placedSign(BlockPlaceEvent event) 
	{
		//1st things 1st. 99% of all blocks placed arent signs. yeet out and stop processing and yoinking RAM if its not a sign.
		//shit. this one has to use a loopy loop, because there's a bunch of signs.
		ArrayList<Material> signs  = new ArrayList<Material>(); 
		//testest signs and I am forgetti to actually put values into this array, loop not looped, so heres the thing
		signs.add(Material.ACACIA_SIGN);
		signs.add(Material.ACACIA_WALL_SIGN);
		signs.add(Material.BIRCH_SIGN);
		signs.add(Material.BIRCH_WALL_SIGN);
		//signs.add(Material.CRIMSON_SIGN);//i realise it's not as usefull for non nether servers but meh. need to test for crashes
		//signs.add(Material.CRIMSON_WALL_SIGN );
		signs.add(Material.DARK_OAK_SIGN);
		signs.add(Material.DARK_OAK_WALL_SIGN);
		signs.add(Material.JUNGLE_SIGN);
		signs.add(Material.JUNGLE_WALL_SIGN);
		//signs.add(Material.LEGACY_SIGN);?? is this the olden signs?
		//signs.add(Material.LEGACY_POST_SIGN);
		//signs.add(Material.LEGACY_WALL_SIGN);
		signs.add(Material.OAK_SIGN);
		signs.add(Material.OAK_WALL_SIGN);
		signs.add(Material.SPRUCE_SIGN);
		signs.add(Material.SPRUCE_WALL_SIGN);
		//signs.add(Material.WARPED_SIGN);//nether too
		//signs.add(Material.WARPED_WALL_SIGN);//nether three
		
		//that stack up there is modifieable. the code below hopefully only works with SIGNS
		
		//since gonna do loops, might cache some values to make easier for cpu to breaTHE this.
		Material blockPlaced = event.getBlockPlaced().getType();
		
		//so if its not one of the signs wanna quick out of this.
		// in other words, if it is one of the signs, then do the process
		
		for (Material MaybeSign : signs) 
		{
			if (blockPlaced == MaybeSign) 
			{
				Sign sign = (Sign) event.getBlockPlaced();
				
				String[] signLines = sign.getLines();
				StringBuffer sb = new StringBuffer();
			      for(int i = 0; i < signLines.length; i++) {
			         sb.append(signLines[i]);
			         sb.append(" ");
			      }
				String singleLine = sb.toString();
				System.out.println("[kickOnTHE] does this look like reasonable thing" + singleLine);
				TWwordSearchIn(singleLine,event.getPlayer());
				break; // found sign, happily doing the deed of the sign letter looking.
			}	
		}
		
	}
	
	private void TWwordSearchIn(String text,Player whoToKick)
	{
		String[] wordsList = text.split(" ");
		
		for(String word : wordsList ) 
		{
			if (word =="the" || word == "The") 
			{
				whoToKick.kickPlayer("T-word");
				System.out.println("[kickonTHE] yeeted "+whoToKick.getName());
				break;
			}
			
		}
		
		
		
	}

	///TODO take a look at general warnings and stuff
	///TODO edit signs list to work with 1.8.9 servers
}
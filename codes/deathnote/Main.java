package lockless.deathnote;


import java.io.File;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.io.IOException;  // Import the IOException class to handle errors

import org.bukkit.BanList.Type;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;



public class Main extends JavaPlugin implements Listener{

	//still debugging inventory == null
	private boolean logToConsole=false;
	private File pluginFolderPath;
    private String foldername = "deathlog";
 
    //wanna copy and paste this shit all around
    private void FolderCreation() 
    {
    	//System.out.println("what's wrong?" +this.getDataFolder().toString() + "vs."+"plugins");
		File authorDir=new File("plugins"+File.separator+"Lockless");
		if(authorDir.exists()==false) 
		{
			try{
				authorDir.mkdirs();
		    }
		    catch(Exception e){
		    	System.out.println("try make authordir fail ");
		    	e.printStackTrace();
		    } 
			if(authorDir.exists()) 
			{
				System.out.println("\nHope you enjoy\n-Lockless\n\n");
			}
		}
		
		pluginFolderPath = new File(authorDir+File.separator+foldername);
		if(pluginFolderPath.exists()==false) 
		{
			try{
				System.out.println("plugin deathlog folder doesnt exist. creating");
				pluginFolderPath.mkdirs();
		    }
		    catch(Exception e){
		    	System.out.println("try make dir fail ");
		    	e.printStackTrace();
		    }
			
		}
		
    }

	@Override
	public void onEnable()
	{
		//instance = this;
		System.out.println("enabling ondeath logging");
		getServer().getPluginManager().registerEvents(this, this);
		//checkIfMy folder exists.? make one?
		FolderCreation();
		
		
		
		
		
		
	}
//async is stupid error
	@EventHandler 
	private void OnDeath(PlayerDeathEvent e) 
	{
		if(e==null) 
		{
			System.out.println("[deathnotes] went ooop");
			return;
		}
		
		SimpleDateFormat formatter= new SimpleDateFormat("yyyy'y'MM'm'dd'd'HH'h'");
		Date date = new Date(System.currentTimeMillis());
		//System.out.println(formatter.format(date));
		if(logToConsole) 
		{
			System.out.println("\n\nplayer died and it could be logged\n" + e.getEntity().getName()+ "\n"+ formatter.format(date));
			System.out.println(e.getDeathMessage());
		}
		//always tries to write to file
		try
		{
		 
			PrintWriter printer = new PrintWriter(pluginFolderPath.getPath()+File.separator+formatter.format(date) +"-"+e.getEntity().getDisplayName()+"-deathnote.txt", "UTF-8");
		    
			printer.println(e.getEntity().getDisplayName());
		    printer.println(e.getDeathMessage());
		    printer.print(getWitnesses(e.getEntity(),64));
		    printer.print(getInventoryStringC(e.getEntity().getInventory()));
		    printer.close();
		} 
		catch (IOException ex)
		{
		   System.out.println("well that print writer didnt work "+ex.toString());
		}
		Player bannedNoob = (Player)(e.getEntity());			
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		Date expiryDate = cal.getTime();
		System.out.print("unban "+cal.toString());
		
		Bukkit.getBanList(Type.NAME).addBan(bannedNoob.getName(), "you died", expiryDate, "here is a note certifying death"+e.getDeathMessage());
		
		bannedDude = bannedNoob;
		//totem of undying isnt really death :D
		// need a task, because of "entity removed while ticking" makes the player stay online, but they are kicked. shows up on player lsit as well as could be taking up a player slot
		kicktask=new BukkitRunnable() {
			
			@Override
			public void run() 
			{
				bannedDude.kickPlayer(ChatColor.RED+""+ChatColor.BOLD+ "You died");
			}
		};
		
		kicktask.runTaskLater(this,1l);
	}	
	private BukkitRunnable kicktask;
	private Player bannedDude;
	
	
	
	@EventHandler
	private void loginwarning(PlayerJoinEvent event) 
	{
		event.getPlayer().sendMessage(" just don't "+ChatColor.RED+" die ");
	}

	
	private String getInventoryStringC(PlayerInventory inv) 
	{
		try 
		{

			ItemStack[] inventory =inv.getContents();
			ItemStack[] armor = inv.getArmorContents();
			//System.out.println("inventory "+(inventory!=null)+"    armor "+(armor!=null)); //these true true for player !!!!
			String readableList="Inventory \n";
			for(int i=0 ; i<inventory.length;i++) 
			{
				if(inventory[i] !=null) 
				{
					
					if(i%9 == 0 ) 
					{
						readableList+=" \n ";
					}
					readableList+="   "+inventory[i].toString() +" "+inventory[i].getAmount();
					if(inventory[i].getItemMeta().hasEnchants()) 
					{
						readableList+= " enchanted";
					}
				}
				
			}
			readableList+="\n"+"Armor";
			for(int i=0 ; i<armor.length;i++) 
			{
				if(armor[i] !=null) 
				{
					readableList+="\n"+i + " : "+armor[i].toString();
					if(armor[i].getItemMeta().hasEnchants()) 
					{
						readableList+= " enchanted";
					}
				}
			}
			return readableList;
		}
		catch(NullPointerException e) 
		{
			System.out.println ("smt null error with inventory");
		}
		return "";
	}
	

	//todo actually tell me about enchanments
	@SuppressWarnings("unused")
	private String getInventoryStringB(Player p) 
	{
		try 
		{

			ItemStack[] inventory = p.getInventory().getContents();
			ItemStack[] armor = p.getInventory().getArmorContents();
			//System.out.println("inventory "+(inventory!=null)+"    armor "+(armor!=null)); //these true true for player !!!!
			String readableList="Inventory \n";
			for(int i=0 ; i<inventory.length;i++) 
			{
				if(inventory[i] !=null) 
				{
					
					if(i == 8 || i ==(8+9) || i==(8+9+9) ) 
					{
						readableList+=" \n ";
					}
					readableList+="   "+inventory[i].toString() +" "+inventory[i].getAmount();
					if(inventory[i].getItemMeta().hasEnchants()) 
					{
						readableList+= " enchanted";
					}
				}
				
			}
			readableList+="\n"+"Armor";
			for(int i=0 ; i<armor.length;i++) 
			{
				if(armor[i] !=null) 
				{
					readableList+="\n"+i + " : "+armor[i].toString();
					if(armor[i].getItemMeta().hasEnchants()) 
					{
						readableList+= " enchanted";
					}
				}
			}
			return readableList;
		}
		catch(NullPointerException e) 
		{
			System.out.println ("smt null error with inventory");
		}
		return "";
	}
	
	private String getWitnesses(Player dedMan,double boxSize) 
	{
		String deathWitnesses="";
		
		List<Entity> entities = dedMan.getNearbyEntities(boxSize,boxSize,boxSize);
		dedMan.setPlayerListName("[dead]"+dedMan.getName());
		int count=0;
		for(Entity en:entities) 
		{
			if(en.getType() == EntityType.PLAYER) 
			{
				count++;
				deathWitnesses+=((Player)en).getDisplayName()+" ";
			}
		}
		if(count>0) 
		{
			return "/nwitnessess:"+count+deathWitnesses;			
		}else {return "";}
	}
	
	
	
}

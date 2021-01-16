package lockless.deathnote;


import java.io.File;
import java.io.PrintWriter;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.io.IOException;  // Import the IOException class to handle errors

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
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
		/*both these work
		try{
		    PrintWriter writer = new PrintWriter(pluginFolderPath+File.separator+"printWriterLines.txt", "UTF-8");
		    writer.println("The first line");
		    writer.println("The second line");
		    writer.close();
		} catch (IOException e) {
		   System.out.println("well that print writer didnt work "+e.toString());
		}
		
		try{
			FileWriter writer = new FileWriter(pluginFolderPath+File.separator+"fileWriterLines.txt");
		    writer.write("The first line\n");
		    writer.write("The second line\n");
		    writer.close();
		} catch (IOException e) {
		   System.out.println("well that print writer didnt work "+e.toString());
		}
		*/
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
	//last event
	private PlayerDeathEvent savedEvent;
	private PlayerInventory savedInv;
	private Player savedPlayer;
	private BukkitRunnable asyncTask;
	@EventHandler
	private void OnDeath(PlayerDeathEvent e) 
	{
		
		//maybe async could help with leggg
		if(asyncTask.isCancelled()) 
		{
			savedEvent = e;
			savedPlayer = (Player)savedEvent.getEntity();
			savedInv = savedPlayer.getInventory();	
			asyncTask = new BukkitRunnable() 
			{
				@Override
				public void run() {
					// TODO Auto-generated method stub
					theAssTassk(savedPlayer,savedEvent,savedInv);
					cancel();
				}
			};
			asyncTask.runTaskLaterAsynchronously(this, 1);
			
		}
		else 
		{
			System.out.println("unlogged"+e.getDeathMessage());
		}
		
		
		
	}	
	private void theAssTassk(Player p , PlayerDeathEvent d,PlayerInventory inv ) 
	{
		//sysTime
		SimpleDateFormat formatter= new SimpleDateFormat("yyyy'y'MM'm'dd'd'HH'h");
		Date date = new Date(System.currentTimeMillis());
		//System.out.println(formatter.format(date));
		if(logToConsole) 
		{
			System.out.println("\n\nplayer died and it could be logged\n" + p.getName()+ "\n"+ formatter.format(date) +  "\n"+ d.getDeathMessage());
			System.out.println(p.getDisplayName());
			System.out.println("deathnote " +d.getDeathMessage());
		}
		//always tries to write to file
		try
		{
		    PrintWriter printer = new PrintWriter(pluginFolderPath.getPath()+File.separator+formatter.format(date) +" "+p.getName().toString()+" deathnote.txt", "UTF-8");
		    printer.println(p.getDisplayName());
		    printer.println("deathnote " +d.getDeathMessage());
		    //printer.println("dropped exp" + e.getDroppedExp() +" to compare to playerstat "+p.getExp());
		    //printer.print(getInventoryStringA(p));
		    printer.println("Lv"+p.getLevel());
		    printer.print(getWitnesses(p,64));
		    printer.print(getInventoryStringC(inv));
		    printer.close();
		} 
		catch (IOException ex)
		{
			//totalFails
		   System.out.println("well that print writer didnt work "+ex.toString());
		}
		
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

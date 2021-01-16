package lockless.demondragonkings;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class Main extends JavaPlugin{

	private double spawnRadius=10000;
	private long lastCall;
	private long cooldown=300000;
	
	private File pluginFolderPath;
    private String foldername = "demonDragonKings";
    
    private int currentWorthyOverworld=0;
    private int currentWorthyNether=0;
    private int currentWorthyEnd=0;
    
    private boolean thereIsSouth=false;
    private boolean thereIsSouthWest=false;
    private boolean thereIsSouthEast=false;
    private boolean thereIsWest=false;
    private boolean thereIsEast=false;
    private boolean thereIsNorth=false;
    private boolean thereIsNorthEast=false;
    private boolean thereIsNorthWest=false;
    private boolean thereIsMiddle=false;
    
    
    /*
    import java.io.File;
	import java.io.PrintWriter;
	import java.util.Date;
	import java.text.SimpleDateFormat;
	import java.util.List;
	import java.io.IOException;  // Import the IOException class to handle errors

     */
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
				System.out.println("plugin "+foldername+ "folder doesnt exist. creating");
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
	
	public void onEnable()
	{
		System.out.println("demondragonkings enabled");
		FolderCreation();
		lastCall = System.currentTimeMillis();
	}
	
	public void onDisable() 
	{
		System.out.println("demondragonkings disabled");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// TODO Auto-generated method stub
		if(label.equalsIgnoreCase("tellmemore")) 
		{
			
			if(sender.hasPermission("history.read")==false) 
			{
				sender.sendMessage("you dont have a permission to read history");
				return true;
			}
			String storytime = ChatColor.GREEN+"The Kings of the overworld rule in their corners \nBeing invincible, overpowered and surrounded by an aura of clout \nThere is a Demon in hell, who senses when somebody enters his domain\nyou've heard about Dragons one of them has assumed the shape similar to yours";
			sender.sendMessage(storytime);
			if(sender instanceof Player) 
			{
				sender.sendMessage(ChatColor.GREEN+"The Worthy lead their tribes");
				sender.sendMessage(ChatColor.RED+"The Worthy stand mighty and endeavour");
				sender.sendMessage(ChatColor.LIGHT_PURPLE+"The Worthy have earned their place among the letters written on feathers of time");
				
				if(thereIsSouth) {sender.sendMessage(ChatColor.GREEN+"there is a worthy one in the South");}
				if(thereIsSouthWest){sender.sendMessage(ChatColor.GREEN+"there is a worthy one in the South-West");}
				if(thereIsSouthEast){sender.sendMessage(ChatColor.GREEN+"there is a worthy one in the South-East");}
				if(thereIsWest){sender.sendMessage(ChatColor.GREEN+"there is a worthy one in the West");}
				if(thereIsEast){sender.sendMessage(ChatColor.GREEN+"there is a worthy one in the East");}
				if(thereIsNorth){sender.sendMessage(ChatColor.GREEN+"there is a worthy one in the North");}
				if(thereIsNorthEast){sender.sendMessage(ChatColor.GREEN+"there is a worthy one in the North-East");}
				if(thereIsNorthWest){sender.sendMessage(ChatColor.GREEN+"there is a worthy one in the North-West");}
				if(thereIsMiddle){sender.sendMessage(ChatColor.GREEN+"there is a worthy one in the Middle");}
	
				
				return true;
			}else 
			{
				if(thereIsSouth) {sender.sendMessage("there is a worthy one in the South");}
				if(thereIsSouthWest){sender.sendMessage("there is a worthy one in the South-West");}
				if(thereIsSouthEast){sender.sendMessage("there is a worthy one in the South-East");}
				if(thereIsWest){sender.sendMessage("there is a worthy one in the West");}
				if(thereIsEast){sender.sendMessage("there is a worthy one in the East");}
				if(thereIsNorth){sender.sendMessage("there is a worthy one in the North");}
				if(thereIsNorthEast){sender.sendMessage("there is a worthy one in the North-East");}
				if(thereIsNorthWest){sender.sendMessage("there is a worthy one in the North-West");}
				if(thereIsMiddle){sender.sendMessage("there is a worthy one in the Middle");}
				if(currentWorthyNether>0) {sender.sendMessage("there is a worthy "+currentWorthyNether+" in the Nether");}
				if(currentWorthyEnd>0) {sender.sendMessage("there is a worthy "+currentWorthyEnd+" in the End");}
			}
			
		}
		/*
		if(label.equalsIgnoreCase("power")) 
		{
			if(!(sender instanceof Player)) 
			{
				sender.sendMessage("need to be a real player  to use /power");
				return true;
			}
			
			if (sender.hasPermission("power.use")) 
			{
				sender.sendMessage("perms limited");
				return true;
			}
			
			sender.sendMessage("power is not implemented yet...");
			return true;
		}
		
		if(label.equalsIgnoreCase("pickpower")) 
		{
			if(!(sender instanceof Player)) 
			{
				sender.sendMessage("need to be areal player to use /pickpower");
				return true;
			}
			
			if (sender.hasPermission("pickpower.use")) 
			{
				sender.sendMessage("perms limited");
				return true;
			}
			
			sender.sendMessage("power is not implemented yet...");
			return true;
		}
		*/
		if(label.equalsIgnoreCase("amiworthy")) 
		{
			
			
			if(!(sender instanceof Player)) 
			{
				sender.sendMessage("need to be a real player to use /worthy");
				return true;
			}
			
			if(sender.hasPermission("history.make")==false) 
			{
				sender.sendMessage("you dont have a permission to make history");
				return true;
			}
			
			if(System.currentTimeMillis() - cooldown < lastCall) 
			{
				System.out.println(" am i worthy cooldown");
				return true;
			}
			else 
			{
				lastCall = System.currentTimeMillis();
			}
			
			/*
			if (sender.hasPermission("worthy.use")) 
			{
				sender.sendMessage("perms limited");
				return true;
			}*/
			
			
			return oncommandWorthy((Player) sender); 
		}
		return false;
	}
	
	private void setDisplayName(Player player, String prefix) 
	{
		String currentName = player.getDisplayName();
		if(currentName.contains(prefix)) 
		{
			player.sendMessage(ChatColor.LIGHT_PURPLE +"you are already there...");
		}
		else 
		{
			player.setDisplayName(prefix+" "+currentName);
		}
	}
	
	private void setMoreMaxHP(Player player, double val) 
	{
		player.sendMessage(ChatColor.LIGHT_PURPLE + "you have become invincible. max health "+val);
		System.out.println("trying to change maxhealth of the worthy" + player.getName());
		player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(val);
		System.out.println("now maxhp seems to be "+(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)));
	}
	
	private boolean oncommandWorthy(Player p) 
	{
		//int level to check against
		int overworldCheck= 30;
		int netherCheck= 300;
		int endCheck= 1337;
		//check world.
		String worldName = p.getWorld().getName();
		System.out.println("worldname "+worldName);
		
		if(worldName == "world") 
		{
			checkPlayerOut(p);
			//determine corner of the world
			if (p.getLevel()>=overworldCheck) 
			{
				Vector vectorPos = p.getEyeLocation().toVector();
				if(vectorPos.getX() > spawnRadius && vectorPos.getZ()>spawnRadius) 
				{
					if(thereIsSouthWest == false) 
					{
						thereIsSouthWest = true;
						p.sendMessage("you are entitled to power of south-west");
						OverworldCheck2(p);
					}
					return true;
				}
				if(vectorPos.getX() > spawnRadius && vectorPos.getZ()<-spawnRadius) 
				{
					if(thereIsNorthWest == false) 
					{
						thereIsNorthWest = true;
						p.sendMessage("you are entitled to power of north-west");
						OverworldCheck2(p);
					}
					return true;
				}
				if(vectorPos.getX() <-spawnRadius && vectorPos.getZ()>spawnRadius) 
				{
				
					if(thereIsSouthEast == false) 
					{
						thereIsSouthEast = true;

						p.sendMessage("you are entitled to power of south-east");
						OverworldCheck2(p);
					}
					return true;
				}
				if(vectorPos.getX() <-spawnRadius && vectorPos.getZ()<-spawnRadius) 
				{
					
					if(thereIsNorthEast == false) 
					{
						thereIsNorthEast = true;

						p.sendMessage("you are entitled to power of north-east");
						OverworldCheck2(p);
					}
					return true;
				}
				//highways?
				if(vectorPos.getX() > spawnRadius ) 
				{
					if(thereIsWest == false) 
					{
						thereIsWest = true;

						p.sendMessage("you are entitled to power of west");
						OverworldCheck2(p);
						
					}
					return true;
				}
				if( vectorPos.getZ()<-spawnRadius) 
				{
					if(thereIsNorth == false) 
					{
						thereIsNorth = true;

						p.sendMessage("you are entitled to power of north");
						OverworldCheck2(p);
					}
					return true;
				}
				if(vectorPos.getZ()>spawnRadius) 
				{
					if(thereIsSouth == false) 
					{
						thereIsSouth = true;

						p.sendMessage("you are entitled to power of south");
						OverworldCheck2(p);
					}
					return true;
				}
				if(vectorPos.getX() <-spawnRadius ) 
				{
					if(thereIsEast == false) 
					{
						thereIsEast = true;

						p.sendMessage("you are entitled to power of east");
						OverworldCheck2(p);
					}
					return true;
				}
				//spawn
				if(thereIsMiddle == false) 
				{
					thereIsMiddle = true;
					p.sendMessage("you are entitled to power of the center Of the World");
					OverworldCheck2(p);
				}
				return true;
			}
			else 
			{
				return true;
			}
			
		}
		
		if(worldName == "world_nether") 
		{
			checkPlayerOut(p);
			if (p.getLevel()>=netherCheck) 
			{
				if(p.getStatistic(Statistic.PLAYER_KILLS)>7) 
				{
					currentWorthyNether++;
					p.sendMessage("you are entitled to power of nether");
					setMoreMaxHP(p,20);
					setDisplayName(p , ChatColor.RED+"[Worthy]");
				}
			}
			return true;
		}
		
		if(worldName == "world_the_end") 
		{
			checkPlayerOut(p);
			if (p.getLevel()>=endCheck) 
			{
				currentWorthyEnd++;
				p.sendMessage("you are entitled to power of the end");
				setMoreMaxHP(p,20);
				setDisplayName(p , ChatColor.LIGHT_PURPLE+"Worthy ");
			}
			return true;
		}
		System.out.println("server remains silent as it's ignoring a weakling question");
		return true;
	}
	
	private void OverworldCheck2(Player player) 
	{
		if (currentWorthyOverworld < 4) 
		{
			currentWorthyOverworld++;
			setMoreMaxHP(player,20);
			setDisplayName(player , ChatColor.GOLD+"[Worthy]");
			
			System.out.println("\n\n "+player.getName()+"Proved their basic worth\n"+currentWorthyOverworld +"o " + currentWorthyNether+"n "+ currentWorthyEnd+"e");
		}
		else 
		{
			player.sendMessage("but the Overworld corners are already held");
		}
	}
	
	private void checkPlayerOut(Player player) 
	{
		boolean debugToFolder=true;
		if(debugToFolder) 
		{
			
			try{
			    PrintWriter writer = new PrintWriter(pluginFolderPath+File.separator+"printWriterLines.txt", "UTF-8");
			    writer.println("checking out character "+player.getName());
			    writer.println("experience "+player.getTotalExperience());
			    writer.println("exp "+player.getExp());
			    writer.println("Level "+player.getLevel()+" <-- this is important");
			    writer.println("hp "+player.getHealth());
			    writer.println("arrows "+player.getArrowsInBody());
			    writer.println("weather "+player.getPlayerWeather());
			    writer.println("1st played "+player.getFirstPlayed());
			    writer.println("locale "+player.getLocale());
			    writer.println("air "+player.getRemainingAir());
			    writer.println("exhaustion "+player.getExhaustion());
			    writer.close();
			} catch (IOException e) {
			   System.out.println("well that print writer didnt work "+e.toString());
			}
		}
		else 
		{			
			System.out.println("checking out character "+player.getName());
			System.out.println("experience "+player.getTotalExperience());
			System.out.println("exp "+player.getExp());
			System.out.println("Level "+player.getLevel()+" <-- this is important");
			System.out.println("hp "+player.getHealth());
			System.out.println("arrows "+player.getArrowsInBody());
			System.out.println("weather "+player.getPlayerWeather());
			System.out.println("1st played "+player.getFirstPlayed());
			System.out.println("locale "+player.getLocale());
			System.out.println("air "+player.getRemainingAir());
			System.out.println("exhaustion "+player.getExhaustion());
		}
	}
	
	
	
	
	
	/*ideas
	private void powerAquaman() 
	{
		//summon dolphin
	}
	private void powerDemon() 
	{
		// private message coordinates of a single other player in nether
	}
	private void powerDragon() 
	{
		//can do simple flight at sprint speed or smt
	}
	private void powerRainfall() 
	{
		//get Regeneration , Speed , Leap , Strength when in rain
	}
	
	private void powerLevitation() 
	{
		//walkspeed flight in overworld
	}
	
	private void powerGreed() 
	{
		//basicly vault
	}
	
	private void powerGoons() 
	{
		//summon steves with swords to harrass attackers and follow like dogs or smt.
	}
	
	private void powerMidas() 
	{
		//turn coal and iron  and lapis and redstone in inventory to some form of gold
		// tools and armors too. big code
	}
	
	private void powerInvisibility() 
	{
		//turn coal and iron  and lapis and redstone in inventory to some form of gold
	}
	
	private void powerSense100(x,y,z) 
	{
		//senses entities using bounding box its like arcane detection trap anywhere
	}
	
	private void powerTrack() 
	{
		//tries to whiff out some body close or somebody to the east, where clouds are. but only above water level and bigger boundingbox than Sense
	}
	
	*/
}

package lockless.blockRace;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin {

	private List<Material> blocksThatAreGoodForRace = new ArrayList<Material>();
	private int RaceCount=0;
	//some teleportation stuff
	private double howFarToTeleport = 69000;
	private double howFarSpread = 80;
	//for states
	private boolean debuggingsomeBlocks=false;
	private boolean preparing=false;
	private boolean racing=false;
	private String ruleset="put away your gear, items and blocks before the start";
	//for timers
	private int preparationCountDown=100;//usefull for /ready
	private int preparationSeconds=100; //every new person to join resets this preparation timer
	private int blocksPerRace=6; // should make it quicker than 30minutes in the end
	private int minutesPerBlock=5; // should make it quicker than 30 minutes
	//private int racers=0;
	private Player winner;
	private String CompiledScoreString="Nobody raced since server started";
	//tasks, got some null errors but i dont give a fuck, console doesnt bleed im ok with that
	private BukkitRunnable preparationTask;
	private BukkitRunnable raceCheckLoop;
	
	private World raceWorld;
	// these Players gonna get checked a lot
	private List<Player> participants =  new ArrayList<Player>(); 
	//tryhard part
	private List<Integer> scores = new ArrayList<Integer>();
	private List<Integer> blocksFound = new ArrayList<Integer>();
	private List<Integer> possibleScore = new ArrayList<Integer>();
	private List<Material> checkPointMaterial = new ArrayList<Material>();
	private List<Material> ORcheckPointMaterial = new ArrayList<Material>(); // makes shit ez+1
	// ready to race
	private List<Boolean> readyToRace = new ArrayList<Boolean>();
	//handmade /back lol
	private List<Location> startingLocations = new ArrayList<Location>();
	private List<Float> startingExperience = new ArrayList<Float>();
	private List<Location> startingBedLocations = new ArrayList<Location>();//players trying to escape spawn with blockrace? i call it a bug.
	
	//no i dont, use possible score == 0 to lose.
	//save EXP not to be involved in the race. risk free?
	private Material  testBlockMaterial = Material.GRASS_BLOCK;
	
	
	private File pluginFolderPath;
    private String foldername = "blockrace";
    private String latestRaceLog;
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
				System.out.println("plugin "+foldername+" folder doesnt exist. creating");
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
		System.out.println("enabling blockRace");
		//todo maybe have a config.yml
		FolderCreation();
		SetupBlockRaceMaterials();
		System.out.println("Current count of RaceBlockMaterials is "+blocksThatAreGoodForRace.size());
		raceWorld = getServer().getWorld("world");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) 
	{
		if(label.equalsIgnoreCase("blockrace")) 
		{
			if((sender instanceof Player)==false) 
			{
				System.out.println("preparing "+preparing);
				System.out.println("racing "+racing);
				if(racing) 
				{System.out.print("\n");
					for(Player p:participants) 
					{
						if(p != null) 
						{
							System.out.print(p.getName()+" ");
						}
					}
					System.out.println("\n");
				}
				return true;
			}
			
			if(args.length == 0) 
			{
				sender.sendMessage("It's like what Dream and George have. big respect for them for finding new ways to enjoys blockgame.this plugin is a weekend of me just spaghetti codign in a a non-native language to make my server a bit more cool. currently racing with 3 players is the best way to drive the server to a grinding halt. I encourage you to test it out /blockrace start");
			}
			else if (args[0].equalsIgnoreCase("help")) 
			{
				if(!sender.hasPermission("blockrace.play")) 
				{
					return true;
				}
				return commandFullInfo(sender);
			}
			else if (args[0].equalsIgnoreCase("start")) 
			{
				if(!sender.hasPermission("blockrace.play")) 
				{
					return true;
				}
				return commandStart(sender);
			}
			else if (args[0].equalsIgnoreCase("yes")) 
			{
				if(!sender.hasPermission("blockrace.play")) 
				{
					return true;
				}
				return commandYes(sender);
			}else if(args[0].equalsIgnoreCase("ready")) 
			{
				if(!sender.hasPermission("blockrace.play")) 
				{
					return true;
				}
				return commandReady(sender);
			}
			else if (args[0].equalsIgnoreCase("rejoin")) 
			{
				if(!sender.hasPermission("blockrace.play")) 
				{
					return true;
				}
				if(sender instanceof Player) 
				{
					if(racing==true || preparing == true) 
					{
						
						
						for(int  i=0;i<participants.size();i++) 
						{
							if (participants.get(i).getName() == ((Player)sender).getName())
							{
								System.out.println("checking old online thing "+participants.get(i).isOnline());//maybe this debug will give some ideas about cracked alts stealing the show.
								//im guessing its tied to entities. but who knows. hoping it gives FALSE
								participants.set(i, (Player)sender);//this can totally be abused AF
								sender.sendMessage(ChatColor.GOLD + "BACK ON TRACK!");
								quickInfo(i);
								return true;
							}
						}
						sender.sendMessage(ChatColor.RED + "Can't do that");
						return true;
					}
					else 
					{
						sender.sendMessage("that race is probably finished");
					}
					sender.sendMessage("ur xp" +((Player)sender).getExp() );
				}
				return true;
			}
			else if(args[0].equalsIgnoreCase("score")) 
			{
				if(!sender.hasPermission("blockrace.play")) 
				{
					return true;
				}
				return commandScore(sender);
			}else if(args[0].equalsIgnoreCase("blockracecancel")) 
			{
				if(!sender.hasPermission("blockrace.tests")) 
				{
					return true;
				}
				return commandCancel(sender);
			}else  if(args[0].equalsIgnoreCase("set"))
			{
				if(!sender.hasPermission("blockrace.edit")) 
				{
					return true;
				}
				//setting shit up on the fly
				if(args[0].equalsIgnoreCase("blockraceruleset")) 
				{
					//does this shit break it?!!?!?
					ruleset = args.toString(); // 1st time using args. hurrdurr gon break
					return true;
				}else if(args[1].equalsIgnoreCase("blocksperrace")) 
				{
					if(racing == false && preparing == false) 
					{
						sender.sendMessage("setting rounds of blockrace");
						int parsedValue = miTryParse(args[0],0,10);
						if(parsedValue ==0) 
						{
							sender.sendMessage("but its a stupid value");
						}
						else 
						{
							blocksPerRace = parsedValue;	
						}
					}
					else 
					{
						sender.sendMessage("can't set during a race");
					}
					return true;
				}else if(args[1].equalsIgnoreCase("minutesperblock")) 
				{
					if(racing == false && preparing == false) 
					{
						sender.sendMessage("setting minutes per block of blockrace");
						int parsedValue =miTryParse(args[0],0,40);
						if(parsedValue ==0) 
						{
							sender.sendMessage("but its a stupid value");
						}
						else 
						{
							if(parsedValue>20) {sender.sendMessage("imma set that, but holy fuck, do you want to resurrect the Aztec civilization in there?");}
							minutesPerBlock = parsedValue;	
						}
					}
					else 
					{
						sender.sendMessage("can't set during a race");
					}
					return true;
				}else if(args[1].equalsIgnoreCase("preptime")) 
				{
					if(racing == false && preparing == false) 
					{
						sender.sendMessage("setting  preparation time in seconds blockrace");
						int parsedValue =miTryParse(args[0],15,300);
						if(parsedValue ==0) 
						{
							sender.sendMessage("but its a stupid value");
						}
						else 
						{
							preparationSeconds = parsedValue;	
						}
					}
					else 
					{
						sender.sendMessage("can't set during a race");
					}
					return true;
				}else 
				{
					sender.sendMessage("set what???");
				return true;
				}
			}
			return true;
			
		}
		
		
		
		
		
		
		if(label.equalsIgnoreCase("blockracetesttp")) 
		{
			if(!sender.hasPermission("blockrace.tests")) 
			{
				return true;
			}
			if(sender instanceof Player) 
			{
				myTestTpCommandLogic((Player)sender);
			}
			return true;
		}
		
		
		if(label.equalsIgnoreCase("blockracetestdetection")) 
		{
			if(!sender.hasPermission("blockrace.tests")) 
			{
				return true;
			}
			return testDetectionLogic(sender);
		}
		
		return false;
	}
	
	
	//returns 0 if shit
	private int miTryParse(String s, int min, int max) 
	{
		int  trying = Integer.parseInt(s);
		if(trying >min && trying <max) 
		{
			return trying;
		}
		
		return 0;
		
	}
	
	
	private void DoTests() 
	{
		for(Player p :Bukkit.getServer().getOnlinePlayers())
		{
			final Location loc = p.getLocation().getBlock().getRelative(BlockFace.DOWN).getLocation(); // vomits
			final Block b = loc.getBlock();
			final Material type = b.getType();
			//Whatever Material you want
			if (type == testBlockMaterial) 
			{
				System.out.println(p.getName()+ "is standing on a grassblock");
			}
			else 
			{
				System.out.println(p.getName()+ " is standing on "+type);
			}
		}
	}
	
	private boolean checkQuick(Player p, Material m) 
	{
		//what Block is the player looking at?
		if (p.getTargetBlockExact(16).getType() == m ) 
		{
			return true;
		}
		return false;
	}
	
	private boolean checkIfBlock(Player p, Material m) 
	{
		final Location loc = p.getLocation().getBlock().getRelative(BlockFace.DOWN).getLocation(); // vomits
		final Block b = loc.getBlock();
		final Material type = b.getType();
		//Whatever Material you want
		return (type == testBlockMaterial) ;
	}
	
	private boolean checkIfBlockButMore(Player p, Material m) 
	{
		final Location loc = p.getLocation().getBlock().getRelative(BlockFace.DOWN).getLocation(); // vomits
		
		List<Material> MaterialsBelow = new ArrayList<Material>(); 
		int someRadius = 2;
		for(int x = -someRadius;x<someRadius;x++) 
		{
			for(int y = -someRadius;y<someRadius;y++) 
			{
				for(int z = -someRadius;z<someRadius;z++) 
				{
					double ox = loc.getBlockX()+x;
					double oy = loc.getBlockY()+y;
					double oz = loc.getBlockZ()+z;
					
					Location blockToArrayLocation =new Location(p.getWorld(),ox,oy,oz);
					Material tmpM = blockToArrayLocation.getBlock().getType();
					if(MaterialsBelow.contains(tmpM)==false) 
					{
						MaterialsBelow.add(tmpM);						
					}
					//logic is loc + xyz is block
					
					//System.out.println(p.getName() + " has " + tmpM);
				}
			}
		}
		if(debuggingsomeBlocks) 
		{			
			for(Material _mNames:MaterialsBelow) 
			{
				System.out.println(p.getName() + " has " + _mNames);
			}
		}
		for(Material _m :MaterialsBelow) 
		{
			if (_m== m) 
			{
				return true;
			}
		}
		
		
		//Whatever Material you want
		return false;
	}
	

	

	private void SetupBlockRaceMaterials() 
	{
		blocksThatAreGoodForRace = new ArrayList<Material>();
		
		for(Material m : Material.values()) 
		{
			//here we go with a scary staircase
			if (m.isBlock()) 
			{
				if(m.isRecord()) 
				{
					//System.out.println("record "+ m.name());
				}
				else 
				{
					if (m.name().contains("SKULL") ||m.name().contains("HEAD")) 
					{
						//System.out.println("skulls and heads "+ m.name());
					}
					else 
					{
						/*
						 END_ROD raw END_ROD
[11:08:49] [Server thread/INFO]: 174 CHORUS_PLANT raw CHORUS_PLANT
[11:08:49] [Server thread/INFO]: 175 CHORUS_FLOWER raw CHORUS_FLOWER
[11:08:49] [Server thread/INFO]: 176 PURPUR_BLOCK raw PURPUR_BLOCK
[11:08:49] [Server thread/INFO]: 177 PURPUR_PILLAR raw PURPUR_PILLAR
[11:08:49] [Server thread/INFO]: 178 PURPUR_STAIRS raw PURPUR_STAIRS
[11:08:49] [Server thread/INFO]: 179 SPAWNER raw SPAWNER 
						 
						  */
						if(m.name().equalsIgnoreCase("DRAGON_EGG")||m.name().equalsIgnoreCase("END_ROD")||m.name().equalsIgnoreCase("CHORUS_PLANT")||m.name().equalsIgnoreCase("CHORUS_FLOWER")||m.name().equalsIgnoreCase("PURPUR_BLOCK")||m.name().equalsIgnoreCase("PURPUR_PILLAR")||m.name().equalsIgnoreCase("PURPUR_STAIRS")||m.name().equalsIgnoreCase("SPAWNER")) 
						{
							//System.out.println("beyond dragon shit or a fucking spawner"+m.name());
						}
						else 
						{
							if(m.name().contains("AIR")==false ) 
							{
								blocksThatAreGoodForRace.add(m);								
							}
						}
						
					}
					
					
				}
				
				
				
			
			}
			else 
			{
				//System.out.println("not block "+ m.name());
			}
			
		}
		System.out.println("there are "+blocksThatAreGoodForRace.size()+" good blocks");
	}
	
	
	
	private ArrayList<Material> checkBlockButMoreButGib(Player p)
	{
		final Location loc = p.getLocation().getBlock().getRelative(BlockFace.DOWN).getLocation(); // vomits
		
		ArrayList<Material> MaterialsAround = new ArrayList<Material>(); 
		int someRadius = 5;
		for(int x = -someRadius;x<=someRadius;x++) 
		{
			for(int y = -someRadius;y<someRadius;y++) 
			{
				for(int z = -someRadius;z<=someRadius;z++) 
				{
					double ox = loc.getBlockX()+x;
					double oy = loc.getBlockY()+y;
					double oz = loc.getBlockZ()+z;
					
					Location blockToArrayLocation =new Location(p.getWorld(),ox,oy,oz);
					Material tmpM = blockToArrayLocation.getBlock().getType();
					if(tmpM.isBlock()) 
					{						
						if(MaterialsAround.contains(tmpM)==false) 
						{
							MaterialsAround.add(tmpM);						
						}
					}
				}
			}
		}
		return MaterialsAround;
	}
	
	private Material getRandomMaterial() 
	{
		//Material Rng=Material.values(new Random().nextInt(Material.values().length));
		int rng = (int)(Math.random() * blocksThatAreGoodForRace.size());
		System.out.println("random material " + rng);
		//all blocks but removes some blocks at start of race to make rng a bit more fair.
		return blocksThatAreGoodForRace.get(rng);
	}
	
	private Material getRNGMaterialButClearIt() 
	{
		if(blocksThatAreGoodForRace.size()==0) 
		{
			System.out.println("reseting blocksThatAreGoodForRace during race");
			SetupBlockRaceMaterials();
		}
		int rng =(int)(Math.random() * blocksThatAreGoodForRace.size());
		Material returnthis=blocksThatAreGoodForRace.get(rng);
		blocksThatAreGoodForRace.remove(rng);
		System.out.println(" there are blocksThatAreGoodForRace " + blocksThatAreGoodForRace.size());
		System.out.println("rem RNGmaterial " + rng);
		return returnthis;
		
	}
	
	private void killStupidEasyMaterials(List<Material> mats) 
	{
		blocksThatAreGoodForRace.removeAll(mats);	
		System.out.println(" new blocksThatAreGoodForRace size = "+blocksThatAreGoodForRace.size());
	}

	
	private void myTestTpCommandLogic(Player p) 
	{
		System.out.println("test");
		//lets assume that there is a block that is not lava atleas once in 3 tries. shit rng results in falling in lava, but fuck it
		//this is the general direction
		double x = Math.random() * 2 -1;
		double z = Math.random() * 2 -1;
		double currentVectorLength = Math.sqrt(x*x+z*z);//but this needs to be howFarToTeleport
		
		//accordign to my trigonometry knowledge if i keep the angles the same i could just fucking go ahead and multiply x,z by some ratio
		double someRatio = howFarToTeleport/currentVectorLength;
		x *= someRatio;
		z *= someRatio;
		p.sendMessage("teleporting you somewhere around "+ x +";"+z);
		double y = 69;
		//just let htis siht run in the dark without any prints and plei geim lol
		for(int notlavapls=0;notlavapls<3;notlavapls++) 
		{
			double addsomeX = Math.random() * 2 -1;
			double addsomeZ = Math.random() * 2 -1;
			currentVectorLength = Math.sqrt(addsomeX*addsomeX+addsomeZ*addsomeZ);
			someRatio = howFarSpread/currentVectorLength;
			addsomeX = x + addsomeX*someRatio;
			addsomeZ = z + addsomeZ*someRatio;
			//its a good saturday/sunday, but that VVV
			//player.getWorld().getHighestBlockYAt(x, z);//the shittiest command ever, like srsly it's fucking dirt//in that other plugin it works ok. not gonna touch this code anymore, cuz gotta respect my past self. im not here to optimize code really.
			boolean foundGood=false;
			//pls have ground in that y range
			for (int i = 5; i<255;i++) 
			{
				//found ground. stupid Y is inverted, this doesnt make any sense!
				if(p.getWorld().getBlockAt(new Location(p.getWorld(),addsomeX,i,addsomeZ)).getType() == Material.AIR) 
				{
					//cool now check not lava
					//fuck hard, code harder, play like pleb
					boolean foundPooTooHotToHandleForPleb=false;
					for(int blockCheckLava = 0 ; blockCheckLava<5; blockCheckLava++) 
					{
						if(p.getWorld().getBlockAt(new Location(p.getWorld(),x,i+blockCheckLava,z)).getType() == Material.LAVA) 
						{
							foundPooTooHotToHandleForPleb=true;
							break;
						}
					}
					if(foundPooTooHotToHandleForPleb ==false) 
					{
						foundGood=true;
					}
					y = i-2;//-1 compensate for leg 
					break;
				}
			}
			if(foundGood) 
			{
				break;
			}
		}
		//would like to say sorry for those who spawn inside rock, but im out of code.
		Location testLocation = new Location(p.getWorld(),x,y,z);
		p.teleport(testLocation);
		
		
	}

	//test was ok like 5/7 for sunday morning.
	//ctrl+c ctrl+v that shit like a professional
	private void LegitBlockRaceTp() 
	{
		System.out.println("proper tp");
		//this is the general direction
		double x = Math.random() * 2 -1;
		double z = Math.random() * 2 -1;
		double currentVectorLength = Math.sqrt(x*x+z*z);//but this needs to be howFarToTeleport
		//accordign to my trigonometry knowledge if i keep the angles the same i could just fucking go ahead and multiply x,z by some ratio
		double someRatio = howFarToTeleport/currentVectorLength;
		x *= someRatio;
		z *= someRatio;
		//lets assume that there is a block that is not lava atleas once in 3 tries. shit rng results in falling in lava, but fuck it
		for(Player p :participants) 
		{
			p.sendMessage("teleporting you somewhere around "+ x +";"+z);
			double y = 69;
			//just let htis siht run in the dark without any prints and plei geim lol
			for(int notlavapls=0;notlavapls<3;notlavapls++) 
			{
				double addsomeX = Math.random() * 2 -1;
				double addsomeZ = Math.random() * 2 -1;
				currentVectorLength = Math.sqrt(addsomeX*addsomeX+addsomeZ*addsomeZ);
				someRatio = howFarSpread/currentVectorLength;
				addsomeX = x + addsomeX*someRatio;
				addsomeZ = z + addsomeZ*someRatio;
				//its a good saturday/sunday, but that VVV
				//player.getWorld().getHighestBlockYAt(x, z);//the shittiest command ever, like srsly it's fucking dirt
				boolean foundGood=false;
				//pls have ground in that y range
				//pls no mountains 
				//fuck it, players who spawn in caves deserve the challange. 5 minutes of strongman punching rock
				for (int i = 5; i<250;i++) 
				{
					//found ground. stupid Y is inverted, this doesnt make any sense!
					Location inspectingThisLocationRn = new Location(raceWorld,addsomeX,i,addsomeZ);
					//just to filter out all bad blocks really quick. i hope this one if statement is quick
					if(raceWorld.getBlockAt(inspectingThisLocationRn).getType() == Material.AIR) 
					{
						if(isSafeLocation(inspectingThisLocationRn) == true) 
						{
								
							//cool now check not lava
							//fuck hard, code harder, play like pleb
							boolean foundPooTooHotToHandleForPleb=false;
							for(int blockCheckLava = -4 ; blockCheckLava<4; blockCheckLava++) 
							{
								if(p.getWorld().getBlockAt(new Location(p.getWorld(),x,i+blockCheckLava,z)).getType() == Material.LAVA) 
								{
									foundPooTooHotToHandleForPleb=true;
									break;
								}
							}
							if(foundPooTooHotToHandleForPleb ==false) 
							{
								foundGood=true;
							}
							y = i+1;//+1 compensate for leg
							//okay i dont know what i meant by that anymore. leG? fucking leg? but that +1 really helps to not spawn in wall
							//door stuck
							//pls stop writing random shit when you ought to be sleeping nigga.
							if(foundGood) 
							{
								break;//breaks out of VerticalSliceCheck
							}
						}
					}
				}
				if(foundGood) 
				{
					break;//breaks out of plsnotlava thing
				}
				System.out.println("tp is kek");
			}
			
			//would like to say sorry for those who spawn inside rock, but im out of code.
			Location raceTPLocation = new Location(raceWorld,x,y,z);
			p.teleport(raceTPLocation);	
		}
	}
	
	private boolean testDetectionLogic(CommandSender sender) 
	{
		if(!(sender instanceof Player)) 
		{
		
			DoTests();
			System.out.println("lol u do tests, noob");
			return true;
		}
		
		Player playerOne= (Player) sender;
		if(!(playerOne.hasPermission("blockrace.tests")))
		{
			playerOne.sendMessage("what?");
		}
		
		if(checkIfBlock(playerOne, testBlockMaterial)) 
		{
			playerOne.sendMessage("quick check for dirt grass worked ");
			return true;
		}
		else 
		{
			if(checkIfBlockButMore(playerOne, testBlockMaterial)) 
			{
				playerOne.sendMessage("laggier check for dirt grass worked");
				return true;
			}
			else 
			{
				playerOne.sendMessage("no GRASS_BLOCKS around. im pretty sure. or is this a bug?");
			}
		}
		
		return true;
	}

	private boolean commandStart(CommandSender sender) 
	{
		//iq test
		if(preparing == false && racing ==false) 
		{
			preparing=true;
			//sender automatically involved
			if(sender instanceof Player) 
			{
				//racers++;
				Player p = (Player) sender;
				raceWorld = p.getWorld();
				if(raceWorld != getServer().getWorld("world")) 
				{
					p.sendMessage(ChatColor.GOLD + "The race is set on "+raceWorld.getName());
				}
				//send invite to all
				for (Player somePlayer : Bukkit.getServer().getOnlinePlayers()) 
				{
					if(somePlayer.getName() != p.getName())
					{						
						somePlayer.sendMessage(ChatColor.GOLD+""+p.getName()+" wants to race against the clock with some block");
						somePlayer.sendMessage(ChatColor.GOLD+"Type /blockraceyes to join");
						//somePlayer.sendMessage(ChatColor.LIGHT_PURPLE+"might as well join for ~30 minutes of fun");
					}
				}
				p.sendMessage(ChatColor.GOLD+"You started the BlockRace");
				return commandYes(p);//assume
			}
			else 
			{
				//console sent blockrace invite
				//just invite all.
				for (Player somePlayer : Bukkit.getServer().getOnlinePlayers()) 
				{	
					somePlayer.sendMessage(ChatColor.GOLD+"OPERATOR invites all to race against the clock with some block");
					somePlayer.sendMessage(ChatColor.GOLD+"Type /blockraceyes to join the race");
					somePlayer.sendMessage(ChatColor.LIGHT_PURPLE+"might as well join for <3 * 10 minutes of fun");
				}
			}
		
		
		//start timer runnable
			preparationTask = new BukkitRunnable() 
			{
				@Override
				public void run() 
				{
					for (Player p:participants) {
						p.sendMessage(ChatColor.GOLD+"Starting the #"+(RaceCount+1)+"Race");
					}
					raceStart();
				}
			};
			//start timer;
			preparationTask.runTaskLater(this,(long)(preparationSeconds*20));
		}
		else 
		{
			sender.sendMessage("Can only start race when there is no race happening");
		}
		
		return true;
	}
	
	private boolean commandYes(CommandSender sender) 
	{
		if(!(sender instanceof Player)) 
		{
			sender.sendMessage("what're u doing with the console");
			return true;
		}
		Player p =(Player) sender;
		if (preparing == false) 
		{
			System.out.println("cock block");
			if (racing ==true) 
			{
				p.sendMessage(ChatColor.GOLD+"the race already started, you can track it with /blockracescore");
			}
			else 
			{
				p.sendMessage(ChatColor.GOLD+"there is no race currently you can track it with /blockracestart");
			}
			return true;
		}
		
		//reset timer
		if(preparationTask != null ) 
		{
			if (preparationTask.isCancelled() == false) 
			{
				preparationTask.cancel();//this was big bug. now to fixx 99 others
			}
		}
		
		preparationCountDown = preparationSeconds;
		preparationTask = new BukkitRunnable() 
		{
			
			@Override
			public void run() 
			{
				preparationCountDown--;
				if(preparationCountDown>0) 
				{
					if(preparationCountDown < preparationSeconds/2) 
					{
						int siz = readyToRace.size();
						for(boolean r:readyToRace ) 
						{
							if(r) 
							{
								siz--;
							}
						}
						for (Player p:participants) {
							p.sendMessage(ChatColor.GOLD+"Starting the #"+(RaceCount+1)+"Race");
						}
						if(siz<=0) 
						{
							System.out.println("blockrace started on ready");
							raceStart();	
						}
					}
				}
				else 
				{
					for (Player p:participants) {
						p.sendMessage(ChatColor.GOLD+"Starting the #"+(RaceCount+1)+"Race");
					}
					System.out.println("blockrace started on preptime");
					raceStart();					
				}
			}
		};

		preparationTask.runTaskTimer(this,(long)(preparationSeconds),3);
		
		for (Player part:participants) 
		{
			if(part != (Player)sender) 
			{
				part.sendMessage(ChatColor.GOLD+""+part.getName()+" enters the BlockRace");				
			}
		}
		
		System.out.println("player joins race");
		//start timer;
	
		//adds player
		participants.add(p); 
		//important part
		scores.add(0);
		blocksFound.add(0);
		possibleScore.add(60*minutesPerBlock);
		// ready to race
		readyToRace.add(false);
		startingLocations.add(p.getLocation()); 
		startingExperience.add(p.getExp());
		startingBedLocations.add(p.getBedSpawnLocation());
		checkPointMaterial.add(Material.DIAMOND_BLOCK);//or something amazingly stupid
		ORcheckPointMaterial.add(Material.IRON_BLOCK);
		/*
		participants = new ArrayList<Player>();
		scores= new ArrayList<Integer>();
		blocksFound= new ArrayList<Integer>();
		possibleScore= new ArrayList<Integer>();
		readyToRace= new ArrayList<Boolean>();
		startingLocations= new ArrayList<Location>();
		startingExperience= new ArrayList<Float>();
		startingBedLocations= new ArrayList<Location>();

		checkPointMaterial= new ArrayList<Material>();
		*/
		//sends rules
		p.sendMessage(ChatColor.GOLD + "quick rules:"+ruleset);
		//p.sendMessage(ChatColor.GOLD + "and get pumped af (yellowline song from the movie redline is recommendation)");
		p.sendMessage(ChatColor.GOLD + "in "+preparationSeconds+" seconds from now you will be teleported to a far off location");
		p.sendMessage(ChatColor.GOLD + "and you will have "+minutesPerBlock+" to find each of the "+blocksPerRace +" blocks.");
		p.sendMessage(ChatColor.GOLD + "1st to find all wins.");
		p.sendMessage(ChatColor.GOLD + "you will get your hunger replenished and missing hearts healed");
		p.sendMessage(ChatColor.GOLD + "you "+ChatColor.RED+"die"+ChatColor.GOLD+", you lose");
		//p.sendMessage(ChatColor.LIGHT_PURPLE+"hey, and /blockraceready if you emptied your inventory and stand in an safe location.");
		
		return true;
	}


	private boolean commandFullInfo(CommandSender sender) 
	{
		if(!(sender instanceof Player)) 
		{
			sender.sendMessage("this is not a console command");
		}
		Player p = (Player) sender;
		if(participants.contains(p)) 
		{
			int myIndex = 0;
			for(int i = 0; i < participants.size();i++) 
			{
				if(p.getName() == participants.get(i).getName()) 
				{
					myIndex=i;
				}
			} 
			/*
			 private List<Integer> scores = new ArrayList<Integer>();
			private List<Integer> blocksFound = new ArrayList<Integer>();
			private List<Integer> possibleScore = new ArrayList<Integer>();
			private List<Material> checkPointMaterial = new ArrayList<Material>();
			// ready to race
			private List<Boolean> readyToRace = new ArrayList<Boolean>();
			
			 */
			
			p.sendMessage(ChatColor.GREEN+"Giving all info");
			p.sendMessage(ChatColor.GOLD+"seconds left "+ possibleScore.get(myIndex));
			p.sendMessage(ChatColor.GOLD+"goal " +ChatColor.UNDERLINE+""+checkPointMaterial.get(myIndex) +ChatColor.RESET+""+ChatColor.GOLD+" or "+ChatColor.UNDERLINE+""+ORcheckPointMaterial.get(myIndex).name());
			p.sendMessage(ChatColor.GOLD+"xp before race "+ startingExperience.get(myIndex));
			p.sendMessage(ChatColor.GOLD+"xp now "+ (startingExperience.get(myIndex)+p.getExp()));
			p.sendMessage(ChatColor.GOLD+"xp after race "+ (startingExperience.get(myIndex)+p.getExp())+ChatColor.RED+" unless you die lol. you are risking something here");
			p.sendMessage(ChatColor.GOLD+"blocks found "+ blocksFound.get(myIndex));
		}
		else 
		{
			p.sendMessage("according to youtube analytics");
			p.sendMessage("join another BlockRace");
			
			return true;
		}
		return true;
	}
	
	private boolean commandCancel(CommandSender sender) 
	{
		//only console
		if(!(sender instanceof Player)) 
		{
			//teleports all players back
			if (racing ==true) 
			{
				
				for (int i =0;i<participants.size();i++) 
				{
					participants.get(i).sendMessage(ChatColor.LIGHT_PURPLE+"Forced cancel race");
					participants.get(i).teleport(startingLocations.get(i));
				}
			}
			HardReset();
			
			//clears lists
			
		}
		else 
		{
			sender.sendMessage(ChatColor.RED+"Can't stop the race you Roboworld scum");
		}
		System.out.println("cancel");
		// 
		return true;
	}
	
	private boolean commandScore(CommandSender sender) 
	{
		if(racing == true) 
		{
			String raceInfo="Current standings in #"+RaceCount+"BlockRace";
			for(int i=0;i<participants.size();i++) 
			{
				raceInfo+=ChatColor.WHITE + "" +participants.get(i).getName()+" scored "+scores.get(i)+"\n"; 				
			}
			sender.sendMessage(raceInfo);
		}
		else 
		{
			sender.sendMessage(CompiledScoreString);
		}
		return true;
	}
	
	private boolean commandReady(CommandSender sender) 
	{
		//sets return location
		if(racing == false && preparing == true) 
		{
			if(sender instanceof Player) 
			{
				if(participants.contains(sender)) 
				{
					for(int i=0;i<participants.size();i++) 
					{
						if(sender.getName() == participants.get(i).getName()) 
						{
							sender.sendMessage(ChatColor.GOLD + " you are ready, return location set ");
							readyToRace.set(i, true);
							startingExperience.set(i , ((Player)sender).getExp());
							startingLocations.set(i, ((Player)sender).getLocation());
						}
					}
				}
			}
		}
		return true;
	}

	
	/**
	 * informs player of current goals
	 * */
	private void quickInfo(int index) 
	{
	
		int myIndex=index;
		Player p = participants.get(myIndex);
		if(myIndex == 69) 
		{
			p.sendMessage("WHAT?!");
			System.out.println("THE FUCK?!");//this actually got called by some hacker fella and i hate him. idk how to fix. how did he do it?! nobody knows. he managed to set a fucking private integer in a method that runs for 0.0015ms multiple times in a race. im scarred for life.
		}
		else 
		{
			p.sendMessage(ChatColor.GREEN+"trying to help");
			p.sendMessage(ChatColor.GOLD+"you have about "+ possibleScore.get(myIndex)+ " seconds left");
			p.sendMessage(ChatColor.GOLD+"need to find " +ChatColor.UNDERLINE+""+checkPointMaterial.get(myIndex) +ChatColor.RESET+""+ChatColor.GOLD+" or "+ChatColor.UNDERLINE+""+ORcheckPointMaterial.get(myIndex).name());
		}
		
		
	}
	
	
	private void raceStart() 
	{
		System.out.println("race start");
		preparationTask.cancel();
		newRaceLog();
		//meh, setup the race task here
		//doublesettingbecause retarded
		for(int i=participants.size()-1;i>=0;i--) 
		{
			possibleScore.set(i,minutesPerBlock*60);
		}
		//and then removing motherfucking dups
		
		List<Player> dupSearch = new ArrayList<Player>(); // i know there is a way to block shit like this in commandYes or something. but imma put it here, since this is acalled only once. imma let my players feel like they ouatsmrted me for about 50 seconds. 
		for(int i=participants.size()-1;i>=0;i--) 
		{
			if(dupSearch.contains(participants.get(i))==false) 
			{
				dupSearch.add(participants.get(i));
			}
			else 
			{
				//this is what i do with dups
				silentRemovalFromRace(i);//not tp, just data manage
			}
		}
		raceCheckLoop = new BukkitRunnable() {
			private int stupidshitchecktimersync=0;
			@Override
			public void run() {
				//racers = participants.size();//i could probably remove the mbiguitiy, but lemme be retarded
				if(participants.size() == 0) 
				{
					for(Player p:Bukkit.getServer().getOnlinePlayers()) 
					{
						p.sendMessage(ChatColor.GOLD+" you need just a bit more practice and a lot more luck ");
						resetRace();
					}
				}
				else 
				{
					//debuging sync
					stupidshitchecktimersync++;
					System.out.println("blockrace : "+stupidshitchecktimersync);
					for(int i=participants.size()-1;i>=0;i--) 
					{
						System.out.println(blocksFound.get(i)+" found/timeleft "+possibleScore.get(i));
					} 
					//debugging sync
					
					//gotta go inverse since it has remove procedure down there
					for(int i=participants.size()-1;i>=0;i--) 
					{
						if(participants.get(i) != null) 
						{
							
							
							if(participants.get(i).getHealth()<=0) 
							{
								System.out.println("removing dead "+i+""+participants.get(i).getName() + " from race");
								removeFromRace(i);
								participants.get(i).sendMessage(ChatColor.RED+""+ChatColor.BOLD+"YOU DIED IN A BLOCKRACE!");
							}
							//player still alive
							else 
							{
								//time is running out!
								possibleScore.set(i, possibleScore.get(i)-1);
								if(possibleScore.get(i)<=0) 
								{
									System.out.println("removing slow "+i+""+participants.get(i).getName() + " from race");
									removeFromRace(i);
									participants.get(i).sendMessage(ChatColor.RED+""+ChatColor.BOLD+"YOU FELL BEHIND AND GOT ELIMINATED FROM BLOCKRACE!");
								}
								//time hasn't run out yet
								else 
								{
									//lemmeput all the timer iffy stairs here
									int currentlyTrackedTime = possibleScore.get(i);
									
									if(currentlyTrackedTime<=10) 
									{
										if(currentlyTrackedTime == 10) 
										{
											participants.get(i).sendMessage(ChatColor.RED+"10 seconds...");									
										}
										if(possibleScore.get(i)==5) 
										{
											participants.get(i).sendMessage(ChatColor.RED+"5");
										}
										if(possibleScore.get(i)==4) 
										{
											participants.get(i).sendMessage(ChatColor.RED+"4");
										}
										if(possibleScore.get(i)==3) 
										{
											participants.get(i).sendMessage(ChatColor.RED+"3");
										}
										if(possibleScore.get(i)==2) 
										{
											participants.get(i).sendMessage(ChatColor.RED+"2");
										}
										if(possibleScore.get(i)==1) 
										{
											participants.get(i).sendMessage(ChatColor.RED+"1");
										}
									}
									//QuickCheckVictory1
									if(checkQuick(participants.get(i), checkPointMaterial.get(i))) 
									{
										WinContProcedure(i , checkPointMaterial.get(i));
									}
									//whatever player is looking at isn't a CheckPoint
									else
									{
										//QuickCheckVictory2
										if(checkQuick(participants.get(i), ORcheckPointMaterial.get(i))) 
										{
											WinContProcedure(i , ORcheckPointMaterial.get(i));
										}
										else 
										{
											//SlowCheck1
											if(checkIfBlockButMore(participants.get(i),checkPointMaterial.get(i))) 
											{
												WinContProcedure(i,checkPointMaterial.get(i));
											}
											else 
											{
												//slowCheck2
												if(checkIfBlockButMore(participants.get(i),ORcheckPointMaterial.get(i))) 
												{
													WinContProcedure(i,ORcheckPointMaterial.get(i));
												}
											}
										}
									}
								}
							}	
						}
						//null player
						else 
						{
							
							possibleScore.set(i,possibleScore.get(i)-1); 
							if(possibleScore.get(i)<=0)
							{
								System.out.println("removing slow "+i+"afk from race");
								silentRemovalFromRace(i);
							}
						}
					}
				}
			}
		};
		
		raceCheckLoop.runTaskTimer(this, 100, 20);
		//setups material 1st
		
		//initial setup
		for(int i =0 ; i < checkPointMaterial.size() ; i++) 
		{
			checkPointMaterial.set(i,getRandomMaterial());
		}
		for(int i =0 ; i < ORcheckPointMaterial.size() ; i++) 
		{
			ORcheckPointMaterial.set(i,getRandomMaterial());
		}
		
		BukkitRunnable quickRemind = new BukkitRunnable() {
			@Override
			public void run() 
			{
				for( int i = 0 ;i < participants.size(); i++ ) 
				{
					participants.get(i).sendMessage(ChatColor.YELLOW+"GO GO GO GET " + checkPointMaterial.get(i).name() + " or " + ORcheckPointMaterial.get(i).name());
				}
			}
		};
		quickRemind.runTaskLater(this, 10);
		
		preparing=false;
		racing=true;
		
		//generate some terrain away from spawn and teleport players there.
		System.out.println("started race with "+participants.size());
		//find a spot to start the race
		//cant find fucking vector2 library, need my fuckign spyglass 
		
		if(participants.size() > 0) 
		{
			for(int i=0;i<participants.size();i++) 
			{
				if(readyToRace.get(i)==false) 
				{
					participants.get(i).sendMessage(ChatColor.GOLD + "you were not ready, but you signed up for it and "+ChatColor.RED+"HERE WE GO");
					startingExperience.set(i , participants.get(i).getExp());
					startingLocations.set(i, participants.get(i).getLocation());
				}
			}
			
			//Location raceMiddleLocation = new Location(participants.get(0).getWorld(),(double )6969,(double )69,(double )6969);
			for(Player p: participants) 
			{
				p.setFoodLevel(20);
				p.setHealth(20);
				p.setExp(0);
			}
			LegitBlockRaceTp();
			//System.out.println("would start the race but there is a part of the procedure missing");//set the 1st block//bitch did it
		}
		else 
		{
			System.out.println("here");//how did the code get here. oh yeah. the OP is faggot
			resetRace();
		}
	}
	
	private void WinContProcedure(int index,Material found) 
	{
		//find the file!!!!!
		LoggingRaces(participants.get(index).getName()+" found " +found.toString());
		
		//checking the same copy pasted code is tiring. better use a method to check one place instead of 4.
		blocksFound.set(index, blocksFound.get(index)+1);
		//win
		if(blocksFound.get(index)>=blocksPerRace) 
		{
			LoggingRaces("Winner " +participants.get(index).getName());
			winner = participants.get(index);
			scores.set(index, scores.get(index) + possibleScore.get(index));
			System.out.println(participants.get(index).getName()+"\t scoring \t"+scores.get(index));//i will believe it when i see it!
			raceFinish();
		}
		//not win, so continue
		else 
		{
			possibleScore.set(index, 60*minutesPerBlock);
			final  ArrayList<Material> tmpMList = checkBlockButMoreButGib(participants.get(index));
			killStupidEasyMaterials(tmpMList);//this shit will make it a lot more difficult in round 6 or smt.
			participants.get(index).sendMessage(ChatColor.GOLD+"you found "+checkPointMaterial.get(index));
			checkPointMaterial.set(index, getRNGMaterialButClearIt());
			ORcheckPointMaterial.set(index, getRNGMaterialButClearIt());
			quickInfo(index);
		}
	}
	
	private void newRaceLog() 
	{
		SimpleDateFormat formatter= new SimpleDateFormat("yyyy'y'MM'm'dd'd'HH'h'");
		Date date = new Date(System.currentTimeMillis());
		latestRaceLog = pluginFolderPath.getPath()+File.separator+"Blockrace "+RaceCount+" "+formatter.format(date) +".log";
	}
	private void LoggingRaces(String LogWhat) 
	{
		
		try{
			File raceLog = new File(latestRaceLog);
			if (raceLog.exists()==false)
			{
				System.out.println("should make log");
				PrintWriter printer = new PrintWriter(latestRaceLog, "UTF-8");
		    	printer.println(LogWhat);
		    	printer.close();
			}
			else
			{
				System.out.println("shouldnt make log");
				PrintWriter printer = new PrintWriter(latestRaceLog, "UTF-8");
		    	printer.append("\n"+LogWhat);
		    	printer.close();
			}
		} catch (IOException e) {
		   System.out.println("well that print writer didnt work "+e.toString());
		}
		
	}


	private void raceFinish() 
	{
		//System.out.println("race finished");
		raceCheckLoop.cancel();// loop calls this, htis cancels it. disgusten
		RaceCount++;
		CompiledScoreString = ChatColor.GOLD+""+winner.getName()+" WINS the #"+RaceCount+" BlockRace\n\n";//need to set winner
		for(int i=0;i<participants.size();i++) 
		{
			participants.get(i).sendMessage(CompiledScoreString);
		}
		//getScore
		
		for(int i=0;i<participants.size();i++) 
		{
			if(participants.get(i).getName() == winner.getName()) 
			{
				CompiledScoreString+=ChatColor.GOLD + "" +participants.get(i).getName()+" scored "+scores.get(i)+"\n"; 								
			}
			else 
			{
				CompiledScoreString+=ChatColor.WHITE + "" +participants.get(i).getName()+" scored "+scores.get(i)+"\n"; 			
			}		
		}
		
		resetRace();
	}
	
	private void resetRace() 
	{
		System.out.println("race reset");
		racing = false;
		preparing = false;
		if(preparationTask.isCancelled() == false) 
		{
			System.out.println("reset cancel that");
			preparationTask.cancel();			
		}
		if(raceCheckLoop.isCancelled() == false) 
		{
			System.out.println("reset cancel that");
			raceCheckLoop.cancel();			
		}
		
		
		//racers=0;
		for(int i = participants.size()-1; i>=0;i--) 
		{
			removeFromRace(i);
		}
		participants = new ArrayList<Player>();
		startingLocations= new ArrayList<Location>();
		startingExperience= new ArrayList<Float>();
		startingBedLocations= new ArrayList<Location>();
		scores= new ArrayList<Integer>();
		blocksFound= new ArrayList<Integer>();
		possibleScore= new ArrayList<Integer>();
		checkPointMaterial= new ArrayList<Material>();
		ORcheckPointMaterial= new ArrayList<Material>();
		readyToRace= new ArrayList<Boolean>();
		System.out.println("race reset setting up materials again");
		SetupBlockRaceMaterials();
	}
	
	
	
	
	/**
	 * for force reset 
	 */
	private void HardReset() 
	{
		//only should be used for testing purposes
		//players are teleported back;
		//give EXP back and clear all lists
		for (int i =0;i<participants.size();i++) 
		{
			participants.get(i).setExp(startingExperience.get(i));
		}
		System.out.println("HARD RESET");
		racing = false;
		preparing = false;
		if(preparationTask.isCancelled() == false) 
		{
			System.out.println("reset cancel that");
			preparationTask.cancel();			
		}
		if(raceCheckLoop.isCancelled() == false) 
		{
			System.out.println("reset cancel that");
			raceCheckLoop.cancel();			
		}
		
		
		
		participants = new ArrayList<Player>();
		startingLocations= new ArrayList<Location>();
		startingExperience= new ArrayList<Float>();
		startingBedLocations= new ArrayList<Location>();
		scores= new ArrayList<Integer>();
		blocksFound= new ArrayList<Integer>();
		possibleScore= new ArrayList<Integer>();
		checkPointMaterial= new ArrayList<Material>();
		ORcheckPointMaterial= new ArrayList<Material>();
		readyToRace= new ArrayList<Boolean>();
		System.out.println("race reset setting up materials again");
		SetupBlockRaceMaterials();
	}
	
	
	
	private void removeFromRace(int index) 
	{
		System.out.println("removing player");
		LoggingRaces(participants.get(index).getName() + " lost, couldn't find "+checkPointMaterial.get(index).name() + " or "+ORcheckPointMaterial.get(index).name());
		participants.get(index).teleport(startingLocations.get(index));
		
		//reseting xp is stupid
		//participants.get(index).giveExp( (startingExperience.get(index)));i wish java was different. but it is what it is
		float totalExp = startingExperience.get(index) + participants.get(index).getExp();
		participants.get(index).setExp(totalExp);
		participants.get(index).setBedSpawnLocation(startingBedLocations.remove(index));
		
		participants.remove(index);
		startingLocations.remove(index);
		startingExperience.remove(index);
		startingBedLocations.remove(index);
		scores.remove(index);
		blocksFound.remove(index);
		possibleScore.remove(index);
		checkPointMaterial.remove(index);
		readyToRace.remove(index);
	}
	

	
	/**
     *in case a player is signed up twice into the race
     */
	private void silentRemovalFromRace(int index) 
	{
		participants.remove(index);
		startingLocations.remove(index);
		startingExperience.remove(index);
		startingBedLocations.remove(index);
		scores.remove(index);
		blocksFound.remove(index);
		possibleScore.remove(index);
		checkPointMaterial.remove(index);
		readyToRace.remove(index);
	}

	 /**
     * Checks if a location is safe (solid ground with 2 breathable blocks)
     * thx https://www.spigotmc.org/members/billygalbreath.29442/ billy
     * @param location Location to check
     * @return True if location is safe
     */
    public static boolean isSafeLocation(Location location) 
    {
    	//player.getWorld().getHighestBlockAt(x, z);
    	//System.out.println("safe location");// this shit spammed my console with 1000 lines in a single race 
        Block feet = location.getBlock();
        if (feet.getType() != Material.AIR && feet.getLocation().add(0, 1, 0).getBlock().getType() != Material.AIR) {
            return false; // not transparent (will suffocate)
        }
        Block head = feet.getRelative(BlockFace.UP);
        if (head.getType()!= Material.AIR) {
            return false; // not transparent (will suffocate)
        }
        Block ground = feet.getRelative(BlockFace.DOWN);
        if (!ground.getType().isSolid()) {
            return false; // not solid
        }
        return true;
    }


    //easier TP pls and figure out time shenanigans with two players. why the one who starts it has more time?

}

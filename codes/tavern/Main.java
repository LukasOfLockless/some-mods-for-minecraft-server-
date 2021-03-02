package lockless.tavern;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin
{
	
	private ArrayList<tavern> taverns;
	
	private class tavern
	{
		public String name;
		public Location location;
		public tavern(String _n ,Location _l) 
		{
			name = _n;
			location = _l;
		}
	}
	
	@Override
	public void onEnable() {
		taverns = new ArrayList<tavern>();
		
		System.out.println("[tavern] enabled");
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(label.equalsIgnoreCase("tavernbuild")) 
		{
			if(sender instanceof Player) 
			{
				Player p = (Player) sender;
				
				commandBuild(p,args[0]);
				
				return true;
				
			}
			else 
			{
				
				if (taverns.size() == 0) 
				{
					sender.sendMessage("no taverns");
					return true;
				}
				
				
				sender.sendMessage("[Tavern] list");
				for (tavern t : taverns) 
				{
					sender.sendMessage(t.name + "\t"+ t.location.getX()+ ","+t.location.getZ());
				}
				sender.sendMessage("[Tavern] list");
				
			}
			return true;
		}
		if(label.equalsIgnoreCase("tavernbuild")) 
		{
			if(sender instanceof Player) 
			{
				Player p = (Player) sender;
				
				commandBuild(p,args[0]);
				
				return true;
				
			}
			else 
			{
				
				if (taverns.size() == 0) 
				{
					sender.sendMessage("no taverns");
					return true;
				}
				
				
				sender.sendMessage("[Tavern] list");
				for (tavern t : taverns) 
				{
					sender.sendMessage(t.name + "\t"+ t.location.getX()+ ","+t.location.getZ());
				}
				sender.sendMessage("[Tavern] list");
				
			}
			return true;
		}
		if(label.equalsIgnoreCase("taverngps")) 
		{
			if(sender instanceof Player) 
			{
				Player p = (Player) sender;
				
				commandGps(p,args[0]);
				
				return true;
				
			}
			else 
			{
				
				
			}
			return true;
		}
		
		return true;
	}
	
	//logic
	private ArrayList<Material> lookAtBlocks(Location loc)
	{
		ArrayList<Material> foundMaterials = new ArrayList<Material>();
		int someRadius = 9;
		for(int x = -someRadius;x<someRadius;x++) 
		{
			for(int y = -someRadius;y<someRadius;y++) 
			{
				for(int z = -someRadius;z<someRadius;z++) 
				{
					double ox = loc.getBlockX()+x;
					double oy = loc.getBlockY()+y;
					double oz = loc.getBlockZ()+z;
					
					Location blockToArrayLocation =new Location(loc.getWorld(),ox,oy,oz);
					Material tmpM = blockToArrayLocation.getBlock().getType();
					if(foundMaterials.contains(tmpM)==false) 
					{
						foundMaterials.add(tmpM);						
					}
					//logic is loc + xyz is block
					
					//System.out.println(p.getName() + " has " + tmpM);
				}
			}
		}
		
		
		return foundMaterials;
	}
	
	private boolean distanceCheck (Location l) 
	{
		if (taverns.size()==0) 
		{
			return true;
		}
		double GoodDistanceForSpacing = 500;
		double x1 = l.getX();
		double z1 = l.getZ();
		
		
		//sqr stuff
		for (tavern t : taverns) 
		{
			double x2 = t.location.getX();
			double z2 = t.location.getZ();
			if ((x1-x2)*(x1-x2) + (z1-z2)*(z1-z2)<GoodDistanceForSpacing*GoodDistanceForSpacing) 
			{
				return false;
			}
		}
		return true;
	}
	
	private boolean buildCheck(Location loc) 
	{
		int countNotDirtNotAir = 0;
		int someRadius = 6;
		int someHeight = 10;
		for(int x = -someRadius;x<someRadius;x++) 
		{
			for(int y = -someHeight;y<someHeight;y++) 
			{
				for(int z = -someRadius;z<someRadius;z++) 
				{
					double ox = loc.getBlockX()+x;
					double oy = loc.getBlockY()+y;
					double oz = loc.getBlockZ()+z;
					
					Location blockToArrayLocation =new Location(loc.getWorld(),ox,oy,oz);
					Material tmpM = blockToArrayLocation.getBlock().getType();
					
					if ((tmpM == Material.AIR || tmpM == Material.DIRT ||  tmpM == Material.STONE )==false) 
					{
						countNotDirtNotAir++;	
					}
					
				}
			}
		}
		System.out.println("[tavern] checking tavern stuff:\nnotshit " + countNotDirtNotAir+"\ntotal"+(someRadius*someRadius*someHeight));
		if (countNotDirtNotAir>128) 
		{
			return true;
		}
		return false;
	}
	
	private boolean chimneyCheck(Block b) 
	{
		Location loc = b.getLocation();
		
		int top = 200;
		if (loc.getBlockY()>=top) 
		{
			top = loc.getWorld().getMaxHeight();
		}
		for(int i = (int)b.getLocation().getY()+1;i<top;i++) 
		{
			loc.setY(i);
			Block aircheck =  b.getWorld().getBlockAt(loc);
			boolean ok=(aircheck.getType() == Material.CAMPFIRE)||aircheck.isEmpty() ||(aircheck.getType().isOccluding()==false);
			
			if (ok == false) 
			{
				System.out.println("chimney blocked at "+i+" "+aircheck.getType());
				return false;
			}
			System.out.println(aircheck);
		}
		return true;
	}
	
	private Block findFirePlace(Location loc) 
	{
		//search like beds > 9x5x9
		
		ArrayList<Block> campfires = new ArrayList<Block>();
		int someRadius = 4;
		int someHeight = 2;
		for(int x = -someRadius;x<someRadius;x++) 
		{
			for(int y = -someHeight;y<someHeight;y++) 
			{
				for(int z = -someRadius;z<someRadius;z++) 
				{
					double ox = loc.getBlockX()+x;
					double oy = loc.getBlockY()+y;
					double oz = loc.getBlockZ()+z;
					
					Location blockToArrayLocation =new Location(loc.getWorld(),ox,oy,oz);
					Block tmpM = blockToArrayLocation.getBlock();
					
					if (tmpM.getType() == Material.CAMPFIRE) 
					{
						campfires.add(tmpM);
					}
					
				}
			}
		}
		
		if(campfires.size()==0) 
		{
			return null;
			
		}
		return campfires.get((int) (campfires.size()*Math.random()));
	}
	
	//name from command, loca en el blocka cocka
	private void addTavern(String name, Block loca ) 
	{
		taverns.add(new tavern(name , loca.getLocation()));
	}
	
	
	
	
	
	private void commandBuild(Player p, String arg) 
	{
		Location l = p.getLocation();
		p.sendMessage(ChatColor.GOLD + "checking Tavern build prerequisites . . .");
		
		//distance
		if(distanceCheck(l)==false) 
		{
			p.sendMessage(ChatColor.LIGHT_PURPLE+" Too close to other taverns");
			return ;
		}
		//block
		//find fireplace
		Block firePlace=findFirePlace(l);
		if(firePlace == null) 
		{
			p.sendMessage(ChatColor.LIGHT_PURPLE+" no fireplace");
			return ;
		}
		
		if(chimneyCheck(firePlace)) 
		{
			p.sendMessage(ChatColor.LIGHT_PURPLE+" chimney blocked");
			return ;
		}
		if(buildCheck(l)==false) 
		{
			p.sendMessage(ChatColor.LIGHT_PURPLE+" did you really build it? use something that isn't dirt");
			return;
		}
		
		ArrayList<Material> variety = lookAtBlocks(l);
		if(variety.size()<10) 
		{
			p.sendMessage(ChatColor.LIGHT_PURPLE+"This establishment looks too shabby to pass as a tavern");
			return;
		}
		
		
		
		if(arg =="") 
		{
			p.sendMessage(ChatColor.LIGHT_PURPLE+" could be a tavern here");
			p.sendMessage(ChatColor.LIGHT_PURPLE+" forgot to name it? /tavernbuild <nameofthetavern>");
			
		}
		else
		{
			addTavern(arg , firePlace);
			p.sendMessage("added a tavern  called "+arg+" to the list");
		}
	}
	//TODO tavern gps
	private void commandGps(Player p, String nameOfTavern) 
	{
		Location tavernsLocation = null;
		tavern bestGuess=null;
		double bestGuessCentage=0;
		for(tavern t: taverns) 
		{
			String comparison = t.name;
			
		}
	}
	
	// TODO same as for creation
	//TODO autocomplete
	//hold a fucking block and then read shit from there. will neeed to find out a lot  of shit
	private void commandTeleport(Player p, String nameOfTavern)
	{
		// distance block chimmm build varrriety and then maybe a cooldown
		
	}
	private void commandList(Player p) 
	{
		String names = " ";
		for(tavern t : taverns) 
		{
			names += t.name + " ";
		}
		p.sendMessage(getName());
	}
	
	
	
	
	
	//foilder sheit
	private int rng =69;
	private void stupidWrite(String shoveThis) 
	{
		File folder = new File("plugins"+File.separator + "Lockless"+File.separator + "taverns");
		if(folder.exists() == false) 
		{
			try{
				folder.mkdirs();
		    }
		    catch(Exception e){
		    	System.out.println("try make dir fail "+e.getMessage());
		    	e.printStackTrace();
		    }
		}
		//with that soprted can try smt else now
		if (rng == 69) 
		{
			rng = (int)(Math.random() * 10000); 
			
		}
		PrintWriter writer=null;
		try 
		{
			writer = new PrintWriter(folder + File.separator+"tvrn"+rng+".txt" , "UTF-8");
			writer.println("\n"+shoveThis);
		}
		catch(IOException e) 
		{
			System.out.println("ooop"+e.getMessage());
		}
		finally 
		{
			writer.close();
		}
		System.out.println("prob logged smt");
	}
	
	private void save() 
	{
		for( tavern t : taverns )
		{
			String shovethere = t.name +" "+t.location.getX()+" "+t.location.getY()+" "+t.location.getZ();
			stupidWrite(shovethere);
		}
		
	}
	
}
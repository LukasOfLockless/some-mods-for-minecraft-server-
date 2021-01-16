package lockless.eat;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin  {

	private File pluginFolderPath;
    private String foldername = "eat";
    private int requireStarvation=6; 
    private int setNotStarve=10;
    private int requirehaveBlockBreak =1000;
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
		System.out.println("eatdirt enabled");
		FolderCreation();
	}
	
	public void onDisable() 
	{
		System.out.println("eatdirt disabled");
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(label.equalsIgnoreCase("eatdirt")) 
		{
			if(!(sender instanceof Player)) 
			{
				sender.sendMessage("need to be areal player to use eat");
				return true;
			}
			if (sender.hasPermission("eat.play")) 
			{
				sender.sendMessage(ChatColor.RED+"you just cant eat dirt");
				return true;
			}
			
			Player playerOne = (Player) sender;
			if(playerOne.getStatistic(Statistic.MINE_BLOCK)<requirehaveBlockBreak) 
			{
				sender.sendMessage(ChatColor.RED+"you dont look like the type who knows how to eat it");
			}
		
			
			Block block  = playerOne.getTargetBlock(null, 0);
			System.out.println(playerOne.getName() + " is looking at " + block.getType()+" and data "+ block.getBlockData());
			if(playerOne.getFoodLevel()>requireStarvation) 
			{
				playerOne.sendMessage(ChatColor.DARK_GRAY+"you just aren't there yet. you gotta be hungry");
				return true;
			}
			else 
			{
				playerOne.sendMessage(ChatColor.LIGHT_PURPLE+playerOne.getName() + " is looking at " + block.getType());
				var theTypeOfShitIlookat = block.getType();
				
				if (theTypeOfShitIlookat== Material.GRASS_BLOCK || theTypeOfShitIlookat ==Material.DIRT) 
				{
					block.breakNaturally();
					return eatDirt(playerOne);
					
				}
				else 
				{
					playerOne.sendMessage(ChatColor.LIGHT_PURPLE+"you would like to eat some dirt, but you just aren't looking at dirt. you are looking at "+theTypeOfShitIlookat);

					
				}
			}
			//System.out.println( "eating dirt reached end code. lagg?" );
			return true;
		}
		
		
		
		if(label.equalsIgnoreCase("eatsand")) 
		{
			if(!(sender instanceof Player)) 
			{
				sender.sendMessage("need to be a real player to use eat");
				return true;
			}
			if (sender.hasPermission("eat.play")) 
			{
				sender.sendMessage(ChatColor.RED+"you just cant eat sand");
				return true;
			}
			Player playerOne = (Player) sender;
			if(playerOne.getStatistic(Statistic.MINE_BLOCK)<requirehaveBlockBreak) 
			{
				sender.sendMessage(ChatColor.RED+"you dont look like the type who knows how to eat it");
			}
			
			//todo check if player is looking at dirt or grass
			Block block  = playerOne.getTargetBlock(null, 0);
			System.out.println(playerOne.getName() + " is looking at " + block.getType()+" and data "+ block.getBlockData());
			if(playerOne.getFoodLevel()>requireStarvation) 
			{
				playerOne.sendMessage(ChatColor.DARK_GRAY+"you just aren't there yet. you gotta be hungry");
				return true;
			}
			else 
			{
				playerOne.sendMessage(ChatColor.LIGHT_PURPLE+playerOne.getName() + " is looking at " + block.getType());
				var theTypeOfShitIlookat = block.getType();
				
				if (theTypeOfShitIlookat== Material.SAND ) 
				{
					block.breakNaturally();
					return eatSand(playerOne);
				}
				else 
				{
					playerOne.sendMessage(ChatColor.LIGHT_PURPLE+"you would like to eat some sand, but you just aren't looking at sand. you are looking at "+theTypeOfShitIlookat);
					
				}
			}
			//System.out.println( "eating sand reached end code. lagg?" );
			
			return eatSand(playerOne);
		}
		
		return false;
	}
	
	private boolean eatSand(Player p) 
	{
		
		p.sendMessage(ChatColor.BOLD+"you ate sand");
		p.setFoodLevel(setNotStarve);
		//get set totalexperience didnt set shit, but it looks like the score or smt. hmmm....
		float expLevelsLost = p.getExp();				
		p.setExp(0);
		//todo cooldown 15 minutes or smt.
		//no way that this will need a cd mechanic
		if(expLevelsLost>0) 
		{
			p.chat("slamst");
			p.sendMessage(ChatColor.BOLD+"but it cost you your dignity and "+expLevelsLost+" levels");				
			System.out.println(p.getName() + " ate sand "+ expLevelsLost);
		}
		try{
		    PrintWriter writer = new PrintWriter(pluginFolderPath+File.separator+p.getName()+"EATS.txt", "UTF-8");
		    writer.append(" sand");
		    writer.close();
		} catch (IOException e) {
		   System.out.println("well that print writer didnt work "+e.toString());
		}
		
		return true;
	}
	private boolean eatDirt(Player p) 
	{
		p.sendMessage(ChatColor.BOLD+"you ate dirt");
		p.setFoodLevel(setNotStarve);
		//get set totalexperience didnt set shit, but it looks like the score or smt. hmmm....
		float expLevelsLost = p.getExp();				
		p.setExp(0);
		if(expLevelsLost>0) 
		{
			p.sendMessage(ChatColor.BOLD +""+ ChatColor.GRAY + "shlamsht");//-.- what is this. didnt see anything in console or in game whilst doing this .chat shit
			p.sendMessage(ChatColor.BOLD+"but it cost you your dignity and "+expLevelsLost+" levels");				
			System.out.println(p.getName() + " ate dirt "+ expLevelsLost);
		}
		//take away power and title. 
		try{
		    PrintWriter writer = new PrintWriter(pluginFolderPath+File.separator+p.getName()+"EATS.txt", "UTF-8");
		    writer.append(" dirt");
		    writer.close();
		} catch (IOException e) {
		   System.out.println("well that print writer didnt work "+e.toString());
		}
		
		return true;
	}
	
	
	
}

package lockless.killquest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.entity.EntitySpawnEvent;

public class Main extends JavaPlugin implements Listener{

	private File pluginFolderPath;
	private String foldername="killquest";
	private File savedData;
	private double checkTotalQuestRadius=15000;
	private double onePartAffectArea=1000;
	
	
	private int currentlyClear=0;
	private int currentlyRunning=0;
	private Map<Integer,Boolean> areaClear;
	private Map<Integer, ArrayList<Integer> > killquests;//can i use it like this instead of that i mean im not retarded but ">>"!??!? 
	private Map<Integer, Player> killquesters;
	
	/*
	private List<Integer> areaSpooders;
	private List<Integer> areaZomboz;
	private List<Integer> areaDingos;
	private List<Integer> areaNeets;
	*/
	
	
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
		
		savedData = new File(authorDir+File.separator+foldername+File.separator+"questLog.txt","UTF-8");
		
		//done
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
	

	
	public void onEnable() {
		System.out.println("killquest enabled");
		FolderCreation();
		doSomeLoad();
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	public void onDisable() 
	{
		System.out.println("killquest saving data and disabling");
		doSomeSave();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) 
	{
		if(label.equalsIgnoreCase("killquest")) 
		{
			if(!(sender instanceof Player)) 
			{
				//so what if the console uses the command?
				//could be used to track active killquests
				//start track save i guess;
				return true;
			}
			Player playerOne = (Player) sender;
			if(playerIsWithinTrackingRange(playerOne)) 
			{
				playerOne.sendMessage("you are in killquest location {"+getLocationsKillQuestIndex(playerOne.getLocation().getX(), playerOne.getLocation().getZ())+"}");
				currentlyClear = QuickMath();
				playerOne.sendMessage("areas safe " +currentlyClear);					
			}
			else 
			{
				playerOne.sendMessage("you are out of range for killquest to track kills");
			}
			
			
			
			return true;
		}
		
		return false;
	}
	
	
	
	//logic
	private boolean playerIsWithinTrackingRange(Player p) 
	{
		double x = p.getLocation().getX();
		if(x > checkTotalQuestRadius) 
		{
			return false;
		}
		if(x < -checkTotalQuestRadius) 
		{
			return false;
		}
		double z = p.getLocation().getZ();
		if(z > checkTotalQuestRadius) 
		{
			return false;
		}
		if(z < -checkTotalQuestRadius) 
		{
			return false;
		}
		return true;
	}
	private boolean entityIsWithinTrackingRange(Location loc) 
	{
		double x =  loc.getX();
		if(x > checkTotalQuestRadius) 
		{
			return false;
		}
		if(x < -checkTotalQuestRadius) 
		{
			return false;
		}
		double z = loc.getZ();
		if(z > checkTotalQuestRadius) 
		{
			return false;
		}
		if(z < -checkTotalQuestRadius) 
		{
			return false;
		}
		return true;
	}
	
	
	private int getLocationsKillQuestIndex(double x , double z) 
	{
		System.out.println("killquest loc? x int is "+((int)((x+checkTotalQuestRadius)/onePartAffectArea))+" total "+(int)(2*checkTotalQuestRadius/onePartAffectArea*2*checkTotalQuestRadius/onePartAffectArea)+"y"+(int)((z+checkTotalQuestRadius)/onePartAffectArea));
		int theIndexIs = (int)((x+checkTotalQuestRadius)/onePartAffectArea)+(int)(2*checkTotalQuestRadius/onePartAffectArea)*(int)((z+checkTotalQuestRadius)/onePartAffectArea);
		return theIndexIs;
	}
	private boolean checkQuestComplete(Integer index) 
	{
		List<Integer> checkthis =(List) killquests.get(index);
		Integer sum =0;
		for(int i =0;i<checkthis.size() ; i++) 
		{
			sum+=checkthis.get(i);
		}
		
		if (sum < 256) 
		{
			return false;
		}
		if(sum >=1024) 
		{
			return true;
		}
		for(int i =0;i<checkthis.size() ; i++) 
		{
			if(checkthis.get(i)<64) 
			{
				return false;
			}
		}
		return true;
	}
	private int QuickMath() 
	{
		int countSome=0;
		for(int i = 0 ; i<areaClear.size();i++)
		{
			if(areaClear.get(i)) 
			{
				countSome++;
			}
		}
		
		return countSome;
	}
	//event listeners
	
	//typefilter
	@EventHandler
	private void onmobSpawn(EntitySpawnEvent event) 
	{
		//filter what to cancel
		if(entityIsWithinTrackingRange(event.getLocation())) 
		{
			String outputintotrashcan = ""+event.getEntityType();
			outputintotrashcan +=" is on index "+getLocationsKillQuestIndex(event.getLocation().getX() , event.getLocation().getZ());
			System.out.println("killquest "+outputintotrashcan);
		}
		
		
		
	}
	
	
	
	
	
	
	//makes file management
	private void initSaving() 
	{
		//makes a file filled with 0
		String printthis="";
		int howWide = (int)(2*checkTotalQuestRadius/onePartAffectArea);
		int howLong = (int)(2*checkTotalQuestRadius/onePartAffectArea);
		for(int iWide = 0 ;iWide<howWide;iWide++ ) 
		{
			for(int iLong = 0 ;iLong<howLong;iLong++ ) 
			{
				printthis+="0";
			}
			printthis+="\n";
		}
		try{
			FileWriter writer = new FileWriter(savedData);
		    writer.write(printthis);
		    writer.close();
		} catch (IOException e) {
		   System.out.println("well that print writer didnt work "+e.toString());
		}
		
	}
	
	private void doSomeLoad() 
	{
		

		System.out.println("killquest loading");
		if(savedData.exists() == false) 
		{
			System.out.println("initialLoad");
			initSaving();
		}
		
		try{
			FileReader reader = new FileReader(savedData);
			BufferedReader buffer=new BufferedReader(reader); 
			String line="";
			Integer MapIndex = 0;
			ArrayList<Integer> emptylist = new ArrayList<Integer>();
			emptylist.add(0);//creepers
			emptylist.add(0);//skeletons
			emptylist.add(0);//spiders
			emptylist.add(0);//zombies
			
			while((line=buffer.readLine()) != null) 
			{
				for(int i = 0; i <line.length();i++) 
				{
					if(line.charAt(i)=='0') 
					{
						areaClear.put(MapIndex, false);
						MapIndex++;
					}
					else if(line.charAt(i)=='1') 
					{
						areaClear.put(MapIndex, true);
						MapIndex++;
					}
				}
			}
			
			
			
			
			
			
		    reader.close();
		}
		catch (IOException e) 
		{
		   System.out.println("well that print writer didnt work "+e.toString());
		}
		
	}
	
	
	
	private void doSomeSave() 
	{
		
		System.out.println("killquest saving");
		String printthis="";
		for(int i =0;i<areaClear.size();i++) 
		{
			if(areaClear.get(i)) 
			{
				printthis+="1";	
			}
			else 
			{
				printthis+="0";
			}
		}
		try{
			FileWriter writer = new FileWriter(savedData);
		    writer.write(printthis);
		    writer.close();
		} catch (IOException e) {
		   System.out.println("well that print writer didnt work "+e.toString());
		}
	}
	
}

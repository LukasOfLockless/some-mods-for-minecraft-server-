package lockless.compassAndNames;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener{

	@Override
	public void onEnable() {
		System.out.println("[crafting north compasses]");
		getServer().getPluginManager().registerEvents(this, this);
		nicks = new ArrayList<nickname>();
		FolderCreation();
		LoadNicknames();
	}
	@Override
	public void onDisable() {
		System.out.println("saving nicknames");
		SaveNicknames();
	}
	
	
	@EventHandler
	private void makeJoinName(PlayerJoinEvent event)
	{
		Player p = event.getPlayer();
		String nickname = searchNicks(p.getName());
		if(nickname !=null) 
		{
			System.out.println("successful nickname set");
			p.setDisplayName(nickname);
			p.setPlayerListName(nickname);			
		}

		System.out.println("check compass in nether/end pls");
//		Location north =new Location(getServer().getWorlds().get(0), 0, 0, -100000);//hope this is north
//		p.setCompassTarget(north);
		p.setCompassTarget(p.getWorld().getBlockAt((int)p.getLocation().getX(), 0, -12550820).getLocation());
		p.updateInventory();
		//
		//Location doubleCheck = p.getCompassTarget();
		//System.out.println("doublecheckin, the compass should point towards 0 -1000000, but its actually " +doubleCheck.getX()+" "+doubleCheck.getZ()); //prints ok
		System.out.println("jsut copied code inv");
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if(label.equalsIgnoreCase("nicknameadd")) 
		{
			
			
			if(sender instanceof Player) 
			{
				sender.sendMessage("what???");
			}else
			{
				if (args==null) 
				{
					sender.sendMessage("args null");
				}
				else 
				{
					if(args.length!=2) 
					{
						sender.sendMessage("args missing");
					}
					else 
					{
						appendNick(args[0],args[1]); 
					}
				}
			
			}
			return true;
		}
		if(label.equalsIgnoreCase("nicknamereload")) 
		{
			
			
			if(sender instanceof Player) 
			{
				sender.sendMessage("what???");
			}else
			{
				//TODO roload logic
				sender.sendMessage("not implemented");
			
			}
			return true;
		}
		
		
		return false;
	}
	
	private class nickname 
	{
		private String playerName;
		private String nickname;
		private nickname(String plname, String nick) 
		{
			playerName = plname;
			nickname = nick;
		}	
		
	}
	private ArrayList<nickname> nicks;
	private String searchNicks(String player) 
	{
		for(int i =0; i<nicks.size();i++) 
		{
			if (nicks.get(i).playerName.equals(player)) 
			{
				return nicks.get(i).nickname;
			}
		}
		return null;
	}
	
	//make folder
	//make file.
	//read names assoc
	private File pluginFolderPath;
    private String foldername = "nicknames";
    private String saveFileNickStorage;
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
		saveFileNickStorage = pluginFolderPath+File.separator+"storage.txt";
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
	
    private void LoadNicknames() 
    {
    	FileReader reader=null;
    	BufferedReader buffer=null;
    	try 
    	{
    		reader = new FileReader(saveFileNickStorage);

			buffer=new BufferedReader(reader); 
    		String line = "";
    		
    		
    		while( (line=buffer.readLine()) != null) 
			{
    			String name="",nick="";
    			boolean IsNicknameTime=false;
				for(int i = 0; i <line.length();i++) 
				{
					if(line.charAt(i) == ' ' && IsNicknameTime == false) 
					{
						IsNicknameTime=true;
					}
					else 
					{
						if(IsNicknameTime) 
						{
							nick+=line.charAt(i);
						}
						else 
						{
							name+=line.charAt(i);
						}
					}
				}
				//hope nicks is initialized atm
				nicks.add(new nickname(name, nick));
			}
    		
    	}
    	catch(IOException e) 
    	{
    		System.out.println("io "+e.getMessage());
    	}
    	finally 
    	{
    		try 
		    {
		    	reader.close();
		    	buffer.close();
		    }
		    catch (Exception e)
		    {
		    	System.out.println("error load "+e.toString());
		    }
    	}
    }
    
    private void appendNick(String pName,String Nick) 
    {
    	try{
		    PrintWriter writer = new PrintWriter(saveFileNickStorage, "UTF-8");
		    writer.append("\n"+pName+" "+Nick);
		    writer.close();
		} catch (IOException e) {
		   System.out.println("well that print writer didnt work "+e.toString());
		}
    	//then look for a player with the nickname like that online
    	boolean success=false;
    	for(Player p:getServer().getOnlinePlayers()) 
    	{
    		if (p.getName().equalsIgnoreCase(pName)) 
    		{
    			success=true;
    			p.setDisplayName(Nick);
    			p.setPlayerListName(Nick);
    		}
    		
    	}
    	
    	if(success) 
    	{
    		System.out.println("successfully set players nickname");
    		
    	}else 
    	{
    		System.out.println("failed to set nickname for player, but it might'vebeen wrtten to file.");
        	
    	}
    	nicks.add(new nickname(pName, Nick));
    	
    }
    
    
    private void SaveNicknames() 
    {
    	try{
		    PrintWriter writer = new PrintWriter(saveFileNickStorage, "UTF-8");
		    writer.flush();
		    for(nickname n :nicks) 
		    {
		    	writer.println(n.playerName+" "+n.nickname);
		    	
		    }
		    writer.close();
		} catch (IOException e) {
		   System.out.println("well that print writer didnt work "+e.toString());
		}
    }
}

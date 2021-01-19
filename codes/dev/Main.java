package lockless.dev;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{

	@Override
	public void onEnable() 
	{
		System.out.println("report and news on");
		// TODO Auto-generated method stub
		FolderCreation();
		super.onEnable();
	}
	@Override
	public void onDisable() {
		System.out.println("report andnews off");
		// TODO Auto-generated method stub
		super.onDisable();
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// TODO Auto-generated method stub
		if(label.equalsIgnoreCase("report")) 
		{
			if(sender instanceof Player) 
			{
				Player p = (Player) sender;
				p.sendMessage(ChatColor.GOLD+""+ChatColor.UNDERLINE+"thank you for that");
				writeNewFile(p.getName()+" "+p.getLocation().getX()+" "+p.getLocation().getY()+" "+p.getLocation().getZ()+"\n"+args.toString()+command.toString() +label.toString());
				return true;
			}
			return true;
		}
		if(label.equalsIgnoreCase("news")) 
		{
			if(sender instanceof Player) 
			{
				Player p = (Player) sender;
				p.sendMessage(readNews());
				
				return true;
			}
			return true;
		}
		return super.onCommand(sender, command, label, args);
	}
	private File pluginFolderPath;
	private String foldername="dev";
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
		
    }
	double rng=0;
	private void writeNewFile(String report) 
	{
		rng = Math.random()*6969;
		try{
			FileWriter writer = new FileWriter(pluginFolderPath+File.separator+"report"+rng+".txt");
		    writer.write("report");
		    writer.close();
		} catch (IOException e) {
		   System.out.println("well that print writer didnt work "+e.toString());
		   System.out.println(report);
		}	
	}
	private String readNews() 
	{
		String news="";
        try {

            // First of all, you need to define file you want to read.
            File fileToRead = new File("/Users/aleksiaaltonen/Desktop/testfile.txt");

            // Setup BufferedReader
            BufferedReader br = new BufferedReader(new FileReader(fileToRead));

            // Read line by line
            String line = null;

            while ((line = br.readLine()) != null) {
                news +=line;//System.out.println(line);
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
        	e.printStackTrace();
        }
        return news;
    }
}

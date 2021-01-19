package lockless.pack;


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



public class Main extends JavaPlugin implements Listener{

	//still debugging inventory == null
	private boolean logToConsole=false;
	private File pluginFolderPath;
    private String foldername = "testssss";
 
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
	private void readSomething()
	{
		String readread="";
        try {

            // First of all, you need to define file you want to read.
            File fileToRead = new File("/Users/aleksiaaltonen/Desktop/testfile.txt");

            // Setup BufferedReader
            BufferedReader br = new BufferedReader(new FileReader(fileToRead));

            // Read line by line
            String line = null;

            while ((line = br.readLine()) != null) {
                readread +=line;//System.out.println(line);
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
        	e.printStackTrace();
        }
        return readread;
		
	}
	private void writeSomething()
	{
		rng = Math.random()*6969;
		try{
			FileWriter writer = new FileWriter(pluginFolderPath+File.separator+"report"+rng+".txt");
		    writer.write("something"+rng);
		    writer.close();
		} catch (IOException e) {
		   System.out.println("well that print writer didnt work "+e.toString());
		   System.out.println(rng);
		}	
	}
	
	
	
	

	@Override
	public void onEnable()
	{
		//instance = this;
		//checkIfMy folder exists.? make one?
		FolderCreation();
		
	}
	@Override
	public void onDisable()
	{
		system.out.println("bye bye");
		
	}


	
	
	
}

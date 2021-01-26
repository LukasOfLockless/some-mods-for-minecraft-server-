package lockless.celebrate;

import java.io.File;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.io.IOException;  // Import the IOException class to handle errors

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;


public class Main extends JavaPlugin{

	private String reasonToCelebrate="";
	private int countTimesFireworked=0;
	private boolean alreadyCelebrating = false;
	private BukkitRunnable fireworkTask;
	
	private File pluginFolderPath;
    private String foldername = "celebrating";
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
	
	public void onEnable()
	{
		System.out.println("celebrate enabled");
		FolderCreation();
		FolderCreation();
	}
	
	public void onDisable() 
	{
		System.out.println("celebrate disabled");
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		
		if (label.equalsIgnoreCase("celebratestart")) 
		{
			if(sender.hasPermission("celebrate")==false) 
			{
				sender.sendMessage("you dont have a permission to control celebrations");
				return true;
			}
			if(alreadyCelebrating==false) 
			{
				System.out.println("already celebrating");
			}
			if(args != null) 
			{				
				if(args[0] !=null) 
				{
					reasonToCelebrate = args[0];
				}else 
				{
					reasonToCelebrate="";
				}
			}
			
			try{
			    PrintWriter writer = new PrintWriter(pluginFolderPath+File.separator+"Celebrations.txt", "UTF-8");
			    writer.append("celebrating "+reasonToCelebrate);
			    writer.close();
			} catch (IOException e) {
			   System.out.println("well that print writer didnt work "+e.toString());
			}
			
			alreadyCelebrating = true;
			countTimesFireworked=0;
		
			fireworkTask = new BukkitRunnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					countTimesFireworked++;
			    	 for (Player somePlayer : Bukkit.getServer().getOnlinePlayers()) 
			    	 {
			    		 //spawnfirework
			    		 //Spawn the Firework, get the FireworkMeta.
			    		 
			    		 // but maybe spawnmultiple
			    		 int rng = (int)Math.random()*3;
			    		 
			    		 for(int i = 0 ;i<rng;i++ ) 
			    		 {
				    		 Location fireworkLocation = somePlayer.getLocation();
				    		 double x = fireworkLocation.getX() + Math.random()*32-16;
				    		 double y = fireworkLocation.getY() + 32;
				    		 double z = fireworkLocation.getZ() + Math.random()*32-16;
				    		 fireworkLocation.setX(x);
				    		 fireworkLocation.setY(y);
				    		 fireworkLocation.setZ(z);
				    		 
				    		 //Firework fw = (Firework) somePlayer.getWorld().spawnEntity(somePlayer.getLocation(), EntityType.FIREWORK);
				             
				    		Firework fw = (Firework) somePlayer.getWorld().spawnEntity(fireworkLocation, EntityType.FIREWORK);
				             FireworkMeta fwm = fw.getFireworkMeta();
				            
				             //Our random generator
				             Random r = new Random();   
				  
				             //Get the type
				             int rt = r.nextInt(4) + 1;
				             Type type = Type.BALL;       
				             if (rt == 1) type = Type.BALL;
				             if (rt == 2) type = Type.BALL_LARGE;
				             if (rt == 3) type = Type.BURST;
				             if (rt == 4) type = Type.STAR;
				             if (rt == 5) type = Type.CREEPER;
				             
				             //Get our random colours   
				             int r1i = r.nextInt(17) + 1;
				             int r2i = r.nextInt(17) + 1;
				             Color c1 = getColor(r1i);
				             Color c2 = getColor(r2i);
				            
				             //Create our effect with this
				             FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(c1).withFade(c2).with(type).trail(r.nextBoolean()).build();
				            
				             //Then apply the effect to the meta
				             fwm.addEffect(effect);
				            
				             //Generate some random power and set it
				             int rp = r.nextInt(2) + 1;
				             fwm.setPower(rp);
				            
				             //Then apply this to our rocket
				             fw.setFireworkMeta(fwm);       
			    		 }
			    		 try{
			    			    PrintWriter writer = new PrintWriter(pluginFolderPath+File.separator+"printWriterLines.txt", "UTF-8");
			    			    writer.println("The first line");
			    			    writer.println("The second line");
			    			    writer.close();
			    			} catch (IOException e) {
			    			   System.out.println("well that print writer didnt work "+e.toString());
			    			}
			    	 }
			    	 if(Bukkit.getServer().getOnlinePlayers().size()==0 || countTimesFireworked>100) 
			    	 {
			    		 alreadyCelebrating=false;
			    		 this.cancel();
			    	 }
				}
			};
			
			fireworkTask.runTaskTimer(this, 69L, 1337L);
			Collection<? extends Player> allPlayers =  getServer().getOnlinePlayers();
			Iterator<? extends Player> i = allPlayers.iterator();
			while (i.hasNext()) 
			{
				Player thatOtherPlayer = i.next();
				
				thatOtherPlayer.sendMessage(ChatColor.GOLD + " HAPPY CELEBRATING " );
			}

			return true;
		}
		if (label.equalsIgnoreCase("celebratestop")) 
		{
		
			
			if(sender.hasPermission("celebrate")==false) 
			{
				sender.sendMessage("you dont have a permission to control celebrations");
				return true;
			}
			
			System.out.println("did bunch of fireworks " + countTimesFireworked);
			documentCelebration();
			fireworkTask.cancel();
			alreadyCelebrating=false;
			return true;
		}
		
		
		return false;
	}
	
	private Color getColor(int i) {
		Color c = null;
		if(i==1){
		c=Color.AQUA;
		}
		if(i==2){
		c=Color.BLACK;
		}
		if(i==3){
		c=Color.BLUE;
		}
		if(i==4){
		c=Color.FUCHSIA;
		}
		if(i==5){
		c=Color.GRAY;
		}
		if(i==6){
		c=Color.GREEN;
		}
		if(i==7){
		c=Color.LIME;
		}
		if(i==8){
		c=Color.MAROON;
		}
		if(i==9){
		c=Color.NAVY;
		}
		if(i==10){
		c=Color.OLIVE;
		}
		if(i==11){
		c=Color.ORANGE;
		}
		if(i==12){
		c=Color.PURPLE;
		}
		if(i==13){
		c=Color.RED;
		}
		if(i==14){
		c=Color.SILVER;
		}
		if(i==15){
		c=Color.TEAL;
		}
		if(i==16){
		c=Color.WHITE;
		}
		if(i==17){
		c=Color.YELLOW;
		}
		 
		return c;
		}

	private void documentCelebration() 
	{
		SimpleDateFormat formatter= new SimpleDateFormat("yyyy'y'MM'm'dd'd'HH'h'");
		Date date = new Date(System.currentTimeMillis());
		String fileName = pluginFolderPath+File.separator+"Celebrations.txt";
		
		try{
		    PrintWriter writer = new PrintWriter(fileName, "UTF-8");
		    writer.append(formatter.format(date)+" "+reasonToCelebrate+" fireworks launched "+countTimesFireworked+"\n");
		    writer.close();
		} catch (IOException e) {
		   System.out.println("well that celebrate writer didnt work "+e.toString());
		}
		
	}
}

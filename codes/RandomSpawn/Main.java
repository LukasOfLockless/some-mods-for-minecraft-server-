package lockless.randomSpawn;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.plugin.java.JavaPlugin;
//import org.bukkit.scheduler.BukkitRunnable;
  

public class Main extends JavaPlugin implements Listener
{
	public boolean DRUGS=true;
	private World mainWorld;
	private boolean gotMainWorld = false;
	private double respawnLengthFromO =10000;
	public static Main instance;
	public DoDrugs otherClass;
	@Override
	public void onEnable() 
	{
		instance = this;
		System.out.println(" enabling random spawn");
		getServer().getPluginManager().registerEvents((Listener)this, instance);
		if(DRUGS) 
		{			
			otherClass = new DoDrugs();
			getServer().getPluginManager().registerEvents((Listener)otherClass, instance);
		}
		
		mainWorld = getServer().getWorld("world");
		if(mainWorld != null) 
		{
			if(mainWorld.getName().contains("world") == false) 
			{
				gotMainWorld = false;
				System.out.println("aww shit, where the WORLD at?");
				int index = 0;
				for(World w:getServer().getWorlds()) 
				{
					System.out.println(index + " " +w.getName());
				}
			}
			else 
			{
				gotMainWorld = true;
			}
		}
		else 
		{
			System.out.println("you just dont want the rng spawn to work do you?");
			getServer().getPluginManager().disablePlugin(this);
		}
	}
	
	public static Main getMain() 
	{
		return instance;
	}
	
	@Override
	public void onDisable()
	{
		System.out.println(" disabling random spawn");
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerRespawn(PlayerRespawnEvent event)
	{
		//System.out.println("LOW priority setting respawn location");
		Player playerOne = (Player)event.getPlayer();
		if(playerOne.getBedSpawnLocation()!=null) 
		{
			playerOne.sendMessage("Hey you");
			playerOne.sendMessage("finally awake");
			//System.out.println("current respawn locataion"+LocGibXYZ(event.getRespawnLocation())+"but i wanna change it to bed"+LocGibXYZ(playerOne.getBedSpawnLocation()));
			event.setRespawnLocation(playerOne.getBedSpawnLocation());
		}
		else
		{
			System.out.println("rngRespawnWorks as intended");
			Location rngTp = tpRNG(playerOne);
			event.setRespawnLocation(rngTp);
		}
		return;
	}

	
	@EventHandler
	public void onDeath(PlayerDeathEvent event) 
	{
		Player playerOne = (Player)event.getEntity();
		playerOne.sendMessage(ChatColor.RED+""+ChatColor.BOLD +"YOU DIED");
		System.out.println("lol ded"+LocGibXYZ(event.getEntity().getLocation()));
	}
	/* stopping the tests of portals for the time being
	public void onPortalCreate(PortalCreateEvent event) 
	{
		System.out.println("Portal created "+event.getEntity().getName()+" "+LocGibXYZ(event.getEntity().getLocation()));
	}
	public void onUsePortal(PlayerPortalEvent event) 
	{
		System.out.println("Portal use "+event.getPlayer().getName()+" "+LocGibXYZ(event.getPlayer().getLocation()));
	}
	*/
	@EventHandler
	public void onJoin(PlayerJoinEvent event) 
	{
		System.out.println("player joined");
		event.getPlayer().spawnParticle(Particle.PORTAL, event.getPlayer().getEyeLocation().add(1, 0, 1), 5);
		event.getPlayer().spawnParticle(Particle.PORTAL, event.getPlayer().getEyeLocation().add(1, 0, -1), 5);
		event.getPlayer().spawnParticle(Particle.PORTAL, event.getPlayer().getEyeLocation().add(-1, 0, 1), 5);
		event.getPlayer().spawnParticle(Particle.PORTAL, event.getPlayer().getEyeLocation().add(-1, 0, -1), 5);
		//event.getPlayer().sendMessage("OP is training his social skills. Hello.");
	}
	
	private Location tpRNG(Player p) 
	{
		if(gotMainWorld) 
		{
			double x=Math.random()*2*respawnLengthFromO - respawnLengthFromO;
			double z=Math.random()*2*respawnLengthFromO - respawnLengthFromO;
			
			Block highestBlock = mainWorld.getHighestBlockAt((int)x, (int)z);// why does it work good here
			int height = highestBlock.getY();
			Location loc = new Location(mainWorld,x,height+1,z);
			//System.out.println(" found a really high block at "+(int)x+" "+(int)z+ "it is atY"+height+ "its type is "+highestBlock.getType()); 
			
			p.sendMessage(" rng respawn "+(int)x+" "+(int)z);
			return loc;
		}
		else 
		{
			p.sendMessage("you would respawn in a random location, but the /op is a faggot, they should commit /killop. lol");
			return new Location(p.getWorld(),0,69,0);
		}
	}
	private String LocGibXYZ(Location loc) 
	{
		return (" "+loc.getX()+" "+loc.getY()+" "+loc.getZ()+" ");
	}
}

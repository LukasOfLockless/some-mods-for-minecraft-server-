package lockless.InitialJoin;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener{

	private ArrayList<initialisationObj> initialRegs;
	private class initialisationObj
	{
		public String name;
		public int type; 
		//types 0 : 21k plus
		//1 : 10k ++
		//2 : 10k +-
		//3 : 10k --
		//4 : 10k -+
		//5 : 10 rng normals
		public initialisationObj(String thename, int thetype) 
		{
			name = thename;
			type = thetype;
		}
	}
	
	
	@Override
	public void onEnable() 
	{
		// TODO Auto-generated method stub
		initialRegs = new ArrayList<initialisationObj>();
		System.out.println("enabling init rng");
		getServer().getPluginManager().registerEvents(this, this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if((sender instanceof Player) == false) 
		{
			if(label.equalsIgnoreCase("init")) 
			{
				if(args ==null) 
				{
					doTheBigPrint();
					System.out.println("bad, gib args");
					return true;
				}
				else 
				{
					if (args.length!=2) 
					{
						System.out.println("bad, gib ok n args");
						return false;
					}
					else 
					{
						interpretuotiArgumentus(args[0],args[1]);
						return true;
					}
				}
			}
		}
		else 
		{
			sender.sendMessage("What?");
			return true;
		}
		
		return false;
	}
	
	private void doTheBigPrint() 
	{
		System.out.println("init reg dump");
		String[] typetoword= {"far","++","+-","--","-+","rng"};
		for(initialisationObj in:initialRegs) 
		{
			System.out.println(in.name +" would spawn in " + typetoword[in.type]);
		}
	}
	
	private void interpretuotiArgumentus(String agr1, String arg2) 
	{
		//System.out.println("arg1"+agr1+"_arg2"+arg2);
		int thatType=69;
		//types 0 : 21k
		//1 : 10k ++
		//2 : 10k +-
		//3 : 10k --
		//4 : 10k -+
		//5 : 10 rng normals
		
		if(arg2.equalsIgnoreCase( "++")) 
		{
			thatType = 1;
		}else if(arg2.equalsIgnoreCase("+-")) 
		{
			thatType = 2;
		}else if(arg2.equalsIgnoreCase( "--")) 
		{
			thatType = 3;
		}else if(arg2.equalsIgnoreCase("-+")) 
		{
			thatType = 4;
		}else if(arg2.equalsIgnoreCase("rng")) 
		{
			thatType = 5;
		}else if(arg2.equalsIgnoreCase("far")) 
		{
			thatType = 0;
		}
		
		if(thatType == 69) 
		{	
			System.out.println("not good arg2");
			System.out.println("<++ +- -+ -- rng far>");
			return;
		}
		
		initialRegs.add(new initialisationObj(agr1, thatType));
		
	}
	
	@EventHandler
	private void joinCheck1stTime(PlayerJoinEvent event)
	{
		if(initialRegs.size()==0) 
		{
			return;
		}
		Player p= event.getPlayer();
		String theName = p.getName();
		if(p.hasPlayedBefore()==false) 
		{
			System.out.println(theName + "has not played before");
			boolean success=false;
			for(initialisationObj init:initialRegs) 
			{
				if (init.name.equalsIgnoreCase(theName)) 
				{
					success=true;
					System.out.println("gonna try and throw that person to rng loc");
					spawnthatpersonthere(init,p);
					break;
				}
			}
			if(success==false) 
			{
				System.out.println("probably because this system is written by a monkey");
			}
		}
		//also 
		if(getServer().getOnlinePlayers().size()<9) 
		{
			p.sendMessage(ChatColor.LIGHT_PURPLE+ "ded server lol");
			p.sendMessage(ChatColor.LIGHT_PURPLE+ "anyway, hf");
			p.sendMessage(ChatColor.LIGHT_PURPLE+ "\t-admin");
		}
		
	}
	
	private void spawnthatpersonthere(initialisationObj remInit, Player playerTwo) 
	{
		Location tpLoca = playerTwo.getLocation();
		//0 : 21k plus
		//1 : 10k ++
		//2 : 10k +-
		//3 : 10k --
		//4 : 10k -+
		//5 : 10 rng normals
		if(remInit.type==0) 
		{
			int rng = (int)(4*Math.random());
			int rng2 = (int)(42000*Math.random()-21000);
			if(rng==0) 
			{
				tpLoca = playerTwo.getWorld().getHighestBlockAt(21000, rng2).getLocation();	
			}
			if(rng==1) 
			{
				tpLoca = playerTwo.getWorld().getHighestBlockAt(-21000, rng2).getLocation();	
			}
			if(rng==2) 
			{
				tpLoca = playerTwo.getWorld().getHighestBlockAt(rng2, 21000).getLocation();	
			}
			if(rng==3) 
			{
				tpLoca = playerTwo.getWorld().getHighestBlockAt(rng2, -21000).getLocation();	
			}
		}
		if(remInit.type==1) 
		{
			int rng1=(int)(10000*Math.random());
			int rng2=(int)(10000*Math.random());
			tpLoca = playerTwo.getWorld().getHighestBlockAt(rng1, rng2).getLocation();
		}
		if(remInit.type==2) 
		{

			int rng1=(int)(10000*Math.random());
			int rng2=(int)(-10000*Math.random());
			tpLoca = playerTwo.getWorld().getHighestBlockAt(rng1, rng2).getLocation();
			
		}
		if(remInit.type==3)
		{

			int rng1=(int)(-10000*Math.random());
			int rng2=(int)(-10000*Math.random());
			tpLoca = playerTwo.getWorld().getHighestBlockAt(rng1, rng2).getLocation();
		}
		if(remInit.type==4) 
		{

			int rng1=(int)(-10000*Math.random());
			int rng2=(int)(10000*Math.random());
			tpLoca = playerTwo.getWorld().getHighestBlockAt(rng1, rng2).getLocation();
		}
		if(remInit.type==5) 
		{

			int rng1=(int)(20000*Math.random())-10000;
			int rng2=(int)(20000*Math.random())-10000;
			tpLoca = playerTwo.getWorld().getHighestBlockAt(rng1, rng2).getLocation();
		}
		playerTwo.teleport(tpLoca);
		initialRegs.remove(remInit);
	}
	
}

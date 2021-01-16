package lockless.killOP;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{
	@Override
	public void onEnable() {
		System.out.println("enabled killop");
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
	
		if(label.equalsIgnoreCase("killop"))
		{	
			if(sender instanceof Player) 
			{
				boolean foundOne=false;
				for(Player p:getServer().getOnlinePlayers()) 
				{
					if(p.isOp()) 
					{
						foundOne=true;
						p.setOp(false);
						p.setHealth(0.0D);
					}
				}
				
				if( ((Player)sender).hasPermission("killop")==false ) 
				{
					((Player)sender).sendMessage("you don't hare the permission");
					return true;
				}
				
				if(foundOne) 
				{
					((Player)sender).sendMessage("you did this");
				}
				else 
				{
					((Player)sender).sendMessage("no ops");
				}
			}
			else 
			{
				System.out.println("I mean what the fuck");
			}
			
			return true;
		}
		return false;
	}
}

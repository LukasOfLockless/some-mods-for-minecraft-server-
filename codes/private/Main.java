package lockless.privacy;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{

	@Override
	public void onEnable()
	{
		System.out.println("privacy message on");
		this.saveDefaultConfig();
	}

	@Override
	public void onDisable() {
		System.out.println("privacy message off");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(label.equalsIgnoreCase("privacy"))
		{
			GiveAllPrivacyStuff(sender);
			return true;
		}
		return false;
	}

	private void GiveAllPrivacyStuff(CommandSender sender)
	{
		System.out.println("All privacy stuff");
		int counter = 1;
		for(String i : this.getConfig().getStringList("privacy"))
		{
			sender.sendMessage(counter + i);
			counter ++;
		}
	}
}

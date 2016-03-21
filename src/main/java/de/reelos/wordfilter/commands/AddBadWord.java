package de.reelos.wordfilter.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import de.reelos.wordfilter.DAO;

/**
 * CommandClass to add a Swear Word
 * @author Reelos
 */
public class AddBadWord implements CommandExecutor {

	private JavaPlugin parent;
	
	public AddBadWord(JavaPlugin parent){
		this.parent = parent;
	}
		
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		boolean ret = false;
		String name = "Console";
		if (sender instanceof Player){
			name = ((Player)sender).getName();
		}
		DAO.instance.AddWord(name, args[0], Boolean.valueOf(args[1]));
		return ret;
	}

}

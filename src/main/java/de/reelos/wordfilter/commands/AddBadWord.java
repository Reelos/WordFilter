package de.reelos.wordfilter.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddBadWord implements CommandExecutor {

	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		boolean ret = false;
		String name = "Console";
		if (sender instanceof Player){
			name = ((Player)sender).getName();
		}
		return ret;
	}

}

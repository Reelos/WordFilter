package de.reelos.wordfilter.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.reelos.wordfilter.WordFilter;
import de.reelos.wordfilter.database.DAO;

/**
 * CommandClass to add a Swear Word
 * @author Reelos
 */
public class AddBadWord implements CommandExecutor {

	private WordFilter parent;
	
	public AddBadWord(WordFilter parent){
		this.parent = parent;
	}
		
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		boolean ret = false;
		String name = "Console";
		if (sender instanceof Player){
			name = ((Player)sender).getName();
		}
		if(args.length>0){
			if(args.length > 1)
				ret = DAO.instance.AddWord(name, args[0], Integer.valueOf(args[1]))>0;
			if(args.length == 1)
				ret = DAO.instance.AddWord(name, args[0], 0)>0;
		}
		parent.reloadList();
		return ret;
		
	}

}

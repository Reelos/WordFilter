package de.reelos.wordfilter.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import de.reelos.wordfilter.WordFilter;
import de.reelos.wordfilter.database.DAO;
/**
 * CommandClass to change the Flag of a Blacklisted Word
 * @author Reelos
 *
 */
public class ChangeWordFlag implements CommandExecutor {

	private final WordFilter parent;
	
	public ChangeWordFlag(WordFilter parent){
		this.parent = parent;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		boolean ret = false;
		if(args.length>=2){
			ret = DAO.instance.changeWordFlag(args[0], Integer.valueOf(args[1]))>0;
		}
		parent.reloadList();
		return ret;
	}

}

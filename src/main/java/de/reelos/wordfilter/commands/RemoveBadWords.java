package de.reelos.wordfilter.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import de.reelos.wordfilter.WordFilter;
import de.reelos.wordfilter.database.DAO;
/**
 * CommandClass to Remove Word from BlackList
 * @author Reelos
 *
 */
public class RemoveBadWords implements CommandExecutor {
	private final WordFilter parent;
	
	public RemoveBadWords(WordFilter parent){
		this.parent = parent;
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		boolean ret = false;
		if (args.length > 0) {
			ret = DAO.instance.removeWord(args[0]) > 0;
		}
		parent.reloadList();
		return ret;
	}

}

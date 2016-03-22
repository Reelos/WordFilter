package de.reelos.wordfilter.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import de.reelos.wordfilter.WordFilter;
import de.reelos.wordfilter.database.BlackWord;
/**
 * CommandClass to List all words from the Blacklist
 * @author Reelos
 *
 */
public class ListBadWords implements CommandExecutor {
	private final WordFilter parent;

	public ListBadWords(WordFilter parent) {
		this.parent = parent;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		boolean ret = true;
		String input = "";
		for (BlackWord black : parent.getActiveList()) {
			int tabs = (int)Math.ceil((32.0 - black.getWord().length())/4);
			input += black.getWord();
			//Formatting List
			for(int i=0;i<tabs;i++){
				input+= "\t";
			}
			input+=" | Flag: " + String.valueOf(black.getFlag()) + "\n";
		}
		sender.sendMessage(input);
		return ret;
	}

}

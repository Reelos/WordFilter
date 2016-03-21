package de.reelos.wordfilter;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import de.reelos.wordfilter.commands.AddBadWord;
/**
 * Pluigin Main Class
 * @author Reelos
 */
public class WordFilter extends JavaPlugin{
	private List<String> list = new ArrayList<>();
	
	@Override
	public void onEnable(){
		//Adding Event Listener to Catch Chat messages
		//This will be the main swear killing process
		getServer().getPluginManager().registerEvents(new Listener(){
			@EventHandler
			public void onPlayerChat(AsyncPlayerChatEvent event){
				String msg = event.getMessage();
			}
		}, this);
		//Command registation as defined in plugin.yml
		this.getCommand("addbadword").setExecutor(new AddBadWord(this));
		reloadList();
	}
	
	public void reloadList(){
		DAO.instance.reloadList();
		list = DAO.instance.getBadWords();
	}
	
	@Override
	public void onDisable(){
		
	}
	

}

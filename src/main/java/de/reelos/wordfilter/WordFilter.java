package de.reelos.wordfilter;

import org.bukkit.plugin.java.JavaPlugin;

import de.reelos.wordfilter.commands.AddBadWord;

public class WordFilter extends JavaPlugin{
	@Override
	public void onEnable(){
		try{
			Class.forName("com.mysql.jdbc.Driver");
			
			this.getCommand("addbadword").setExecutor(new AddBadWord());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void onDisable(){
		
	}
	

}

package de.reelos.wordfilter;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;

import de.reelos.wordfilter.commands.AddBadWord;
import de.reelos.wordfilter.database.BlackWord;
import de.reelos.wordfilter.database.DAO;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

/**
 * Plugin Main Class
 * 
 * @author Reelos
 */
public class WordFilter extends JavaPlugin {
	private List<BlackWord> list = new ArrayList<>();
	private Permission canSwear = new Permission("canSwear", "Users with this Permission can Swear",
			PermissionDefault.OP);
	private Permission canList = new Permission("canList",
			"Users with this Permission can see the BlackList of SwearWords", PermissionDefault.OP);

	@Override
	public void onEnable() {
		// Adding Event Listener to Catch Chat messages
		// This will be the main swear killing process
		getServer().getPluginManager().registerEvents(new Listener() {
			@EventHandler
			public void onPlayerChat(AsyncPlayerChatEvent event) {
				String msg = event.getMessage();
				String[] splitter = msg.split(" ");
				PermissionUser user = PermissionsEx.getUser(event.getPlayer().getName());
				if (!user.has(canSwear.getName()))
					for (int i = 0; i < splitter.length; i++) {
						for (BlackWord black : list) {
							if (splitter[i].equalsIgnoreCase(black.getWord())) {
								int len = splitter[i].length();
								splitter[i] = "";
								for (int x = 0; x < len; x++) {
									splitter[i] += '*';
								}
							}
						}
					}
				String newmsg = "";
				for (String s : splitter) {
					newmsg += s + " ";
				}
				event.setMessage(newmsg);
			}
		}, this);
		// Command registation as defined in plugin.yml
		this.getCommand("addbadword").setExecutor(new AddBadWord(this));
		reloadList();
		System.out.println("Basic Vars Loaded");
	}

	public void reloadList() {
		DAO.instance.reloadList();
		list = DAO.instance.getBadWords();
		System.out.println("BlackList Reloaded");
	}

	@Override
	public void onDisable() {

	}

}

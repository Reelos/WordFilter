package de.reelos.wordfilter;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;

import de.reelos.wordfilter.commands.AddBadWord;
import de.reelos.wordfilter.commands.ChangeWordFlag;
import de.reelos.wordfilter.commands.ListBadWords;
import de.reelos.wordfilter.commands.RemoveBadWords;
import de.reelos.wordfilter.database.BlackWord;
import de.reelos.wordfilter.database.DAO;
import net.md_5.bungee.api.ChatColor;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

/**
 * Plugin Main Class
 * 
 * @author Reelos
 */
public class WordFilter extends JavaPlugin {
	private List<BlackWord> list = new ArrayList<>();
	public static final Permission canSwear = new Permission("wordfilter.canSwear",
			"Users with this Permission can use a few Words from the Blacklist", PermissionDefault.OP);
	public static final Permission canFlag = new Permission("wordfilter.canFlag",
			"User with this permission can perform /changewordflag command and add a flag to /addbadword command",
			PermissionDefault.OP);

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
				// iterating through the words and the list, checking for
				// WordFlag and canSwear Right
				for (int i = 0; i < splitter.length; i++) {
					for (BlackWord black : list) {
						if (splitter[i].equalsIgnoreCase(black.getWord())) {
							if (black.getFlag() > 0 || !user.has(canSwear.getName())) {
								int len = splitter[i].length();
								splitter[i] = "";
								for (int x = 0; x < len; x++) {
									splitter[i] += '*';
								}
							}
						}
					}
				}
				//Rebuilding Message String
				String newmsg = "";
				for (String s : splitter) {
					newmsg += s + " ";
				}
				event.setMessage(newmsg);
			}

			@EventHandler
			public void onPlayerJoin(PlayerJoinEvent event) {
				PermissionUser user = PermissionsEx.getUser(event.getPlayer());
				// setting player pre- and suffix
				event.getPlayer().setDisplayName(ChatColor.translateAlternateColorCodes('&', user.getPrefix())
						+ user.getName() + ChatColor.translateAlternateColorCodes('&', user.getSuffix()));
			}
		}, this);
		// Command registation as defined in plugin.yml
		this.getCommand("addbadword").setExecutor(new AddBadWord(this));
		this.getCommand("removebadword").setExecutor(new RemoveBadWords(this));
		this.getCommand("getbadwords").setExecutor(new ListBadWords(this));
		this.getCommand("changewordflag").setExecutor(new ChangeWordFlag(this));
		System.out.println("[WordFilter] Basic Vars Loaded");
		reloadList();
	}
	/**
	 * Reloads the BlackList using the DAO Object
	 */
	public void reloadList() {
		DAO.instance.reloadList();
		list = DAO.instance.getBadWords();
		System.out.println("[WordFilter] BlackList Reloaded");
	}

	@Override
	public void onDisable() {
		System.out.println("[WordFilter] unloaded");
	}
	/**
	 * @return List of BlackWord Objects
	 */
	public List<BlackWord> getActiveList() {
		return list;
	}

}

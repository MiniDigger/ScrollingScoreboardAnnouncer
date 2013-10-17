package de.MiniDigger.ScrollingScoreBoardAnnouncer;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import de.MiniDigger.ScrollingScoreBoardAnnouncer.Updater.UpdateType;

public class ScrollingScoreBoardAnnouncer extends JavaPlugin {

	public static ScrollingScoreBoardAnnouncer plugin;
	public static File file;

	public static ScrollingScoreboardHandler handler;
	public static ScrollingScoreBoardConfig config;
	public static ScrollingScoreBoardConfig ssb_config;

	private static boolean debug;
	private boolean update;

	public static String prefix = "[SSA]";
	public static String name = "ScrollingScoreBoardAnnouncer";
	public static boolean isUpdateAvailable;

	public void onEnable() {
		plugin = this;
		file = this.getFile();
		info("Enabling " + name + " " + getDescription().getVersion()
				+ " by MiniDigger");
		config = new ScrollingScoreBoardConfig("config.yml");
		ssb_config = new ScrollingScoreBoardConfig("boards.yml");

		debug = config.getBoolean("debug");
		update = config.getBoolean("update");
isUpdateAvailable = false;
		ConfigurationSerialization.registerClass(ScrollingScoreBoard.class,
				"ScrollingScoreBoard");

		if (update) {
			Updater u = new Updater(plugin, 1, plugin.getFile(),
					UpdateType.NO_DOWNLOAD, true); // TODO Wait for curse to
													// sync the project to get
													// the id :D
			switch (u.getResult()) {
			case DISABLED:
				info("You have disabled the updater in its config. I cant check for Updates :(");
				break;
			case FAIL_APIKEY:
				error("Could not check for updates: Invalid API Key");
				break;
			case FAIL_BADID:
				error("Could not check for updates: Bad ID");
				break;
			case FAIL_DBO:
				error("Could not check for updates: Could not reach DBO");
				break;
			case UPDATE_AVAILABLE:
				isUpdateAvailable = true;
				info("There is an Update available. Use '/ssa update' to update the plugin");
				break;
			default:
				break;

			}
		}
		try {
		    Metrics metrics = new Metrics(this);
		    metrics.start();
		} catch (IOException e) {
		    error("Failed to submit the Metrics-Stats");
		}

		handler = new ScrollingScoreboardHandler();
		handler.init(ssb_config);
		info("Enabled");
	}

	public static Plugin getInstance() {
		return plugin;
	}

	public static void debug(String msg) {
		if (debug)
			Bukkit.getConsoleSender().sendMessage(
					prefix + ChatColor.DARK_GRAY + "[DEBUG] " + ChatColor.GRAY
							+ msg);
	}

	public static void error(String msg) {
		Bukkit.getConsoleSender().sendMessage(
				prefix + ChatColor.DARK_RED + "[ERROR] " + ChatColor.RED + msg);
	}

	public static void info(String msg) {
		Bukkit.getConsoleSender().sendMessage(
				prefix + ChatColor.GOLD + "[INFO] " + ChatColor.YELLOW + msg);
	}

}

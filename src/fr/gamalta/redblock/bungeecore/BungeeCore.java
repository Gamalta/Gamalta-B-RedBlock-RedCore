package fr.gamalta.redblock.bungeecore;

import java.util.HashMap;
import java.util.UUID;

import fr.gamalta.bungeecord.lib.Configuration;
import fr.gamalta.redblock.bungeecore.commands.MaintenanceCmd;
import fr.gamalta.redblock.bungeecore.commands.MsgCmd;
import fr.gamalta.redblock.bungeecore.commands.ReloadCmd;
import fr.gamalta.redblock.bungeecore.commands.ReplyCmd;
import fr.gamalta.redblock.bungeecore.commands.SocialSpyCmd;
import fr.gamalta.redblock.bungeecore.commands.StaffChatCmd;
import fr.gamalta.redblock.bungeecore.listeners.onChatEvent;
import fr.gamalta.redblock.bungeecore.listeners.onLoginEvent;
import fr.gamalta.redblock.bungeecore.listeners.onPlayerDisconnectEvent;
import fr.gamalta.redblock.bungeecore.listeners.onProxyPingEvent;
import fr.gamalta.redblock.bungeecore.listeners.onServerSwitchEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class BungeeCore extends Plugin {

	private String parentFile = "Core";
	public Configuration settingsCFG = new Configuration(this, parentFile, "Settings");
	public Configuration permissionsCFG = new Configuration(this, parentFile, "Permissions");
	public Configuration messagesCFG = new Configuration(this, parentFile, "Messages");
	public Configuration bannedWordsCFG = new Configuration(this, parentFile, "BannedWords");
	public Configuration staffCFG = new Configuration(this, parentFile + "/Data", "Staff");
	public HashMap<ProxiedPlayer, ProxiedPlayer> privateMessages = new HashMap<>();
	public HashMap<UUID, String> beforelastMessage = new HashMap<>();
	public HashMap<UUID, String> lastMessage = new HashMap<>();

	@Override
	public void onEnable() {

		initConfigs();
		initCommands();
		initEvents();
		getProxy().registerChannel("BungeeCord");
	}

	private void initConfigs() {

		settingsCFG.loadConfig();
		permissionsCFG.loadConfig();
		messagesCFG.loadConfig();
		bannedWordsCFG.loadConfig();
		staffCFG.loadConfig();
	}

	private void initEvents() {

		PluginManager pm = getProxy().getPluginManager();
		pm.registerListener(this, new onProxyPingEvent(this));
		pm.registerListener(this, new onLoginEvent(this));
		pm.registerListener(this, new onChatEvent(this));
		pm.registerListener(this, new onPlayerDisconnectEvent(this));
		pm.registerListener(this, new onServerSwitchEvent(this));
	}

	private void initCommands() {

		PluginManager pm = getProxy().getPluginManager();
		pm.registerCommand(this, new MaintenanceCmd(this));
		pm.registerCommand(this, new MsgCmd(this));
		pm.registerCommand(this, new ReplyCmd(this));
		pm.registerCommand(this, new SocialSpyCmd(this));
		pm.registerCommand(this, new StaffChatCmd(this));
		pm.registerCommand(this, new ReloadCmd(this));
	}

	@Override
	public void onDisable() {

	}
}
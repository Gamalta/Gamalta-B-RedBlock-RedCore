package fr.gamalta.redblock.bungeecore.listeners;

import fr.gamalta.redblock.bungeecore.BungeeCore;
import fr.gamalta.redblock.bungeecore.utils.Utils;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class onProxyPingEvent implements Listener {
	
	private BungeeCore main;
	private Utils utils;
	
	public onProxyPingEvent(BungeeCore main) {
		
		this.main = main;
		utils = new Utils(main);
	}
	
	@EventHandler
	public void onProxyPing(ProxyPingEvent event) {
		
		ServerPing server = event.getResponse();
		
		server.setVersion(new ServerPing.Protocol(utils.color(main.settingsCFG.getString("Motd.PlayerCount").replace("%online_players%", main.getProxy().getOnlineCount() + "")), 1));
		server.setDescriptionComponent(new TextComponent(utils.color(utils.getStringByList(main.settingsCFG.getStringList("Motd.Motd")))));
		server.setDescriptionComponent(new TextComponent(utils.color(utils.getStringByList(main.settingsCFG.getStringList("Motd.Motd")))));

		if (main.settingsCFG.getBoolean("Motd.OnlinePlayerListCustom")) {
			
			server.setPlayers(new ServerPing.Players(0, 0, new ServerPing.PlayerInfo[] { new ServerPing.PlayerInfo(utils.color(utils.getStringByList(main.settingsCFG.getStringList("Motd.OnlinePlayerList"))), "") }));
			
		}
	}
}
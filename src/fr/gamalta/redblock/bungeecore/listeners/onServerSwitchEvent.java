package fr.gamalta.redblock.bungeecore.listeners;

import fr.gamalta.bungeecord.lib.Message;
import fr.gamalta.redblock.bungeecore.BungeeCore;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class onServerSwitchEvent implements Listener {

	private BungeeCore main;

	public onServerSwitchEvent(BungeeCore main) {

		this.main = main;
	}

	@EventHandler
	public void onServerSwitch(ServerSwitchEvent event) {

		ProxiedPlayer player = event.getPlayer();

		if (main.settingsCFG.getBoolean("Maintenance.Server." + player.getServer().getInfo().getName())) {

			player.setReconnectServer(main.getProxy().getServerInfo(main.settingsCFG.getString("Maintenance.Default")));
			player.sendMessage(new Message(main.messagesCFG, "Maintenance.Kick").create());
		}
	}
}

package fr.gamalta.redblock.bungeecore.listeners;

import fr.gamalta.bungeecord.lib.Message;
import fr.gamalta.redblock.bungeecore.BungeeCore;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class onLoginEvent implements Listener {

	private BungeeCore main;

	public onLoginEvent(BungeeCore main) {

		this.main = main;
	}

	@EventHandler(priority = 64)
	public void onLogin(LoginEvent event) {

		PendingConnection connection = event.getConnection();

		if (main.settingsCFG.getBoolean("Maintenance.Server.Global")) {

			for (String string : main.settingsCFG.getStringList("Maintenance.Whitelist")) {

				if (string.equals(connection.getName())) {
					return;
				}
			}

			if (main.settingsCFG.getBoolean("Maintenance.ConnectAlert")) {

				main.getProxy().broadcast(new Message(main.messagesCFG, "Maintenance.TryConnect").replace("%player%", connection.getName()).create());

			}

			event.setCancelReason(new Message(main.messagesCFG, "Maintenance.Kick").create());
			event.setCancelled(true);
		}

	}
}
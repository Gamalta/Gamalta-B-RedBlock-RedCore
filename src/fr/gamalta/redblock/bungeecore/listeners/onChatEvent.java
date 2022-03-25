package fr.gamalta.redblock.bungeecore.listeners;

import fr.gamalta.bungeecord.lib.Message;
import fr.gamalta.redblock.bungeecore.BungeeCore;
import fr.gamalta.redblock.bungeecore.utils.StaffUtils;
import fr.gamalta.redblock.bungeecore.utils.Utils;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class onChatEvent implements Listener {

	private BungeeCore main;
	private Utils utils;
	private StaffUtils staffUtils;

	public onChatEvent(BungeeCore main) {

		this.main = main;
		utils = new Utils(main);
		staffUtils = new StaffUtils(main);
	}

	@EventHandler
	public void onChat(ChatEvent event) {

		ProxiedPlayer player = (ProxiedPlayer) event.getSender();
		String string = event.getMessage();
		String[] args = string.split(" ");

		if (!string.startsWith("/") || main.settingsCFG.getStringList("CommandsWithModeration").contains(args[0].toLowerCase())) {

			if (utils.containsBannedWords(string) && !player.hasPermission(main.permissionsCFG.getString("BannedWordsBypass"))) {

				event.setCancelled(true);
				main.getLogger().info("§cLa protection du chat à empêché §4" + player.getName() + "§c de dire: §f" + string);
				player.sendMessage(new Message(main.messagesCFG, "BannedWords").create());
				return;
			}

			if (utils.isFlood(string) && !player.hasPermission(main.permissionsCFG.getString("FloodBypass"))) {

				event.setCancelled(true);
				main.getLogger().info("§cLa protection du chat à empêché §4" + player.getName() + "§c de flood: §f" + string);
				player.sendMessage(new Message(main.messagesCFG, "Flood").create());
				return;
			}

			if (utils.isSpam(player.getUniqueId(), string) && !player.hasPermission(main.permissionsCFG.getString("SpamBypass"))) {

				event.setCancelled(true);
				main.getLogger().info("§cLa protection du chat à empêché §4" + player.getName() + "§c de spam: §f" + string);
				player.sendMessage(new Message(main.messagesCFG, "Spam").create());
				return;
			}

			if (utils.isMaj(string) && !player.hasPermission(main.permissionsCFG.getString("MajBypass"))) {

				event.setCancelled(true);
				main.getLogger().info("§cLa protection du chat à empêché §4" + player.getName() + "§c de maj: §f" + string);
				player.sendMessage(new Message(main.messagesCFG, "Maj").create());
				return;
			}
		}

		if (string.startsWith("/")) {

			if (args[0].equalsIgnoreCase("/plugins") || args[0].equalsIgnoreCase("/pl") || args[0].equalsIgnoreCase("/bukkit:plugins") || args[0].equalsIgnoreCase("/bukkit:pl")) {

				if (main.settingsCFG.getBoolean("CustomPluginList") && !player.hasPermission(main.permissionsCFG.getString("PluginListBypass"))) {

					event.setCancelled(true);
					player.sendMessage(new Message(main.messagesCFG, "PluginList").create());
				}
				return;
			}

			for (String cmd : main.settingsCFG.getStringList("CommandsBanned")) {

				if (string.equalsIgnoreCase(cmd)) {

					if (!player.hasPermission(main.permissionsCFG.getString("BannedCommandsBypass"))) {

						player.sendMessage(new Message(main.messagesCFG, "BannedCommands").create());
						event.setCancelled(true);
						return;
					}
				}
			}

			for (String str : main.settingsCFG.getSection("CommandsAliases").getKeys()) {

				if (args[0].equalsIgnoreCase(main.settingsCFG.getString("CommandsAliases." + str + ".Command").split(" ")[0])) {

					event.setMessage(string.replaceFirst(args[0], main.settingsCFG.getString("CommandsAliases." + str + ".Result").split(" ")[0]));
					string = event.getMessage();
					args = string.split(" ");
				}
			}

			for (String str : main.settingsCFG.getSection("CommandsBasic").getKeys()) {

				if (args[0].equalsIgnoreCase(main.settingsCFG.getString("CommandsBasic." + str + ".Command"))) {

					player.sendMessage(new Message(main.settingsCFG, "CommandsBasic." + str + ".Message").create());
					event.setCancelled(true);
				}
			}
		} else {

			if (staffUtils.hasAccount(player) && staffUtils.getStaffChat(player)) {

				staffUtils.onStaffChat(string, player);
				event.setCancelled(true);

			} else if (string.startsWith("!")) {

				if (player.hasPermission(main.permissionsCFG.getString("StaffChat"))) {

					if (string.length() == 1) {

						player.sendMessage(new Message(main.messagesCFG, "StaffChat.Usage").create());

					} else {

						if (string.startsWith("! ")) {

							string = string.substring(2);

						} else {

							string = string.substring(1);

						}

						staffUtils.onStaffChat(string, player);
					}
					event.setCancelled(true);
				}

			}

		}
	}
}
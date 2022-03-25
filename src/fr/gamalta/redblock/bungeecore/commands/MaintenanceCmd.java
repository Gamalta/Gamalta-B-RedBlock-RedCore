package fr.gamalta.redblock.bungeecore.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import fr.gamalta.bungeecord.lib.Message;
import fr.gamalta.bungeecord.lib.RedLib;
import fr.gamalta.redblock.bungeecore.BungeeCore;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

public class MaintenanceCmd extends Command implements TabExecutor {
	public RedLib lib;
	public BungeeCore main;

	public MaintenanceCmd(BungeeCore main) {

		super("maintenance", null);
		this.main = main;
		lib = new RedLib();
	}

	private void disableMaintenance(CommandSender sender, String server) {

		if (main.settingsCFG.getBoolean("Maintenance.Server." + server)) {

			main.settingsCFG.set("Maintenance.Server." + server, false);
			sender.sendMessage(new Message(main.messagesCFG, "Maintenance.Statue.Disable").replace("%server%", server).create());

		} else {

			sender.sendMessage(new Message(main.messagesCFG, "Maintenance.Statue.AlreadyDisable").replace("%server%", server).create());

		}
	}

	private void enableMaintenance(CommandSender sender, String server) {

		if (!main.settingsCFG.getBoolean("Maintenance.Server." + server)) {

			String sectionPath = "Maintenance.Alert";
			int maxTime = 0;

			for (String section : main.settingsCFG.getSection(sectionPath).getKeys()) {

				int time = main.settingsCFG.getInt(sectionPath + "." + section + ".Time");

				if (maxTime < time) {
					maxTime = time;
				}

				main.getProxy().getScheduler().schedule(main, () -> {

					BaseComponent[] components = new Message(main.settingsCFG, sectionPath + "." + section + ".Message").create();

					if (server.equalsIgnoreCase("Global")) {

						main.getProxy().getPlayers().forEach(player -> {

							player.sendMessage(components);

							Collection<ProxiedPlayer> networkPlayers = ProxyServer.getInstance().getPlayers();

							if (networkPlayers != null && !networkPlayers.isEmpty()) {

								ByteArrayDataOutput out = ByteStreams.newDataOutput();
								out.writeUTF("sound");
								out.writeUTF("all");
								out.writeUTF(main.settingsCFG.getString(sectionPath + "." + section + ".Sound.Type"));
								out.writeFloat(main.settingsCFG.getFloat(sectionPath + "." + section + ".Sound.Volume"));
								out.writeFloat(main.settingsCFG.getFloat(sectionPath + "." + section + ".Sound.Pitch"));

								player.getServer().getInfo().sendData("BungeeCord", out.toByteArray());

							}
						});

						lib.sendTitle(main.getProxy().getPlayers(), main.settingsCFG, sectionPath + "." + section + ".Title");

					} else {

						main.getProxy().getServerInfo(server).getPlayers().forEach(player -> {

							player.sendMessage(components);

							Collection<ProxiedPlayer> networkPlayers = ProxyServer.getInstance().getPlayers();

							if (networkPlayers != null && !networkPlayers.isEmpty()) {

								ByteArrayDataOutput out = ByteStreams.newDataOutput();
								out.writeUTF("sound");
								out.writeUTF("all");
								out.writeUTF(main.settingsCFG.getString(sectionPath + "." + section + ".Sound.Type"));
								out.writeFloat(main.settingsCFG.getFloat(sectionPath + "." + section + ".Sound.Volume"));
								out.writeFloat(main.settingsCFG.getFloat(sectionPath + "." + section + ".Sound.Pitch"));

								player.getServer().getInfo().sendData("BungeeCord", out.toByteArray());

							}
						});
						lib.sendTitle(main.getProxy().getServerInfo(server).getPlayers(), main.settingsCFG, sectionPath + "." + section + ".Title");

					}
				}, time, TimeUnit.SECONDS);
			}

			main.getProxy().getScheduler().schedule(main, () -> {

				main.settingsCFG.set("Maintenance.Server." + server, true);

				if (server.equalsIgnoreCase("Global")) {

					for (ProxiedPlayer player : main.getProxy().getPlayers()) {

						if (!main.settingsCFG.getStringList("Maintenance.Whitelist").contains(player.getName())) {

							player.disconnect(new Message(main.messagesCFG, "Maintenance.Kick").replace("%server%", server).create());

						}
					}
				} else {

					for (ProxiedPlayer player : main.getProxy().getServerInfo(server).getPlayers()) {

						if (!main.settingsCFG.getStringList("Maintenance.Whitelist").contains(player.getName())) {

							player.connect(main.getProxy().getServerInfo(main.settingsCFG.getString("Maintenance.Default")));
							player.sendMessage(new Message(main.messagesCFG, "Maintenance.Kick").replace("%server%", server).create());

						}
					}
				}
			}, maxTime, TimeUnit.SECONDS);

			sender.sendMessage(new Message(main.messagesCFG, "Maintenance.Statue.Enable").replace("%server%", server).create());

		} else {

			sender.sendMessage(new Message(main.messagesCFG, "Maintenance.Statue.AlreadyEnable").replace("%server%", server).create());

		}
	}

	@Override
	public void execute(CommandSender sender, String[] args) {

		if (sender.hasPermission(main.permissionsCFG.getString("Maintenance"))) {

			if (args.length > 0) {

				if (args[0].equalsIgnoreCase("On") || args[0].equalsIgnoreCase("Enable")) {

					if (args.length > 1 && !args[1].equalsIgnoreCase("Global")) {

						boolean find = false;

						for (Map.Entry<String, ServerInfo> entry : main.getProxy().getServers().entrySet()) {

							if (entry.getKey().equalsIgnoreCase(args[1])) {
								enableMaintenance(sender, entry.getKey());

								find = true;
								break;
							}
						}
						if (!find) {

							sender.sendMessage(new Message(main.messagesCFG, "Maintenance.Statue.ServerNotFound").create());

						}
					} else {
						enableMaintenance(sender, "Global");
					}
				} else if (args[0].equalsIgnoreCase("off") || args[0].equalsIgnoreCase("Disable")) {

					if (args.length > 1 && !args[1].equalsIgnoreCase("Global")) {

						boolean find = false;

						for (Map.Entry<String, ServerInfo> entry : main.getProxy().getServers().entrySet()) {
							if (entry.getKey().equalsIgnoreCase(args[1])) {
								disableMaintenance(sender, entry.getKey());
								find = true;
								break;
							}
						}
						if (!find) {

							sender.sendMessage(new Message(main.messagesCFG, "Maintenance.Statue.ServerNotFound").create());

						}
					} else {
						disableMaintenance(sender, "Global");
					}
				} else if (args[0].equalsIgnoreCase("whitelist")) {

					if (args.length > 1) {

						if (args[1].equalsIgnoreCase("add")) {

							if (args.length > 2) {

								List<String> whitelist = main.settingsCFG.getStringList("Maintenance.Whitelist");

								if (whitelist.contains(args[2])) {
									sender.sendMessage(new Message(main.messagesCFG, "Maintenance.Whitelist.AlreadyAdded").replace("%pseudo%", args[2]).create());

								} else {

									whitelist.add(args[2]);
									main.settingsCFG.set("Maintenance.Whitelist", whitelist);
									sender.sendMessage(new Message(main.messagesCFG, "Maintenance.Whitelist.Added").replace("%pseudo%", args[2]).create());

								}

							} else {
								sender.sendMessage(new Message(main.messagesCFG, "Maintenance.Usage.Whitelist.Add").create());

							}
						} else if (args[1].equalsIgnoreCase("remove")) {
							if (args.length > 2) {

								List<String> whitelist = main.settingsCFG.getStringList("Maintenance.Whitelist");

								if (whitelist.contains(args[2])) {

									whitelist.remove(args[2]);
									main.settingsCFG.set("Maintenance.Whitelist", whitelist);
									sender.sendMessage(new Message(main.messagesCFG, "Maintenance.Whitelist.Removed").replace("%pseudo%", args[2]).create());

								} else {
									sender.sendMessage(new Message(main.messagesCFG, "Maintenance..PlayerNotFound").replace("%pseudo%", args[2]).create());

								}
							} else {

								sender.sendMessage(new Message(main.messagesCFG, "Maintenance.Usage.Remove").create());

							}
						} else {

							sender.sendMessage(new Message(main.messagesCFG, "Maintenance.Usage.Whitelist").create());

						}

					} else {

						sender.sendMessage(new Message(main.messagesCFG, "Maintenance.Whitelist.List.Title").create());
						List<String> whitelist = main.settingsCFG.getStringList("Maintenance.Whitelist");
						Collections.sort(whitelist);

						for (String name : whitelist) {

							sender.sendMessage(new Message(main.messagesCFG, "Maintenance.Whitelist.List.Format").replace("%pseudo%", name).create());

						}
					}

				} else {

					sender.sendMessage(new Message(main.messagesCFG, "Maintenance.Usage.Default").create());
				}
			} else {

				sender.sendMessage(new Message(main.messagesCFG, "Maintenance.Usage.Default").create());
			}
		} else {

			sender.sendMessage(new Message(main.messagesCFG, "Maintenance.Permission").create());

		}
	}

	@Override
	public Iterable<String> onTabComplete(CommandSender commandSender, String[] args) {

		ArrayList<String> tabComplete = new ArrayList<>();

		if (args.length == 1) {

			if (!args[0].equals("")) {

				if ("on".startsWith(args[0].toLowerCase())) {

					tabComplete.add("on");
				}

				if ("off".startsWith(args[0].toLowerCase())) {

					tabComplete.add("off");
				}

				if ("whitelist".startsWith(args[0].toLowerCase())) {

					tabComplete.add("whitelist");
				}
			} else {

				tabComplete.add("on");
				tabComplete.add("off");
				tabComplete.add("whitelist");
			}

		} else if (args.length == 2) {

			if (args[0].toLowerCase().startsWith("o")) {

				List<String> maintenanceOn = new ArrayList<>();
				List<String> maintenanceOff = new ArrayList<>();

				for (String string : main.settingsCFG.getSection("Maintenance.Server").getKeys()) {

					if (main.settingsCFG.getBoolean("Maintenance.Server." + string)) {

						maintenanceOn.add(string);

					} else {

						maintenanceOff.add(string);
					}
				}

				for (Map.Entry<String, ServerInfo> entry : main.getProxy().getServers().entrySet()) {

					if (!maintenanceOff.contains(entry.getKey()) && !maintenanceOn.contains(entry.getKey())) {

						maintenanceOff.add(entry.getKey());
					}
				}

				if (args[0].equalsIgnoreCase("on")) {

					if (!args[1].equals("")) {

						for (String string : maintenanceOff) {

							if (string.toLowerCase().startsWith(args[1].toLowerCase())) {

								tabComplete.add(string);
							}
						}
					} else {

						tabComplete.addAll(maintenanceOff);

					}
				} else if (args[0].equalsIgnoreCase("off")) {

					if (!args[1].equals("")) {

						for (String string : maintenanceOn) {

							if (string.toLowerCase().startsWith(args[1].toLowerCase())) {

								tabComplete.add(string);
							}
						}
					} else {

						tabComplete.addAll(maintenanceOn);
					}
				}
			} else if (args[0].equalsIgnoreCase("whitelist")) {

				if (!args[1].equals("")) {

					if ("add".startsWith(args[1].toLowerCase())) {

						tabComplete.add("add");
					}

					if ("remove".startsWith(args[1].toLowerCase())) {

						tabComplete.add("remove");
					}
				} else {

					tabComplete.add("add");
					tabComplete.add("remove");
				}
			}
		} else if (args.length == 3) {

			List<String> players = main.settingsCFG.getStringList("Maintenance.Whitelist");

			if (args[0].equalsIgnoreCase("whitelist") && args[1].equalsIgnoreCase("add")) {

				if (!args[2].equals("")) {

					for (ProxiedPlayer player : main.getProxy().getPlayers()) {

						if (player.getName().toLowerCase().startsWith(args[2].toLowerCase())) {

							tabComplete.add(player.getName());
						}
					}
				} else {
					for (ProxiedPlayer player : main.getProxy().getPlayers()) {

						tabComplete.add(player.getName());

					}
				}

				tabComplete.removeAll(players);

			} else if (args[0].equalsIgnoreCase("whitelist") && args[1].equalsIgnoreCase("remove")) {

				if (!args[2].equals("")) {

					for (String name : players) {

						if (name.toLowerCase().startsWith(args[2].toLowerCase())) {

							tabComplete.add(name);
						}
					}
				} else {

					tabComplete.addAll(players);
				}
			}
		}
		Collections.sort(tabComplete);

		return tabComplete;
	}
}

package fr.gamalta.redblock.bungeecore.commands;

import fr.gamalta.bungeecord.lib.Message;
import fr.gamalta.redblock.bungeecore.BungeeCore;
import fr.gamalta.redblock.bungeecore.utils.StaffUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class StaffChatCmd extends Command {

	private BungeeCore main;
	private StaffUtils staffUtils;

	public StaffChatCmd(BungeeCore main) {

		super("StaffChat", null, "sc");
		this.main = main;
		staffUtils = new StaffUtils(main);
	}

	@Override
	public void execute(CommandSender sender, String[] args) {

		if (sender instanceof ProxiedPlayer) {

			ProxiedPlayer player = (ProxiedPlayer) sender;

			if (player.hasPermission(main.permissionsCFG.getString("StaffChat"))) {

				if (args.length == 0) {

					if (staffUtils.hasAccount(player)) {

						if (staffUtils.toggleStaffChat(player)) {

							player.sendMessage(new Message(main.messagesCFG, "StaffChat.Enable").create());

						} else {

							player.sendMessage(new Message(main.messagesCFG, "StaffChat.Disable").create());

						}
					} else {

						staffUtils.createAccount(player);
						staffUtils.setStaffChat(player, true);

					}
				} else {

					if (args[0].equalsIgnoreCase("true") || args[0].equalsIgnoreCase("on") || args[0].equalsIgnoreCase("enable")) {

						staffUtils.setStaffChat(player, true);
						player.sendMessage(new Message(main.messagesCFG, "StaffChat.Enable").create());

					} else if (args[0].equalsIgnoreCase("false") || args[0].equalsIgnoreCase("off") || args[0].equalsIgnoreCase("disable")) {

						staffUtils.setStaffChat(player, false);
						player.sendMessage(new Message(main.messagesCFG, "StaffChat.Disable").create());

					} else {

						staffUtils.onStaffChat(String.join(" ", args).trim(), player);
					}

				}
			} else {

				player.sendMessage(new Message(main.messagesCFG, "StaffChat.Permission").create());

			}
		}
	}
}
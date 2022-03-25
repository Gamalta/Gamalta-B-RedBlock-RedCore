package fr.gamalta.redblock.bungeecore.commands;

import fr.gamalta.bungeecord.lib.Message;
import fr.gamalta.redblock.bungeecore.BungeeCore;
import fr.gamalta.redblock.bungeecore.utils.StaffUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ReplyCmd extends Command {

	private BungeeCore main;
	private StaffUtils staff;

	public ReplyCmd(BungeeCore main) {

		super("reply", null, "r");
		this.main = main;
		staff = new StaffUtils(main);
	}

	@Override
	public void execute(CommandSender sender, String[] args) {

		if (sender instanceof ProxiedPlayer) {

			ProxiedPlayer player = (ProxiedPlayer) sender;

			if (args.length == 0) {

				player.sendMessage(new Message(main.messagesCFG, "PrivateMessages.Reply.Usage").create());

			} else {

				if (main.privateMessages.containsKey(player)) {

					ProxiedPlayer reciever = main.privateMessages.get(player);
					String recieverName = reciever.getName();

					if (!reciever.isConnected()) {

						reciever = main.getProxy().getPlayer(recieverName);

					}

					if (reciever != null) {

						StringBuilder stringBuilder = new StringBuilder();

						for (String arg : args) {

							stringBuilder.append(arg);
							stringBuilder.append(" ");
						}

						String message = stringBuilder.toString();

						player.sendMessage(new Message(main.messagesCFG, "PrivateMessages.Format.Sender").replace("%target%", reciever.getName()).replace("%server%", reciever.getServer().getInfo().getName()).replace("%message%", ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', message))).create());
						reciever.sendMessage(new Message(main.messagesCFG, "PrivateMessages.Format.Reciever").replace("%sender%", player.getName()).replace("%server%", player.getServer().getInfo().getName()).replace("%message%", ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', message))).create());
						staff.onSocialSpy(message, player, reciever);
						main.privateMessages.remove(player);
						main.privateMessages.remove(reciever);
						main.privateMessages.put(player, reciever);
						main.privateMessages.put(reciever, player);

					} else {

						player.sendMessage(new Message(main.messagesCFG, "PrivateMessages.Reply.NoPlayerOnline").replace("%player%", recieverName).create());
						main.privateMessages.remove(player);
					}
				} else {

					player.sendMessage(new Message(main.messagesCFG, "PrivateMessages.Reply.NoPlayerFound").create());

				}
			}
		}
	}
}
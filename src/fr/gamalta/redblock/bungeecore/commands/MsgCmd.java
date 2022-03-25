package fr.gamalta.redblock.bungeecore.commands;

import fr.gamalta.bungeecord.lib.Message;
import fr.gamalta.redblock.bungeecore.BungeeCore;
import fr.gamalta.redblock.bungeecore.utils.StaffUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class MsgCmd extends Command {

	private BungeeCore main;
	private StaffUtils staffUtils;

	public MsgCmd(BungeeCore main) {

		super("msg", null, "m", "tell", "t", "whisper", "w");
		this.main = main;
		staffUtils = new StaffUtils(main);

	}

	@Override
	public void execute(CommandSender sender, String[] args) {

		if (sender instanceof ProxiedPlayer) {

			ProxiedPlayer player = (ProxiedPlayer) sender;

			if (!(args.length <= 1)) {

				if (!args[0].equals(player.getName())) {

					ProxiedPlayer reciever = main.getProxy().getPlayer(args[0]);

					if (reciever != null) {

						StringBuilder stringBuilder = new StringBuilder();

						for (int i = 1; i < args.length; i++) {

							stringBuilder.append(args[i]);
							stringBuilder.append(" ");
						}

						String message = stringBuilder.toString();

						player.sendMessage(new Message(main.messagesCFG, "PrivateMessages.Format.Sender").replace("%target%", reciever.getName()).replace("%server%", reciever.getServer().getInfo().getName()).replace("%message%", ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', message))).create());
						reciever.sendMessage(new Message(main.messagesCFG, "PrivateMessages.Format.Reciever").replace("%sender%", player.getName()).replace("%server%", player.getServer().getInfo().getName()).replace("%message%", ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', message))).create());
						staffUtils.onSocialSpy(message, player, reciever);
						main.privateMessages.remove(player);
						main.privateMessages.remove(reciever);
						main.privateMessages.put(player, reciever);
						main.privateMessages.put(reciever, player);

					} else {

						player.sendMessage(new Message(main.messagesCFG, "PrivateMessages.Messages.NoPlayerFound").replace("%player%", args[0]).create());

					}

				} else {

					player.sendMessage(new Message(main.messagesCFG, "PrivateMessages.Messages.Alone").create());
				}

			} else {

				player.sendMessage(new Message(main.messagesCFG, "PrivateMessages.Messages.Usage").create());

			}
		} else {

			sender.sendMessage(new Message(main.messagesCFG, "PrivateMessages.Console").create());
		}
	}
}
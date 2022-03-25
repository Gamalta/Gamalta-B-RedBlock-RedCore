package fr.gamalta.redblock.bungeecore.commands;

import fr.gamalta.bungeecord.lib.Message;
import fr.gamalta.redblock.bungeecore.BungeeCore;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class ReloadCmd extends Command {

	private BungeeCore main;

	public ReloadCmd(BungeeCore main) {

		super("B-RedCore", null, "BRedCore", "B-Core", "BCore");
		this.main = main;
	}

	@Override
	public void execute(CommandSender sender, String[] args) {

		if (sender.hasPermission(main.permissionsCFG.getString("ReloadConfig"))) {

			main.bannedWordsCFG.loadConfig();
			main.messagesCFG.loadConfig();
			main.settingsCFG.loadConfig();
			main.permissionsCFG.loadConfig();
			main.staffCFG.loadConfig();

			sender.sendMessage(new Message(main.messagesCFG, "ReloadConfig").create());

		}
	}
}
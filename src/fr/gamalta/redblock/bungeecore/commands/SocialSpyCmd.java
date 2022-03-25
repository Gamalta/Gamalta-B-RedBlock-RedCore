package fr.gamalta.redblock.bungeecore.commands;

import fr.gamalta.bungeecord.lib.Message;
import fr.gamalta.redblock.bungeecore.BungeeCore;
import fr.gamalta.redblock.bungeecore.utils.StaffUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class SocialSpyCmd extends Command {

    private BungeeCore main;
    private StaffUtils staffUtils;

    public SocialSpyCmd(BungeeCore main) {

        super("socialspy");
        this.main = main;
        staffUtils = new StaffUtils(main);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (sender instanceof ProxiedPlayer) {

            ProxiedPlayer player = (ProxiedPlayer) sender;

            if (sender.hasPermission(main.permissionsCFG.getString("SocialSpy"))) {

                if (args.length == 0) {

                    if (staffUtils.hasAccount(player)) {

                        if (staffUtils.toogleSocialSpy(player)) {

                            player.sendMessage(new Message(main.messagesCFG, "PrivateMessages.SocialSpy.Enable").create());

                        } else {

                            player.sendMessage(new Message(main.messagesCFG, "PrivateMessages.SocialSpy.Disable").create());

                        }
                    } else {

                        staffUtils.createAccount(player);
                        staffUtils.setSocialSpy(player, true);

                    }
                } else if (args[0].equalsIgnoreCase("on") || args[0].equalsIgnoreCase("true")) {

                    staffUtils.setSocialSpy(player, true);
                    player.sendMessage(new Message(main.messagesCFG, "PrivateMessages.SocialSpy.Enable").create());

                } else if (args[0].equalsIgnoreCase("off") || args[0].equalsIgnoreCase("false")) {

                    staffUtils.setSocialSpy(player, false);
                    player.sendMessage(new Message(main.messagesCFG, "PrivateMessages.SocialSpy.Disable").create());

                } else {

                    player.sendMessage(new Message(main.messagesCFG, "PrivateMessages.SocialSpy.Usage").create());

                }
            } else {

                player.sendMessage(new Message(main.messagesCFG, "PrivateMessages.SocialSpy.Permission").create());

            }
        }
    }
}
package fr.gamalta.redblock.bungeecore.utils;

import fr.gamalta.bungeecord.lib.Message;
import fr.gamalta.redblock.bungeecore.BungeeCore;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class StaffUtils {

	BungeeCore main;

	public StaffUtils(BungeeCore main) {

		this.main = main;
	}

	public boolean hasAccount(ProxiedPlayer player) {

		return hasAccount(player.getName());

	}

	public boolean hasAccount(String name) {

		return main.staffCFG.contains("Staff." + name);

	}

	public void createAccount(ProxiedPlayer player) {

		createAccount(player.getName());

	}

	public void createAccount(String name) {

		if (!main.staffCFG.contains("Staff." + name)) {

			main.staffCFG.set("Staff." + name + ".StaffChat", false);
			main.staffCFG.set("Staff." + name + ".SocialSpy", false);
		}
	}

	public void deleteAccount(ProxiedPlayer player) {

		deleteAccount(player.getName());
	}

	public void deleteAccount(String name) {

		if (main.staffCFG.contains("Staff." + name)) {

			main.staffCFG.set("Staff" + name, null);

		}
	}

	public void resetAccount(ProxiedPlayer player) {

		resetAccount(player.getName());
	}

	public void resetAccount(String name) {

		main.staffCFG.set("Staff." + name + ".StaffChat", false);
		main.staffCFG.set("Staff." + name + ".SocialSpy", false);

	}

	public boolean toogleSocialSpy(ProxiedPlayer player) {

		return toggleSocialSpy(player.getName());
	}

	public boolean toggleSocialSpy(String name) {

		if (main.staffCFG.contains("Staff." + name)) {

			if (main.staffCFG.getBoolean("Staff." + name + ".SocialSpy")) {

				main.staffCFG.set("Staff." + name + ".SocialSpy", false);

			} else {

				main.staffCFG.set("Staff." + name + ".SocialSpy", true);
				return true;

			}
		}
		return false;
	}

	public boolean toggleStaffChat(ProxiedPlayer player) {

		return toggleStaffChat(player.getName());
	}

	public boolean toggleStaffChat(String name) {

		if (main.staffCFG.contains("Staff." + name)) {

			if (main.staffCFG.getBoolean("Staff." + name + ".StaffChat")) {

				main.staffCFG.set("Staff." + name + ".StaffChat", false);

			} else {

				main.staffCFG.set("Staff." + name + ".StaffChat", true);
				return true;
			}
		}
		return false;
	}

	public boolean getSocialSpy(ProxiedPlayer player) {

		return getSocialSpy(player.getName());
	}

	public boolean getSocialSpy(String name) {

		if (main.staffCFG.contains("Staff." + name)) {

			return main.staffCFG.getBoolean("Staff." + name + ".SocialSpy");

		}

		return false;
	}

	public void setSocialSpy(ProxiedPlayer player, Boolean bool) {

		setSocialSpy(player.getName(), bool);
	}

	public void setSocialSpy(String name, Boolean bool) {

		if (main.staffCFG.contains("Staff." + name)) {

			main.staffCFG.set("Staff." + name + ".SocialSpy", bool);

		}
	}

	public boolean getStaffChat(ProxiedPlayer player) {

		return getStaffChat(player.getName());
	}

	public boolean getStaffChat(String name) {

		if (main.staffCFG.contains("Staff." + name)) {

			return main.staffCFG.getBoolean("Staff." + name + ".StaffChat");

		}

		return false;
	}

	public void setStaffChat(ProxiedPlayer player, Boolean bool) {

		setStaffChat(player.getName(), bool);
	}

	public void setStaffChat(String name, Boolean bool) {

		if (main.staffCFG.contains("Staff." + name)) {

			main.staffCFG.set("Staff." + name + ".StaffChat", bool);

		}
	}

	public void onSocialSpy(String string, ProxiedPlayer sender, ProxiedPlayer reciever) {

		BaseComponent[] message = new Message(main.messagesCFG, "PrivateMessages.Format.SocialSpy").replace("%sender%", sender.getName()).replace("%sender_server%", sender.getServer().getInfo().getName()).replace("%receiver%", reciever.getName()).replace("%receiver_server%", reciever.getServer().getInfo().getName()).replace("%message%", string).create();

		for (ProxiedPlayer staff : main.getProxy().getPlayers()) {

			if (staff.hasPermission(main.permissionsCFG.getString("SocialSpy"))) {

				if (hasAccount(staff) && getSocialSpy(staff)) {

					staff.sendMessage(message);

				}
			}
		}

		main.getProxy().getConsole().sendMessage(message);
	}

	public void onStaffChat(String string, ProxiedPlayer sender) {

		BaseComponent[] message = new Message(main.messagesCFG, "StaffChat.Format").replace("%sender%", sender.getName()).replace("%sender_server%", sender.getServer().getInfo().getName()).replace("%message%", string).create();

		for (ProxiedPlayer staff : main.getProxy().getPlayers()) {

			if (staff.hasPermission(main.permissionsCFG.getString("StaffChat"))) {

				staff.sendMessage(message);

			}
		}

		main.getProxy().getConsole().sendMessage(message);
	}
}

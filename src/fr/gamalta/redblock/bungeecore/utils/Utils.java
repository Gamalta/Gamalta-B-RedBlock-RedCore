package fr.gamalta.redblock.bungeecore.utils;

import java.text.Normalizer;
import java.util.List;
import java.util.UUID;

import fr.gamalta.redblock.bungeecore.BungeeCore;
import net.md_5.bungee.api.ChatColor;

public class Utils {

	BungeeCore main;

	public Utils(BungeeCore main) {

		this.main = main;
	}

	public String color(String string) {

		return ChatColor.translateAlternateColorCodes('&', string);
	}

	public String getStringByList(List<String> list) {

		String string = "";

		for (String str : list) {

			string = string + str + "\n";

		}
		return string.substring(0, string.length() - 1);
	}

	public boolean isSpam(UUID uuid, String string) {

		string = undoPunctuation(string);

		if (string.equals(main.lastMessage.get(uuid)) && string.equals(main.beforelastMessage.get(uuid))) {

			return true;

		} else {

			if (main.beforelastMessage.containsKey(uuid)) {

				main.beforelastMessage.remove(uuid);
			}

			if (main.lastMessage.containsKey(uuid)) {

				main.beforelastMessage.put(uuid, main.lastMessage.get(uuid));
				main.lastMessage.remove(uuid);
			}

			main.lastMessage.put(uuid, string);
		}

		return false;
	}

	public boolean isFlood(String string) {

		string = string.toLowerCase();

		char ch = string.charAt(0);
		int spam = 1;
		int max = main.settingsCFG.getInt("Flood");

		for (int i = 1; i < string.length(); i++) {

			if (ch == string.charAt(i)) {

				spam++;

			} else {

				ch = string.charAt(i);
				spam = 1;

			}

			if (spam >= max) {

				return true;

			}
		}
		return false;
	}

	public boolean isMaj(String string) {

		int upperCase = 0;

		for (Character ch : string.toCharArray()) {
			if (Character.isUpperCase(ch)) {

				upperCase++;

			}
		}

		if (upperCase * 100 / string.length() >= main.settingsCFG.getDouble("Maj")) {

			return true;

		}
		return false;
	}

	public boolean containsBannedWords(String string) {

		for (String bannedWords : main.bannedWordsCFG.getStringList("BannedWords")) {

			if (undoPunctuation(string).contains(undoPunctuation(bannedWords))) {

				return true;
			}
		}
		return false;
	}

	public static String undoPunctuation(String string) {

		return Normalizer.normalize(string, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase().replace(" ", "").replaceAll("\\p{Punct}", "").replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

	}
}

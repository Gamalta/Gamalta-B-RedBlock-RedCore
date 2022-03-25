package fr.gamalta.bungeecord.lib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class RedLib {
	
	public String color(String string) {
		
		return ChatColor.translateAlternateColorCodes('&', string);
	}

	public boolean containsIgnoreCase(List<String> list, String string) {
		
		return list.stream().filter(s -> s.equalsIgnoreCase(string)).findFirst().isPresent();
	}
	
	public boolean containsIgnoreCase(String string0, String string1) {
		
		return string0.toLowerCase().contains(string1.toLowerCase());
	}
	
	public void sendTitle(Collection<? extends ProxiedPlayer> players, Configuration configuration, String path) {
		
		sendTitle(new ArrayList<ProxiedPlayer>(players), configuration, path);
	}
	
	public void sendTitle(List<ProxiedPlayer> players, Configuration config, String path) {
		
		Title title = ProxyServer.getInstance().createTitle();
		path = path + ".";
		
		Object obj = config.get(path);
		
		if (obj instanceof String) {
			
			title.subTitle(TextComponent.fromLegacyText((String) obj));
			
		} else {
			
			if (config.contains(path + "Title")) {
				
				title.title(TextComponent.fromLegacyText(color(config.getString(path + "Title"))));
				
			}
			
			if (config.contains(path + "SubTitle")) {
				
				title.subTitle(TextComponent.fromLegacyText(color(config.getString(path + "SubTitle"))));
				
			}
			
			if (config.contains(path + "FadeIn")) {
				
				title.fadeIn(config.getInt(path + "FadeIn"));
				
			}
			
			if (config.contains(path + "Stay")) {
				
				title.stay(config.getInt(path + "Stay"));
				
			}
			
			if (config.contains(path + "FadeOut")) {
				
				title.fadeOut(config.getInt(path + "FadeOut"));
				
			}
		}
		players.forEach(player -> player.sendTitle(title));
	}
	
	public void sendTitle(ProxiedPlayer player, Configuration configuration, String path) {
		
		sendTitle(Arrays.asList(player), configuration, path);
		
	}
}

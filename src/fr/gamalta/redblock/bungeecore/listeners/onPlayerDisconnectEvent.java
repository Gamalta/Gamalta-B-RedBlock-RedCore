package fr.gamalta.redblock.bungeecore.listeners;

import fr.gamalta.redblock.bungeecore.BungeeCore;
import fr.gamalta.redblock.bungeecore.utils.StaffUtils;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class onPlayerDisconnectEvent implements Listener {

    private BungeeCore main;
    private StaffUtils staffUtils;

    public onPlayerDisconnectEvent(BungeeCore main) {

        this.main = main;
        staffUtils = new StaffUtils(main);

    }

    @EventHandler
    public void onPlayerDisconnect(PlayerDisconnectEvent event) {

        ProxiedPlayer player = event.getPlayer();
        main.privateMessages.remove(player);

        if (staffUtils.hasAccount(player) && staffUtils.getStaffChat(player)) {

            staffUtils.setStaffChat(player, false);
        }
    }
}
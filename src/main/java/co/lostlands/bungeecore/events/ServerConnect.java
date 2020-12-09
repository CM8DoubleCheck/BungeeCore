package co.lostlands.bungeecore.events;

import co.lostlands.bungeecore.main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.IOException;
import java.util.logging.Level;

public class ServerConnect implements Listener {
    private final main plugin;

    public ServerConnect(main pl) {
        plugin = pl;
    }
    @EventHandler
    public void onServerConnect(ServerConnectEvent e) throws IOException {
        if(!e.getReason().equals(ServerConnectEvent.Reason.JOIN_PROXY)) return;
        ProxiedPlayer p = e.getPlayer();
        String hostname = p.getPendingConnection().getVirtualHost().getHostString();
        hostname = hostname.replace('.', '_');
        if (plugin.getConfig().getString("routes." + hostname).length() > 0) {
            String serverName = plugin.getConfig().getString("routes." + hostname);
            ServerInfo target = ProxyServer.getInstance().getServerInfo(serverName);
            if (target != null) {
                e.setTarget(target);
            } else {
                plugin.getLogger().log(Level.WARNING, ChatColor.RED + "Failed to route player "+p.getName()+" to "+serverName+" using hostname "+hostname+": Server does not exist.");
            }
        }
    }
}

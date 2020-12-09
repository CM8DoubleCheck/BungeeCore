package co.lostlands.bungeecore.commands;

import co.lostlands.bungeecore.main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;

public class bungeecore extends Command {
    private co.lostlands.bungeecore.main plugin;

    public bungeecore(main pl) {
        super("bungeecore", "bungeecore.admin", "bc");
        plugin = pl;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix") + "&a BungeeCore version 1.0 by DoubleCheck")));
            return;
        }
        switch (args[0]) {
            case "reload":

                plugin.getLogger().info("Reloading...");

                List<String> currentServers = plugin.getConfig().getStringList("servers.enabled"); //Get servers before reload

                try {
                    plugin.loadConfig();
                    sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix") + "&a Reloaded config.")));
                    List<String> updatedServers = plugin.getConfig().getStringList("servers.enabled"); //Get servers after reload

                    if (!currentServers.equals(updatedServers)) {
                        //Changes have been made, update
                        sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix") + "&a Updating servers...")));
                        List<String> difference = new ArrayList<>(CollectionUtils.disjunction(currentServers, updatedServers));

                        for (int i = 0; i < difference.size(); i++) {
                            if (updatedServers.contains(difference.get(i))) {
                                //Server added
                                String serverName = difference.get(i);

                                String serverMOTD = plugin.getConfig().getString("servers.available."+serverName+".motd");
                                String addr = plugin.getConfig().getString("servers.available."+serverName+".address");
                                boolean restricted = plugin.getConfig().getBoolean("servers.available."+serverName+".restricted");
                                plugin.getLogger().info("Registering server "+serverName+" ["+addr+"]");

                                InetSocketAddress socketAddress = new InetSocketAddress(
                                        addr.substring(0, addr.lastIndexOf(":")),
                                        Integer.parseInt(addr.substring(addr.lastIndexOf(":")+1)));

                                ServerInfo newServer = ProxyServer.getInstance().constructServerInfo(serverName, socketAddress, serverMOTD, restricted);
                                System.out.println(newServer);
                                ProxyServer.getInstance().getServers().put(serverName, newServer);
                                plugin.getLogger().info("Registered server "+serverName);
                            } else {
                                //Server removed
                                String serverName = difference.get(i);
                                updatedServers.remove(serverName);
                                plugin.getLogger().info("Removing server "+ serverName + "...");
                                for (ProxiedPlayer p : ProxyServer.getInstance().getServerInfo(serverName).getPlayers()) {
                                    p.disconnect(new TextComponent(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix") + " " + plugin.getConfig().getString("messages.server-removed"))));
                                }
                                ProxyServer.getInstance().getServers().remove(serverName);
                                plugin.getLogger().info("Removed server "+serverName);
                            }
                        }
                        sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix") + "&a Updated servers successfully.")));
                    }

                    plugin.getLogger().info("Plugin reloaded.");

                } catch (IOException e) {
                    sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix") + "&c Failed to reload config.")));
                    e.printStackTrace();
                }


                /*
                try {
                    plugin.loadBans();
                    sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix") + "&a Reloaded banned players.")));
                } catch (IOException e) {
                    sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix") + "&c Failed to reload banned players.")));
                    e.printStackTrace();
                }
                */
                break;
            default:
                sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix") + "&c Unknown command")));
                break;
        }

    }
}
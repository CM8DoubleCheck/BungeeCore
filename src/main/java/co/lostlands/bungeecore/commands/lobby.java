package co.lostlands.bungeecore.commands;

import co.lostlands.bungeecore.main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class lobby extends Command {

    private main plugin;

    public lobby(main pl) {
        super("lobby", "", "hub");
        plugin = pl;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if ((sender instanceof ProxiedPlayer)) { //check if sender is player
            ProxiedPlayer p = (ProxiedPlayer)sender;
            if (p.getServer().toString().equals(plugin.getConfig().getString("lobby"))) {
                //Already connected to the lobby server
                p.sendMessage(new ComponentBuilder("You are already in the lobby").color(ChatColor.RED).create());
            } else {
                p.sendMessage(new ComponentBuilder("Sending you to the lobby!").color(ChatColor.GREEN).create());
                p.connect(ProxyServer.getInstance().getServerInfo(plugin.getConfig().getString("lobby")));
            }

        }
    }
}

package co.lostlands.bungeecore.commands;

import co.lostlands.bungeecore.main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.io.IOException;
import java.util.ArrayList;

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
                try {
                    plugin.loadConfig();
                    sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix") + "&a Reloaded config.")));
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
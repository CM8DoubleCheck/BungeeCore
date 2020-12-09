package co.lostlands.bungeecore.commands;

import co.lostlands.bungeecore.main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class alert extends Command {

    private main plugin;

    public alert(main pl) {
        super("alert", "bungeecore.alert", "announce");
        plugin = pl;
    }
    @Override
    public void execute(CommandSender sender, String[] args) {
        for (ProxiedPlayer proxy : ProxyServer.getInstance().getPlayers()) {
            StringBuilder str = new StringBuilder();
            for (int i = 0; i < args.length; i++) {
                str.append(args[i] + " ");
            }
            String s = str.toString();
            String finalString = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("alert-format").replace("{message}", s));
            System.out.println(finalString);
            proxy.sendMessage(new TextComponent(finalString));
        }
    }
}
package co.lostlands.bungeecore;

import co.lostlands.bungeecore.events.ServerConnect;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.JsonConfiguration;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;
import java.nio.file.Files;

//Commands
import co.lostlands.bungeecore.commands.alert;
import co.lostlands.bungeecore.commands.bungeecore;
import co.lostlands.bungeecore.commands.lobby;

//Events
import co.lostlands.bungeecore.events.ProxyPing;

public final class main extends Plugin implements Listener {
    Configuration config;
    JsonObject bans;
    @Override
    public void onEnable() {
        if (!getDataFolder().exists()) getDataFolder().mkdir();
        if (!new File(getDataFolder(), "icons").exists()) {
            getLogger().info("Created icons folder");
            new File(getDataFolder(), "icons").mkdir();
        }
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            try (InputStream in = getResourceAsStream("config.yml")) {
                getLogger().info("Created config file");
                Files.copy(in, configFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        File bansFile = new File(getDataFolder(), "banned-players.json");
        if (!bansFile.exists()) {
            try (InputStream in = getResourceAsStream("banned-players.json")) {
                getLogger().info("Created banned-players file");
                Files.copy(in, bansFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            loadConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*
        try {
            loadBans();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */


        //Commands
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new bungeecore(this));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new lobby(this));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new alert(this));

        //Events
        ProxyServer.getInstance().getPluginManager().registerListener(this, new ProxyPing(this));
        ProxyServer.getInstance().getPluginManager().registerListener(this, new ServerConnect(this));

        getLogger().info("Successfully Enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin Disabled.");
    }
    public void loadConfig() throws IOException {
        config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
    }
    public void loadBans() throws IOException {
        bans = new JsonParser().parse(new FileReader(getDataFolder() + "/banned-players.json")).getAsJsonObject();
    }
    public Configuration getConfig() {
        return config;
    }
    public JsonObject getBans() {return bans;}
}

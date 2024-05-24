package me.lenyz.olobby;

import me.lenyz.olobby.commands.QueueCommand;
import me.lenyz.olobby.configuration.ConfigManager;
import me.lenyz.olobby.listeners.PlayerListeners;
import me.lenyz.olobby.managers.LobbyManager;
import me.lenyz.olobby.managers.QueueManager;
import me.lenyz.olobby.storages.ServerStorage;
import me.lenyz.olobby.storages.UserStorage;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class OLobby extends JavaPlugin {

    private ConfigManager configManager;
    private LobbyManager lobbyManager;
    private QueueManager queueManager;
    private ServerStorage serverStorage;
    private UserStorage userStorage;
    private Logger logger;

    @Override
    public void onEnable() {
        logger = getLogger();
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        configManager = new ConfigManager(this, logger);
        lobbyManager = new LobbyManager(configManager.getConfig("config"), configManager.getSpawn());
        serverStorage = new ServerStorage(configManager.getServers());
        userStorage = new UserStorage();
        queueManager = new QueueManager(this, configManager, userStorage, serverStorage);

        Bukkit.getPluginManager().registerEvents(new PlayerListeners(lobbyManager, userStorage, serverStorage), this);
        this.getCommand("queue").setExecutor(new QueueCommand(serverStorage, queueManager, userStorage));
    }

    @Override
    public void onDisable() {

    }
}

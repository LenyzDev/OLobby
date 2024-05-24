package me.lenyz.olobby.managers;

import me.lenyz.olobby.OLobby;
import me.lenyz.olobby.configuration.ConfigManager;
import me.lenyz.olobby.objects.Server;
import me.lenyz.olobby.objects.ServerQueue;
import me.lenyz.olobby.storages.ServerStorage;
import me.lenyz.olobby.storages.UserStorage;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class QueueManager {

    private Map<String, ServerQueue> serverQueues;
    private UserStorage userStorage;
    private ServerStorage serverStorage;
    private OLobby oLobby;
    private String updateTitle;
    private String updateSubTitle;
    private int updateDelay;

    public QueueManager(OLobby oLobby, ConfigManager configManager, UserStorage userStorage, ServerStorage serverStorage){
        this.serverQueues = new HashMap<>();
        this.serverStorage = serverStorage;
        this.userStorage = userStorage;
        this.oLobby = oLobby;

        this.updateTitle = configManager.getTitle();
        this.updateSubTitle = configManager.getSubtitle();
        this.updateDelay = configManager.getDelay();

        for(Server server : serverStorage.getServers().values()) {
            serverQueues.put(server.getId(), new ServerQueue(server.getId(), this));
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                updateQueues();
            }
        }.runTaskTimer(oLobby, 20L * updateDelay, 20L * updateDelay);
    }

    public ServerQueue getServerQueue(String serverName){
        return serverQueues.get(serverName);
    }

    public String getUpdateTitle(int position){
        return updateTitle.replace("&", "§")
                .replace("{position}", format(position));
    }

    public String getUpdateSubTitle(int position){
        return updateSubTitle.replace("&", "§")
                .replace("{position}", format(position));
    }

    private void updateQueues(){
        for (Map.Entry<String, ServerQueue> entry : serverQueues.entrySet()) {
            ServerQueue serverQueue = entry.getValue();
            if(serverQueue.getQueue().isEmpty()) continue;
            serverQueue.updateQueue();
        }
    }

    public void connectPlayer(Player player, String server){
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        if(userStorage.containsUser(player.getUniqueId())){
            userStorage.getUser(player.getUniqueId()).setQueue(null);
        }

        try {
            out.writeUTF("Connect");
            out.writeUTF(server);
        } catch (IOException e) {
            e.printStackTrace();
        }

        player.sendPluginMessage(oLobby, "BungeeCord", b.toByteArray());
    }

    public boolean serverIsOnline(String server){
        if(!serverStorage.containsServer(server)) return false;
        Server serverObject = serverStorage.getServer(server);
        try {
            Socket s = new Socket(serverObject.getHost(), serverObject.getPort());
            s.close();
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    public static String format(int value){
        return String.format(Locale.GERMAN, "%,.0f", (double) value);
    }

}

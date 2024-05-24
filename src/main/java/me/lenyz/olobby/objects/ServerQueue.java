package me.lenyz.olobby.objects;

import me.lenyz.olobby.managers.QueueManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.UUID;

public class ServerQueue {

    private String server;
    private LinkedHashSet<UUID> queue;
    private QueueManager queueManager;

    public ServerQueue(String server, QueueManager queueManager){
        this.queue = new LinkedHashSet<>();
        this.server = server;
        this.queueManager = queueManager;
    }

    public void addPlayer(UUID uuid){
        queue.add(uuid);
    }

    public void removePlayer(UUID uuid){
        queue.remove(uuid);
    }

    public boolean containsPlayer(UUID uuid){
        return queue.contains(uuid);
    }

    public void updateQueue(){
        Iterator<UUID> iterator = queue.iterator();
        int position = 0;
        while(iterator.hasNext()){
            Player player = Bukkit.getPlayer(iterator.next());
            if (player == null || !player.isOnline()) {
                iterator.remove();
                continue;
            }
            position++;

            if(position == 1) {
                player.sendTitle(queueManager.getUpdateTitle(position), queueManager.getUpdateSubTitle(position));
                if(!queueManager.serverIsOnline(server)) continue;
                queueManager.connectPlayer(player, server);
                iterator.remove();
            } else {
                player.sendTitle(queueManager.getUpdateTitle(position), queueManager.getUpdateSubTitle(position));
            }
        }
    }

    public LinkedHashSet<UUID> getQueue(){
        return queue;
    }
}

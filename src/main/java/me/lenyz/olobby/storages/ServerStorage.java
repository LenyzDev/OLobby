package me.lenyz.olobby.storages;

import me.lenyz.olobby.objects.Server;

import java.util.Map;

public class ServerStorage {

    private Map<String, Server> servers;

    public ServerStorage(Map<String, Server> servers){
        this.servers = servers;
    }

    public Map<String, Server> getServers(){
        return servers;
    }

    public Server getServer(String serverName){
        return servers.get(serverName);
    }

    public void addServer(Server server){
        servers.put(server.getName(), server);
    }

    public void removeServer(String serverName){
        servers.remove(serverName);
    }

    public boolean containsServer(String serverName){
        return servers.containsKey(serverName);
    }
}

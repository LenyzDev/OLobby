package me.lenyz.olobby.inventories;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.lenyz.olobby.managers.LobbyManager;
import me.lenyz.olobby.managers.QueueManager;
import me.lenyz.olobby.objects.Server;
import me.lenyz.olobby.objects.User;
import me.lenyz.olobby.storages.ServerStorage;
import me.lenyz.olobby.storages.UserStorage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ServerInventory implements InventoryProvider {

    private ServerStorage serverStorage;

    public ServerInventory(ServerStorage serverStorage){
        this.serverStorage = serverStorage;
    }

    public static SmartInventory getInventory(LobbyManager lobbyManager, ServerStorage serverStorage) {
        return SmartInventory.builder()
                .id("serverInventory")
                .provider(new ServerInventory(serverStorage))
                .size(lobbyManager.getMenuSize(), 9)
                .title(lobbyManager.getMenuName())
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        for(Server server : serverStorage.getServers().values()){
            contents.set(server.getSlotY(), server.getSlotX(), ClickableItem.of(server.getItemStack(), e -> {
                player.performCommand("queue " + server.getId());
                player.closeInventory();
            }));
        }
    }

    @Override
    public void update(Player player, InventoryContents contents) {
    }
}

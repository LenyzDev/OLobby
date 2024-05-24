package me.lenyz.olobby.objects;

import org.bukkit.inventory.ItemStack;

public class Server {

    private final String id;
    private final String name;
    private final String host;
    private final int port;
    private final int slotX;
    private final int slotY;
    private ItemStack itemStack;

    public Server(String id, String name, String host, int port, int slotX, int slotY, ItemStack itemStack) {
        this.id = id;
        this.name = name;
        this.host = host;
        this.port = port;
        this.slotX = slotX;
        this.slotY = slotY;
        this.itemStack = itemStack;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public int getSlotX() {
        return slotX;
    }

    public int getSlotY() {
        return slotY;
    }
}

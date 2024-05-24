package me.lenyz.olobby.managers;

import com.lib.items.ItemBuilder;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.stream.Collectors;

public class LobbyManager {

    private String menuName;
    private int menuSize;
    private Location lobbyLocation;
    private ItemStack serverSelector;
    private ItemStack enablePVP;
    private ItemStack disablePVP;

    public LobbyManager(FileConfiguration config, Location lobbyLocation) {
        this.menuName = config.getString("menu.name");
        this.menuSize = config.getInt("menu.size");
        this.lobbyLocation = lobbyLocation;

        ItemStack selector = new ItemBuilder(Material.matchMaterial(config.getString("items.selector.material")), 1, (short) config.getInt("items.selector.data"))
                .setDisplayName(config.getString("items.selector.name").replace("&", "§"))
                .setLore(config.getStringList("items.selector.lore").stream().map(s -> s.replace("&", "§")).collect(Collectors.toList()))
                .build();

        ItemStack enable =new ItemBuilder(Material.matchMaterial(config.getString("items.enable.material")), 1, (short) config.getInt("items.enable.data"))
                .setDisplayName(config.getString("items.enable.name").replace("&", "§"))
                .setLore(config.getStringList("items.enable.lore").stream().map(s -> s.replace("&", "§")).collect(Collectors.toList()))
                .build();

        ItemStack disable =new ItemBuilder(Material.matchMaterial(config.getString("items.disable.material")), 1, (short) config.getInt("items.disable.data"))
                .setDisplayName(config.getString("items.disable.name").replace("&", "§"))
                .setLore(config.getStringList("items.disable.lore").stream().map(s -> s.replace("&", "§")).collect(Collectors.toList()))
                .build();

        NBTItem nbtSelector = new NBTItem(selector);
        nbtSelector.setBoolean("olobby_selector", true);

        NBTItem nbtEnable = new NBTItem(enable);
        nbtEnable.setBoolean("olobby_pvp", true);

        NBTItem nbtDisable = new NBTItem(disable);
        nbtDisable.setBoolean("olobby_pvp", true);

        this.serverSelector = nbtSelector.getItem();
        this.enablePVP = nbtEnable.getItem();
        this.disablePVP = nbtDisable.getItem();
    }

    public String getMenuName() {
        return menuName;
    }

    public int getMenuSize() {
        return menuSize;
    }

    public Location getLobbyLocation() {
        return lobbyLocation;
    }

    public ItemStack getServerSelector() {
        return serverSelector;
    }

    public ItemStack getEnablePVP() {
        return enablePVP;
    }

    public ItemStack getDisablePVP() {
        return disablePVP;
    }
}

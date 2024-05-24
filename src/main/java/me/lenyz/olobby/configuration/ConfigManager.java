package me.lenyz.olobby.configuration;

import com.lib.items.ItemBuilder;
import me.lenyz.olobby.OLobby;
import me.lenyz.olobby.objects.Server;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ConfigManager {

    private ConfigUtils configUtils;

    public ConfigManager(OLobby main, Logger logger){
        configUtils = new ConfigUtils(main, logger);

        logger.info("Loading configurations...");
        configUtils.createConfig("config");
        logger.info("Configurations loaded.");
    }

    public FileConfiguration getConfig(String file){
        return configUtils.getConfig(file);
    }

    public String getTitle(){
        FileConfiguration config = configUtils.getConfig("config");
        return config.getString("title");
    }

    public String getSubtitle(){
        FileConfiguration config = configUtils.getConfig("config");
        return config.getString("subtitle");
    }

    public int getDelay(){
        FileConfiguration config = configUtils.getConfig("config");
        return config.getInt("delay");
    }

    public Location getSpawn(){
        FileConfiguration config = configUtils.getConfig("config");
        String location[] = config.getString("lobby").split(",");
        return new Location(
                Bukkit.getWorld(location[0]),
                Double.parseDouble(location[1]),
                Double.parseDouble(location[2]),
                Double.parseDouble(location[3]),
                Float.parseFloat(location[4]),
                Float.parseFloat(location[5])
        );
    }

    public Map<String, Server> getServers(){
        FileConfiguration config = configUtils.getConfig("config");
        Map<String, Server> servers = new HashMap<>();
        for(String key : config.getConfigurationSection("servers").getKeys(false)){
            ItemStack itemStack = new ItemBuilder(Material.matchMaterial(config.getString("servers."+key+".material")), 1, (short) config.getInt("servers."+key+".data", 0))
                    .setDisplayName(config.getString("servers."+key+".name").replace("&", "§"))
                    .setLore(config.getStringList("servers."+key+".lore").stream().map(s -> s.replace("&", "§")).collect(Collectors.toList()))
                    .build();
            Server server = new Server(key,
                    config.getString("servers."+key+".name"),
                    config.getString("servers."+key+".address"),
                    config.getInt("servers."+key+".port"),
                    config.getInt("servers."+key+".x"),
                    config.getInt("servers."+key+".y"),
                    itemStack
            );
            servers.put(key, server);
        }
        return servers;
    }
}

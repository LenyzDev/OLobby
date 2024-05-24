package me.lenyz.olobby.configuration;

import me.lenyz.olobby.OLobby;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.logging.Logger;

public class ConfigUtils {

    private OLobby main;
    private Logger logger;
    public ConfigUtils(OLobby main, Logger logger){
        this.main = main;
        this.logger = logger;
    }

    public void createConfig(String file) {
        if (!(new File(main.getDataFolder(), file + ".yml")).exists()) {
            logger.info("Configuration '"+file+"' created.");
            main.saveResource(file + ".yml", false);
        }

    }

    public FileConfiguration getConfig(String file) {
        File archive = new File(main.getDataFolder() + File.separator + file + ".yml");
        logger.info("Configuration '"+file+"' loaded.");
        return YamlConfiguration.loadConfiguration(archive);
    }

    public void createFolder(String folderName) {
        try {
            File folder = new File(main.getDataFolder() + File.separator + folderName);
            if (!folder.exists()) {
                folder.mkdirs();
            }
        } catch (Throwable var2) {
            logger.warning("Failed to create %folder% folder".replace("%folder%", folderName));
            var2.printStackTrace();
        }

    }

    public void createFile(File file) {
        try {
            logger.info("File '"+file+"' created.");
            file.createNewFile();
        } catch (Throwable var2) {
            logger.warning("Failed to create %file% file".replace("%file%", file.getName()));
            var2.printStackTrace();
        }

    }

    public File getFolder(String folder) {
        return new File(main.getDataFolder() + File.separator + folder);
    }
    public File getFile(String file, String folder) {
        return new File(main.getDataFolder() + File.separator + folder, file + ".yml");
    }

    public File getFile(String file) {
        return new File(main.getDataFolder() + File.separator + file + ".yml");
    }

    public FileConfiguration getConfiguration(File file) {
        return YamlConfiguration.loadConfiguration(file);
    }

    public void deleteFile(File file) {
        file.delete();
    }

}

package me.lenyz.olobby.commands;

import me.lenyz.olobby.managers.QueueManager;
import me.lenyz.olobby.objects.Server;
import me.lenyz.olobby.objects.User;
import me.lenyz.olobby.storages.ServerStorage;
import me.lenyz.olobby.storages.UserStorage;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class QueueCommand implements CommandExecutor {

    private ServerStorage serverStorage;
    private QueueManager queueManager;
    private UserStorage userStorage;

    public QueueCommand(ServerStorage serverStorage, QueueManager queueManager, UserStorage userStorage){
        this.serverStorage = serverStorage;
        this.queueManager = queueManager;
        this.userStorage = userStorage;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        if(args.length == 1){
            if(!serverStorage.containsServer(args[0])) return false;
            Server server = serverStorage.getServer(args[0]);
            if(!player.hasPermission("olobby.server."+server.getId())){
                player.playSound(player.getLocation(), Sound.NOTE_BASS, 1, 1);
                player.sendMessage("§cYou don't have permission to join this server.");
                return false;
            }

            if(player.hasPermission("olobby.queue.bypass")){
                queueManager.connectPlayer(player, server.getId());
                return true;
            }

            User user = userStorage.getUser(player.getUniqueId());

            if(user.getQueue() != null){
                player.playSound(player.getLocation(), Sound.NOTE_BASS, 1, 1);
                queueManager.getServerQueue(user.getQueue()).removePlayer(player.getUniqueId());
                user.setQueue(null);
                return true;
            }

            player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1, 1);
            queueManager.getServerQueue(args[0]).addPlayer(player.getUniqueId());
            user.setQueue(args[0]);
            return true;
        }
        return false;
    }

}
